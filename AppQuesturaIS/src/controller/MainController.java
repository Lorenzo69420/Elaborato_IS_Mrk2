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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Database;
import model.Person;
import model.PoliceStation;
import model.Reservation;

public class MainController {

	private Stage mainStage = new Stage();
	private ObservableList<String> activityList = FXCollections.observableArrayList();
	private ObservableList<String> policeStationList = FXCollections.observableArrayList();

	// Login page
	private FXMLLoader logLoader;
	private LoginController logController;
	private Scene logScene;

	// Register page
	private FXMLLoader regLoader;
	private RegisterController regController;
	private Scene regScene;

	// ActivitySelector page
	private FXMLLoader actUserLoader;
	private ActivityUserController actUserController;
	private Scene actUserScene;

	// ActivitySetter page
	private FXMLLoader actAdminLoader;
	private ActivityAdminController actAdminController;
	private Scene actAdminScene;

	// MessagePrompt page
	private FXMLLoader msgPromptLoader;
	private MessagePromptController msgPromptController;
	private Scene msgPromptScene;

	// Person from login operations
	private Person currentPerson;
	
	// AddTutor Page
	private FXMLLoader addTutorLoader;
	private AddTutorController addTutorController;
	private Scene addTutorScene;

	public EventHandler<ActionEvent> getCloseHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				getPromptController().getStage().close();
			}
		};
	}
	
	public MainController() throws IOException, SQLException {
		Database.init("jdbc:postgresql://localhost:5432/elaborato_is", "admin", "password", false);

		getActivityList();
		getPSList();

		// Login setup
		logLoader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
		logScene = new Scene(logLoader.load());
		logController = logLoader.getController();
		logController.setMainController(this);
		mainStage.setScene(logScene);
		mainStage.setTitle("Login");
		
		// Register setup
		regLoader = new FXMLLoader(getClass().getResource("../view/Register.fxml"));
		regScene = new Scene(regLoader.load());
		regController = regLoader.getController();
		regController.setMainController(this);
		// Activity Selector setup
		actUserLoader = new FXMLLoader(getClass().getResource("../view/ActivityReservation.fxml"));
		actUserController = new ActivityUserController();
		actUserLoader.setController(actUserController);
		actUserScene = new Scene(actUserLoader.load());
		actUserController.setDescriptionText();
		actUserController.setMainController(this);
		actUserController.addAll();
		populateActivityReservation();
		// Activity Setter setup
		actAdminLoader = new FXMLLoader(getClass().getResource("../view/ActivityReservation.fxml"));
		actAdminController = new ActivityAdminController();
		actAdminLoader.setController(actAdminController);
		actAdminScene = new Scene(actAdminLoader.load());
		actAdminController.setDescriptionText();
		actAdminController.setMainController(this);
		actAdminController.addAll();
		populateActivitySetter();
		// MessagePrompt setup
		msgPromptLoader = new FXMLLoader(getClass().getResource("../view/MessagePrompt.fxml"));
		msgPromptScene = new Scene(msgPromptLoader.load());
		msgPromptController = msgPromptLoader.getController();
		msgPromptController.setMC(this);
		msgPromptController.setScene(msgPromptScene);
		
		// AddTutor setup 
		addTutorLoader = new FXMLLoader(getClass().getResource("../view/AddTutor.fxml"));
		addTutorScene = new Scene(addTutorLoader.load());
		addTutorController = addTutorLoader.getController();
		addTutorController.setScene(addTutorScene);
		addTutorController.setMC(this);
		
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
		mainStage.setScene(actUserScene);
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

	private void populateActivitySetter() {
		actAdminController.populateSelector(activityList, policeStationList);
	}

	private void populateActivityReservation() {
		actUserController.populateSelector(activityList, policeStationList);
	}

	private void getActivityList() {
		Arrays.asList(Reservation.ReservationType.values()).forEach(type -> activityList.add(type.toDisplayString()));
	}

	private void getPSList() throws SQLException {
		policeStationList.addAll(PoliceStation.getStations());
	}

	public void close() {
		mainStage.close();
	}
}
