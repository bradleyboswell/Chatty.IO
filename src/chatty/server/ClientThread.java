package chatty.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class ClientThread implements Runnable{
	
	PrintWriter pw;
	BufferedReader br;
	int id = -1;
	
	Socket socket;
	public ClientThread(Socket newsocket) throws IOException {
		socket = newsocket;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream(), true);
		System.out.println("client thread created");
	}
	

	public void run() {
		System.out.println("thread started");
		while(true) {
			System.out.println(id + ": checking input stream...");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(br.ready()) {
					System.out.println("clientthread buffered reader detected message from client. Attempting to send message to other clients");
					for(int i = 0; i < Server.clientList.size(); i++)
//					Server.clientList.elementAt(i).pw.println(br.readLine());
					Server.clientList.elementAt(i).getPw().println(socket.getRemoteSocketAddress() + " " + System.currentTimeMillis() + ":"+br.readLine());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int i) {
		id = i;
	}


	public PrintWriter getPw() {
		return pw;
	}


	public void setPw(PrintWriter pw) {
		this.pw = pw;
	}
	

}
