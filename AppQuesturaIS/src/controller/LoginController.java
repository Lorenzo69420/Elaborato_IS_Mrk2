package controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.NoSuchUserException;
import model.Person;

public class LoginController extends AbstractController {

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

	protected LoginController(MainController MC) {
		super("Login", MC);
	}

	@FXML
	protected void adminLogReq(ActionEvent event) {
		if (!checkField()) {
			getMC().showMessagePrompt("Inserisci un Codice Fiscale valido", getMC().getCloseHandler(), true);
			return;
		}

		try {
			this.logPerson = Person.get(IDField.getText());

			if (logPerson.isAdmin()) {
				getMC().switchToActivitySetter();
			} else {
				getMC().showMessagePrompt("L'utente selezionato non è admin", getMC().getCloseHandler(), true);
			}
		} catch (NoSuchUserException e) {
			getMC().showMessagePrompt(
					"Il codice fiscale inserito non corrisponde a nessuna persona nell'anagrafica, "
							+ "per ulteriori chiarementi contattare la mail aiuto@questura.anagrafica.it",
					getMC().getCloseHandler(), true);
		} catch (SQLException E) {
			System.out.println("Database compro-fjDSVIAM...Database compromised");
		}
	}

	@FXML
	protected void exitApp(ActionEvent event) {
		getMC().close();
	}

	@FXML
	protected void logReq(ActionEvent event) {
		if (!checkField()) {
			getMC().showMessagePrompt("Inserisci un Codice Fiscale valido", getMC().getCloseHandler(), true);
			return;
		}

		try {
			this.logPerson = Person.get(IDField.getText());

			if (logPerson.isRegister()) {
				getMC().switchToActivityReservation();
			} else {
				getMC().showMessagePrompt("L'utente selezionato non è registrato", getMC().getCloseHandler(), true);
			}
		} catch (NoSuchUserException e) {
			getMC().showMessagePrompt(
					"Il codice fiscale inserito non corrisponde a nessuna persona nell'anagrafica, "
							+ "per ulteriori chiarementi contattare la mail aiuto@questura.anagrafica.it",
					getMC().getCloseHandler(), true);
		} catch (SQLException E) {
			System.out.println("Database compro-fjDSVIAM...Database compromised");
		}

	}

	@FXML
	protected void setName(ActionEvent event) {
		if (checkField()) {
			getMC().showMessagePrompt("Inserisci un ID valido", getMC().getCloseHandler(), true);
		} else {
			this.ID = IDField.getText();
		}
	}

	@FXML
	protected void switchReg(ActionEvent event) {
		getMC().switchToRegister();
	}

	protected Person getLogPerson() {
		return logPerson;
	}

	private boolean checkField() {
		return !IDField.getText().equals("");
	}
}
