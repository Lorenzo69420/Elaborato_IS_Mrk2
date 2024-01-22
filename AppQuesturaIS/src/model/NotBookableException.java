package model;

public class NotBookableException extends Exception {
	public enum Types {
		NO_PREVIOUS_REQ,
		UNDER_MONTH_REQ,
		ALREDY_HAVE_PASSPORT, 
		NOT_EXPIRED, 
		MISSING_PASSPORT, 
		ALREDY_BOOKED
	}
	
	private final Types type;
	
	public NotBookableException(Types type) {
		this.type = type;
	}

	public Types getType() {
		return type;
	}
}
