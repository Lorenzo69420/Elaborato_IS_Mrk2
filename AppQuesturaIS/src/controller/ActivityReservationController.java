package controller;

import java.sql.SQLException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.NoSuchUserException;
import model.Person;
import model.PoliceStation;
import model.Reservation;

public class ActivityReservationController {

    @FXML
    private ComboBox<String> activitySelector;

    @FXML
    private DatePicker dateSelector;

    @FXML
    private Text errorText;

    @FXML
    private ComboBox<String> policeStationSelector;

    @FXML
    private CheckBox select1;

    @FXML
    private CheckBox select2;

    @FXML
    private CheckBox select3;

    @FXML
    private CheckBox select4;

    @FXML
    private HBox slotBox;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @FXML
    private Text text3;

    @FXML
    private Text text4;
    
    private Person currentPerson;
    
    private MainController MC;
    
    private PoliceStation ps;
    
    private LocalDate ld;
    
    private String activity;
    
    private CheckBox[] selectBoxs = {select1,select2,select3,select4};
    private Text[] texts = {text1,text2,text3,text4};
    @FXML
    void confirm(ActionEvent event) throws NoSuchUserException, SQLException {
    	if (checkAll()) {
    		
    		// This section adds the reservations to the slots in the interface only if it has the same activity type
    		
    		ps = new PoliceStation(policeStationSelector.getValue());
    		ld = dateSelector.getValue();
    		activity = activitySelector.getValue();
    		Reservation.ReservationType RT = null ;
    		
    		//Searching the right activity
    		
    		for (var T : Reservation.ReservationType.values()) {
    			if (T.toDisplayString().equals(activity)) {
    				RT = T;
    			}
    		}
    		
    		//Setting the slots
    		
    		for (int slot = 0; slot < 4; slot++) {
    			Reservation R = new Reservation(ld.atTime(8+slot,0),ps);
    			R = R.getCompleteReservation();
    			selectBoxs[slot].setDisable(R.getState().equals(Reservation.ReservationState.BOOKED_UP));
    			selectBoxs[slot].setDisable(!R.getType().toDisplayString().equals(activity));
    			texts[slot].setText(R.getType().toDisplayString().equals(activity)? activity:"");
    		}
    		
    	} else {
    		//If any of the slots are not inserted (null) the errorText is setted to the following message
    		
    		errorText.setText("Inserisci tutti e tre i campi richiesti prima di premere conferma");
    	}
    }

    private boolean checkAll() {
		// TODO Auto-generated method stub
		return checkAct() && checkPS() && checkDate();
	}

	private boolean checkDate() {
		// TODO Auto-generated method stub
		return !(dateSelector.getValue() == null);
	}

	private boolean checkPS() {
		// TODO Auto-generated method stub
		return !(policeStationSelector.getValue() == null);
	}

	private boolean checkAct() {
		// TODO Auto-generated method stub
		return !(activitySelector.getValue() == null);
	}

	@FXML
    void exit(ActionEvent event) {
    	MC.close();
    }

    @FXML
    void reserve(ActionEvent event) {

    }

	public void setMC(MainController mainController) {
		// TODO Auto-generated method stub
		MC = mainController;
	}

	public ComboBox<String> getActSelector() {
		// TODO Auto-generated method stub
		return activitySelector;
	}

	public ComboBox<String> getPSSelector() {
		// TODO Auto-generated method stub
		return policeStationSelector;
	}

	public void setPerson(Person currentPerson) {
		this.currentPerson = currentPerson;
	}

}
