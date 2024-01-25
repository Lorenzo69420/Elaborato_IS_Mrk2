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

public class ActivitySetterController {
	
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
	private Text text1;

	@FXML
	private Text text2;

	@FXML
	private Text text3;

	@FXML
	private Text text4;


	public MainController mainController;
	private LocalDate date;
	private PoliceStation policeStation;
	private ArrayList<Text> actTexts = new ArrayList<>();
	private ArrayList<CheckBox> actBoxs = new ArrayList<>();

	public void addAll() {
		Text[] txts = { text1, text2, text3, text4 };
		actTexts.addAll(Arrays.asList(txts));
		CheckBox[] CBs = { select1, select2, select3, select4 };
		actBoxs.addAll(Arrays.asList(CBs));
		disableCheckBoxs();
	}

	private void disableCheckBoxs() {
		actBoxs.forEach(s -> {
			s.setDisable(true);
			s.setSelected(false);
		});
	}

	@FXML
	void confirm() throws NoSuchUserException, SQLException {
		if (!checkConfirm()) {
			errorText.setText(
					"Uno o più campi sono vuoti. \nInserisci correttamente la questura e il giorno che desideri");
			return;
		}

		date = dateSelector.getValue();
		policeStation = new PoliceStation(policeStationSelector.getValue());
		errorText.setText(
				"Stai visualizzando i turni della questura di " + policeStation.getTown() + " nel giorno " + date.toString());

		for (int slot = 0; slot < 4; slot++) {
			Reservation reservation = new Reservation(date.atTime(8 + slot, 0), policeStation);
			reservation = reservation.getCompleteReservation();
			Reservation.ReservationType reservationType = reservation.getType();
			boolean notBookable = reservation.getState().equals(Reservation.ReservationState.BOOKED_UP);
			actBoxs.get(slot).setDisable(notBookable);
			String str = reservationType == null ? "Vuoto" : reservationType.toDisplayString();
			actTexts.get(slot).setText(str);
		}
	}

	private boolean checkConfirm() {
		return checkDate() && checkPS();
	}

	private boolean checkPS() {
		return !(policeStationSelector.getValue() == null) && !policeStationSelector.getValue().isBlank();
	}

	private boolean checkDate() {
		return !(dateSelector.getValue() == null) && !dateSelector.getValue().toString().isBlank();
	}

	@FXML
	void saveSelections(ActionEvent event) throws SQLException, NoSuchUserException {
		if (!checkAll()) {
			errorText.setText("Seleziona il tipo di attivita da inserire nella questura e giorno selezionati "
					+ "precedentemente, altrimenti premi conferma nuovamente");
			return;
		}

		for (int slot = 0; slot < 4; slot++) {
			if (!actBoxs.get(slot).isSelected()) {
				continue;
			}
			actBoxs.get(slot).setSelected(false);
			Reservation.ReservationType RT = getResType(activitySelector.getValue());

			// If the data inserted into the selectors are the same as when it has been
			// clicked 'Conferma' , it can proceed with the reservation insertion into the
			// DB
			if (!dateSelector.getValue().equals(date) || !policeStationSelector.getValue().equals(policeStation.getTown())) {
				errorText.setText("Se vuoi cambiare questura e giorno premi conferma nuovamente");
				continue;
			}

			Reservation reservation = new Reservation(RT, date.atTime(8 + slot, 0), policeStation);
			reservation.insert();
			errorText.setText("Inserimento della prenotazione avvenuto con successo");
			confirm();
			disableCheckBoxs();
		}

	}

	private Reservation.ReservationType getResType(String type) {
		return Arrays.asList(Reservation.ReservationType.values()).stream()
				.filter(types -> types.toDisplayString().equals(type)).findFirst().orElse(null);
	}

	// CheckAll just checks that all the fields on the app interface had been
	// initialized
	private boolean checkAll() {
		return checkConfirm() && checkType();
	}

	private boolean checkType() {
		return !(activitySelector.getValue() == null);
	}

	@FXML
	void exit(ActionEvent event) {
		mainController.switchToLogin();
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public ComboBox<String> getActSelector() {
		return activitySelector;
	}

	public ComboBox<String> getPSSelector() {
		return policeStationSelector;
	}
}
