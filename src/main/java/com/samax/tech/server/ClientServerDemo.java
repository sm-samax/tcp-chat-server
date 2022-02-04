package com.samax.tech.server;

import com.samax.tech.tcp.server.ChatServer;

import java.util.concurrent.TimeUnit;

import com.samax.tech.tcp.client.Client;

public class ClientServerDemo {

	public static void main(String[] args) {
		new Thread(() -> new ChatServer().start("localhost", 3333)).start();
		
	}

}
