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
		try {
			this.server = new ServerSocket(port);
			System.out.println(String.format("Starting server on %s:%d...", address, port));

			Socket client = server.accept();
			
			System.out.println("Connected");
			
			while (running) {
				String message = new String(client.getInputStream().readAllBytes());
				System.out.println("Server >>> Received: " + message);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void disconnectClient(Socket client) {
		try {
			client.close();
			System.out.println("Disconnecting...");
		} catch (IOException e) {
			e.printStackTrace();
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
}
