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
		disableAll();
	}
	private void disableAll() {
		Arrays.asList(selectBoxs).forEach(e -> e.setDisable(true));
	}
	@FXML
	void confirm() throws NoSuchUserException, SQLException {
		errorText.setText("");
		if (checkAll()) {

			// This section adds the reservations to the slots in the interface only if it
			// has the same activity type

			ps = new PoliceStation(policeStationSelector.getValue());
			ld = dateSelector.getValue();
			activity = activitySelector.getValue();
			Reservation.ReservationType RT = getResType(activity);
			Passport currPassport = currentPerson.getLastPassport();

			// Setting the slots

			for (int slot = 0; slot < 4; slot++) {
				Reservation R = new Reservation(ld.atTime(8 + slot, 0), ps);
				R = R.getCompleteReservation();
				if (R.getType() != null) {
					boolean difActivity = !R.getType().toDisplayString().equals(activity);
					boolean notBookable = R.getState().equals(Reservation.ReservationState.BOOKED_UP);
					selectBoxs[slot].setDisable(difActivity || notBookable);
					
					texts[slot].setText(R.getType().toDisplayString().equals(activity) ? activity : R.getType().toDisplayString());
				
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
		MC.switchToLogin();
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
			} catch (NoSuchUserException e) {
				
			} catch (SQLException e) {
				
			}
			
			if (!res.getType().equals(getResType(activity))) {
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
				res.book(currentPerson);
				errorText.setText("Prenotazione andata a buon fine");
				confirm();
			} catch (NotBookableException e) {
				String str = "Errore non definito ops";
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
				
				switch (e.getType()) {
				case ALREDY_BOOKED:
					
					Reservation request = currentPerson.getRequest();
					str = "La prenotazione per il ritiro del suo passaporto è gia stata confermata per il giorno "
							+ dateFormatter.format(request.getCalendarDate().getTime())
							+ " nella questura di " + request.getPlace().getTown();
					break;
				case ALREDY_HAVE_PASSPORT:
					str = "Nel sistema è gia presente un suo passaporto ("
							+ currentPerson.getLastPassport().getPassID()
							+ ") se lo ha perso, oppure le è stato rubato o invece è scaduto selezioni l'attività opportuna";
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
					Date expiryDate = currentPerson.getLastPassport().getExpiryDate().getTime();
					str = "Il suo passaporto non risulta essere scaduto. La data di scadenza è "
							+ dateFormatter.format(expiryDate);
					break;
				case NO_PREVIOUS_REQ:
					str = "Non è presente nessun passaporto da emettere, richiedi prima un emissione";	
					break;
				case UNDER_MONTH_REQ:
					Calendar tmpCal = currentPerson.getRequest().getCalendarDate();
					tmpCal.add(Calendar.MONTH, 1); 
					str = "La data di emissione del suo passaporto sarà disponibile dal giorno " + dateFormatter.format(tmpCal.getTime()) + " in poi";
					break;
				default:
					break;
				}
				errorText.setText(str);
			} catch (SQLException e) {
				
			} catch (NoSuchUserException e) {
				
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
		return policeStationSelector;
	}

	public void setPerson(Person currentPerson) {
		this.currentPerson = currentPerson;
	}

	private Reservation.ReservationType getResType(String type) {
		for (var result: Reservation.ReservationType.values()) {
			if (result.toDisplayString().equals(type)) {
				return result;
			}
		}
		return null;
	}
}
