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
	private BorderPane logPane;
	private LoginController logController;
	private Scene logScene;

	// Register page
	private FXMLLoader regLoader;
	private BorderPane regPane;
	private RegisterController regController;
	private Scene regScene;

	// ActivitySelector page
	private FXMLLoader actLoader;
	private BorderPane actPane;
	private ActivityReservationController actController;
	private Scene actScene;

	// ActivitySetter page
	private FXMLLoader actSetLoader;
	private BorderPane actSetPane;
	private ActivitySetterController actSetController;
	private Scene actSetScene;

	// MessagePrompt page
	private FXMLLoader msgPromptLoader;
	private BorderPane msgPromptPane;
	private MessagePromptController msgPromptController;
	private Scene msgPromptScene;

	// Person from login operations
	private Person currentPerson;

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
		logPane = logLoader.load();
		logController = logLoader.getController();
		logController.setMainController(this);
		logScene = new Scene(logPane);
		mainStage.setScene(logScene);
		// Register setup
		regLoader = new FXMLLoader(getClass().getResource("../view/Register.fxml"));
		regPane = regLoader.load();
		regController = regLoader.getController();
		regController.setMainController(this);
		regScene = new Scene(regPane);
		// Activity Selector setup
		actLoader = new FXMLLoader(getClass().getResource("../view/ActivityReservation.fxml"));
		actPane = actLoader.load();
		actController = actLoader.getController();
		actController.setMainController(this);
		actController.addAll();
		actScene = new Scene(actPane);
		populateActivityReservation();
		// Activity Setter setup
		actSetLoader = new FXMLLoader(getClass().getResource("../view/ActivitySetter.fxml"));
		actSetPane = actSetLoader.load();
		actSetController = actSetLoader.getController();
		actSetController.setMainController(this);
		actSetController.addAll();
		actSetScene = new Scene(actSetPane);
		populateActivitySetter();
		// MessagePrompt setup
		msgPromptLoader = new FXMLLoader(getClass().getResource("../view/MessagePrompt.fxml"));
		msgPromptPane = msgPromptLoader.load();
		msgPromptController = msgPromptLoader.getController();
		msgPromptScene = new Scene(msgPromptPane);
		msgPromptController.setScene(msgPromptScene);
		msgPromptController.setMC(this);
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

	public void showMessagePrompt(String message, EventHandler<ActionEvent> event) {
		msgPromptController.setup(message, event);
	}

	public MessagePromptController getPromptController() {
		return msgPromptController;
	}

	public void switchToActivitySetter() {
		mainStage.setScene(actSetScene);
		start();
	}

	private void populateActivitySetter() {
		// actSetController.getActSelector().getItems().addAll(activityList);
		// actSetController.getPSSelector().getItems().addAll(policeStationList);
		actSetController.populateSelector(activityList, policeStationList);
	}

	private void populateActivityReservation() {
		// actController.getActSelector().getItems().addAll(activityList);
		// actController.getPSSelector().getItems().addAll(policeStationList);
		actController.populateSelector(activityList, policeStationList);
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
