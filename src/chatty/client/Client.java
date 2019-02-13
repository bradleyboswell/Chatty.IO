package chatty.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import chatty.server.SThread;
import chatty.server.Server;
import chatty.ui.ChatRoom;

public class Client {
	public static final String hostName = "localhost";
	public static final int portNum = 3000;
	
	public Scanner uIn;
	public String uName;
	public String sHost;
	public int sPort;
	
	public Client(String uName, int sPort, String sHost) {
		this.uName = uName;
		this.sPort = portNum;
		this.sHost = hostName;
	}
	
	
	public void launch(Scanner sc) {
		try {
			Socket socket = new Socket(sHost,sPort);
			Thread.sleep(1000);
			
			SThread sThread = new SThread(socket,uName);
			Thread authThread = new Thread(sThread);
			authThread.start();
			
			while(authThread.isAlive()) {
				if(sc.hasNextLine()) {
					sThread.appendData(sc.nextLine());
				}else {
					Thread.sleep(200);   //Optional
				}
			}
			
			
		}catch(IOException | InterruptedException ex) {
			System.out.println("Connection Error!");
			ex.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String nameIn = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("Input a username");
		while (nameIn == null) {
			nameIn = sc.nextLine();
		}
		
		Client user = new Client(nameIn,portNum,hostName);
		user.launch(sc);
	}

}
