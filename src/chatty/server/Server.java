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
	private String user;

	public Server(int port) { //mainServer
		this.sPort = port;
	}
	public Server(Socket socket, String user) { //thread for each socket
		this.socket = socket;
		this.user = user;
		
	}

	public static void main(String[] args) throws InterruptedException {
		Server mainServer = new Server(servPort);
		mainServer.launch();
	}

	@Override
	public void run() {
		try {
			System.out.println("Connected: " + this.socket.getRemoteSocketAddress());

			cOutput = new PrintWriter(this.socket.getOutputStream(),false);
			Scanner sc = new Scanner(this.socket.getInputStream());
			while(!sc.hasNext()) {
			this.user = sc.nextLine();
			}
			System.out.println("Username: " + this.user);

			while(!this.socket.isClosed()) {
				if(sc.hasNextLine()) {
					String in = sc.nextLine();

					Server.getConnectedClients().forEach(Server->{
						PrintWriter cOut = Server.getWriter();
						if(cOut != null) {
							cOut.write(Server.user + ": " + in + "\r\n");
							cOut.flush();
						}
					});

				}
			}

			System.out.println("client with socket " + this.socket.getLocalPort() + " is closed");

			sc.close();
		} catch (IOException e) {	
			System.out.println("Error sending data...");
			e.printStackTrace();
		}
	}

	public PrintWriter getWriter() {

		return cOutput;
	}

	public static ArrayList<Server> getConnectedClients() {

		return connectedClients;
	}

	public void launch() throws InterruptedException {
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
				user = "temp";
				Server newClientThread = new Server(socket, user); 
				connectedClients.add(newClientThread);
				newClientThread.run();
				

			} catch (IOException e) {
				System.out.println("Failed to accept client on port: " + sSocket.getLocalPort());
				e.printStackTrace();
			}


		}

	}




}
