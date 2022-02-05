package com.samax.tech.tcp.server.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.samax.tech.tcp.server.ChatServer;

public class ChatServerGUI {

	private JFrame frame;
	private JTextField tf_address;
	private JTextField tf_port;
	private JButton btn_start;
	private JButton btn_stop;
	
	private ChatServer server;
	
	public ChatServerGUI() {
		server = new ChatServer();
		initGUI();
		initEvents();
	}

	private void initGUI() {
		frame = new JFrame("Chat server");
		
		JPanel pane = new JPanel(new GridLayout(3, 2, 5, 5));
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(pane);
		
		Dimension dimensionTf = new Dimension(125, 25);
		
		tf_address = new JTextField("localhost");
		tf_address.setMinimumSize(dimensionTf);
		tf_address.setPreferredSize(dimensionTf);
		
		tf_port = new JTextField("3333");
		tf_port.setMinimumSize(dimensionTf);
		tf_port.setPreferredSize(dimensionTf);
		
		btn_start = new JButton("Start");
		btn_stop = new JButton("Stop");
		btn_stop.setEnabled(false);
		
		frame.add(new Label("Address: "));
		frame.add(tf_address);
		frame.add(new Label("Port: "));
		frame.add(tf_port);
		frame.add(btn_start);
		frame.add(btn_stop);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void initEvents() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		btn_start.addActionListener(e -> start());
		
		btn_stop.addActionListener(e -> stop());
	}
	
	private void stop() {
		
		try {
			server.shutdown();
//			server = new ChatServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tf_address.setEnabled(true);
		tf_port.setEnabled(true);
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
	}

	private void start() {
		if(tf_address.getText().isBlank() || tf_port.getText().isBlank())
			JOptionPane.showMessageDialog(frame, "Address and port fields can't be empty!", "Incorrect start", JOptionPane.ERROR_MESSAGE);
		else
		{
			try {
				String address = tf_address.getText();
				int port = Integer.parseInt(tf_port.getText());
				
				server.start(address, port);
				
				tf_address.setEnabled(false);
				tf_port.setEnabled(false);
				btn_start.setEnabled(false);
				btn_stop.setEnabled(true);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(frame, "Port must be a number!", "Illegal port argument", JOptionPane.ERROR_MESSAGE);
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(frame, "Given address was not correct!", "Illegal address argument", JOptionPane.ERROR_MESSAGE);			} catch (IOException e) {
			}
			
		}
	}

	public static void main(String[] args) {
		new ChatServerGUI();
	}
}
