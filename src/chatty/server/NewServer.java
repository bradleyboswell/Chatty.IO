package chatty.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class NewServer {

	public static final int portnum = 1500;

	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket sSocket = new ServerSocket(portnum);
		System.out.println("Server opened on " + sSocket.getLocalSocketAddress() + ":" + sSocket.getLocalPort() + ":" + sSocket.getInetAddress());
		Socket socket = sSocket.accept();
		System.out.println("Connection accepted from " + socket.getRemoteSocketAddress());

		DataInputStream din = new DataInputStream(socket.getInputStream());
		DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

		Scanner type = new Scanner(System.in);
		
		String chatmsg = "";
		String servmsg = "";
		System.out.println("checking for messages from client...");
		Thread.sleep(3000);
		
		chatmsg = din.readUTF();
		System.out.println("message from client " + socket.getRemoteSocketAddress() + ": " + chatmsg);
		
		
		type.close();

		socket.close();
		sSocket.close();
	}

}
