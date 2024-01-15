package TestBackEnd;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.sql.Date;

public class Person {
	private String IDcode;
	private String name;
	private String surname;
	private String placeBirth;
	private Calendar dateBirth;
	private String belonging_category; //TODO: non so se sia il termine corretto
	private String num_sanitario; //TODO: anche qua
	private Person tutor = null;
	
	public Person(String IDcode, String name, String surname, String placeBirth, Calendar dateBirth, String belonging_category, String num_sanitario) {
		this.IDcode = IDcode;
		this.name = name;
		this.surname = surname;
		this.placeBirth = placeBirth;
		this.dateBirth = dateBirth;
		this.belonging_category = belonging_category;
		this.num_sanitario = num_sanitario;
	}
	
	public void asignTutor(Person tutor) {
		this.tutor = tutor;
	}
	
	@Override
	public String toString() {
		java.util.Date birth = dateBirth.getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		
		return "Name: " + name + "\nSurname: " + surname + "\nID code: " + IDcode + "\nPlace of birth: " + placeBirth + "\nDate of birth: " + dateFormatter.format(birth);
	}
	
	public boolean isAdult() {
		Calendar currentDate = Calendar.getInstance();
		
		currentDate.add(Calendar.YEAR, -18);
		
		return currentDate.after(this.dateBirth);
	}
}
