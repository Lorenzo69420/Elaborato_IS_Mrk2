package testing;

import java.util.Calendar;

import model.Database;
import model.Passport;
import model.Passport.PassportState;
import model.Person;
import model.PoliceStation;

public class MainTest {

	public static void main(String[] args) throws Exception {
		try {
			Database.init("jdbc:postgresql://localhost:5432/elaborato_is", "admin", "password", true);

			Person edi = new Person("EDI", "Edi", "De candido", 'm', "Trentino",
					new Calendar.Builder().setDate(2001, Calendar.DECEMBER, 27).build(), null, 123456);
			edi.insert();
			edi.register();

			Person lorenzo = new Person("LOR", "Lorenzo", "Bianchi", 'm', "Verona",
					new Calendar.Builder().setDate(2002, Calendar.SEPTEMBER, 14).build(), null, 789);
			lorenzo.insert();
			lorenzo.makeAdmin();
			lorenzo.register();
			
			

			PoliceStation ps = new PoliceStation("Verona");
			ps.insert();
			ps = new PoliceStation("Vicenza");
			ps.insert();
			ps = new PoliceStation("Padova");
			ps.insert();
			ps = new PoliceStation("Belluno");
			ps.insert();
			

			Passport pass = new Passport("EDI", new Calendar.Builder().setDate(2013, Calendar.JANUARY, 23).build(),
					PassportState.VALID, ps);
			pass.insert();
			
			
			
			Database.close();
		} catch (Exception e) {
			throw e;
		}

	}

}