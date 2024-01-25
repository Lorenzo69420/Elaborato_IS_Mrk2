package controller;

import java.sql.SQLException;

import model.PoliceStation;
import model.Reservation;

public class ActivitySetterController extends ActivityController {
	private static String UPDATE_ERROR_STRING = "Uno o più campi sono vuoti. Inserisci correttamente la questura e "
			+ "la data che desideri";
	private static String SAVE_ERROR_STRING = "Informazioni disallineate, prima di inserire la disponibilità premi "
			+ "\"Conferma\" nuovamente";

	@Override
	protected boolean checkBeforeUpdate() {
		return checkPoliceStationSelector() && checkDatePicker();
	}

	@Override
	protected boolean checkBeforeSave() {
		return checkPoliceStationSelector() && checkDatePicker() && checkActivitySelector();
	}

	@Override
	protected String getUpdateErrorString() {
		return UPDATE_ERROR_STRING;
	}

	@Override
	protected String getSaveErrorString() {
		return SAVE_ERROR_STRING;
	}

	@Override
	protected void getSelectorValues() {
		setDate(getDatePicker().getValue());
		setPoliceStation(new PoliceStation(getPoliceStationSelector().getValue()));
	}

	@Override
	protected boolean disableButton(Reservation reservation) {
		return reservation.getState().equals(Reservation.ReservationState.BOOKED_UP);
	}

	@Override
	protected void insertReservation(int hour) {
		if (!checkIntegrity()) {
			getErrorText().setText("Se vuoi cambiare questura o data, premi \"Conferma\" nuovamente");
			return;
		}

		var reservation = new Reservation(getReservationType(getActivitySelector().getValue()),
				getDate().atTime(hour, 0), getPoliceStation());
		try {
			reservation.insert();
			getErrorText().setText("Inserimento andato a buon fine");
			updateWindow();
			disableActivityBoxs();
		} catch (SQLException e) {
			System.err.println("C'è stato un errore nell'inserimento della prenotazione: " + e.getMessage());
		}
	}

}
