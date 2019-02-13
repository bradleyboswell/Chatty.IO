package chatty.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public static final int portnum = 3000;
	public static ServerSocket serversocket = null;
	
	public static void main(String[] args) {
		//Open a socket on the portnumber
		try {
			serversocket = new ServerSocket(3000);
			System.out.println("Listening on port " + portnum +"...");
		}catch (IOException ex) {
			System.out.println("Failed to open a socket on port "+portnum);
			System.exit(1);
		}

	}

}
