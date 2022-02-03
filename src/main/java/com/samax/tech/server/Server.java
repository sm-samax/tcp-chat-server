package com.samax.tech.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

	private ServerSocket server;
	private List<Socket> clients;
	private ExecutorService exe;

	private int port;
	private String address;
	private int max_connections = 32;
	private boolean running = true;

	public Server() {
		this("localhost", 8080);
	}

	public Server(String address, int port) {
		this.address = address;
		this.port = port;

		exe = Executors.newFixedThreadPool(max_connections + 1);
		clients = new CopyOnWriteArrayList<>();
	}

	public void run() {
		exe.submit(clearDisconnectedClients());

		try {
			this.server = new ServerSocket(port);
			System.out.println(String.format("Starting server on %s:%d...", address, port));

			while (running) {
				waitForClients();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Runnable clearDisconnectedClients() {
		return () -> {
			while (running) {
				for (Socket s : clients)
					if (s.isClosed()) {
						clients.remove(s);
						System.out.println("Disconnected!");
					}
			}
		};
	}

	private void waitForClients() {
		try {
			Socket client = server.accept();
			exe.submit(serverTask(client));
		} catch (Exception e) {
			return;
		}
	}

	private Runnable serverTask(Socket client) {
		return () -> {
			System.out.println("Connected!");
			clients.add(client);

			while(running)
				receiveMessage(client);
		};
	}

	private void disconnectClient(Socket client) {
		try {
			client.close();
			System.out.println("Disconnecting...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveMessage(Socket client) {
		String msg = null;

		try {
			msg = new String(client.getInputStream().readAllBytes());
			if(!msg.isBlank())
			{
				System.out.println("Received!");
				System.out.println("From client: " + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

//		if(msg != null)
//			sendMessageToAllClients(msg);
	}

	private void sendMessageToAllClients(String msg) {
		for (Socket client : clients)
			try {
				client.getOutputStream().write(msg.getBytes());
			} catch (IOException e) {
				continue;
			}
	}

	public synchronized void shutDown() {
		for (Socket client : clients)
			disconnectClient(client);

		running = false;

		exe.shutdown();

		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().start();
	}
}
