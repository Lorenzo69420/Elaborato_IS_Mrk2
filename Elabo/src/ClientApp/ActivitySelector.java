package ClientApp;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import Calendar.ActivityType;
import Calendar.Date;
import Calendar.Day;
import Calendar.Month;
import Calendar.Slot;
import Calendar.Week;
import Calendar.Year;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ActivitySelector  extends AbstractAppPane  {
	private HBox Layout = new HBox();
	private TreeSet<Year>  years;
	private Year year;
	private Date date;
	private Week week;
	private ActivitySelector precWeek;
	private ActivitySelector nextWeek; 
	private ObservableList<ActivityType> Activities = FXCollections.observableArrayList();
	private Map<Day,HashMap<Slot,ComboBox<ActivityType>>> WeekMap = new HashMap<>();
	private Scene Original;
	
	public ActivitySelector (TreeSet<Year> years,Date date) {
		this.years = years;
		this.date = date;
		Original = scena;
		textFill();
		//precWeek = new ActivitySelector(year,week.getWeekDays().first().getDate().getPrec());
		
		
		
		gridSetup();
		scena.setRoot(this.Layout);
		//System.out.println("DioB");
	}
	public ActivitySelector (TreeSet<Year> years,Date date,Scene oScena) {
		this.years = years;
		this.date = date;
		Original = oScena;
		textFill();		
		gridSetup();
		oScena.setRoot(this.Layout);
	}
	
	@Override
	public void gridSetup() {
		Button prec = new Button("Previous");
		prec.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				//System.out.println(week.getWeekDays().first().getDate().getPrec());
				try {
					precWeek = new ActivitySelector(years,week.getWeekDays().first().getDate().getPrec(),Original);
				
				}catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
			}
			
		});
		prec.setMinWidth(50);
		Layout.getChildren().add(prec);
		for (Day D : week.getWeekDays()) {
			VBox DBox = new VBox();
			DBox.setMinWidth(200);
			DBox.getChildren().add(new Text(D.toString()));
			for (Slot S : D.getSlots()) {
				if (!S.getActivity().equals(ActivityType.Closed)) {
					DBox.getChildren().add(WeekMap.get(D).get(S));
					WeekMap.get(D).get(S).setValue(S.getActivity());
					WeekMap.get(D).get(S).setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							S.SetActivity(WeekMap.get(D).get(S).getValue());
							
						}
						
					});
				} else {
					DBox.getChildren().add(new Text("Closed"));
				}
			}
			Layout.getChildren().add(DBox);
		}
		Button next = new Button("Next");
		next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					nextWeek = new ActivitySelector(years,week.getWeekDays().last().getDate().iterator().next(),Original);
				
				}catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
		next.setMinWidth(50);
		Layout.getChildren().add(next);	
	}

	@Override
	public void textFill() {
		try {
			getWeekandYear(date);
			WeekMap.clear();
			Activities.addAll(ActivityType.Names());
			for (Day D: week.getWeekDays()) {
				WeekMap.put(D,new HashMap<Slot,ComboBox<ActivityType>>());
				for (Slot S: D.getSlots()) {
					WeekMap.get(D).put(S, new ComboBox<ActivityType>(Activities));
				}
			}
		} catch(Exception e) {
			//Couldn't find the week
			System.out.println("Couldn't find the week");
		}
	}
	public void getWeekandYear(Date Wdate) throws WeekNotFoundException{
		
		for(Year Y:years) {
			if (Y.getYear()==Wdate.getYear()) {
				year = Y;
				for(Month M:year.getMonths()) {
					if (M.getName().ordinal() == Wdate.getMonth()-1) {
						//System.out.println(M);
						for(Week W:M.getWeeks()) {
							
							if((W.getWeekDays().first().getDate().compareTo(Wdate) == -1 || W.getWeekDays().first().getDate().compareTo(Wdate) == 0) && (W.getWeekDays().last().getDate().compareTo(Wdate) == 1 || W.getWeekDays().last().getDate().compareTo(Wdate) == 0)) {
								this.week = W;
								return;
							}
							
						}
					}
				}
			}
		}
		throw new WeekNotFoundException();
	}
	public HBox getLayout() {
		return Layout;
	}
}
