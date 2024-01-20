package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import model.NoSuchUserException;
import model.PoliceStation;
import model.Reservation;

public class ActivitySetterController{

    @FXML
    private ComboBox<String> activitySelector;

    @FXML
    private DatePicker dateSelector;

    @FXML
    private ComboBox<String> policeStationSelector;

    @FXML
    private Button saveButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button selectButton;

    @FXML
    private CheckBox select1;

    @FXML
    private CheckBox select2;

    @FXML
    private CheckBox select3;

    @FXML
    private CheckBox select4;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @FXML
    private Text text3;

    @FXML
    private Text text4;
    
    @FXML
    private Text errorText;
    
    public  MainController MC;
    
    private ArrayList<Text> actTexts = new ArrayList<>();
    private ArrayList<CheckBox> actBoxs = new ArrayList<>();
    void addAll() {
    	Text[] txts = {text1,text2,text3,text4};
    	actTexts.addAll(Arrays.asList(txts));
    	CheckBox[] CBs = {select1,select2,select3,select4};
    	actBoxs.addAll(Arrays.asList(CBs));
    }
    

    @FXML
    void add1(ActionEvent event) {

    }

    @FXML
    void add2(ActionEvent event) {

    }

    @FXML
    void add3(ActionEvent event) {

    }

    @FXML
    void add4(ActionEvent event) {

    }
    @FXML
    void getReservation(ActionEvent event) throws NoSuchUserException, SQLException {
    	if (checkAll()) {
    		ArrayList<Reservation> resList = new ArrayList<>();
    		LocalDate ld = dateSelector.getValue();
        	for (int hour = 8; hour < 12 ; hour++) {
        		Reservation R = new Reservation(ld.atTime(hour,0),new PoliceStation(policeStationSelector.getValue()));
        		R = R.getCompleteReservation();
        		System.out.println("add result: " + resList.add(R));
        	}
        	for (int slot = 0; slot < 4; slot++) {
        		Reservation.ReservationType rt = resList.get(slot).getType();
        		String str = rt == null ? "Vuoto" : rt.toString();
        		actTexts.get(slot).setText(str);
        	}
    	} else {
    		errorText.setText("Uno o più campi sono vuoti. \nInserisci correttamente la questura e il giorno che desideri");
    	}
    	
    }

    private boolean checkAll() {
		// TODO Auto-generated method stub
		return checkDate() && checkPS();
	}


	private boolean checkPS() {
		// TODO Auto-generated method stub
		return !(policeStationSelector.getValue() == null) && !policeStationSelector.getValue().isBlank();
	}


	private boolean checkDate() {
		// TODO Auto-generated method stub
		return !(dateSelector.getValue() == null) && !dateSelector.getValue().toString().isBlank();
	}
    


	@FXML
    void saveSelections(ActionEvent event) {

    }
    @FXML
    void goBack(ActionEvent event) {
    	MC.switchToLogin();
    }

    @FXML
    void setActivity(ActionEvent event) {

    }

    @FXML
    void setDate(ActionEvent event) {

    }

    @FXML
    void setPoliceStation(ActionEvent event) {

    }

	public void setMC(MainController mainController) {
		MC = mainController;
	}
	
	public ComboBox<String> getActSelector() {
		return activitySelector;
	}

	public ComboBox<String> getPSSelector() {
		// TODO Auto-generated method stub
		return policeStationSelector;
	}

}
