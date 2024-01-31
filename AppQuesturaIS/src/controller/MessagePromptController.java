package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessagePromptController {
	
	private MainController MC;
	private Stage promptStage;
	private Scene pomptScene;

    @FXML
    private Text messageText;

    @FXML
    private Button procedeButton;

    @FXML
    private void exitApp() {
    	promptStage.close();
    }
    
    public void setup(String message, EventHandler<ActionEvent> event) {
    	messageText.setText(message);
    	procedeButton.setOnAction(event);
    	promptStage.show();
    }
    	
    

	public void setMC(MainController mC) {
		MC = mC;
	}

	public void setStageAndScene(Stage promptStage, Scene pomptScene) {
		this.promptStage = promptStage;
		this.pomptScene = pomptScene;
		this.promptStage.setScene(pomptScene);
	}
	
	public Stage getStage() {
		return promptStage;
	}

}
