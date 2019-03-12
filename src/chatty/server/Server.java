package chatty.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {

	public static void main(String[] args) throws UnknownHostException, IOException {
		int port = 1500;
		Socket clientSocket;
		DataInputStream dis;
		DataOutputStream dos;
		ServerSocket ssocket = new ServerSocket(port);
		clientSocket = ssocket.accept();
		
		
		
	}

}
