package TestBackEnd;

import java.util.Calendar;

public class Main {

	public static void main(String[] args) throws Exception {
		DatabaseManager.init("jdbc:postgresql://localhost:5432/elaborato_is", "edidec", "Univr2024");
		DatabaseManager.createTable();
		
		Person sbatachiones = new Person("TIA2", "Mattias", "Giambirtones", 't', 
				"Terronia", new Calendar.Builder().setDate(2002, Calendar.DECEMBER, 27).build(), 
				"Africa", Long.valueOf(69420));
		sbatachiones.insert();
	}

}