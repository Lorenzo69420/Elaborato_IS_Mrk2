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
	private Stage promptStage = new Stage();
	private Scene promptScene;

    @FXML
    private Text messageText;

    @FXML
    private Button procedeButton;

    public void setup(String message, EventHandler<ActionEvent> event) {
    	messageText.setText(message);
    	procedeButton.setOnAction(event);
    	promptStage.show();
    }
    	
    

	public void setMC(MainController mC) {
		this.MC = mC;
	}

	public void setScene(Scene promptScene) {
		this.promptScene = promptScene;
		this.promptStage.setScene(this.promptScene);
	}
	
	public Stage getStage() {
		return promptStage;
	}

}
