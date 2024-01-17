package TestBackEnd;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Passport {
	public enum PassportState {
		VALID,
		EXPIRED,
		LOST,
		STOLEN,
		DAMEGED
	}
	
	private final String taxID;
	private final Calendar releaseDate;
	private final Calendar expiryDate;
	private final PassportState state;
	private int passportID;
	
	public Passport(String taxID, Calendar releaseDate, PassportState state) {
		this.taxID = taxID;
		this.releaseDate = releaseDate;
		this.expiryDate = (Calendar) releaseDate.clone();
		expiryDate.add(Calendar.YEAR, 10);
		this.state = state;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		
		return "TaxID: " + taxID + "\nRelease date: " + dateFormatter.format(releaseDate.getTime()) + "\nExpiry date: " + dateFormatter.format(expiryDate.getTime()); 
	}
	
	public void insert() throws SQLException {
		DatabaseManager.insert(this);
	}
	
	public void setID(int passportID) {
		this.passportID = passportID;
	}
	
	public String getTaxID() {
		return taxID;
	}
	
	public Calendar getReleaseDate() {
		return releaseDate;
	}
	
	public Calendar getExpiryDate() {
		return expiryDate;
	}
	
	public PassportState getState() {
		return state;
	}
}
