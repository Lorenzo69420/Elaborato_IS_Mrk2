package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

public class DatabaseManager {
	private static boolean initialized = false;
	private static Connection connection = null;

	private static final String PERSON_TABLE = "CREATE TABLE IF NOT EXISTS person("
			+ "tax_id VARCHAR(16) PRIMARY KEY NOT NULL, " + "name TEXT NOT NULL, " + "surname TEXT NOT NULL, "
			+ "date_birth DATE NOT NULL, " + "place_birth TEXT NOT NULL, " + "health_card_num BIGINT, "
			+ "belonging_category TEXT, " + "tutor_id VARCHAR(16), " + "sex CHAR NOT NULL, " 
			+ "register BOOLEAN NOT NULL, " + "admin BOOLEAN NOT NULL);";

	private static final String PERSON_CONSTRAINT = "DO $$ BEGIN"
			+ "    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_tutor_id') THEN"
			+ "        ALTER TABLE person ADD CONSTRAINT fk_tutor_id FOREIGN KEY (tutor_id) REFERENCES person(tax_id);"
			+ "    END IF;" + "END $$;";

	private static final String PASSPORT_TABLE = "CREATE TABLE IF NOT EXISTS passport("
			+ "passport_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," + "tax_id VARCHAR(16) NOT NULL, "
			+ "release_date DATE NOT NULL, " + "expiry_date DATE NOT NULL, " + "release_location TEXT NOT NULL, "
			+ "state TEXT NOT NULL);";

	private static final String PASSPORT_CONSTRAINT = "DO $$ BEGIN"
			+ "    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_person_id') THEN"
			+ "        ALTER TABLE passport ADD CONSTRAINT fk_person_id FOREIGN KEY (tax_id) REFERENCES person(tax_id);"
			+ "    END IF;"
			+ "    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_release_location') THEN"
			+ "        ALTER TABLE passport ADD CONSTRAINT fk_release_location FOREIGN KEY (release_location) REFERENCES police_station(town);"
			+ "    END IF;" + "END $$;";

	private static final String POLICE_TABLE = "CREATE TABLE IF NOT EXISTS police_station("
			+ "town TEXT PRIMARY KEY NOT NULL);";

	private static final String RESERVATION_TABLE = "CREATE TABLE IF NOT EXISTS reservation("
			+ "passport_id BIGINT PRIMARY KEY NOT NULL, " + "booked_by VARCHAR(16) NOT NULL, " + "state TEXT NOT NULL, "
			+ "type TEXT NOT NULL, " + "date TIMESTAMP NOT NULL, " + "place TEXT NOT NULL);";

	private static final String RESERVATION_CONSTRAINT = "DO $$ BEGIN"
			+ "    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booked_by') THEN"
			+ "        ALTER TABLE reservation ADD CONSTRAINT fk_booked_by FOREIGN KEY (booked_by) REFERENCES person(tax_id);"
			+ "    END IF;" + "    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_place') THEN"
			+ "        ALTER TABLE reservation ADD CONSTRAINT fk_place FOREIGN KEY (place) REFERENCES police_station(town);"
			+ "    END IF;" + "END $$;";
	
	
	public static void init(String url, String username, String password, boolean debugMode) throws SQLException {
		if (initialized) {
			throw new RuntimeException("The database manager is alredy initialized");
		}

		connection = DriverManager.getConnection(url, username, password);
		initialized = true;
		
		if (debugMode) {
			dropTable(true);
		}
	}

	public static void close() throws SQLException {
		if (!initialized) {
			throw new RuntimeException("The database manager is not inizialized, use DatabaseManager.init()");
		}

		connection.close();
		initialized = false;
	}

	public static void createTable() throws SQLException {
		createPersonTable();
		createPoliceTable();
		createPassportTable();
		createReservationTable();
	}

	public static void createPersonTable() throws SQLException {
		connection.prepareStatement(PERSON_TABLE).execute();
		connection.prepareStatement(PERSON_CONSTRAINT).execute();
	}

	public static void createPassportTable() throws SQLException {
		connection.prepareStatement(PASSPORT_TABLE).execute();
		connection.prepareStatement(PASSPORT_CONSTRAINT).execute();
	}

	public static void createPoliceTable() throws SQLException {
		connection.prepareStatement(POLICE_TABLE).execute();
	}

	public static void createReservationTable() throws SQLException {
		connection.prepareStatement(RESERVATION_TABLE).execute();
		connection.prepareStatement(RESERVATION_CONSTRAINT).execute();
	}

	public static void dropTable(boolean cascade) throws SQLException {
		dropPersonTable(cascade);
		dropPassportTable(cascade);
		dropPoliceTable(cascade);
		dropReservationTable(cascade);
	}

	public static void dropPersonTable(boolean cascade) throws SQLException {
		connection.prepareStatement("DROP TABLE IF EXISTS person " + (cascade ? "CASCADE" : "") + ";").execute();
	}

	public static void dropPassportTable(boolean cascade) throws SQLException {
		connection.prepareStatement("DROP TABLE IF EXISTS passport " + (cascade ? "CASCADE" : "") + ";").execute();
	}

	public static void dropPoliceTable(boolean cascade) throws SQLException {
		connection.prepareStatement("DROP TABLE IF EXISTS police_station " + (cascade ? "CASCADE" : "") + ";")
				.execute();
	}

	public static void dropReservationTable(boolean cascade) throws SQLException {
		connection.prepareStatement("DROP TABLE IF EXISTS reservation " + (cascade ? "CASCADE" : "") + ";").execute();
	}

	public static void insert(Person person) throws SQLException {
		var statement = connection.prepareStatement("INSERT INTO person VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		statement.setString(1, person.getTaxID());
		statement.setString(2, person.getName());
		statement.setString(3, person.getSurname());
		statement.setDate(4, new Date(person.getDateBirth().getTime().getTime()));
		statement.setString(5, person.getPlaceBirth());
		statement.setInt(6, person.getHealthCardNumber());
		statement.setString(7, person.getBelongingCategory());
		statement.setString(8, person.getTutorID());
		statement.setString(9, String.valueOf(person.getSex()));
		statement.setBoolean(10, false);
		statement.setBoolean(11, false);
		
		statement.executeUpdate();
	}

	public static void insert(Passport passport) throws SQLException {
		var statement = connection.prepareStatement(
				"INSERT INTO passport(tax_id, release_date, expiry_date, release_location, state) VALUES (?, ?, ?, ?, ?)");

		statement.setString(1, passport.getTaxID());
		statement.setDate(2, new Date(passport.getReleaseDate().getTime().getTime()));
		statement.setDate(3, new Date(passport.getExpiryDate().getTime().getTime()));
		statement.setString(4, passport.getReleaseLocation().getTown());
		statement.setString(5, passport.getState().toString());

		statement.executeUpdate();

		var query = connection
				.prepareStatement("SELECT passport_id FROM passport WHERE tax_id=? ORDER BY passport_id DESC LIMIT 1;");

		query.setString(1, passport.getTaxID());

		var result = query.executeQuery();
		if (!result.next()) {
			throw new NoSuchElementException("no passports are associated with the given tax code");
		}

		passport.setID(result.getInt("passport_id"));
	}

	public static void insert(PoliceStation policeStation) throws SQLException {
		var statement = connection.prepareStatement("INSERT INTO police_station VALUES (?)");

		statement.setString(1, policeStation.getTown());

		statement.executeUpdate();
	}

	public static void insert(Reservation reservation) throws SQLException {
		var statement = connection.prepareStatement("INSERT INTO reservation VALUES (?, ?, ?, ?, ?, ?)");

		statement.setInt(1, reservation.getPassport().getPassID());
		statement.setString(2, reservation.getBookedBy().getTaxID());
		statement.setString(3, reservation.getState().toString());
		statement.setString(4, reservation.getType().toString());
		statement.setTimestamp(5, Timestamp.valueOf(reservation.getDate()));
		statement.setString(6, reservation.getPlace().getTown());

		statement.executeUpdate();
	}

	public static Person getPerson(String taxID) throws SQLException, NoSuchUserException {
		var query = connection.prepareStatement("SELECT * FROM person WHERE tax_id = ?");

		query.setString(1, taxID);

		var result = query.executeQuery();
		if (!result.next()) {
			throw new NoSuchUserException();
		}

		var cal = Calendar.getInstance();
		cal.setTime(result.getDate("date_birth"));

		return new Person(taxID, result.getString("name"), result.getString("surname"),
				result.getString("sex").charAt(0), result.getString("place_birth"), cal,
				result.getString("belonging_category"), result.getInt("health_card_num"));
	}
	
	public static List<String> getPoliceStation() throws SQLException {
		List<String> result = new ArrayList<>();
		
		var query = connection.prepareStatement("SELECT * FROM police_station");
		
		var resultQuery = query.executeQuery();
		while (resultQuery.next()) {
			result.add(resultQuery.getString("town"));
		}
		
		return result;
	}

	public static boolean existsPerson(Person person) throws SQLException {
		var query = connection.prepareStatement("SELECT 1 FROM person WHERE tax_id = ? AND name = ? AND surname = ? AND place_birth = ? AND date_birth = ? LIMIT 1");
		
		query.setString(1, person.getTaxID());
		query.setString(2, person.getName());
		query.setString(3, person.getSurname());
		query.setString(4, person.getPlaceBirth());
		query.setDate(5, new Date(person.getDateBirth().getTime().getTime()));
		
		var result = query.executeQuery();
		
		return result.next();
	}
}
