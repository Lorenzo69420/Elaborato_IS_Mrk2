package controller;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class RegisterController {

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
    private ComboBox<?> placeField;

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
    
    

    @FXML
    void appExit(ActionEvent event) {

    }

    @FXML
    void getPerson(ActionEvent event) {

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
    void setBirthplace(ActionEvent event) {
    	
    }

    @FXML
    void setID(ActionEvent event) {
    	if (IDField.getText().equals("")) {
    		IDLabel.setText("Put a valid ID");
    	} else {
    		IDLabel.setText("");
    	}
    }

    @FXML
    void setName(ActionEvent event) {
    	if (nameField.getText().equals("")) {
    		nameLabel.setText("Put a valid name");
    	} else {
    		nameLabel.setText("");
    	}
    }

    @FXML
    void setSurname(ActionEvent event) {

    }

    @FXML
    void switchToLogin(ActionEvent event) {

    }

}
