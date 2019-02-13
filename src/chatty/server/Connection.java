package chatty.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;


//Handles Server/Client Connections
public abstract class Connection {

	private Consumer<Serializable> onCall;  //Store serializable objects recieved on the network
	private ConThread connection = new ConThread();
	
	
	public Connection(Consumer<Serializable> onCall) {
		this.onCall = onCall;			//set callback
		connection.setDaemon(true);  	//run thread in background so it wont block any executions
	}
	
	
	//Open a new connection
	public void open() throws Exception{
		connection.start();  //create new conthread and execute its run function
	}
	
	//Send message on network
	public void send(Serializable dataStream) throws Exception {
		connection.output.writeObject(dataStream);		//send object to output string which gets handled by input stream
	}
		
	//Close a connection
	public void close() throws Exception {
		connection.socket.close();  //closes a connection
	}

	protected abstract String getIpAddress();   //Used to find server and connect
	protected abstract int getPortNum(); //Get the port number for connection
	protected abstract boolean isServer();   //Check if connection is client or server
	
	
	//Reads the connection thread 
	public class ConThread extends Thread{
		private Socket socket;
		private ObjectOutputStream output;
		
		@Override
		public void run() {
			try (ServerSocket s = isServer() ? new ServerSocket(getPortNum()) : null;         //If the connection is not the client, create a server
					Socket socket = isServer() ? s.accept() : new Socket(getIpAddress(),getPortNum());    //if the connection is client, connect to the server
					ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());  //declare output for data stream
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream())){    //declare input for the data stream		
				this.socket = socket;
				this.output = output;
				socket.setTcpNoDelay(true); //Disable buffering to send messages quicker
				System.out.println(input);
				
				while(true) {
					Serializable dataStream = (Serializable) input.readObject();  //Cast to serializable so we can read the dataStream
					onCall.accept(dataStream);
				}
				
			}catch (Exception ex) {
				onCall.accept("Connection Closed");  //sends error to callback
			}
		}
	}	
}
