package model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import model.NotBookableException.Types;

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

	public void book(Person person) throws SQLException, NotBookableException {
		Calendar requestDate = DatabaseManager.getRequestDate(person);
		switch (this.type) {
		case COLLECTION:
			if (requestDate == null) {
				throw new NotBookableException(Types.NO_PREVIOUS_REQ);
			}

			requestDate.add(Calendar.MONTH, 1);
			if (requestDate.after(this.getCalendarDate())) {
				throw new NotBookableException(Types.UNDER_MONTH_REQ);
			}
			DatabaseManager.deleteRequest(person);
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

			Calendar bookableExpired = (Calendar) lastPassport.getExpiryDate().clone();
			bookableExpired.add(Calendar.MONTH, -6);
			if (bookableExpired.after(this.getCalendarDate())) {
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

			if (lastPassport.getExpiryDate().before(this.getCalendarDate())) {
				throw new NotBookableException(Types.EXPIRED);
			}
			break;
		default:
			break;
		}

		if (this.type != ReservationType.COLLECTION) {
			if (requestDate != null) {
				throw new NotBookableException(Types.ALREDY_BOOKED);
			}
			this.bookedBy = person;
			DatabaseManager.insertRequest(this);
		}

		this.bookedBy = person;
		this.state = ReservationState.BOOKED_UP;
		DatabaseManager.book(this);
	}

	// TODO LORE SISTEMA STA ROBA, GRZ
	public void setState(ReservationState newState) throws Exception {
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

	public void setPassport(Passport passport) {
		this.passport = passport;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Person getBookedBy() {
		return bookedBy;
	}

	public void setBookedBy(Person person) {
		bookedBy = person;
	}

	public PoliceStation getPlace() {
		return place;
	}

	public Calendar getCalendarDate() {
		Calendar result = Calendar.getInstance();
		result.setTime(Date.from(this.getDate().atZone(ZoneId.systemDefault()).toInstant()));
		return result;
	}
}
