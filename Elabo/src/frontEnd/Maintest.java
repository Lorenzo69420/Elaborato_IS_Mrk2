package frontEnd;

import javafx.application.Application;
import javafx.stage.Stage;
public class Maintest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new LoginPage().getScene());
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
