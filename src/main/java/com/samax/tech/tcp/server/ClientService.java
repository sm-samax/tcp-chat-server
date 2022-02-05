package com.samax.tech.tcp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientService extends Thread implements AutoCloseable{

	private ChatServer server;
	private Socket client;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ClientService(ChatServer server, Socket client) throws IOException {
		this.server = server;
		this.client = client;

		oos = new ObjectOutputStream(client.getOutputStream());
		ois = new ObjectInputStream(client.getInputStream());
		
		System.out.println("Connected!");
	}

	@Override
	public void run() {
		try {
			while (!client.isClosed()) {
				String msg = (String) ois.readObject();
				server.dispatchReceivedMessage(msg);
			}
		} catch (Exception e) {}
		
		server.remove(this);
	}

	public void sendMessage(String msg) {
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			System.out.println("Can't send message!");
		}
	}

	@Override
	public void close() throws IOException {
		client.close();
	}
}
