package chatty.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.*;

public class Client extends Application implements Runnable {

	public static final String hostName = "localhost";
	public static final int portNum = 1500;
	public String uName;
	public String sHost;
	public LinkedList<String> dataStrings;
	public int sPort;

	public boolean hasMessages = false;

	public Client(String uName, int sPort, String sHost) {
		this.uName = uName;
		this.sPort = portNum;
		this.sHost = hostName;
		dataStrings = new LinkedList<String>();
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter username: ");
		String name = input.nextLine();
		Client mainClient = new Client(name, portNum, hostName);
		mainClient.openSocket(input);
		//		launch();
	}
	public void openSocket(Scanner sc) {
		try {
			Socket socket = new Socket(sHost, sPort);
			InputStream servInData = socket.getInputStream();
			PrintWriter servOut = new PrintWriter(socket.getOutputStream());
			Scanner servSc = new Scanner(servInData);
			
			servOut.write(this.uName);
			
			System.out.println("Welcome: " + uName);

			System.out.println("Local Port: " + socket.getLocalPort());
			System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());


			
			while(!socket.isClosed()) {
				if(servInData.available()!=0) {
					if(servSc.hasNextLine()) {
						System.out.println(servSc.nextLine());
					}
				}
				if(sc.hasNextLine()) {
					String chatMsg = "";
						chatMsg = sc.nextLine();
					
					servOut.println(chatMsg);
					servOut.flush();
				}
			}
			socket.close();
			servSc.close();
		} catch (IOException  e) {
			System.out.println("An error occured...");
			e.printStackTrace();
		}
		
		sc.close();


	}

	@Override
	public void run() {


	}
	@Override
	public void start(Stage PrimaryStage) throws Exception {
		PrimaryStage.show();

	}

}
