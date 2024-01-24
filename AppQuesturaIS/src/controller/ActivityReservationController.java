package controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.DatabaseManager;
import model.NoSuchUserException;
import model.NotBookableException;
import model.Passport;
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

	private MainController mainController;

	private PoliceStation policeStation;

	private LocalDate date;

	private String activity;

	private CheckBox[] selectBoxs;
	private Text[] texts;

	public void addAll() {
		CheckBox[] selectBoxs = { select1, select2, select3, select4 };
		Text[] texts = { text1, text2, text3, text4 };
		this.selectBoxs = selectBoxs;
		this.texts = texts;
		disableAll();
	}

	private void disableAll() {
		Arrays.asList(selectBoxs).forEach(e -> {
			e.setDisable(true);
			e.setSelected(false);
		});
	}

	@FXML
	void confirm() throws NoSuchUserException, SQLException {
		errorText.setText("");
		if (!checkAll()) {
			// If any of the slots are not inserted (null) the errorText is setted to the
			// following message
			errorText.setText("Inserisci tutti e tre i campi richiesti prima di premere conferma");
			return;
		}

		// This section adds the reservations to the slots in the interface only if it
		// has the same activity type
		policeStation = new PoliceStation(policeStationSelector.getValue());
		date = dateSelector.getValue();
		activity = activitySelector.getValue();

		// Setting the slots
		for (int slot = 0; slot < 4; slot++) {
			Reservation reservation = new Reservation(date.atTime(8 + slot, 0), policeStation);
			reservation = reservation.getCompleteReservation();
			if (reservation.getType() == null) {
				texts[slot].setText("Vuoto");
				continue;
			}
			boolean difActivity = !reservation.getType().toDisplayString().equals(activity);
			boolean notBookable = reservation.getState().equals(Reservation.ReservationState.BOOKED_UP);
			selectBoxs[slot].setDisable(difActivity || notBookable);
			texts[slot].setText(reservation.getType().toDisplayString());
		}
	}

	private boolean checkAll() {
		return checkAct() && checkPS() && checkDate();
	}

	private boolean checkDate() {
		return !(dateSelector.getValue() == null);
	}

	private boolean checkPS() {
		return !(policeStationSelector.getValue() == null);
	}

	private boolean checkAct() {
		return !(activitySelector.getValue() == null);
	}

	@FXML
	void exit(ActionEvent event) {
		mainController.switchToLogin();
	}

	@FXML
	void reserve(ActionEvent event) throws SQLException {
		if (!checkAll() || !checkIntegrity()) {
			errorText.setText("Per cambiare attività, questura e giorno premi 'Conferma' nuovamente");
			return;
		}
		for (int slot = 0; slot < 4; slot++) {
			if (!selectBoxs[slot].isSelected()) {
				continue;
			}
			selectBoxs[slot].setSelected(false);
			Reservation reservation = new Reservation(date.atTime(8 + slot, 0), policeStation);

			try {
				reservation = reservation.getCompleteReservation();
			} catch (Exception e) {
			}

			if (!reservation.getType().equals(getResType(activity))) {
				errorText.setText("Sono state modificate le disponibilità, ecco quelle aggiornate");
				try {
					confirm();
					disableAll();
				} catch (Exception e) {
					System.out.println("Virus getting over us!!!!!");
				}
				return;
			}

			try {
				reservation.book(currentPerson);
				errorText.setText("Prenotazione andata a buon fine");
				confirm();
			} catch (NotBookableException e) {
				errorText.setText(getDisplayError(e.getType()));
			} catch (Exception e) {
			}
		}
	}

	private boolean checkIntegrity() {
		return checkPSIntegrity() && checkDateIntegrity() && checkActIntegrity();
	}

	private boolean checkActIntegrity() {
		return activity.equals(activitySelector.getValue());
	}

	private boolean checkDateIntegrity() {
		return date.equals(dateSelector.getValue());
	}

	private boolean checkPSIntegrity() {
		return policeStation.getTown().equals(policeStationSelector.getValue());
	}

	public void setMC(MainController mainController) {
		this.mainController = mainController;
	}

	public ComboBox<String> getActSelector() {
		return activitySelector;
	}

	public ComboBox<String> getPSSelector() {
		return policeStationSelector;
	}

	public void setPerson(Person currentPerson) {
		this.currentPerson = currentPerson;
	}

	private Reservation.ReservationType getResType(String type) {
		return Arrays.asList(Reservation.ReservationType.values()).stream()
				.filter(types -> types.toDisplayString().equals(type)).findFirst().orElse(null);
	}

	private String getDisplayError(NotBookableException.Types type) throws SQLException {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

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
	}
}
