package controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Database;
import model.NoSuchUserException;
import model.Person;

public class LoginController {

	@FXML
	private TextField IDField;

	@FXML
	private Label IDLabel;

	@FXML
	private Button adminLogButton;

	@FXML
	private Button exitButton;

	@FXML
	private Button logButton;

	@FXML
	private Button regButton;

	private String ID;
	private Person logPerson;
	private MainController mainController;

	@FXML
	void adminLogReq(ActionEvent event) {
		if (!checkField()) {
			getMC().showMessagePrompt("Inserisci un Codice Fiscale valido", getMC().getCloseHandler());
			return;
		}
		
		try {
			this.logPerson = Person.get(IDField.getText());

			if (logPerson.isAdmin()) {
				mainController.switchToActivitySetter();
			} else {
				getMC().showMessagePrompt("L'utente selezionato non è admin", getMC().getCloseHandler());
			}
		} catch (NoSuchUserException e) {
			getMC().showMessagePrompt(
					"Il codice fiscale inserito non corrisponde a nessuna persona nell'anagrafica, "
							+ "per ulteriori chiarementi contattare la mail aiuto@questura.anagrafica.it",
					getMC().getCloseHandler());
		} catch (SQLException E) {
			System.out.println("Database compro-fjDSVIAM...Database compromised");
		}
	}

	@FXML
	void exitApp(ActionEvent event) {
		mainController.close();
	}

	@FXML
	void logReq(ActionEvent event) {
		if (!checkField()) {
			getMC().showMessagePrompt("Inserisci un Codice Fiscale valido", getMC().getCloseHandler());
			return;
		}
		
		try {
			this.logPerson = Person.get(IDField.getText());

			if (logPerson.isRegister()) {
				mainController.switchToActivityReservation();
			} else {
				getMC().showMessagePrompt("L'utente selezionato non è registrato", getMC().getCloseHandler());
			}
		} catch (NoSuchUserException e) {
			getMC().showMessagePrompt(
					"Il codice fiscale inserito non corrisponde a nessuna persona nell'anagrafica, "
							+ "per ulteriori chiarementi contattare la mail aiuto@questura.anagrafica.it",
					getMC().getCloseHandler());
		} catch (SQLException E) {
			System.out.println("Database compro-fjDSVIAM...Database compromised");
		}

	}

	@FXML
	void setName(ActionEvent event) {
		if (checkField()) {
			getMC().showMessagePrompt("Inserisci un ID valido", getMC().getCloseHandler());
		} else {
			this.ID = IDField.getText();
		}
	}

	@FXML
	void switchReg(ActionEvent event) {
		mainController.switchToRegister();
	}

	public void setMainController(MainController mC) {
		mainController = mC;
	}

	public Person getLogPerson() {
		return logPerson;
	}

	public MainController getMC() {
		return mainController;
	}
	
	private boolean checkField() {
		return !IDField.getText().equals("");
	}
}
