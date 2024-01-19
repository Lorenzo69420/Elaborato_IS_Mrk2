package model;

import java.sql.SQLException;
import java.time.LocalDateTime;

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
		ISSUANCE_DAMEGED,
		ISSUANCE_EXPIRED,
		ISSUANCE_LOST,
		ISSUANCE_STOLEN;
		
		public String toString() {
			switch(this) {
			case COLLECTION:
				return "Ritiro";
			case ISSUANCE_DAMEGED:
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
	
	
	public Reservation(ReservationType type, LocalDateTime date, Passport passport, Person person, PoliceStation place) throws SQLException {
		this.state = ReservationState.BOOKABLE;
		this.type = type;
		this.date = date;
		this.passport = passport;
		this.bookedBy = person;
		this.place = place;
	}
	
	public void insert() throws SQLException {
		DatabaseManager.insert(this);
	}
	
	public void book(Person person) throws SQLException {
		this.bookedBy = person;
		this.state = ReservationState.BOOKED_UP;
	}
	
	public void changeState(ReservationState newState) {
		if(newState == ReservationState.BOOKED_UP) {
			System.err.println("wrong function\n");
		}
		this.state = newState;
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
