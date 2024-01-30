package controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.DatabaseManager;
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
    	try {
    		this.logPerson = DatabaseManager.getPerson(IDField.getText());
    		
    		if (logPerson.isAdmin()) {
    			mainController.switchToActivitySetter();
    		} else {
    			IDLabel.setText("L'utente selezionato non è admin");    			
    		}
    	} catch (NoSuchUserException e ) {
    		IDLabel.setText("Utente non trovato");
    	} catch (SQLException E ) {
			System.out.println("Database compro-fjDSVIAM...Database compromised");
		}
    }

    @FXML
    void exitApp(ActionEvent event) {
    	mainController.close();
    }

    @FXML
    void logReq(ActionEvent event) {
    	try {
    		this.logPerson = Person.get(IDField.getText());
  
    		if (logPerson.isRegister()) {
    			mainController.switchToActivityReservation();
    		} else {
    			IDLabel.setText("L'utente selezionato non è registrato");    			
    		}
    	} catch (NoSuchUserException e ) {
    		IDLabel.setText("Utente non trovato");
    	} catch (SQLException E ) {
			System.out.println("Database compro-fjDSVIAM...Database compromised");
		}

	} 
    
    @FXML
    void setName(ActionEvent event) {
    	if (IDField.getText().equals("")) {
    		IDLabel.setText("Inserisci un ID valido");
    	} else {
    		IDLabel.setText("");
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

}
