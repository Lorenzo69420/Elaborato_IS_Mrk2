package controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.DatabaseManager;

public class MainController {
	
	private Stage mainStage = new Stage();
	
	//Login page
	private FXMLLoader logLoader; 
	private BorderPane logPane;
	private LoginController logController;
	private Scene logScene;
	
	//Register page
	private FXMLLoader regLoader; 
	private BorderPane regPane;
	private RegisterController regController;
	private Scene regScene;
	
	//ActivitySelector page
	private FXMLLoader actLoader; 
	private BorderPane actPane;
	private ActivityReservationController actController;
	private Scene actScene;
	
	public MainController () throws IOException, SQLException {
		DatabaseManager.init("jdbc:postgresql://localhost:5432/elaborato_is", "admin", "password");
		// Login setup
		logLoader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
		logPane = logLoader.load();
		logController = logLoader.getController();
		logController.setMC(this);
		logScene = new Scene(logPane);
		mainStage.setScene(logScene);
		// Register setup
		regLoader = new FXMLLoader(getClass().getResource("../view/Register.fxml"));
		regPane = regLoader.load();
		regController = regLoader.getController();
		regController.setMC(this);
		regScene = new Scene(regPane);
		// Activity Selector setup
		actLoader = new FXMLLoader(getClass().getResource("../view/ActivityReservation.fxml"));
		actPane = actLoader.load();
		actController = actLoader.getController();
		actController.setMC(this);
		actScene = new Scene(actPane);
	}
	public void start() {
		mainStage.show();
	}
	public void switchToLogin() {
		mainStage.setScene(logScene);
		start();
	}
	public void switchToRegister() {
		mainStage.setScene(regScene);
		start();
	}
	public void switchToActivitySelector() {
		mainStage.setScene(actScene);
		start();
	}
}