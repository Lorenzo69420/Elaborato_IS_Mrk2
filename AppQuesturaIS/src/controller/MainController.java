package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;
import model.Person;
import model.PoliceStation;
import model.Reservation;

public class MainController {

	private Stage mainStage;
	public MainController (Stage stage) {
		mainStage = stage;
	} 
	// Login page
	
	private LoginController logController;
	private Scene logScene;

	// Register page
	
	private RegisterController regController;
	private Scene regScene;

	// ActivitySelector page
	
	private ActivityUserController actUserController;

	// ActivitySetter page
	
	private ActivityAdminController actAdminController;
	private Scene actAdminScene;

	// MessagePrompt page
	
	private MessagePromptController msgPromptController;

	// Person from login operations
	private Person currentPerson;
	
	// AddTutor Page
	private AddTutorController addTutorController;

	public EventHandler<ActionEvent> getCloseHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				getPromptController().getStage().close();
			}
		};
	}
	
	public MainController() {
		try {
			Database.init("jdbc:postgresql://localhost:5432/elaborato_is", "admin", "password", false);
		} catch (SQLException | RuntimeException e) {
			e.printStackTrace();
		}

		// Login setup
		
		logController = new LoginController(this);
		logScene = logController.getScene();
		mainStage.setScene(logScene);
		mainStage.setTitle("Login");
		
		
		// Register setup
		
		regController = new RegisterController(this);
		regScene = regController.getScene();
		
		
		// Activity Selector setup
		actUserController = new ActivityUserController(this);
		
		// Activity Setter setup
		actAdminController = new ActivityAdminController(this);
		actAdminScene = actAdminController.getScene();
		
		// MessagePrompt setup
		msgPromptController = new MessagePromptController(this);
		
		// AddTutor setup 
		addTutorController = new AddTutorController(this);
	}

	public void start() {
		mainStage.show();
	}

	public void switchToLogin() {
		mainStage.setScene(logScene);
		mainStage.setTitle("Login");
		start();
	}

	public void switchToRegister() {
		regController.emptySelector();
		mainStage.setScene(regScene);
		mainStage.setTitle("Registrazione");
		start();
	}

	public void switchToActivityReservation() {
		actUserController.emptySelector();
		currentPerson = logController.getLogPerson();
		actUserController.setPerson(currentPerson);
		mainStage.setScene(actUserController.getScene());
		mainStage.setTitle("Prenotazione");
		start();
	}

	public void showMessagePrompt(String message, EventHandler<ActionEvent> event) {
		msgPromptController.setup(message, event);
	}
	
	public void showAddTutor(Person minor) {
		addTutorController.setup(minor);
	}

	public MessagePromptController getPromptController() {
		return msgPromptController;
	}

	public void switchToActivitySetter() {
		actAdminController.emptySelector();
		mainStage.setScene(actAdminScene);
		mainStage.setTitle("Disponibilità");
		start();
	}

	public void close() {
		try {
			Database.close();
		} catch (Exception e ) {
			e.printStackTrace();
		}
		mainStage.close();
	}
}
