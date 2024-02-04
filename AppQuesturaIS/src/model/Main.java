package model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;

import model.Passport.PassportState;
import model.Reservation.ReservationState;
import model.Reservation.ReservationType;

public class Main {

	public static void main(String[] args) throws Exception {
		try {
			Database.init("jdbc:postgresql://localhost:5432/elaborato_is", "admin", "password", true);

			Person sbatachiones = new Person("TIA", "Mattias", "Giambirtones", 't', "Terronia",
					new Calendar.Builder().setDate(2022, Calendar.DECEMBER, 27).build(), "Africa", 69420);
			sbatachiones.insert();
			sbatachiones.register();

			Person IlKingNeto = new Person("CH4D", "Neto", "Whites", '$', "vostri muri",
					new Calendar.Builder().setDate(2002, Calendar.SEPTEMBER, 14).build(), "Supremo", 9001);
			IlKingNeto.insert();
			IlKingNeto.makeAdmin();
			IlKingNeto.register();
			sbatachiones.addTutor(IlKingNeto);
			
			

			PoliceStation ps = new PoliceStation("Verona");
			ps.insert();
			ps = new PoliceStation("Vicenza");
			ps.insert();
			ps = new PoliceStation("Padova");
			ps.insert();
			ps = new PoliceStation("Belluno");
			ps.insert();

			Passport pass = new Passport("TIA", new Calendar.Builder().setDate(2020, Calendar.JANUARY, 23).build(),
					PassportState.VALID, ps);
			pass.insert();

			Reservation res = new Reservation(ReservationType.COLLECTION, LocalDateTime.now(), pass, sbatachiones, ps,
					ReservationState.BOOKABLE);
			res.insert();
			res = new Reservation(ReservationType.ISSUANCE_LOST, LocalDateTime.of(2023, Month.JANUARY, 1, 9, 0),
					new PoliceStation("Verona"));
			res.insert();
			res = new Reservation(ReservationType.ISSUANCE_NEW, LocalDateTime.of(2023, Month.JANUARY, 1, 9, 0),
					new PoliceStation("Verona"));
			res.insert();
			res = new Reservation(ReservationType.ISSUANCE_EXPIRED, LocalDateTime.of(2023, Month.JANUARY, 22, 10, 0),
					null, sbatachiones, ps, ReservationState.BOOKED_UP);
			//res.book(sbatachiones);
			res = new Reservation(ReservationType.COLLECTION, LocalDateTime.of(2024, Month.FEBRUARY, 2, 10, 0), ps);
			res.book(sbatachiones);
			res = new Reservation(ReservationType.COLLECTION, LocalDateTime.of(2023, Month.JANUARY, 1, 10, 0), pass,
					sbatachiones, ps, ReservationState.BOOKED_UP);
			res.insert();

			@SuppressWarnings("unused")
			Person sbat = new Person("TIA", "Mattias", "Giambirtones", "Terronia",
					new Calendar.Builder().setDate(2002, Calendar.DECEMBER, 27).build());
			//sbat.exists();

			Person fethe = new Person("FETHER", "Federico", "Buchi", "Buco di culo di Illasi",
					new Calendar.Builder().setDate(2001, Calendar.DECEMBER, 29).build());
			fethe.insert();
			fethe.register();
			
			Person teo = new Person("TEO", "Leonardo", "Baldassari", "Golosangeles",
					new Calendar.Builder().setDate(2010, Calendar.OCTOBER, 10).build());
			teo.insert();
			Database.close();
			System.out.println("Setuppato il database");
		} catch (NotBookableException e) {
			System.out.println(e.getType());
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

}