package com.samax.tech.tcp.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.samax.tech.tcp.client.Client;
import com.samax.tech.tcp.server.ChatServer;
import com.samax.tech.tcp.server.gui.ChatServerGUI;

public class ClientGUI {

	private Client client;
	private AtomicBoolean connected = new AtomicBoolean(false);
	private Thread receiveTask;
	
	private JFrame frame;
	private JList<String> messages;
	private DefaultListModel<String> list;
	private JTextField tf_name;
	private JTextField tf_message;
	private JButton btn_send;
	private JButton btn_connect;
	private JButton btn_disconnect;
	private JButton btn_quit;
	private ConnectionDialog dialog;
	
	public ClientGUI() {
		initGUI();
		initEvents();
		
		receiveTask = new Thread(() -> {
			while(true)
			{
				if(connected.get())
				{
					try {
						String msg = client.receiveMessage();
						if(!msg.isBlank())					
							list.addElement(msg);
					} catch (Exception e) {
						continue;
					}
				}
			}
		});
		
		receiveTask.start();
	}

	private void initGUI() {
		frame = new JFrame("Chat Application");
		LayoutManager layout = new BorderLayout(5, 5);
		frame.getContentPane().setLayout(layout);
		
		dialog = new ConnectionDialog();
		
		Dimension dimensionTf = new Dimension(100, 25);
		
		tf_name = new JTextField();
		tf_name.setMinimumSize(dimensionTf);
		tf_name.setPreferredSize(dimensionTf);
		
		dimensionTf = new Dimension(225, 25);
		
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
		
		JScrollPane scrollPane = new JScrollPane(messages);
		scrollPane.setMinimumSize(dimensionMessages);
		scrollPane.setPreferredSize(dimensionMessages);
		
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
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(down, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
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
			int result = dialog.show();
			
			if(result == JOptionPane.OK_OPTION)
			{
				client = new Client(tf_name.getText());
				try {
					client.connect(dialog.getAddress(), dialog.getPort());
					
					tf_name.setEnabled(false);
					btn_connect.setEnabled(false);
					btn_disconnect.setEnabled(true);
					btn_send.setEnabled(true);
					
					connected.set(true);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "Server is not available yet!", "Unable to connect", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	private void quit()
	{
		disconnect();
		System.exit(0);
	}
	
	private void disconnect()
	{
		if(client != null && connected.get())
			client.disconnect();
		
		connected.set(false);
		list.clear();
		
		tf_name.setEnabled(true);
		btn_connect.setEnabled(true);
		btn_disconnect.setEnabled(false);
		btn_send.setEnabled(false);
	}
	
	private void send()
	{
		if(!tf_message.getText().isBlank())
		{
			this.receiveTask.interrupt();
		
			try {
				client.sendMessage(String.format("%s: %s", client.getName(), tf_message.getText()));
				tf_message.setText("");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame,
						"Cant send message, maybe your network not work properly or the server was closed.",
						"Unable to send message", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException {		
		new ClientGUI();
	}
}
