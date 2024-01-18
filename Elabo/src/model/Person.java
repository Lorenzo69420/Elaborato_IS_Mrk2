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
	private final String belongingCategory; //TODO: non so se sia il termine corretto
	private final int healthCardNumber; //TODO: anche qua
	private Person tutor = null;
	
	public Person(String IDcode, String name, String surname, char sex, String placeBirth, Calendar dateBirth, String belongingCategory, int healthCardNumber) throws SQLException {
		this.taxID = IDcode;
		this.name = name;
		this.surname = surname;
		this.sex = sex;
		this.placeBirth = placeBirth;
		this.dateBirth = dateBirth;
		this.belongingCategory = belongingCategory;
		this.healthCardNumber = healthCardNumber;
	}
	
	//TODO
	public boolean exists() {
		return false;
	}
	
	public void asignTutor(Person tutor) {
		this.tutor = tutor;
	}
	
	@Override
	public String toString() {
		java.util.Date birth = dateBirth.getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		
		return "Name: " + name + "\nSurname: " + surname + "\nID code: " + taxID + "\nPlace of birth: " + placeBirth + "\nDate of birth: " + dateFormatter.format(birth);
	}
	
	public boolean isAdult() {
		Calendar currentDate = Calendar.getInstance();
		
		currentDate.add(Calendar.YEAR, -18);
		
		return currentDate.after(this.dateBirth);
	}
	
	public void insert() throws SQLException {
		DatabaseManager.insert(this);
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

	public String getTutorID() {
		if (tutor == null) {
			return null;
		}
		return tutor.taxID;
	}

	public char getSex() {
		return sex;
	}

}
