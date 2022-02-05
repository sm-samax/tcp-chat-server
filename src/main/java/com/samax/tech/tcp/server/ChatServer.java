package com.samax.tech.tcp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatServer {
	private static final int MAX_CONNECTION = 32;
	
	private ServerSocket server;
	private AtomicBoolean running = new AtomicBoolean();
	
	private List<ClientService> clients;

	public ChatServer() {
		clients = new ArrayList<>();
	}

	public void start(String address, int port) throws UnknownHostException, IOException {
		
		server = new ServerSocket(port, MAX_CONNECTION, InetAddress.getByName(address));
		System.out.println(String.format("Server starting on %s:%d...", address, port));

		running.set(true);
		acceptClients().start();
	}

	private Thread acceptClients() {
		return new Thread(() -> {
			while (running.get()) {
				try {
					ClientService cs = new ClientService(this, server.accept());
					clients.add(cs);
					cs.start();
				} catch (Exception e) {}
			}
		});
	}

	public void remove(ClientService client)
	{
		clients.remove(client);
		System.out.println("Disconnected!");
	}
	
	public void dispatchReceivedMessage(String msg) {
		for (ClientService client : clients)
			client.sendMessage(msg);
	}

	public void shutdown() throws IOException {
		for (var client : clients)
			try {
				client.close();
			} catch (Exception e) {}

		running.set(false);
		server.close();
		System.out.println("Shuting down...");
	}
}
