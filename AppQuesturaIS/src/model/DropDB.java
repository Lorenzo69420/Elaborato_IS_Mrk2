package model;

import java.sql.SQLException;

public class DropDB {

	public static void main(String[] args) throws SQLException {
		DatabaseManager.init("jdbc:postgresql://localhost:5432/elaborato_is", "edidec", "Univr2024");
		DatabaseManager.dropTable(true);
		DatabaseManager.close();
		
		System.out.println("DROPPATO!!!!");
	}

}