package TestBackEnd;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class DatabaseManager {
	private static boolean initialized = false;
	private static Connection connection = null;
	
	private static final String PERSON_TABLE = "CREATE TABLE IF NOT EXISTS person(" +
											   "tax_id VARCHAR(16) PRIMARY KEY NOT NULL, " +
											   "name TEXT NOT NULL, " +
											   "surname TEXT NOT NULL, " +
											   "date_birth DATE NOT NULL, " +
											   "place_birth TEXT NOT NULL, " +
											   "health_card_num BIGINT NOT NULL," +
											   "belonging_category TEXT NOT NULL, " +
											   "tutor_id VARCHAR(16), " +
											   "sex CHAR NOT NULL);";	
	
	private static final String PERSON_CONSTRAINT = "DO $$ BEGIN" +
													"    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tutor_id') THEN" +
													"        ALTER TABLE person ADD CONSTRAINT fk_tutor_id FOREIGN KEY (tutor_id) REFERENCES person(tax_id);" +
													"    END IF;" +
													"END $$;";
	
	private static final String PASSPORT_TABLE = "CREATE TABLE IF NOT EXISTS passport(" +
												 "passport_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
												 "tax_id VARCHAR(16) NOT NULL, " + 
												 "release_date DATE NOT NULL, " +
												 "expiry_date DATE NOT NULL, " +
												 "state TEXT NOT NULL);";
	
	private static final String PASSPORT_CONSTRAINT = "DO $$ BEGIN" +
													  "    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_person_id') THEN" +
													  "        ALTER TABLE passport ADD CONSTRAINT fk_person_id FOREIGN KEY (tax_id) REFERENCES person(tax_id);" +
													  "    END IF;" +
													  "END $$;";
	
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
		DatabaseManager.createPersonTable();
		DatabaseManager.createPassportTable();
	}
		
	public static void createPersonTable() throws SQLException {
		DatabaseManager.connection.prepareStatement(PERSON_TABLE).execute();
		DatabaseManager.connection.prepareStatement(PERSON_CONSTRAINT).execute();
	}
	
	public static void createPassportTable() throws SQLException {
		DatabaseManager.connection.prepareStatement(PASSPORT_TABLE).execute();
		DatabaseManager.connection.prepareStatement(PASSPORT_CONSTRAINT).execute();
	}
	
	public static void dropTable(boolean cascade) throws SQLException {
		DatabaseManager.dropPersonTable(cascade);
		DatabaseManager.dropPassportTable(cascade);
	}
	
	public static void dropPersonTable(boolean cascade) throws SQLException {
		connection.prepareStatement("DROP TABLE IF EXISTS person " + (cascade ? "CASCADE" : "") + ";").execute();
	}
	
	public static void dropPassportTable(boolean cascade) throws SQLException {
		connection.prepareStatement("DROP TABLE IF EXISTS passport " + (cascade ? "CASCADE" : "") + ";").execute();
	}
	
	public static void insert(Person person) throws SQLException {
		var statement = connection.prepareStatement("INSERT INTO person VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

	    statement.setString(1, person.getTaxID());
	    statement.setString(2, person.getName());
	    statement.setString(3, person.getSurname());
	    statement.setDate(4, new Date(person.getDateBirth().getTime().getTime()));
	    statement.setString(5, person.getPlaceBirth());
	    statement.setLong(6, person.getHealthCardNumber());
	    statement.setString(7, person.getBelongingCategory());
	    statement.setString(8, person.getTutorID());
	    statement.setString(9, String.valueOf(person.getSex()));

	    statement.executeUpdate();
	}
	
	public static void insert(Passport passport) throws SQLException {
		var statement = connection.prepareStatement("INSERT INTO passport(tax_id, release_date, expiry_date, state) VALUES (?, ?, ?, ?)");
		
		statement.setString(1, passport.getTaxID());
		statement.setDate(2, new Date(passport.getReleaseDate().getTime().getTime()));
		statement.setDate(3, new Date(passport.getExpiryDate().getTime().getTime()));
		statement.setString(4, passport.getState().toString());
		
		statement.executeUpdate();
		
		var query = connection.prepareStatement("SELECT passport_id FROM passport WHERE tax_id=? ORDER BY passport_id DESC LIMIT 1;");
		
		query.setString(1, passport.getTaxID());
		
		var result = query.executeQuery();
		if (!result.next()) {
            throw new NoSuchElementException("no passports are associated with the given tax code");
        }
		
		passport.setID(result.getInt("passport_id"));
	}
}
