package TestBackEnd;

import java.sql.SQLException;

public class MainDB {

	public static void main(String[] args) throws SQLException {
		DatabaseManager.init("jdbc:postgresql://localhost:5432/elaborato_is", "edidec", "Univr2024");
		DatabaseManager.dropPersonTable(true);
		
		DatabaseManager.close();
	}

}
