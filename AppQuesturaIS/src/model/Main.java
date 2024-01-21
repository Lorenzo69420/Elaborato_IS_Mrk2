package model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;

import model.Passport.PassportState;
import model.Reservation.ReservationState;
import model.Reservation.ReservationType;

public class Main {

	public static void main(String[] args) throws Exception {
		DatabaseManager.init("jdbc:postgresql://localhost:5432/elaborato_is", "admin", "password", true);

		DatabaseManager.createTable();

		Person sbatachiones = new Person("TIA3", "Mattias", "Giambirtones", 't', "Terronia",
				new Calendar.Builder().setDate(2002, Calendar.DECEMBER, 27).build(), "Africa", 69420);
		sbatachiones.insert();
		
		Person IlKingNeto = new Person("CH4D", "Neto", "Whites", '$', "vostri muri",
				new Calendar.Builder().setDate(2002, Calendar.SEPTEMBER, 14).build(), "Supremo", 9001);
		IlKingNeto.insert();
		IlKingNeto.makeAdmin();
		sbatachiones.addTutor(IlKingNeto);
		

		PoliceStation ps = new PoliceStation("Verona");
		ps.insert();
		ps = new PoliceStation("Vicenza");
		ps.insert();
		ps = new PoliceStation("Padova");
		ps.insert();
		ps = new PoliceStation("Belluno");
		ps.insert();

		System.out.println(DatabaseManager.getPoliceStation());

		Passport pass = new Passport("TIA3", Calendar.getInstance(), PassportState.VALID, ps);
		pass.insert();

		Reservation res = new Reservation(ReservationType.COLLECTION, LocalDateTime.now(), pass, sbatachiones, ps, ReservationState.BOOKABLE);
		res.insert();
		res = new Reservation(ReservationType.ISSUANCE_LOST,LocalDateTime.of(2023, Month.JANUARY, 1, 9, 0),new PoliceStation("Verona"));
		res.insert();
		res = new Reservation(ReservationType.ISSUANCE_NEW,LocalDateTime.of(2023, Month.JANUARY, 1, 9, 0),new PoliceStation("Verona"));
		res.insert();
		res = new Reservation(ReservationType.COLLECTION, LocalDateTime.of(2023, Month.JANUARY, 1, 10, 0), pass, sbatachiones, ps, ReservationState.BOOKED_UP);
		res.insert();
		res = new Reservation(ReservationType.ISSUANCE_EXPIRED, LocalDateTime.of(2023, Month.JANUARY, 1, 10, 0), pass, sbatachiones, ps, ReservationState.BOOKED_UP);
		res.insert();
		
		Person sbat = new Person("TIA3", "Mattias", "Giambirtones", "Terronia",
				new Calendar.Builder().setDate(2002, Calendar.DECEMBER, 27).build());
		sbat.exists();
		DatabaseManager.close();
	}

}