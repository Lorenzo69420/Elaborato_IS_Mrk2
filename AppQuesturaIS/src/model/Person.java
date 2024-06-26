package model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Person {
	private final String taxID;
	private final String name;
	private final String surname;
	private final char sex;
	private final String placeBirth;
	private final Calendar dateBirth;
	private final String belongingCategory; 
	private final int healthCardNumber; 
	private Person tutor = null;
	
	public Person(String taxID, String name, String surname, String placeBirth, Calendar dateBirth) {
		this(taxID, name, surname, 'm', placeBirth, dateBirth, null, 0);
	}

	public Person(String taxID, String name, String surname, char sex, String placeBirth, Calendar dateBirth,
			String belongingCategory, int healthCardNumber) {
		this.taxID = taxID;
		this.name = name;
		this.surname = surname;
		this.sex = sex;
		this.placeBirth = placeBirth;
		this.dateBirth = dateBirth;
		this.belongingCategory = belongingCategory;
		this.healthCardNumber = healthCardNumber;
	}

	public void exists() throws SQLException, NoSuchUserException {
		Database.existsPerson(this);
	}

	public void asignTutor(Person tutor) {
		this.tutor = tutor;
	}

	@Override
	public String toString() {
		java.util.Date birth = dateBirth.getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		return "Name: " + name + "\nSurname: " + surname + "\nID code: " + taxID + "\nPlace of birth: " + placeBirth
				+ "\nDate of birth: " + dateFormatter.format(birth);
	}

	public boolean isAdult() {
		Calendar currentDate = Calendar.getInstance();

		currentDate.add(Calendar.YEAR, -18);

		return currentDate.after(this.dateBirth);
	}
	
	public void addTutor(Person tutor) throws SQLException {
		this.tutor = tutor;
		
		Database.updateTutor(this);
	}

	public void insert() throws SQLException {
		Database.insert(this);
	}

	public static Person get(String taxID) throws SQLException, NoSuchUserException {
		return Database.getPerson(taxID);
	}

	public String getTaxID() {
		return taxID;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public Calendar getDateBirth() {
		return dateBirth;
	}

	public int getHealthCardNumber() {
		return healthCardNumber;
	}

	public String getPlaceBirth() {
		return placeBirth;
	}

	public String getBelongingCategory() {
		return belongingCategory;
	}

	public Person getTutor() {
		return tutor;
	}

	public char getSex() {
		return sex;
	}
	
	public void register() throws SQLException {
		try {
			this.exists();
		} catch (Exception e) {
			System.out.println("Impossibile aggiungere Registrazione, l'utente non rientra nel database");
		}
		Database.register(this);
	}

	public void makeAdmin() throws SQLException {
		Database.makeAdmin(this);
	}

	public Passport getLastPassport() throws SQLException {
		return Database.getLastPassport(this);
	}
	
	public Reservation getRequest() throws SQLException {
		return Database.getRequest(this);
	}

	public boolean isAdmin() throws SQLException {
		return Database.isAdmin(this);
	}
	
	public boolean isRegister() throws SQLException {
		return Database.isRegister(this);
	}
}
