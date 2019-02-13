package chatty.ui;

import java.io.IOException;

import chatty.client.NewClient;
import chatty.server.Connection;
import chatty.server.NewServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatRoom extends Application{
	
	private TextArea chats = new TextArea();
	
	private boolean isServer = false;    //This is a toggle for choosing if you want to launch server or client. Change this according to test condition
	
	private Connection connection = isServer ? launchServer() : launchClient() ;   //server/client logic
	
	private Parent create() {
		TextField userIn = new TextField();     //input for chat
		userIn.setOnAction(ev -> {			//Send message to server
			String message = isServer ? "Server: " : "Client: ";
			message += userIn.getText();
			userIn.clear();
			
			chats.appendText(message + "\n");
			
			try {
				connection.send(message);
			}catch (Exception ex) {
				chats.appendText("Message could not be delivered at this time...\n");
				ex.printStackTrace();
			}
			
		}); 
		
		//userIn.setOnAction(ev -> sendMessage(userIn));
			
		//Formatting JFx
		chats.setPrefHeight(600);		
		VBox rootPanel = new VBox(20, chats, userIn);
		rootPanel.setPrefSize(600,600);
		return rootPanel;	
	}
	
	//Event handler for sending message
	public void sendMessage(TextField userIn) throws Exception{    //Find a way to get UserNames (Maybe jdbc with database?) JSON objects? 
		String msg ="";		
		try{
			if(isServer) {
				msg+="Server: " + userIn.getText();
				userIn.setText("");
				chats.appendText(msg + "\n");
				
				connection.send(msg);
			}else {
				msg+="Client: " + userIn.getText();		//Instead of client: find a way to get username like in old model
				userIn.setText("");
				chats.appendText(msg + "\n");   //Add message to chats box
				
				connection.send(msg);     //Send message to the socket  [String implements serializable so no conversion needed
			}
		}catch (IOException ex) {
			chats.appendText("Message could not be sent at this time...\n");
			ex.printStackTrace();
		}
	}

	//Launch a new client
	public NewClient launchClient() {
		return new NewClient("127.0.0.1", 55555, dataStream->{  //Consumer function to create a client on the internalIP(multiple machines on same network) or externalServerIP(for internet)
			Platform.runLater(()->{								//Run thread in background so we can still control the UI
				//chats.appendText("New client joined on: 127.0.0.1:55555");
				chats.appendText(dataStream.toString()+"\n");				
			});
		});
	}
	
	//Launch a new server
	public NewServer launchServer() {				
		return new NewServer(55555, dataStream -> {		//Consumer function to receive the data and append the incoming messages
			Platform.runLater(()->{						//Use platform.runLater since we are using a background thread and want control in the ui thread
				//chats.appendText("Started server on port: 55555" );
				chats.appendText(dataStream.toString() + "\n");  //Convert the serializable object to a string and print
			});
		});	
	}
	
	@Override
	public void init() throws Exception {		//Init gets called before start method and called by JFX framework
		connection.open();  //Open a connection on startup
	}
	
	@Override
	public void start(Stage pStage) throws Exception {
		pStage.setScene(new Scene(create()));
		pStage.setTitle(isServer ? "Server" : "Client");
		pStage.show();		
	}
	
	@Override
	public void stop() throws Exception {
		connection.close();    //close the connection
	}
		
	//Main
	public static void main(String[] args) {
		launch(args);
	}
	
}
