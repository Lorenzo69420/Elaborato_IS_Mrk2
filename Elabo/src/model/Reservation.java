package model;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
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
		ISSUANCE
	}
	
	private ReservationState state;
	private final ReservationType type;
	private final LocalDateTime date;
	private final String office;
	private Person bookedBy;
	//TODO da spostare
	private Connection connection;
	
	public Reservation(ReservationType type, LocalDateTime date, String office) throws SQLException {
		this.state = ReservationState.BOOKABLE;
		this.type = type;
		this.date = date;
		this.office = office;
		this.bookedBy = null;
		
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ingegneria", "neto", "fethergay");
		this.connection = connection;
	}
	
	public void book(Person person) throws SQLException {
		this.bookedBy = person;
		this.state = ReservationState.BOOKED_UP;
		
		var statement = connection.prepareStatement("UPDATE reservation SET reservationState = 'BOOKED', person = ? where reservationType = ?)");
	}
	
	public void changeState(ReservationState newState) {
		if(newState == ReservationState.BOOKED_UP) {
			System.err.println("wrong function\n");
		}
		this.state = newState;
	}
	
	public void insert() throws SQLException {
		var stmt = connection.prepareStatement("INSERT INTO reservation VALUES (?, ?, ?, ?, ?)");

	    stmt.setString(1, this.state.toString());
	    stmt.setString(2, this.type.toString());
	    stmt.setTimestamp(3, Timestamp.valueOf(this.date));
	    stmt.setString(4, this.office);
	    stmt.setObject(5, this.bookedBy);
	    
	    System.out.println(stmt);

	    stmt.executeUpdate();
	}
}
