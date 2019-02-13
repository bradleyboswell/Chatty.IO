package chatty.server;

import java.io.Serializable;
import java.util.function.Consumer;

public class NewServer extends Connection {

	private int portNum;  //port number to listen to incoming connections
	
	
	public NewServer(int portNum, Consumer<Serializable> onCall) {
		super(onCall);
		this.portNum = portNum;
	}
	
	@Override
	protected String getIpAddress() {
		return null;    //dont need server ip 
	} 

	@Override
	protected int getPortNum() {
		return portNum;
	}
	
	@Override
	protected boolean isServer() {
		return true;
	}


}
