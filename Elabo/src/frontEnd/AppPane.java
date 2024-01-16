package frontEnd;

import java.util.List;

import javafx.scene.Scene;
public interface AppPane {
	public Scene getScene();
	public void closeStage();
	public void gridInit( List<Integer> xSub, List<Integer> ySub);
}

