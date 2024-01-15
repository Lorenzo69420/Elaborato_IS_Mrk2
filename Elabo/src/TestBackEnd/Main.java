package TestBackEnd;

import java.util.Calendar;

public class Main {

	public static void main(String[] args) {
		Person eli = new Person("ELI", "Elia", "Tonoli", "Verona", new Calendar.Builder().setCalendarType("iso8601").setDate(2001, Calendar.JULY, 30).build());
		System.out.println(eli);
		System.out.println("eli is adult: " + eli.isAdult());
	}

}