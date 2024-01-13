package ClientApp;

import java.util.TreeSet;

import Calendar.Date;
import Calendar.Days;
import Calendar.Year;
import javafx.application.Application;
import javafx.stage.Stage;
public class TestMain extends Application{

	public static void main(String args[]) {
		launch(args);
		//System.out.println(new Year(2023,Days.Monday));
		//System.out.println(new Month(2023,4,Days.Sunday));
		//System.out.println(new Week(new Date(30,4,2023),1));
	}

	@Override
	public void start(Stage stage) throws Exception {
		TreeSet<Year> years = new TreeSet<>();
		Year Y2023 = new Year(2023,Days.Monday);
		Year Y2024 = Y2023.iterator().next();
		years.add(Y2023);
		years.add(Y2024);
		//System.out.println(Y2023 + "\n" + Y2024);
		AppPane Register = null;
		try {
			 Register = new ActivitySelector(years,new Date(1,1,2023));
			 stage.setScene(Register.getScene());
			 stage.show();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Stage secondStage = new Stage();
		secondStage.setScene(new RegPane(10,20,20,UserDataTypes.Names()).getScene());
		secondStage.show();
		
		
	
	}
	
}
