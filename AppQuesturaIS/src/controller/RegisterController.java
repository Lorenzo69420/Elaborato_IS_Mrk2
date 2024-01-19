package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    
    private MainController MC;
    private Person P;

    @FXML
    void appExit(ActionEvent event) {
    	MC.close();
    }

    @FXML
    void getPerson(ActionEvent event) {
    	if (checkAll()) {
    		ZoneId zId = ZoneId.systemDefault();
        	Date date = Date.from(dateField.getValue().atStartOfDay(zId).toInstant());
        	Calendar cl = Calendar.getInstance();
        	cl.setTime(date);
        	P = new Person(IDField.getText(), nameField.getText(), surField.getText(), placeField.getText(),cl);
        	try {
        		P.exists();
        		P = null;
        		MC.switchToLogin();
        	} catch (SQLException e) {
        		MC.close();
        	} catch (NoSuchUserException e) {
    			//setting all label to error message
        		errorText.setText("Uno o più campi sono errati, non è stato possibile trovare la Persona nell' anagrafica corrente, se ");
    		}
    	} else {
    		errorText.setText("Uno o più campi inseriti sono vuoti,\n inserisci tutti i campi in modo corretto");
    	}
    	
    }

    @FXML
    void setBirhdate(ActionEvent event) {
    	if (dateField.getValue().isAfter(LocalDate.now()) || dateField.getValue().toString().equals("")) {
    		dateLabel.setText("Put a valid Date");
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
    	if(surField.getText().equals("")) {
    		surLabel.setText("Inserisci un cognome valido");
    	} else {
    		surLabel.setText("");
    	}
    }

    @FXML
    void switchToLogin(ActionEvent event) {
    	MC.switchToLogin();
    }

	public void setMC(MainController mC) {
		MC = mC;
	}
	private boolean checkAll() {
		return checkName() &&
				checkSur() &&
				checkID() &&
				checkDate() &&
				checkPlace();
	}

	private boolean checkPlace() {
		// TODO Auto-generated method stub
		return !placeField.getText().isBlank();
	}

	private boolean checkDate() {
		// TODO Auto-generated method stub
		return !(dateField.getValue()==null) && !dateField.getValue().toString().isBlank();
	}

	private boolean checkID() {
		// TODO Auto-generated method stub
		return !IDField.getText().isBlank();
	}

	private boolean checkSur() {
		// TODO Auto-generated method stub
		return !surField.getText().isBlank();
	}

	private boolean checkName() {
		// TODO Auto-generated method stub
		return !nameField.getText().isBlank();
	}
 }
