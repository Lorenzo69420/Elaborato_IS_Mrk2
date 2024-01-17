package TestBackEnd;

import java.util.Calendar;

import TestBackEnd.Passport.PassportState;

public class Main {

	public static void main(String[] args) throws Exception {
		DatabaseManager.init("jdbc:postgresql://localhost:5432/elaborato_is", "edidec", "Univr2024");
		DatabaseManager.createTable();
		
		Person sbatachiones = new Person("TIA3", "Mattias", "Giambirtones", 't', 
				"Terronia", new Calendar.Builder().setDate(2002, Calendar.DECEMBER, 27).build(), 
				"Africa", Long.valueOf(69420));
		sbatachiones.insert();
		
		Passport pass = new Passport("TIA3", Calendar.getInstance(), PassportState.VALID);
		pass.insert();
	}

}