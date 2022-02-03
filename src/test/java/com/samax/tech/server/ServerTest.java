package com.samax.tech.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {

	@Before
	public void setUp() throws Exception {
		//Server server = new Server();
	}

	@Test
	public void test() {
		try {
			
			Socket client = null;
			
			for(int i = 0; i < 5; i++)
				client = new Socket("localhost", 8080);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
