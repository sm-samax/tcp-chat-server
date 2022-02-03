package com.samax.tech.server.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.samax.tech.server.Client;
import com.samax.tech.server.Server;

public class ClientGUI {

	private Client client;
	private boolean connected;
	
	
	private JFrame frame;
	private JList<String> messages;
	private DefaultListModel<String> list;
	private JTextField tf_name;
	private JTextField tf_message;
	private JButton btn_send;
	private JButton btn_connect;
	private JButton btn_disconnect;
	private JButton btn_quit;
	private JLabel lbl_serverState;
	
	public ClientGUI() {
		initGUI();
		initEvents();
	}

	private void initGUI() {
		frame = new JFrame("Chat Application");
		LayoutManager layout = new BorderLayout(5, 5);
		frame.getContentPane().setLayout(layout);
		frame.setLocationRelativeTo(null);
		
		Dimension dimensionTf = new Dimension(100, 25);
		
		tf_name = new JTextField();
		tf_name.setMinimumSize(dimensionTf);
		tf_name.setPreferredSize(dimensionTf);
		
		tf_message = new JTextField();
		tf_message.setMinimumSize(dimensionTf);
		tf_message.setPreferredSize(dimensionTf);
		
		btn_send = new JButton("Send");
		btn_send.setEnabled(false);
		
		btn_connect = new JButton("Connect");
		
		btn_disconnect = new JButton("Disconnect");
		btn_disconnect.setEnabled(false);
		
		btn_quit = new JButton("Quit");
	
		Dimension dimensionMessages = new Dimension(150, 150);
		
		list = new DefaultListModel<>();
		
		messages = new JList<>(list);
		messages.setMinimumSize(dimensionMessages);
		messages.setPreferredSize(dimensionMessages);
		
		JLabel lbl_username = new JLabel("Username:");
		
		Container top = new Container();
		top.setLayout(new FlowLayout());
		
		top.add(lbl_username);
		top.add(tf_name);
		top.add(btn_connect);
		top.add(btn_disconnect);
		top.add(btn_quit);
		
		Container down = new Container();
		down.setLayout(new FlowLayout());
		
		down.add(tf_message);
		down.add(btn_send);
		
		frame.add(top, BorderLayout.NORTH);
		frame.add(messages, BorderLayout.CENTER);
		frame.add(down, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private void initEvents() {
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});
		
		btn_quit.addActionListener(e -> quit());
		
		btn_connect.addActionListener(e -> connect());
		
		btn_disconnect.addActionListener(e -> disconnect());
		
		btn_send.addActionListener(e -> send());
		
	}
	
	private void connect() 
	{
		if(tf_name.getText().isBlank())
			JOptionPane.showMessageDialog(frame, "Username field can't be empty!", "Invalid connection", JOptionPane.ERROR_MESSAGE);
		else
		{
			btn_connect.setEnabled(false);
			btn_disconnect.setEnabled(true);
			btn_send.setEnabled(true);
			
			connected = true;
			client = new Client(tf_name.getText());
			client.start();
		}
	}
	
	private void quit()
	{
		disconnect();
		System.exit(0);
	}
	
	private void disconnect()
	{
		if(client != null && connected)
			client.disconnect();
		
		connected = false;
		
		btn_connect.setEnabled(true);
		btn_disconnect.setEnabled(false);
		btn_send.setEnabled(false);
	}
	
	private void send()
	{
		if(!tf_message.getText().isBlank())
		{
			client.sendMessage(String.format("%s: %s", client.getUsername(), tf_message.getText()));			
			list.addElement(client.receiveMessage());
			tf_message.setText("");
		}
	}
	
	public static void main(String[] args) {
		new Server().start();
		new ClientGUI();
	}
}
