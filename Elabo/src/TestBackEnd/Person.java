package TestBackEnd;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

public class Person {
	private String taxID;
	private String name;
	private String surname;
	private char sex;
	private String placeBirth;
	private Calendar dateBirth;
	private String belongingCategory; //TODO: non so se sia il termine corretto
	private Long healthCardNumber; //TODO: anche qua
	private Person tutor = null;
	
	public Person(String IDcode, String name, String surname, char sex, String placeBirth, Calendar dateBirth, String belongingCategory, Long healthCardNumber) throws SQLException {
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
		DatabaseManager.insertPerson(this);
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

	public long getHealthCardNumber() {
		return healthCardNumber;
	}

	public String getPlaceBirth() {
		return placeBirth;
	}

	public String getBelongingCategory() {
		return belongingCategory;
	}

	public Object getTutorID() {
		if (tutor == null) {
			return null;
		}
		return tutor.taxID;
	}

	public char getSex() {
		return sex;
	}

}
