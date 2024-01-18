package model;

import java.sql.SQLException;

public class PoliceStation {
	public final String town;
	
	public PoliceStation(String town) {
		this.town = town;
	}
	
	public void insert() throws SQLException {
		DatabaseManager.insert(this);
	}
	
	public String getTown() {
		return town;
	}
}
