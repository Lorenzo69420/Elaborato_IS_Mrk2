package appMain;

import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
public class RunApp extends Application {

	@Override
	public void start(Stage stage) {
		
		MainController MC = new MainController(stage);
		MC.startApp();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
