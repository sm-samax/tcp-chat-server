package com.samax.tech.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Client extends Thread {

	private Socket socket;
	private String name;
	
	private String host;
	private int port;
	
	private BufferedReader in;
	private PrintWriter out;

	public Client(String name) {
		this(name, "localhost", 8080);
	}
	
	public Client(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
	}

	public void run() {
		try {
			socket = new Socket(host, port);
			System.out.println("Connecting client...");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		
		System.out.println("Sending...");
		out.print(message.getBytes());
		System.out.println("Sent!");
		receiveMessage();
	}
	
	public String receiveMessage() {
		try {
			return new String(in.readLine());
		} catch (IOException e) {
			return "";
		}
	}

	public void disconnect()
	{
		try {
			socket.close();
		} catch (IOException e) {
			return;
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getUsername() {
		return name;
	}
}