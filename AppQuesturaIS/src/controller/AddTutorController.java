package controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.NoSuchUserException;
import model.Person;

public class AddTutorController extends AbstractController {

	protected AddTutorController(MainController MC) {
		super("AddTutor", MC);
		stage.setScene(getScene());
	}

	private Person minor;

	private Stage stage = new Stage();

	@FXML
	private TextField IDField;

	@FXML
	void addTutor(ActionEvent event) {
		String taxID = IDField.getText();
		Person tutor;
		if (taxID == null) {
			getMC().showMessagePrompt("Il codice fiscale non è valido", getMC().getCloseHandler(), true);
		} else {
			try {
				tutor = Person.get(taxID);
				if (tutor.getTaxID().equals(minor.getTaxID())) {
					getMC().showMessagePrompt("Non puoi essere il tutore di te stesso", getMC().getCloseHandler(),
							true);
				} else if (!tutor.isAdult()) {
					getMC().showMessagePrompt("L'utente non è di maggiore età", getMC().getCloseHandler(), true);
				} else {
					minor.register();
					minor.addTutor(tutor);
					getMC().switchToLogin();
					stage.close();
				}
			} catch (SQLException | NoSuchUserException e) {
				if (e instanceof NoSuchUserException) {
					getMC().showMessagePrompt("Utente non trovato", getMC().getCloseHandler(), true);
					return;
				}
				e.printStackTrace();
			}
		}
	}

	protected void setup(Person minor) {
		this.minor = minor;
		stage.show();

	}
}
