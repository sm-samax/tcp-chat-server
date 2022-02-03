package com.samax.tech.server;

import java.util.concurrent.TimeUnit;

public class ClientServerDemo {

	public static void main(String[] args) {
		String address = "localhost";
		int port = 8080;

		Server server = new Server(address, port);
		server.start();
		
		Client client = new Client("Samax");
		client.start();
		
		
		

		try {
			TimeUnit.SECONDS.sleep(3);
			client.sendMessage("Hello World!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
