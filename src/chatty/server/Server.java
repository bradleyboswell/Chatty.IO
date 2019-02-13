package chatty.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import chatty.client.CThread;

public class Server {
	public static final int portnum = 3000;
	
	public int sPort;
	public List<CThread> connectedClients;    //List to hold threads for connected clients on the server
	
	
	public Server(int port) {
		this.sPort = port;
	}
	
	public void launch() {
		ServerSocket sSocket = null;
		connectedClients = new ArrayList<CThread>();
		try {
			sSocket = new ServerSocket(sPort);
			clientSearch(sSocket);
		}catch(IOException ex) {
			System.out.println("Failed to listen on Port: "+sPort);
			System.exit(1);
		}
	}
	
	
	//Continuously poll for incoming clients on the server
	public void clientSearch(ServerSocket sSocket) {
		
		System.out.println("Server Started Port: " + sSocket.getLocalSocketAddress());
		while(true) {
			try {
				Socket socket = sSocket.accept();
				CThread newclient = new CThread(socket,this);
				Thread newthread = new Thread(newclient);
				newthread.start();
				connectedClients.add(newclient);
				System.out.println(connectedClients.size());
				//System.out.println("New client accepted!");
			}catch (IOException ex) {
				System.out.println("Failed to accept the client on port number:" + sSocket.getLocalPort());
			}
		}
	}
	
	public List<CThread> getConnectedClients(){
		return this.connectedClients;
	}
	
	public static void main(String[] args) {
		Server server = new Server(portnum);
		server.launch();
	}

}
