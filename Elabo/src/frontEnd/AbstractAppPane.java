package frontEnd;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public abstract class AbstractAppPane implements AppPane {
	private GridPane mainPane = new GridPane(); 
	private Scene appScene = new Scene(mainPane);
	private Stage mainStage = new Stage();
	@Override
	public Scene getScene() {
		return appScene;
	}
	public Stage getStage() {
		return mainStage;
	}
	public GridPane getPane() {
		return mainPane;
	}
	@Override
	public void closeStage() {
		mainStage.close();
	}
	@Override
	public void gridInit(List<Integer> xSub, List<Integer> ySub) {
		ArrayList<ColumnConstraints> colSub = new ArrayList<>();
		ArrayList<RowConstraints> rowSub = new ArrayList<>();
		mainPane.setLayoutX(xSub.size());
		mainPane.setLayoutY(ySub.size());
		mainPane.setGridLinesVisible(true);
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
		mainPane.getColumnConstraints().addAll(colSub);
		mainPane.getRowConstraints().addAll(rowSub);
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setMinHeight(ySub.size()*45);
		mainPane.setMinWidth(xSub.size()*180);
		
	}
}
