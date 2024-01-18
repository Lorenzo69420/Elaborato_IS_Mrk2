package model;

import java.time.LocalDateTime;
import java.util.Calendar;

import model.Passport.PassportState;
import model.Reservation.ReservationType;

public class Main {

	public static void main(String[] args) throws Exception {
		DatabaseManager.init("jdbc:postgresql://localhost:5432/elaborato_is", "edidec", "Univr2024");
		DatabaseManager.dropTable(true);
		DatabaseManager.createTable();
		
		Person sbatachiones = new Person("TIA3", "Mattias", "Giambirtones", 't', 
				"Terronia", new Calendar.Builder().setDate(2002, Calendar.DECEMBER, 27).build(), 
				"Africa", Long.valueOf(69420));
		sbatachiones.insert();
		
		PoliceStation ps = new PoliceStation("Verona");
		ps.insert();
		
		Passport pass = new Passport("TIA3", Calendar.getInstance(), PassportState.VALID, ps);
		pass.insert();
		
		Reservation res = new Reservation(ReservationType.COLLECTION, LocalDateTime.now(), pass, sbatachiones, ps);
		res.insert();
	}

}