package controller;

import java.sql.SQLException;

import model.NotBookableException;
import model.Reservation;

public class ActivityAdminController extends ActivityController {

	private static String UPDATE_ERROR_STRING = "Uno o più campi sono vuoti. Inserisci correttamente la questura e "
			+ "la data che desideri";
	private static String DESCRIPTION = "Benvenuto nel sistema di inserimento degli appuntamenti da prendere in questura";

	protected ActivityAdminController(MainController MC) {
		super(MC);
	}

	@Override
	protected void setDescription() {
		getDescriptionText().setText(DESCRIPTION);
		getSaveButton().setText("Inserisci");
	}

	@Override
	protected String getUpdateErrorString() {
		return UPDATE_ERROR_STRING;
	}

	@Override
	protected void insertReservation(int hour) {
		if (!checkIntegrity()) {
			getMC().showMessagePrompt("Se vuoi cambiare questura o data, premi \"Conferma\" nuovamente",
					getMC().getCloseHandler(), true);
			return;
		}

		var reservation = new Reservation(getReservationType(getActivitySelector().getValue()),
				getDate().atTime(hour, 0), getPoliceStation());
		try {
			reservation.insert();
			// using new message prompt : test 1
			getMC().showMessagePrompt("Inserimento andato a buon fine", getMC().getCloseHandler(), false);
		} catch (SQLException e) {
			System.err.println("C'è stato un errore nell'inserimento della prenotazione: " + e.getMessage());
		} catch (NotBookableException e) {
			getMC().showMessagePrompt("Inserimento di : " + reservation.getType().toDisplayString() + " non riuscito, è gia presente un appuntamento in quella fascia oraria", getMC().getCloseHandler(), true);
		}
	}

}
