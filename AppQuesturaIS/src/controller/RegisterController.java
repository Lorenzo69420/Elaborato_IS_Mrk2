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

public class RegisterController extends AbstractController {

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

	private Person person;

	protected RegisterController(MainController MC) {
		super("Register", MC);
	}

	@FXML
	protected void appExit(ActionEvent event) {
		getMC().close();
	}

	@FXML
	protected void getPerson(ActionEvent event) {
		if (!checkAll()) {
			getMC().showMessagePrompt("Uno o più campi inseriti sono vuoti,\n inserisci tutti i campi in modo corretto",
					getMC().getCloseHandler(), true);
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date.from(dateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		person = new Person(IDField.getText(), nameField.getText(), surField.getText(), placeField.getText(), calendar);

		try {
			person.exists();
			if (person.isAdult()) {
				person.register();
				person = null; // :,)
				getMC().switchToLogin();
			} else {
				getMC().showAddTutor(person);
			}

		} catch (SQLException e) {
			getMC().close();
		} catch (NoSuchUserException e) {
			// setting all label to error message
			getMC().showMessagePrompt(
					"Le credenziali indicate non corrispondono a nessuna persona nell'anagrafica, "
							+ "per ulteriori chiarementi contattare la mail aiuto@questura.anagrafica.it",
					getMC().getCloseHandler(), true);
		}
	}

	@FXML
	protected void setBirhdate(ActionEvent event) {
		if (dateField.getValue().isAfter(LocalDate.now()) || dateField.getValue().toString().equals("")) {
			dateLabel.setText("Inserisci una data valida");
		} else {
			dateLabel.setText("");
		}
	}

	@FXML
	protected void setPlace(ActionEvent event) {
		if (placeField.getText().equals("")) {
			placeLabel.setText("Inserisci un luogo valido");
		} else {
			placeLabel.setText("");
		}
	}

	@FXML
	protected void setID(ActionEvent event) {
		if (IDField.getText().equals("")) {
			IDLabel.setText("Inserisci un Codice Fiscale valido");
		} else {
			IDLabel.setText("");
		}
	}

	@FXML
	protected void setName(ActionEvent event) {
		if (nameField.getText().equals("")) {
			nameLabel.setText("Inserisci un nome valido");
		} else {
			nameLabel.setText("");
		}
	}

	@FXML
	protected void setSurname(ActionEvent event) {
		if (surField.getText().equals("")) {
			surLabel.setText("Inserisci un cognome valido");
		} else {
			surLabel.setText("");
		}
	}

	@FXML
	protected void switchToLogin(ActionEvent event) {
		getMC().switchToLogin();
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

	protected void emptySelector() {
		IDField.setText(null);
		nameField.setText(null);
		surField.setText(null);
		dateField.getEditor().clear();
		placeField.setText(null);
	}
}
