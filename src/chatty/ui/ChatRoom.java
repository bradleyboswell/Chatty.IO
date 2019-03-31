package chatty.ui;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import chatty.client.NewClient;
import chatty.server.Connection;
import chatty.server.NewServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChatRoom extends Application{
	
	private Stage stage;
	
	private String username;
	private String ip;
	private int port;
	
	private boolean isServer;
	
	private Connection connection;
	
	private TextArea chats = new TextArea();
	
	private VBox createLogin() {
		VBox login = new VBox();
		HBox buttonBar = new HBox();
		
		login.setSpacing(10);
		login.setPadding(new Insets(5));
		login.setAlignment(Pos.CENTER_LEFT);
		login.setMinWidth(300);
		
		buttonBar.setSpacing(50);
		buttonBar.setPadding(new Insets(10));
		buttonBar.setAlignment(Pos.CENTER);
		
		Label usernameLabel = new Label("Username: ");
		TextField usernameText = new TextField();
		Label serverIpLabel = new Label("Server IP: (leave blank if server)");
		TextField serverIpText = new TextField();
		Label portLabel = new Label("Port #: ");
		TextField portText = new TextField("55555");
		
		Button loginButton = new Button("Login");
		Button clearButton = new Button("Clear");
		Label warningLabel = new Label("");
		warningLabel.setTextFill(Color.RED);
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					port = Integer.parseInt(portText.getText());
				}
				catch(Exception e1) {
					portText.setText("");
				}
				
				if(usernameText.getLength() > 0 && portText.getLength() > 0) {
					try {
						username = usernameText.getText();
						ip = serverIpText.getText();
						
						startChat();
					} 
					catch(Exception e2) {
						e2.printStackTrace();
					}
				} 
				else if(usernameText.getLength() == 0 && portText.getLength() == 0) {
					portText.setText("55555");
					warningLabel.setText("Username and port # are required.");
				}
				else if(usernameText.getLength() == 0) {
					warningLabel.setText("Username is required.");
				}
				else {
					portText.setText("55555");
					warningLabel.setText("Port # is required.");
				}
			}
		});
		
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				usernameText.setText("");
				serverIpText.setText("");
				portText.setText("");
			}
		});
		
		buttonBar.getChildren().addAll(loginButton, clearButton);
		
		login.getChildren().addAll(usernameLabel, usernameText, serverIpLabel, serverIpText, portLabel, portText, warningLabel, buttonBar);
		return login;
	}
	
	private Parent createChat() {
		HBox inputPane = new HBox();
		VBox chatWindow = new VBox(10, chats, inputPane);
		
    	DateFormat timeFormat = new SimpleDateFormat("hh:mm"); // 12 hour time format
    	String time = timeFormat.format(new Date()).toString();
		
		TextField userIn = new TextField(); // input for chat
		Button sendButton = new Button("Send");
    	
		chats.setEditable(false);
		chats.setPrefHeight(500);
		chats.setFocusTraversable(false);

		String joined = time + " " + username + " has joined the chat.";
		
		if(isServer == false) {
			try {
				Thread.sleep(10);
				chats.appendText(joined + "\n");
				connection.send(joined);
			}
			catch (Exception ex) {
				chats.appendText("You were unable to join the chat...\n");
				ex.printStackTrace();
			}
		}
		else {
			chats.appendText(time + " Server: " + username + " has been created.\n");
		}

		sendButton.setOnAction(new EventHandler<ActionEvent>() { // send message to server using button
			public void handle(ActionEvent e) {
				String message = time + " " + username + ": ";
				message += userIn.getText();
				userIn.clear();
				
				chats.appendText(message + "\n");
				
				try {
					connection.send(message);
				} 
				catch (Exception ex) {
					chats.appendText("Message could not be delivered at this time...\n");
					ex.printStackTrace();
				}
			}
		});
		
		userIn.setOnAction(ev -> { // send message to server using keyboard
			String message = time + " " + username + ": ";
			message += userIn.getText();
			userIn.clear();
			
			chats.appendText(message + "\n");
			
			try {
				connection.send(message);
			} 
			catch (Exception ex) {
				chats.appendText("Message could not be delivered at this time...\n");
				ex.printStackTrace();
			}	
		});

		HBox.setHgrow(userIn, Priority.ALWAYS);
		HBox.setHgrow(sendButton, Priority.NEVER);
		
		inputPane.setAlignment(Pos.BOTTOM_RIGHT);
		inputPane.setSpacing(10);
		inputPane.setPadding(new Insets(10));
		inputPane.getChildren().addAll(userIn, sendButton);
		
		chatWindow.setPrefSize(500,500);
		
		return chatWindow;
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
		return new NewClient(ip, port, dataStream->{  //Consumer function to create a client on the internalIP(multiple machines on same network) or externalServerIP(for internet)
			Platform.runLater(()->{								//Run thread in background so we can still control the UI
				//chats.appendText("New client joined on: 127.0.0.1:55555");
				chats.appendText(dataStream.toString()+"\n");				
			});
		});
	}
	
	//Launch a new server
	public NewServer launchServer() {				
		return new NewServer(port, dataStream -> {		//Consumer function to receive the data and append the incoming messages
			Platform.runLater(()->{						//Use platform.runLater since we are using a background thread and want control in the ui thread
				//chats.appendText("Started server on port: 55555" );
				chats.appendText(dataStream.toString() + "\n");  //Convert the serializable object to a string and print
			});
		});	
	}
	
//	@Override
//	public void init() throws Exception {		//Init gets called before start method and called by JFX framework
//		connection.open();  //Open a connection on startup
//	}
	
	@Override
	public void start(Stage pStage) throws Exception {
		stage = pStage;
		pStage.setScene(new Scene(createLogin()));
		pStage.setTitle("Chatty.IO");
		pStage.show();		
	}
	
	public void startChat() throws Exception {
		if(ip.length() == 0) {
			isServer = true;
			InetAddress localhost = InetAddress.getLocalHost();
			ip = localhost.getHostAddress();
			connection = launchServer();
		} 
		else {
			isServer = false;
			connection = launchClient();
		}
		
		connection.open();
		
		stage.setScene(new Scene(createChat()));
		
		if(isServer) {
			stage.setTitle(username + " (Server IP: " + ip + ")");
		} 
		else {
			stage.setTitle(username);
		}
		
		stage.show();
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
