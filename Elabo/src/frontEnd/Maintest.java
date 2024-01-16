package frontEnd;

import javafx.application.Application;
import javafx.stage.Stage;
public class Maintest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		LoginPage LP = new LoginPage();
		new SelectPage();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
