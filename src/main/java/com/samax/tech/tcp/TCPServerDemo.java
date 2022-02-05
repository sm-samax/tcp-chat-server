package com.samax.tech.tcp;

import com.samax.tech.tcp.client.gui.ClientGUI;
import com.samax.tech.tcp.server.gui.ChatServerGUI;

public class TCPServerDemo {

	public static void main(String[] args) {
		new ChatServerGUI();
		new ClientGUI();
		new ClientGUI();
	}

}
