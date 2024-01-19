package model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Passport {
	public enum PassportState {
		NOT_RELEASED,
		VALID,
		EXPIRED,
		LOST,
		STOLEN,
		DAMAGED
	}
	
	private final String taxID;
	private final Calendar releaseDate;
	private final Calendar expiryDate;
	private PassportState state;
	private final PoliceStation releaseLocation;
	private int passportID;
	
	public Passport(String taxID, Calendar releaseDate, PassportState state, PoliceStation releaseLocation) {
		this.taxID = taxID;
		this.releaseDate = releaseDate;
		this.expiryDate = (Calendar) releaseDate.clone();
		expiryDate.add(Calendar.YEAR, 10);
		this.state = state;
		this.releaseLocation = releaseLocation;
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

	public int getPassID() {
		return passportID;
	}
	
	public PoliceStation getReleaseLocation() {
		return releaseLocation;
	}
	
	public void changeState(PassportState state) throws SQLException {
		this.state = state;
		DatabaseManager.changeState(this);
	}
}
