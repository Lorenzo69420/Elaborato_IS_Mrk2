package ClientApp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RegPane extends AbstractAppPane {
	
	private HashMap<String,String> ClientInfo = new HashMap<>();
	private List<String> Names = new ArrayList<>();
	private ComboBox<String> NationBox;
	private ObservableList<String> NationItems = FXCollections.observableArrayList();
	
	private FileToMap FTM = new FileToMap();
	private ObservableList<String> CityItems = FXCollections.observableArrayList();
	private ComboBox<String> CityBox = new ComboBox<>(CityItems);	
	private Map<String,Text> Texts = new HashMap<>();
	private Map<Text,Node> Fields = new HashMap<>();
	
	private void fillCountries(Iterable<String> CountriesNames) {
		for (String Name: CountriesNames) {
			NationItems.add(Name);
		}
		NationBox = new ComboBox<String>(NationItems);
	}
	private void fillCities(Iterable<String> CitiesName) {
		CityItems.clear();
		for (String name: CitiesName) {
			CityItems.add(name);
		}
	}
	
	
	public void gridSetup() {
		pannello.setMinHeight(width);
		pannello.setMinWidth(heigth);
		pannello.setPadding(new Insets(insets)); 
		pannello.setVgap(5); 
		pannello.setHgap(5);
		pannello.setAlignment(Pos.CENTER); 
		int x = 0;
		for (String name : Names) {
			pannello.add(Texts.get(name),0,x);
			
			pannello.add(Fields.get(Texts.get(name)), 1, x++);
			if (name.equals("Luogo")) {
				
				NationBox.valueProperty().addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
						fillCities(FTM.getMap().get(NationBox.getValue()));
						
					}
					
				});
				
			}
		}
		Button RegButton = new Button("Register");
		RegButton.setOnAction( e -> {
			for(String name : UserDataTypes.Names()) {
				if ( Fields.get(Texts.get(name)) instanceof TextField ) {
					ClientInfo.put(name,((TextField)Fields.get(Texts.get(name))).getText());
					((TextField)Fields.get(Texts.get(name))).clear();
				}
				/*ClientInfo.put(name,Fields.get(Texts.get(name)).getText());
				//Fields.get(Texts.get(name)).clear();*/
			}
			if (!CheckRegInfo()) {
				ErrorPane ErSt = new ErrorPane("Invalid Credentials");
				ErSt.getErrorStage().show();
			}
		});
		pannello.add(RegButton, x++, x);
		

	}
	
	public RegPane (int insets, int width, int heigth, ArrayList<String> S) {
		this.width = width;
		this.heigth = heigth;
		this.insets = insets;
		for (String s: S) {
			Names.add(s);
		}
		Set<String> NationSet = new TreeSet<>();
		NationSet.addAll(FTM.getMap().keySet());
		fillCountries(NationSet);
		
		textFill();
		gridSetup();
	}
	
	private boolean CheckRegInfo() {
		//TODO 
		return false;
	}
	
	
	public void textFill() {
		for (String Name : Names) {
			if ( Name.equals("Luogo") ) {
				Texts.put( Name, new Text(Name+" di nascità") );
				Fields.put( Texts.get(Name), NationBox );
			} else if ( Name.equals("Cittàl") ) {
				Texts.put( Name, new Text("Città di nascità") );
				Fields.put( Texts.get(Name), CityBox );
			} else if ( Name.equals("Data") ) {
				Texts.put(Name, new Text(Name+" di nascità") );
				Fields.put( Texts.get(Name), new DatePicker() );
			} else if ( Name.equals("Password") ) {
				Texts.put( Name, new Text(Name) );
				Fields.put( Texts.get(Name), new PasswordField() );
			} else if ( Name.equals("RPassword") ) {
				Texts.put( Name, new Text("Repeat Password") );
				Fields.put( Texts.get(Name), new PasswordField() );	
			} else {
				Texts.put( Name, new Text(Name) );
				Fields.put( Texts.get(Name), new TextField() );
			} 
		}
	}
	
}
