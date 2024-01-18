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
    private MainController MC;
    
    @FXML
    void adminLogReq(ActionEvent event) {

    }

    @FXML
    void exitApp(ActionEvent event) {

    }

    @FXML
    void logReq(ActionEvent event) {
    	try {
    		this.logPerson = DatabaseManager.getPerson(ID);
    	} catch (NoSuchUserException e ) {
    		IDLabel.setText("User not found");
    	} catch (SQLException E ) {
			System.out.println("Database compro-fjDSVIAM...Database compromised");
		} finally {
			
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
    	MC.switchToReg();
    }
    
	public void setMC(MainController mC) {
		MC = mC;
	}

}
