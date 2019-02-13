package chatty.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import chatty.ui.ChatRoom;

public class SThread implements Runnable{
	public Socket socket;
	public String username;
	public boolean status;
	public LinkedList<String> dataStrings;
	public boolean hasMessages = false;
	//public ChatRoom cRoom = new ChatRoom();
	
	
	public SThread(Socket socket, String username) {
		this.socket = socket;
		this.username = username;
		dataStrings = new LinkedList<String>();
	}
	
	public void appendData(String data) {
		synchronized (dataStrings) {
			dataStrings.push(data);
			hasMessages = true;
		}
	}
	
	@Override
	public void run() {
			System.out.println("Welcome :" + username);
			
			System.out.println("Local Port:" + socket.getLocalPort());
			System.out.println("Server =" + socket.getRemoteSocketAddress() + ":" + socket.getPort());
			
		try {	
			//PrintWriter serveOut = new PrintWriter(socket.getOutputStream(),true);
			PrintWriter servOut = new PrintWriter(socket.getOutputStream());
			
			InputStream servInData = socket.getInputStream();
			Scanner servSc = new Scanner(servInData);
			
			while(!socket.isClosed()) {
				if(servInData.available()!=0) {
					if(servSc.hasNextLine()) {
						System.out.println(servSc.nextLine());
					}
				}
				if(hasMessages) {
					String chatMsg = "";
					synchronized(dataStrings) {
						chatMsg = dataStrings.pop();
						hasMessages = !dataStrings.isEmpty();
					}
					//cRoom.chats.appendText(username + ": "+chatMsg);
					servOut.println(username+": "+chatMsg);
					servOut.flush();
					//ORRRR
					//No flush and use serveOut ?
				}
				
			}	
		} catch (IOException e) {
			System.out.println("An error occured...");
			e.printStackTrace();
		}
		
	}

}
