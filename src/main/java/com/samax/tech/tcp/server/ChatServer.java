package com.samax.tech.tcp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private ServerSocket server;
	private boolean running = true;

	private List<ClientService> clients;

	public ChatServer() {
		clients = new ArrayList<>();
	}

	public void start(String address, int port) {
		try {
			server = new ServerSocket(port);
			System.out.println(String.format("Server starting on %s:%d...", address, port));

			while (running) {
				try {
					ClientService cs = new ClientService(this, server.accept());
					clients.add(cs);
					cs.start();
				} catch (Exception e) {}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dispatchReceivedMessage(String msg) {
		for (ClientService client : clients)
			if (client.isAlive())
				client.sendMessage(msg);
			else {
				clients.remove(client);
				System.out.println("Disconnected!");
			}
	}

	public void shutdown() throws IOException {
		running = false;
		server.close();
	}

	public static void main(String[] args) {
		new ChatServer().start("localhost", 3333);
	}
}
