package frontEnd;

import controller.RegisterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class Maintest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		//LoginPage LP = new LoginPage();
		//new SelectPage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Register.fxml"));
        
		BorderPane root = loader.load();
		RegisterController RG = loader.getController();
		Scene diob = new Scene(root);
		
		stage.setScene(diob);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
