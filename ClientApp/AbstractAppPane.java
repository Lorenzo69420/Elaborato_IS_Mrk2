package ClientApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public abstract class AbstractAppPane implements AppPane {
	
	protected GridPane pannello = new GridPane();
	
	protected Scene scena = new Scene(pannello);
	protected int heigth, width, insets;
	
	
	abstract public void gridSetup();
	
	@Override
	abstract public void textFill();
	
	public Scene getScene() {
		return scena;
	}

}
