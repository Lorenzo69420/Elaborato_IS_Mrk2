package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    
    private LocalDate ld;
    private PoliceStation pl;
    
    private ArrayList<Text> actTexts = new ArrayList<>();
    private ArrayList<CheckBox> actBoxs = new ArrayList<>();
    void addAll() {
    	Text[] txts = {text1,text2,text3,text4};
    	actTexts.addAll(Arrays.asList(txts));
    	CheckBox[] CBs = {select1,select2,select3,select4};
    	actBoxs.addAll(Arrays.asList(CBs));
    }
    
    @FXML
    void getReservation(ActionEvent event) throws NoSuchUserException, SQLException {
    	if (checkConfirm()) {
    		List<Reservation> resList = new ArrayList<>();
    		ld = dateSelector.getValue();
    		pl = new PoliceStation(policeStationSelector.getValue());
    		errorText.setText("Stai visualizzando i turni della questura di " + pl.getTown() + " nel giorno " + ld.toString());
        	for (int hour = 8; hour < 12 ; hour++) {
        		
        		Reservation R = new Reservation(ld.atTime(hour,0),pl);
        		R = R.getCompleteReservation();
        		resList.add(R);
        		//System.out.println("add result: " + );
        	}
        	for (int slot = 0; slot < 4; slot++) {
        		Reservation R = resList.get(slot);
        		Reservation.ReservationType rt = R.getType();
        		CheckBox B = actBoxs.get(slot);
        		B.setDisable(R.getState().equals(Reservation.ReservationState.BOOKED_UP));
        		String str = rt == null ? "Vuoto" : rt.toDisplayString();
        		actTexts.get(slot).setText(str);
        	}
    	} else {
    		errorText.setText("Uno o più campi sono vuoti. \nInserisci correttamente la questura e il giorno che desideri");
    	}
    	
    }

    private boolean checkConfirm() {
		// TODO Auto-generated method stub
		return checkDate() && checkPS();
	}


	private boolean checkPS() {
		return !(policeStationSelector.getValue() == null) && !policeStationSelector.getValue().isBlank();
	}


	private boolean checkDate() {
		return !(dateSelector.getValue() == null) && !dateSelector.getValue().toString().isBlank();
	}
    


	@FXML
    void saveSelections(ActionEvent event) throws SQLException {
		int hour = 8;
		Reservation tmp ;
		if (checkAll()) {
			for (int slot = 0; slot < 4; slot++) {
				if (actBoxs.get(slot).isSelected()) {
					actBoxs.get(slot).setSelected(false);
					Reservation.ReservationType RT = null;
					for (var rType : Reservation.ReservationType.values()) {
						if (rType.toDisplayString().equals(activitySelector.getValue())) {
							RT = rType;
						}
					}
					if (!(!dateSelector.getValue().equals(ld) || !policeStationSelector.getValue().equals(pl.getTown()))) {
						tmp = new Reservation(RT,ld.atTime(hour+slot,0),pl);
						tmp.insert();
						errorText.setText("Inserimenti della prenotazione avvenuto con successo");
					} else {
						errorText.setText("Se vuoi cambiare questura e giorno premi conferma nuovamente");
					}
				}
			}
		} else {
			errorText.setText("Seleziona il tipo di attivita da inserire nella questura e giorno selezionati precedentemente, altrimenti premi conferma nuovamente");
		}
		
    }
    private boolean checkAll() {
		// TODO Auto-generated method stub
		return checkConfirm() && checkType();
	}


	private boolean checkType() {
		// TODO Auto-generated method stub
		return !(activitySelector.getValue() == null);
	}


	@FXML
    void goBack(ActionEvent event) {
    	MC.switchToLogin();
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
