package controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.DatabaseManager;
import model.Person;
import model.Reservation;

public class MainController {
	
	private Stage mainStage = new Stage();
	private ObservableList<String> activityList = FXCollections.observableArrayList();
	private ObservableList<String> policeStationList = FXCollections.observableArrayList();
	
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
	
	//ActivitySetter page
	private FXMLLoader actSetLoader; 
	private BorderPane actSetPane;
	private ActivitySetterController actSetController;
	private Scene actSetScene;
	
	//Person from login operations
	private Person currentPerson;
	
	public MainController () throws IOException, SQLException {
		DatabaseManager.init("jdbc:postgresql://localhost:5432/elaborato_is", "admin", "password",false);
		
		getActivityList();
		getPSList();
		
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
		actController.addAll();
		actScene = new Scene(actPane);
		populateActivityReservation();
		// Activity Setter setup
		actSetLoader = new FXMLLoader(getClass().getResource("../view/ActivitySetter.fxml"));
		actSetPane = actSetLoader.load();
		actSetController = actSetLoader.getController();
		actSetController.setMC(this);
		actSetController.addAll();
		actSetScene = new Scene(actSetPane);
		populateActivitySetter();
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
	public void switchToActivityReservation() {
		currentPerson = logController.getLogPerson();
		actController.setPerson(currentPerson);
		mainStage.setScene(actScene);
		start();
	}
	public void switchToActivitySetter() {
		mainStage.setScene(actSetScene);
		start();
	}
	private void populateActivitySetter() {
		actSetController.getActSelector().getItems().addAll(activityList);
		actSetController.getPSSelector().getItems().addAll(policeStationList);
	}
	private void populateActivityReservation() {
		actController.getActSelector().getItems().addAll(activityList);
		actController.getPSSelector().getItems().addAll(policeStationList);
	}
	private void getActivityList() {
		for (var S : Reservation.ReservationType.values()) {
			activityList.add(S.toDisplayString());
		}
	}
	private void getPSList() throws SQLException{
		policeStationList.addAll(DatabaseManager.getPoliceStation());
	}
	public void close() {
		mainStage.close();
	}
	
}
