package chatty.server;

import java.net.Socket;

public class ClientThread implements Runnable{
	
	Socket socket;
	public ClientThread(Socket socket) {
		this.socket = socket;
		run();
	}
	
	public void run() {
		
	}

}
