package chatty.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatRoom extends Application{
	
	public TextArea chats = new TextArea();
	
	public Parent create() {
		TextField userIn = new TextField();
		chats.setPrefHeight(600);
		VBox rootPanel = new VBox(20, chats, userIn);
		rootPanel.setPrefSize(600,600);
		return rootPanel;	
	}
	
	@Override
	public void start(Stage pStage) throws Exception {
		pStage.setScene(new Scene(create()));
		pStage.show();		
	}
	
	public static void main(String[] args) {
		launch(args);

	}


}
