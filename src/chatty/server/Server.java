package chatty.server;

import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server implements Runnable {

	public static final int servPort = 1500;

	private static ArrayList<Server> connectedClients;
	
	private int sPort;
	private Socket socket;
	private PrintWriter cOutput = null;

	public Server(int port) {
		this.sPort = port;
	}
	public Server(Socket socket) {
		this.socket = socket;
		connectedClients.add(this);
	}

	public static void main(String[] args) {
		Server mainServer = new Server(servPort);
		mainServer.launch();
	}

	@Override
	public void run() {
		try {
			cOutput = new PrintWriter(socket.getOutputStream(),false);
			Scanner sc = new Scanner(socket.getInputStream());


			while(!socket.isClosed()) {
				if(sc.hasNextLine()) {
					String in = sc.nextLine();

					Server.getConnectedClients().forEach(Server->{
						PrintWriter cOut = Server.getWriter();
						if(cOut != null) {
							cOut.write(in + "\r\n");
							cOut.flush();
						}
					});

				}
			}

			System.out.println("socket " + socket.getLocalPort() + " is closed");

			sc.close();
		} catch (IOException e) {	
			System.out.println("Error sending data...");
			e.printStackTrace();
		}
	}

	private PrintWriter getWriter() {

		return cOutput;
	}

	public static ArrayList<Server> getConnectedClients() {

		return connectedClients;
	}

	public void launch() {
		connectedClients = new ArrayList<Server>();
		try {
			ServerSocket sSocket = new ServerSocket(sPort);
			clientSearch(sSocket);
		} catch (IOException e) {
			System.out.println("can't bind to socket: " + sPort);
			e.printStackTrace();
		}

	}

	public void clientSearch(ServerSocket sSocket) {
		System.out.println("Server Started Port: " + sSocket.getLocalSocketAddress());
		while(true) {
			try {
				Socket socket = sSocket.accept();
				Server newClientThread = new Server(socket); 
				newClientThread.run();

			} catch (IOException e) {
				System.out.println("Failed to accept client on port: " + sSocket.getLocalPort());
				e.printStackTrace();
			}


		}

	}




}
