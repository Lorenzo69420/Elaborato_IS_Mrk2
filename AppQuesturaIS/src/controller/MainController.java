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
	// Login page
	
	private LoginController logController;
	

	// Register page
	
	private RegisterController regController;
	

	// ActivitySelector page
	
	private ActivityUserController actUserController;

	// ActivitySetter page
	
	private ActivityAdminController actAdminController;
	

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
	
	public MainController(Stage stage) {
		//Getting the database connection
		mainStage = stage;
		try {
			Database.init("jdbc:postgresql://localhost:5432/elaborato_is", "admin", "password", false);
		} catch (SQLException | RuntimeException e) {
			e.printStackTrace();
		}
		// Login setup
		logController = new LoginController(this);
		
		// Register setup
		regController = new RegisterController(this);
	
		// Activity Selector setup
		actUserController = new ActivityUserController(this);
		
		// Activity Setter setup
		actAdminController = new ActivityAdminController(this);
		
		// MessagePrompt setup
		msgPromptController = new MessagePromptController(this);
		
		// AddTutor setup 
		addTutorController = new AddTutorController(this);
		
		//starting app
		switchToLogin();
	}

	public void start() {
		
		mainStage.show();
	}

	public void switchToLogin() {
		mainStage.setScene(logController.getScene());
		mainStage.setTitle("Login");
		start();
	}

	public void switchToRegister() {
		regController.emptySelector();
		mainStage.setScene(regController.getScene());
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
		mainStage.setScene(actAdminController.getScene());
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
