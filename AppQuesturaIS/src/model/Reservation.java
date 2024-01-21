package model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;

import model.NotBookableException.Types;
import model.Passport.PassportState;

public class Reservation {
	public enum ReservationState {
		BOOKABLE, BOOKED_UP, ALREADY_DONE
	}

	public enum ReservationType {
		COLLECTION, ISSUANCE_NEW, ISSUANCE_DAMAGED, ISSUANCE_EXPIRED, ISSUANCE_LOST, ISSUANCE_STOLEN;

		public String toDisplayString() {
			switch (this) {
			case COLLECTION:
				return "Ritiro";
			case ISSUANCE_DAMAGED:
				return "Emissione per deterioramento";
			case ISSUANCE_EXPIRED:
				return "Emissione per scadenza";
			case ISSUANCE_NEW:
				return "Prima emissione";
			case ISSUANCE_LOST:
				return "Emissione per perdita";
			default:
				return "Emissione per furto";
			}
		}
	}

	private ReservationState state;
	private final ReservationType type;
	private final LocalDateTime date;
	private Passport passport;
	private Person bookedBy;
	private final PoliceStation place;

	public Reservation(LocalDateTime date, PoliceStation place) {
		this((ReservationType) null, date, (Passport) null, (Person) null, place, ReservationState.BOOKABLE);
	}

	public Reservation(ReservationType type, LocalDateTime date, PoliceStation place) {
		this(type, date, (Passport) null, (Person) null, place, ReservationState.BOOKABLE);
	}

	public Reservation(ReservationType type, LocalDateTime date, Passport passport, Person person, PoliceStation place,
			ReservationState state) {
		this.state = state;
		this.type = type;
		this.date = date;
		this.passport = passport;
		this.bookedBy = person;
		this.place = place;
	}

	public void insert() throws SQLException {
		DatabaseManager.insert(this);
	}

	//COLLECTION, ISSUANCE_NEW, ISSUANCE_DAMAGED, ISSUANCE_EXPIRED, ISSUANCE_LOST, ISSUANCE_STOLEN;
	public void book(Person person) throws SQLException, NotBookableException {
		switch (this.type) {
		case COLLECTION:
			var requestDate = DatabaseManager.getRequestDate(person);
			if (requestDate == null) {
				throw new NotBookableException(Types.NO_PREVIOUS_REQ);
			}
			requestDate.add(Calendar.MONTH, 1);
			if (requestDate.after(this.date)) {
				throw new NotBookableException(Types.UNDER_MONTH_REQ);
			}
			break;
		case ISSUANCE_NEW:
			var lastPassport = person.getLastPassport();
			if (lastPassport != null) {
				throw new NotBookableException(Types.ALREDY_HAVE_PASSPORT);
			}
			break;
		case ISSUANCE_EXPIRED:
			lastPassport = person.getLastPassport();
			if (lastPassport == null) {
				throw new NotBookableException(Types.MISSING_PASSPORT);
			}
			if (lastPassport.getExpiryDate().after(this.getDate())) {
				throw new NotBookableException(Types.NOT_EXPIRED);
			}
			break;
		case ISSUANCE_DAMAGED: 
		case ISSUANCE_STOLEN:
		case ISSUANCE_LOST:
			lastPassport = person.getLastPassport();
			if (lastPassport == null) {
				throw new NotBookableException(Types.MISSING_PASSPORT);
			}
		default:
			
			break;
		}

		this.bookedBy = person;
		this.state = ReservationState.BOOKED_UP;
		DatabaseManager.book(this);
	}

	public void changeState(ReservationState newState) throws Exception {
		if (newState == ReservationState.BOOKED_UP) {
			System.err.println("wrong function\n");
			throw new Exception();
		}
		this.state = newState;
	}

	public Reservation getCompleteReservation() throws NoSuchUserException, SQLException {
		var result = DatabaseManager.getReservation(this);
		return result != null ? result : this;
	}

	public ReservationState getState() {
		return state;
	}

	public ReservationType getType() {
		return type;
	}

	public Passport getPassport() {
		return passport;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Person getBookedBy() {
		return bookedBy;
	}

	public PoliceStation getPlace() {
		return place;
	}
}
