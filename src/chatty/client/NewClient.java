package chatty.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NewClient {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socket = new Socket("localhost", 1500);
		
		System.out.println("Connected to " + socket.getRemoteSocketAddress());
		
		DataInputStream din = new DataInputStream(socket.getInputStream());
		DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
		
		Scanner type = new Scanner(System.in);
		String msgToSend = "";
		String msgToReceive = "";
		
		System.out.println("Trying to write to server...");
		msgToSend = "hello from " + socket.getLocalPort();
		
		Thread.sleep(1000);
		dout.writeUTF(msgToSend);
		
		System.out.println("message sent");
		
		type.close();
		socket.close();
		
	}

}
