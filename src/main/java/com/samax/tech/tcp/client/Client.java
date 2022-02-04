package com.samax.tech.tcp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

	private Socket socket;

	private String name;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public Client(String name) {
		this.name = name;
	}
	
	public void connect(String host, int port) throws IOException
	{
			this.socket = new Socket(host, port);
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			System.out.println("Connected!");
	}
	
	public String receiveMessage() {
		try {
			return (String) ois.readObject();
		} catch (ClassNotFoundException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}

	public void sendMessage(String msg)
	{
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			System.out.println("Can't send message!");
		}
	}
	
	public String getName() {
		return name;
	}

	public void disconnect() 
	{
		for(var closeable : new AutoCloseable[]{oos, ois, socket})
			try {
				closeable.close();
			} catch (Exception e) {}
	}
}
