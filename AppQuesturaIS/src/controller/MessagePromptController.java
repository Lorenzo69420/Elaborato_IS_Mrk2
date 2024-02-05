package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessagePromptController extends AbstractController {
	
	protected MessagePromptController(MainController MC) {
		super("MessagePrompt", MC);
		promptStage.setScene(getScene());
	}

	private Stage promptStage = new Stage();

    @FXML
    private Text messageText;

    @FXML
    private Button procedeButton;

    public void setup(String message, EventHandler<ActionEvent> event, boolean error) {
    	messageText.setText(message);
    	procedeButton.setOnAction(event);
    	promptStage.show();
    	promptStage.setTitle(error ? "Errore" : "Successo");
    }
	
	public Stage getStage() {
		return promptStage;
	}

}
