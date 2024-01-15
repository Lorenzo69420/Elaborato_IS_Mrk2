package frontEnd;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public abstract class AbstractAppPane implements AppPane {
	protected GridPane MainPane = new GridPane(); 
	private Scene AppScene = new Scene(MainPane);
	@Override
	public Scene getScene() {
		return AppScene;
	}
	@Override
	public void gridSetup(List<Integer> xSub, List<Integer> ySub) {
		ArrayList<ColumnConstraints> colSub = new ArrayList<>();
		ArrayList<RowConstraints> rowSub = new ArrayList<>();
		MainPane.setLayoutX(xSub.size());
		MainPane.setLayoutY(ySub.size());
		MainPane.setGridLinesVisible(true);
		for (var x :xSub) {
			ColumnConstraints tmp = new ColumnConstraints();
			tmp.setPercentWidth(x);
			colSub.add(tmp);
		}
		for (var y :ySub) {
			RowConstraints tmp = new RowConstraints();
			tmp.setPercentHeight(y);
			rowSub.add(tmp);
		}
		MainPane.getColumnConstraints().addAll(colSub);
		MainPane.getRowConstraints().addAll(rowSub);
		MainPane.setHgap(MainPane.heightProperty().get()/10);
	}
}
