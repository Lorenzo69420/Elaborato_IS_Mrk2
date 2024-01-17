package TestBackEnd;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	private static boolean initialized = false;
	private static Connection connection = null;
	
	private static final String PERSON_TABLE = "CREATE TABLE IF NOT EXISTS person(" +
										"taxID VARCHAR(16) PRIMARY KEY NOT NULL, " +
										"name VARCHAR(16) NOT NULL, " +
										"surname VARCHAR(16) NOT NULL, " +
										"data_nascita DATE NOT NULL, " +
										"placeBirth VARCHAR(32) NOT NULL, " +
										"num_sanitario BIGINT NOT NULL," +
										"belonging_category VARCHAR(32) NOT NULL, " +
										"tutor_id VARCHAR(16), " +
										"sex CHAR NOT NULL);" ;
	private static final String PERSON_CONSTRAINT = "ALTER TABLE person ADD CONSTRAINT IF NOT EXISTS fk_tutor_id FOREIGN KEY(tutor_id) REFERENCES person(taxID) ON DELETE SET NULL ON UPDATE CASCADE";
	
	public static void init(String url, String username, String password) throws SQLException {
		if (initialized) {
			throw new RuntimeException("The database manager is alredy initialized");
		}
		
		connection = DriverManager.getConnection(url, username, password);
		initialized = true;
	}
	
	public static void close() throws SQLException {
		if (!initialized) {
			throw new RuntimeException("The database manager is not inizialized, use DatabaseManager.init()");
		} 
		
		connection.close();
		initialized = false;
	}
	
	public static void createTable() throws SQLException {
		DatabaseManager.connection.prepareStatement(PERSON_TABLE).execute();
		DatabaseManager.connection.prepareStatement(PERSON_CONSTRAINT).execute();
	}
	
	public static void dropPersonTable(boolean cascade) throws SQLException {
		connection.prepareStatement("DROP TABLE IF EXISTS person " + (cascade ? "CASCADE" : "") + ";").execute();
	}
	
	public static void insertPerson(Person person) throws SQLException {
		var statement = connection.prepareStatement("INSERT INTO person VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

	    statement.setString(1, person.getTaxID());
	    statement.setString(2, person.getName());
	    statement.setString(3, person.getSurname());
	    statement.setDate(4, new Date(person.getDateBirth().getTime().getTime()));
	    statement.setString(5, person.getPlaceBirth());
	    statement.setLong(6, person.getHealthCardNumber());
	    statement.setString(7, person.getBelongingCategory());
	    statement.setObject(8, person.getTutorID());
	    statement.setString(9, String.valueOf(person.getSex()));
	    
	    System.out.println(statement);

	    statement.executeUpdate();
	}
}
