package controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Database;
import model.NoSuchUserException;
import model.Person;

public class AddTutorController {

	private MainController MC;
	private Person minor;

	private Stage stage = new Stage();

	@FXML
	private TextField IDField;

	@FXML
	void addTutor(ActionEvent event) {
		String taxID = IDField.getText();
		Person tutor;
		if (taxID == null) {
			MC.showMessagePrompt("Il codice fiscale non è valido", MC.getCloseHandler());
		} else {
			try {
				tutor = Database.getPerson(taxID);
				if (tutor.getTaxID().equals(minor.getTaxID())) {
					MC.showMessagePrompt("Non puoi essere il tutore di te stesso", MC.getCloseHandler());
				} else if (!tutor.isAdult()) {
					MC.showMessagePrompt("L'utente non è di maggiore età", MC.getCloseHandler());
				} else {
					minor.register();
					minor.addTutor(tutor);
					MC.switchToLogin();
					stage.close();
				}
			} catch (SQLException | NoSuchUserException e) {
				if (e instanceof NoSuchUserException) {
					MC.showMessagePrompt("Utente non trovato", MC.getCloseHandler());
					return;
				}
				e.printStackTrace();
			}
		}
	}

	public void setMC(MainController mC) {
		MC = mC;
	}

	public void setup(Person minor) {
		this.minor = minor;
		stage.show();

	}

	public void setScene(Scene addTutorScene) {
		this.stage.setScene(addTutorScene);
	}

}
