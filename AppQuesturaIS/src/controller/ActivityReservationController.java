package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;

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

	private MainController MC;

	private PoliceStation ps;

	private LocalDate ld;

	private String activity;

	private CheckBox[] selectBoxs; 
	private Text[] texts;
	public void addAll() {
		CheckBox[] selectBoxs = { select1, select2, select3, select4 };
		Text[] texts = { text1, text2, text3, text4 };
		this.selectBoxs = selectBoxs;
		this.texts = texts;
	}
	@FXML
	void confirm() throws NoSuchUserException, SQLException {
		if (checkAll()) {

			// This section adds the reservations to the slots in the interface only if it
			// has the same activity type

			ps = new PoliceStation(policeStationSelector.getValue());
			ld = dateSelector.getValue();
			activity = activitySelector.getValue();
			Reservation.ReservationType RT = null;
			Passport currPassport = currentPerson.getLastPassport();

			// Searching the right activity

			for (var T : Reservation.ReservationType.values()) {
				if (T.toDisplayString().equals(activity)) {
					RT = T;
				}
			}

			// Setting the slots

			for (int slot = 0; slot < 4; slot++) {
				Reservation R = new Reservation(ld.atTime(8 + slot, 0), ps);
				R = R.getCompleteReservation();
				if (R.getType() != null) {
					selectBoxs[slot].setDisable(R.getState().equals(Reservation.ReservationState.BOOKED_UP));
					selectBoxs[slot].setDisable(!R.getType().toDisplayString().equals(activity));
					texts[slot].setText(R.getType().toDisplayString().equals(activity) ? activity : "");
				
				} else {
					selectBoxs[slot].setDisable(true);
					texts[slot].setText("Vuoto");
				}
			}

		} else {
			// If any of the slots are not inserted (null) the errorText is setted to the
			// following message

			errorText.setText("Inserisci tutti e tre i campi richiesti prima di premere conferma");
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
		MC.close();
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
			Reservation res = new Reservation(ld.atTime(8 + slot, 0), ps);
			
			try {
				res = res.getCompleteReservation();
				res.setBookedBy(currentPerson);
			} catch (NoSuchUserException e) {
				// gestisciti tu le tue eccezioni di merda <3 per edi
			} catch (SQLException e) {
				
			}
			
			try {
				res.book(currentPerson);
				errorText.setText("Prenotazione andata a buon fine");
				confirm();
			} catch (NotBookableException e) {
				String str = "Errore non definito ops";
				switch (e.getType()) {
				case ALREDY_BOOKED:
					// devi settare errorText con una scritta giusta lmao <3
					str = "La prenotazione per il ritiro del suo passaporto è gia stata confermata per il giorno "
							+ currentPerson.getLastPassport().getReleaseDate().toString()
							+ " nella questura di " + currentPerson.getLastPassport().getReleaseLocation();
					break;
				case ALREDY_HAVE_PASSPORT:
					str = "Nel sistema è gia presente un suo passaporto ( "
							+ currentPerson.getLastPassport().getPassID()
							+ " ) se lo ha perso, oppure le è stato rubato o invece è scaduto selezioni l'attività opportuna";
					break;
				case EXPIRED:
					str = "Il tuo passaporto risulta scaduto in data "
							+ currentPerson.getLastPassport().getExpiryDate()
							+ " Seleziona Emissione per scadenza per rinnovarlo";
					break;
				case MISSING_PASSPORT:
					str = "Non risulta che lei abbia gia creato un passaporto, selezioni 'Nuova emissione' per crearne uno";
					break;
				case NOT_EXPIRED:
					str = "Il suo passaporto non risulta essere scaduto. La data di scadenza è "
							+ currentPerson.getLastPassport().getExpiryDate().toString();
					break;
				case NO_PREVIOUS_REQ:
					str = "Non è presente nessun passaporto da emettere, richiedi prima un emissione";	
					break;
				case UNDER_MONTH_REQ:
					Calendar tmpCal = DatabaseManager.getRequestDate(currentPerson);
					tmpCal.add(Calendar.MONTH, 1); 
					str = "La data di emissione del suo passaporto sarà disponibile dal giorno " + tmpCal.toString() + " in poi";
					break;
				default:
					break;
				}
			} catch (SQLException e) {
				
			} catch (NoSuchUserException e) {
				// una volta gestita fino alla ultima cazzo di eccezine di merda fai dei test
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
		return ld.equals(dateSelector.getValue());
	}

	private boolean checkPSIntegrity() {
		return ps.getTown().equals(policeStationSelector.getValue());
	}

	public void setMC(MainController mainController) {

		MC = mainController;
	}

	public ComboBox<String> getActSelector() {

		return activitySelector;
	}

	public ComboBox<String> getPSSelector() {
		// edigay
		return policeStationSelector;
	}

	public void setPerson(Person currentPerson) {
		this.currentPerson = currentPerson;
	}

}
