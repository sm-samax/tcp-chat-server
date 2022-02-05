## TCP Chat server

<p>The mentioned server is made with <i>java.net</i> sockets and can accept multiple client and communicate with them individually.</p>
<p>The project contains both client and server with their related GUI made with <i>java.swing</i>.</p>
<p>The graphical components offer convenient way to server starting at a given address and port, furthermore to client connecting to custom server.</p>
<p>Chatting takes place in the user interface of clients.</p>

### Components

<p>Client side:</p>
<ul>
	<li>Client - represents the client.</li>
	<li>ClientGUI - the actual chat application. Users can join running servers and send messages on it.</li>
	<li>ConnectionDialog - part of client's GUI, queries the address and port of the server to connect.</li>
</ul>

<p>Server side:</p>
<ul>
	<li>ChatServer - implementation of chat server.</li>
	<li>ChatServerGUI - user interface for ChatServer. It can run and stop server on given port and address.</li>
	<li>ClientService - individual thread for joined clients. With this the server can handle multiple client.</li>
</ul>

### Graphical user interface

<p>Made straightforward to use and handles all illegal inputs.</p>

<p></p>

