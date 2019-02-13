package chatty.client;

import java.io.Serializable;
import java.util.function.Consumer;

import chatty.server.Connection;

public class NewClient extends Connection {

	private int portNum;
	private String ipAddress;
	
	public NewClient(String ipAddress, int portNum, Consumer<Serializable> onCall) {
		super(onCall);
		this.ipAddress = ipAddress;
		this.portNum = portNum;
	}
 
	@Override
	protected String getIpAddress() {
		return ipAddress;
	}

	@Override
	protected int getPortNum() {
		return portNum;
	}

	@Override
	protected boolean isServer() {
		return false;
	}

}
