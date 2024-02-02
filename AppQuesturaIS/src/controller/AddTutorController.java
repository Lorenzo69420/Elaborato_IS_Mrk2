package controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Database;
import model.NoSuchUserException;
import model.Person;

public class AddTutorController {
	
	private MainController MC;
	private Person minor;

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
				if (!(tutor = Database.getPerson(taxID)).isAdult()){
					MC.showMessagePrompt("L'utente non è di maggiore età", MC.getCloseHandler());
				} else {
					minor.addTutor(tutor);
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

	public void setMinor(Person minor) {
		this.minor = minor;
	}

}
