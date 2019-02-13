package chatty.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import chatty.server.Server;

public class CThread implements Runnable{
	public Socket socket;
	public Server server;
	public PrintWriter cOutput;
	
	public CThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}
	

	@Override
	public void run() {
		try {
			this.cOutput = new PrintWriter(socket.getOutputStream(),false);
			Scanner sc = new Scanner(socket.getInputStream());
			
		
			while(!socket.isClosed()) {
				if(sc.hasNextLine()) {
					String in = sc.nextLine();
					
					server.getConnectedClients().forEach(client->{
						PrintWriter cOut = client.getWriter();
						if(cOut != null) {
							cOut.write(in + "\r\n");
							cOut.flush();
						}
					});
					
				}
			}
		} catch (IOException e) {	
			System.out.println("Error sending data...");
			e.printStackTrace();
		}
	}
	

	public PrintWriter getWriter() {
		return cOutput;
	}

}
