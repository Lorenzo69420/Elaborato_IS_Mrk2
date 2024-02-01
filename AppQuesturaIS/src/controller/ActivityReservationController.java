package controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.NotBookableException;
import model.Person;
import model.Reservation;

public class ActivityReservationController extends ActivityController {
	private String activity;
	private Person currentPerson;
	private static String UPDATE_ERROR_STRING = "Riempi tutti e tre i campi richiesti prima di premere \"Conferma\"";
	private static String SAVE_ERROR_STRING = "Informazioni disallineate, prima di inserire le disponibilità premi "
			+ "\"Conferma\" nuovamente";

	@Override
	protected boolean checkBeforeUpdate() {
		return super.checkBeforeUpdate() && checkActivitySelector();
	}

	@Override
	protected String getUpdateErrorString() {
		return UPDATE_ERROR_STRING;
	}

	@Override
	protected void getSelectorValues() {
		super.getSelectorValues();
		activity = getActivitySelector().getValue();
	}

	@Override
	protected boolean disableButton(Reservation reservation) {
		return super.disableButton(reservation) || reservation.getType() == null
				|| !reservation.getType().toDisplayString().equals(activity);
	}

	@Override
	protected void insertReservation(int hour) {
		Reservation reservation = new Reservation(getDate().atTime(hour, 0), getPoliceStation());

		try {
			reservation = reservation.getCompleteReservation();
		} catch (SQLException e) {
		}

		if (!reservation.getType().equals(getReservationType(activity))) {
			getMC().showMessagePrompt("Sono state modificate le disponibilità, ecco quelle aggiornate", getMC().getCloseHandler());
			try {
				updateWindow();
				disableActivityBoxs();
			} catch (SQLException e) {
				System.out.println("BOH");
			}
			return;
		}

		try {
			reservation.book(currentPerson);
			getMC().showMessagePrompt("Prenotazione andata a buon fine", getMC().getCloseHandler());
			updateWindow();
		} catch (NotBookableException e) {
			getMC().showMessagePrompt(getDisplayError(e.getType()), getMC().getCloseHandler());
		} catch (Exception e) {
		}
	}

	@Override
	protected boolean checkIntegrity() {
		return super.checkIntegrity() && activity.equals(getActivitySelector().getValue());
	}

	private String getDisplayError(NotBookableException.Types type) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

		try {
			switch (type) {
			case ALREDY_BOOKED:
				Reservation request = currentPerson.getRequest();
				return "La prenotazione per il ritiro del suo passaporto è gia stata confermata per il giorno "
						+ dateFormatter.format(request.getCalendarDate().getTime()) + " nella questura di "
						+ request.getPlace().getTown();
			case ALREDY_HAVE_PASSPORT:
				return "Nel sistema è gia presente un suo passaporto (" + currentPerson.getLastPassport().getPassID()
						+ ") se lo ha perso, oppure le è stato rubato o invece è scaduto selezioni l'attività opportuna";
			case EXPIRED:
				Date expiryDate = currentPerson.getLastPassport().getExpiryDate().getTime();
				return "Il tuo passaporto risulta scaduto in data " + dateFormatter.format(expiryDate)
						+ " Seleziona Emissione per scadenza per rinnovarlo";
			case MISSING_PASSPORT:
				return "Non risulta che lei abbia gia creato un passaporto, selezioni 'Nuova emissione' per crearne uno";
			case NOT_EXPIRED:
				expiryDate = currentPerson.getLastPassport().getExpiryDate().getTime();
				return "Il suo passaporto non risulta essere scaduto. La data di scadenza è "
						+ dateFormatter.format(expiryDate);
			case NO_PREVIOUS_REQ:
				return "Non è presente nessun passaporto da emettere, richiedi prima un emissione";
			case UNDER_MONTH_REQ:
				Calendar bookableDate = currentPerson.getRequest().getCalendarDate();
				bookableDate.add(Calendar.MONTH, 1);
				return "La data di emissione del suo passaporto sarà disponibile dal giorno "
						+ dateFormatter.format(bookableDate.getTime()) + " in poi";
			default:
				return "Errore non definito ops, aggiungi in getDisplayError";
			}
		} catch (SQLException e) {
			return "C'è stato un errore: " + e.getMessage();
		}
	}

	public void setPerson(Person person) {
		currentPerson = person;
	}
}
