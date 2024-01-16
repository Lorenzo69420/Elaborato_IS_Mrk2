package TestBackEnd;

import java.time.LocalDateTime;

public class Reservation {
	private ReservationState state;
	private final ReservationType type;
	private final LocalDateTime date;
	private final String office;
	private final Person boockedBy;
	
	public Reservation(ReservationType type, LocalDateTime date, String office, Person boockedBy) {
		this.state = ReservationState.BOOCKABLE;
		this.type = type;
		this.date = date;
		this.office = office;
		this.boockedBy = boockedBy;
	}
	
	public void changeState(ReservationState newState) {
		this.state = newState;
	}
	
	private enum ReservationState {
		BOOCKABLE,
		BOOKED_UP,
		CONFIRMED,
		ALREDY_DONE
	}
	
	private enum ReservationType {
		COLLECTION,
		ISSUANCE
	}
}
