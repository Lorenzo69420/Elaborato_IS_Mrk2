package model;

public class NotBookableException extends Exception {
	public enum Types {
		NO_PREVIOUS_REQ,
		UNDER_MONTH_REQ
	}
	
	private final Types type;
	
	public NotBookableException(Types type) {
		this.type = type;
	}

	public Types getType() {
		return type;
	}
}
