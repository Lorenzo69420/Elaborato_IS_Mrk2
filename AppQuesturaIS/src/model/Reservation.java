package model;

import java.sql.SQLException;
import java.time.LocalDateTime;

import model.Passport.PassportState;

public class Reservation {
	public enum ReservationState {
		BOOKABLE,
		BOOKED_UP,
		CONFIRMED,
		ALREADY_DONE
	}
	
	public enum ReservationType {
		COLLECTION,
		ISSUANCE_NEW,
		ISSUANCE_DAMAGED,
		ISSUANCE_EXPIRED,
		ISSUANCE_LOST,
		ISSUANCE_STOLEN;
		
		public String toDisplayString() {
			switch(this) {
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
	private final Passport passport;
	private Person bookedBy;
	private final PoliceStation place;
	
	public Reservation(LocalDateTime date, PoliceStation place) {
		this((ReservationType)null, date, (Passport)null, (Person)null, place, (ReservationState)null);
	}
	
	public Reservation(ReservationType type, LocalDateTime date, PoliceStation place) {
		this(type, date, (Passport)null, (Person)null, place, ReservationState.BOOKABLE);
	}
	
	public Reservation(ReservationType type, LocalDateTime date, Passport passport, Person person, PoliceStation place, ReservationState state) {
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
	
	public void book(Person person) throws SQLException, notBookableException {
		ReservationType type = this.getType();
		
		switch(type) {
			case COLLECTION:
				if(!DatabaseManager.hasRequest(person) || !DatabaseManager.getRequestDate(person)) { //fai qua
					throw new notBookableExcecption();
				}
				break;
				//controlla che ci sia un passaporto in richiesto (fatto)
				//e che sia un mese prima di this.date
				
			case ISSUANCE_NEW:
				if(person.getLastPassport() != null || DatabaseManager.hasRequest(person)) {
					throw new notBookableException();
				}
				break;
			default:
				if(person.getLastPassport().getState() != PassportState.VALID || DatabaseManager.hasRequest(person)) {
					throw new notBookableException();
				}
				break;
		}
		
		this.bookedBy = person;
		this.state = ReservationState.BOOKED_UP;
	}
	
	public void changeState(ReservationState newState) throws Exception {
		if(newState == ReservationState.BOOKED_UP) {
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
