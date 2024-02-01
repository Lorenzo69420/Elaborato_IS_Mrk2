package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import model.NoSuchUserException;
import model.Person;

public class RegisterController {

	@FXML
	private Text errorText;
	@FXML
	private TextField IDField;

	@FXML
	private Label IDLabel;

	@FXML
	private DatePicker dateField;

	@FXML
	private Label dateLabel;

	@FXML
	private Button exitButton;

	@FXML
	private Button logButton;

	@FXML
	private TextField nameField;

	@FXML
	private Label nameLabel;

	@FXML
	private TextField placeField;

	@FXML
	private Label placeLabel;

	@FXML
	private Button regButton;

	@FXML
	private BorderPane registerPane;

	@FXML
	private TextField surField;

	@FXML
	private Label surLabel;

	private MainController mainController;
	private Person person;

	@FXML
	void appExit(ActionEvent event) {
		mainController.close();
	}

	@FXML
	void getPerson(ActionEvent event) {
		if (!checkAll()) {
			getMC().showMessagePrompt("Uno o più campi inseriti sono vuoti,\n inserisci tutti i campi in modo corretto",
					getMC().getCloseHandler());
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date.from(dateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		person = new Person(IDField.getText(), nameField.getText(), surField.getText(), placeField.getText(), calendar);

		try {
			person.exists();
			person.register();
			person = null; // :,)
			mainController.switchToLogin();
		} catch (SQLException e) {
			mainController.close();
		} catch (NoSuchUserException e) {
			// setting all label to error message
			getMC().showMessagePrompt(
					"Le credenziali indicate non corrispondono a nessuna persona nell'anagrafica, "
							+ "per ulteriori chiarementi contattare la mail aiuto@questura.anagrafica.it",
					getMC().getCloseHandler());
		}
	}

	@FXML
	void setBirhdate(ActionEvent event) {
		if (dateField.getValue().isAfter(LocalDate.now()) || dateField.getValue().toString().equals("")) {
			dateLabel.setText("Inserisci una data valida");
		} else {
			dateLabel.setText("");
		}
	}

	@FXML
	void setPlace(ActionEvent event) {
		if (placeField.getText().equals("")) {
			placeLabel.setText("Inserisci un luogo valido");
		} else {
			placeLabel.setText("");
		}
	}

	@FXML
	void setID(ActionEvent event) {
		if (IDField.getText().equals("")) {
			IDLabel.setText("Inserisci un Codice Fiscale valido");
		} else {
			IDLabel.setText("");
		}
	}

	@FXML
	void setName(ActionEvent event) {
		if (nameField.getText().equals("")) {
			nameLabel.setText("Inserisci un nome valido");
		} else {
			nameLabel.setText("");
		}
	}

	@FXML
	void setSurname(ActionEvent event) {
		if (surField.getText().equals("")) {
			surLabel.setText("Inserisci un cognome valido");
		} else {
			surLabel.setText("");
		}
	}

	@FXML
	void switchToLogin(ActionEvent event) {
		mainController.switchToLogin();
	}

	public void setMainController(MainController mC) {
		mainController = mC;
	}

	private boolean checkAll() {
		return checkName() && checkSur() && checkID() && checkDate() && checkPlace();
	}

	private boolean checkPlace() {
		return !placeField.getText().isBlank();
	}

	private boolean checkDate() {
		return dateField.getValue() != null && !dateField.getValue().toString().isBlank();
	}

	private boolean checkID() {
		return !IDField.getText().isBlank();
	}

	private boolean checkSur() {
		return !surField.getText().isBlank();
	}

	private boolean checkName() {
		return !nameField.getText().isBlank();
	}

	public MainController getMC() {
		return mainController;
	}

	public void emptySelector() {
		IDField.setText(null);
		nameField.setText(null);
		surField.setText(null);
		dateField.getEditor().clear();
		placeField.setText(null);
	}
}
