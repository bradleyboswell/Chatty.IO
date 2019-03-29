package chatty.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Vector;

public class Server{
	static String ServerIp;
	static Vector<ClientThread> clientList;
	static Socket clientSocket;
	PrintWriter bw;
	BufferedReader br;
	static ServerSocket ssocket;
	public static void main(String[] args) throws UnknownHostException, IOException {
		int i = 0;
		int port = 1500; //Arbitrary, but must be agreed upon
		ServerIp = getServerIP();
		ssocket = new ServerSocket(port);
		clientList = new Vector<ClientThread>();
		System.out.println("Server Started on IP:Port: "+ ServerIp + ":" + ssocket.getLocalPort());
		while(true) {
			System.out.println("Checking server socket for incoming connections");
			clientSocket = ssocket.accept();
			System.out.println("Client accepted on IP:Port: " + clientSocket.getLocalAddress() + ":" + clientSocket.getPort());
			ClientThread temp = new ClientThread(clientSocket);
			Thread t = new Thread(temp);
			clientList.add(temp);
			System.out.println("clientthread added to vector. Running clientthread");
			temp.setID(i++);
			t.start();
			
		}
	}
	private static String getServerIP() throws IOException {
		return new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream())).readLine();
	}
}
