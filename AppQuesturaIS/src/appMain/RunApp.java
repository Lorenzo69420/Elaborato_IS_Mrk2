package appMain;

import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
public class RunApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		//LoginPage LP = new LoginPage();
		//new SelectPage();
		
		MainController MC = new MainController(stage);
		MC.start();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
