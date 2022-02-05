package com.samax.tech.tcp.client.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ConnectionDialog {
	
	private String address;
	private int port;
	
	private JTextField tf_address;
	private JTextField tf_port;
	private JPanel pane;
	
	public ConnectionDialog() {
		pane = new JPanel(new GridLayout(2, 2, 5, 5));
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		Dimension dimensionTf = new Dimension(125, 25);
		
		tf_address = new JTextField("localhost");
		tf_address.setPreferredSize(dimensionTf);
		tf_port = new JTextField("3333");
		tf_port.setPreferredSize(dimensionTf);
		
		pane.add(new JLabel("Address: "));
		pane.add(tf_address);
		pane.add(new JLabel("Port: "));
		pane.add(tf_port);
	}
	
	public int show()
	{
		int result = JOptionPane.showConfirmDialog(null, pane, "Connection details", JOptionPane.PLAIN_MESSAGE);
		
		address = tf_address.getText();
		
		try {
			port = Integer.parseInt(tf_port.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Port must be a number!", "Illegal port argument", JOptionPane.ERROR_MESSAGE);
			tf_port.setText("");
			return -1;
		}
		
		return result;
	}
	
	public String getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
}
