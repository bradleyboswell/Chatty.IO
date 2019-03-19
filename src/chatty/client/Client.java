package chatty.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException {

		byte[] ipAddr = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
		Socket socket = new Socket(InetAddress.getByAddress("68.234.253.116", ipAddr), 1500);
	}

}
