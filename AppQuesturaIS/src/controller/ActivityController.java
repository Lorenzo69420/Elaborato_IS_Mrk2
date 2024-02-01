package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import model.PoliceStation;
import model.Reservation;
import model.Reservation.ReservationType;

public abstract class ActivityController {
	@FXML
	private ComboBox<String> activitySelector;
	@FXML
	private ComboBox<String> policeStationSelector;
	@FXML
	private DatePicker datePicker;
	@FXML
	private Text errorText;
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

	private MainController mainController;
	private LocalDate date;
	private PoliceStation policeStation;
	private ArrayList<Text> activityTexts;
	private ArrayList<CheckBox> activityBoxs;
	
	private static String SAVE_ERROR_STRING = "Informazioni disallineate, prima di inserire la disponibilità premi "
			+ "\"Conferma\" nuovamente";

	protected abstract String getUpdateErrorString();

	protected abstract void insertReservation(int hour);
	
	protected boolean checkBeforeUpdate() {
		return checkPoliceStationSelector() && checkDatePicker();
	}
	
	protected final boolean checkBeforeSave() {
		return checkPoliceStationSelector() && checkDatePicker() && checkActivitySelector();
	}
	
	protected void getSelectorValues() {
		setDate(getDatePicker().getValue());
		setPoliceStation(new PoliceStation(getPoliceStationSelector().getValue()));
	}

	protected boolean disableButton(Reservation reservation) {
		return reservation.getState().equals(Reservation.ReservationState.BOOKED_UP);
	}
	
	@FXML
	final void updateWindow() throws SQLException {
		disableActivityBoxs();
		if (!checkBeforeUpdate()) {
			getMC().showMessagePrompt(getUpdateErrorString(), getMC().getCloseHandler());
			return;
		}

		getSelectorValues();

		for (int slot = 0; slot < 4; slot++) {
			var reservation = new Reservation(date.atTime(8 + slot, 0), policeStation);
			reservation = reservation.getCompleteReservation();
			var reservationType = reservation.getType();
			activityTexts.get(slot).setText(toTypeText(reservationType));
			activityBoxs.get(slot).setDisable(disableButton(reservation));
		}
	}

	private final String toTypeText(ReservationType reservationType) {
		return reservationType == null ? "Libero" : reservationType.toDisplayString();
	}

	@FXML
	final void saveReservation(ActionEvent event) {
		if (!checkBeforeSave()) {
			getMC().showMessagePrompt(SAVE_ERROR_STRING, getMC().getCloseHandler());
			return;
		}

		for (int slot = 0; slot < 4; slot++) {
			if (!activityBoxs.get(slot).isSelected()) {
				continue;
			}
			activityBoxs.get(slot).setSelected(false);
			insertReservation(8 + slot);
		}
		try {
			updateWindow();			
		} catch(SQLException e) {}
	}

	@FXML
	final void back(ActionEvent event) {
		mainController.switchToLogin();
	}

	public final void addAll() {
		activityTexts = new ArrayList<>(List.of(text1, text2, text3, text4));
		activityBoxs = new ArrayList<>(List.of(select1, select2, select3, select4));
		disableActivityBoxs();
	}

	protected final void disableActivityBoxs() {
		activityBoxs.forEach(box -> {
			box.setDisable(true);
			box.setSelected(false);
		});
	}

	protected final boolean checkPoliceStationSelector() {
		return policeStationSelector.getValue() != null;
	}

	protected final boolean checkDatePicker() {
		return datePicker.getValue() != null;
	}

	protected final boolean checkActivitySelector() {
		return activitySelector.getValue() != null;
	}

	protected final Reservation.ReservationType getReservationType(String request) {
		return Arrays.asList(Reservation.ReservationType.values()).stream()
				.filter(type -> type.toDisplayString().equals(request)).findFirst().orElse(null);
	}


	public final void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public final LocalDate getDate() {
		return date;
	}

	public final void setDate(LocalDate date) {
		this.date = date;
	}

	public final PoliceStation getPoliceStation() {
		return policeStation;
	}

	public final void setPoliceStation(PoliceStation policeStation) {
		this.policeStation = policeStation;
	}

	public final void populateSelector(ObservableList<String> activityList, ObservableList<String> policeStationList) {
		this.activitySelector.getItems().addAll(activityList);
		this.policeStationSelector.getItems().addAll(policeStationList);
	}

	protected final ComboBox<String> getActivitySelector() {
		return activitySelector;
	}

	protected final ComboBox<String> getPoliceStationSelector() {
		return policeStationSelector;
	}

	protected final DatePicker getDatePicker() {
		return datePicker;
	}

	protected final Text getErrorText() {
		return errorText;
	}

	protected boolean checkIntegrity() {
		return datePicker.getValue().equals(date) && policeStationSelector.getValue().equals(policeStation.getTown());
	}
	protected MainController getMC() {
		return mainController;
	}
}