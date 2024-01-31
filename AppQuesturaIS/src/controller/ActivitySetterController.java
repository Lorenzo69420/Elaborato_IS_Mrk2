package controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Reservation;

public class ActivitySetterController extends ActivityController {
	private static String UPDATE_ERROR_STRING = "Uno o più campi sono vuoti. Inserisci correttamente la questura e "
			+ "la data che desideri";
	private static String SAVE_ERROR_STRING = "Informazioni disallineate, prima di inserire la disponibilità premi "
			+ "\"Conferma\" nuovamente";		

	@Override
	protected String getUpdateErrorString() {
		return UPDATE_ERROR_STRING;
	}

	@Override
	protected String getSaveErrorString() {
		return SAVE_ERROR_STRING;
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
			// using new message prompt : test 1
			getMC().showMessagePrompt("Inserimento andato a buon fine", new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					getMC().getPromptController().getStage().close();
					
				}
				
			});
		} catch (SQLException e) {
			System.err.println("C'è stato un errore nell'inserimento della prenotazione: " + e.getMessage());
		}
	}

}
