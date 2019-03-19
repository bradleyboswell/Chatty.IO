package chatty.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server {

	public static void main(String[] args) throws UnknownHostException, IOException {
		int port = 1500; //Arbitrary
		String ServerIp = getServerIP();
		
		ArrayList<ClientThread> clientList = new ArrayList<ClientThread>();
		
		Socket clientSocket;
		
		PrintWriter bw;
		BufferedReader br;
		
		
		ServerSocket ssocket = new ServerSocket(port);
		System.out.println("Server Started on IP:Port: "+ ServerIp + ":" + ssocket.getLocalPort());
		clientSocket = ssocket.accept();
		System.out.println("Client accepted on IP:Port: " + clientSocket.getLocalAddress() + ":" + clientSocket.getPort());
		
//		clientList.add(new ClientThread(clientSocket));
		
	}

	private static String getServerIP() throws IOException {
		return new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream())).readLine();
	}

}
