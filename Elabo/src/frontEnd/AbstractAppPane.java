package frontEnd;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public abstract class AbstractAppPane implements AppPane {
	private GridPane MainPane = new GridPane(); 
	private Scene AppScene = new Scene(MainPane);
	@Override
	public Scene getScene() {
		return AppScene;
	}
	@Override
	public void gridSetup(List<Integer> xSub, List<Integer> ySub) {
		
	}
	

}
