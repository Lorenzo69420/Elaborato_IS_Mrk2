package TestBackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

public class Person {
	private String IDcode;
	private String name;
	private String surname;
	private String placeBirth;
	private Calendar dateBirth;
	private String belonging_category; //TODO: non so se sia il termine corretto
	private Long num_sanitario; //TODO: anche qua
	private Person tutor;
	
	private Connection connection = null;
	
	public Person(String IDcode, String name, String surname, String placeBirth, Calendar dateBirth, String belonging_category, Long num_sanitario) throws SQLException {
		this.IDcode = IDcode;
		this.name = name;
		this.surname = surname;
		this.placeBirth = placeBirth;
		this.dateBirth = dateBirth;
		this.belonging_category = belonging_category;
		this.num_sanitario = num_sanitario;
		
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ingegneria", "neto", "fethergay");
		this.connection = connection;
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
		
		return "Name: " + name + "\nSurname: " + surname + "\nID code: " + IDcode + "\nPlace of birth: " + placeBirth + "\nDate of birth: " + dateFormatter.format(birth);
	}
	
	public boolean isAdult() {
		Calendar currentDate = Calendar.getInstance();
		
		currentDate.add(Calendar.YEAR, -18);
		
		return currentDate.after(this.dateBirth);
	}
	
	
	
	public void insert() throws SQLException {
		var stmt = connection.prepareStatement("INSERT INTO person VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

	    stmt.setString(1, this.IDcode);
	    stmt.setString(2, this.name);
	    stmt.setString(3, this.surname);
	    stmt.setDate(4, new Date(this.dateBirth.getTime().getTime()));
	    stmt.setString(5, this.placeBirth);
	    stmt.setLong(6, this.num_sanitario);
	    stmt.setString(7, this.belonging_category);
	    stmt.setObject(8, this.tutor);
	    
	    System.out.println(stmt);

	    stmt.executeUpdate();
	}

}
