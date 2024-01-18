package frontEnd;

import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.DatabaseManager;
public class Maintest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		//LoginPage LP = new LoginPage();
		//new SelectPage();
		
		MainController MC = new MainController();
		MC.start();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
