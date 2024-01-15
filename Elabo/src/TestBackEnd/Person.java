package TestBackEnd;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Person {
	private String IDcode;
	private String name;
	private String surname;
	private String placeBirth;
	private Calendar dateBirth;
	private Person tutor = null;
	
	public Person(String IDcode, String name, String surname, String placeBirth, Calendar dateBirth) {
		this.IDcode = IDcode;
		this.name = name;
		this.surname = surname;
		this.placeBirth = placeBirth;
		this.dateBirth = dateBirth;
	}
	
	public void insertTutor(Person tutor) {
		this.tutor = tutor;
	}
	
	@Override
	public String toString() {
		Date birth = dateBirth.getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		
		return "Name: " + name + "\nSurname: " + surname + "\nID code: " + IDcode + "\nPlace of birth: " + placeBirth + "\nDate of birth: " + dateFormatter.format(birth);
	}
	
	public boolean isAdult() {
		Calendar minBirthAdult = Calendar.getInstance();
		
		minBirthAdult.add(Calendar.YEAR, -18);
		
		return minBirthAdult.after(this.dateBirth);
	}
}
