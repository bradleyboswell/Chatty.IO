package chatty.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class client2 {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		//		byte[] ipAddr = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
		//68.234.253.116
		Socket socket = new Socket("localhost", 1500);
		OutputStreamWriter bw = new OutputStreamWriter(socket.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		Thread.sleep(4000);
		System.out.println("client 2 writing to stream");
		bw.write("asdf\n");
		bw.write("fds\n");
		bw.flush();
		Thread.sleep(1000);
		while(true) {
		Thread.sleep(500);
		System.out.println("client 2 checking messages...");
		if(br.ready()) {
		System.out.println(br.readLine());
		}
		}
//		socket.close();
	}

}
