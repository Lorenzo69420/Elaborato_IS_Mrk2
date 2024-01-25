package model;

import java.sql.SQLException;
import java.util.List;

public class PoliceStation {
	private final String town;
	
	public PoliceStation(String town) {
		this.town = town;
	}
	
	public void insert() throws SQLException {
		DatabaseManager.insert(this);
	}
	
	public String getTown() {
		return town;
	}
	
	public static List<String> getStations() throws SQLException {
		return DatabaseManager.getPoliceStation();
	}
}
