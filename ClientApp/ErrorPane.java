package ClientApp;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorPane extends AbstractAppPane {
	private String ErrorMessage;
	private Stage ErrorStage = new Stage();
	public ErrorPane(String ErrorMessage) {
		this.ErrorMessage = ErrorMessage;
		gridSetup();
		ErrorStage.setScene(scena);
	}
	@Override
	public void gridSetup() {
		pannello.setMinHeight(50);
		pannello.setMinWidth(200);
		pannello.setPadding(new Insets(10)); 
		pannello.setVgap(5); 
		pannello.setHgap(5);
		pannello.setAlignment(Pos.CENTER); 
		pannello.add(new Text(ErrorMessage), 0, 0);
		Button CloseButton = new Button("Close");
		pannello.add(CloseButton, 0, 1);
		CloseButton.setOnAction( e -> {
			ErrorStage.close();
		});

	}
	public Stage getErrorStage() {
		return ErrorStage;
	}
	@Override
	public void textFill() {
		// TODO Auto-generated method stub
		// Not used for this Pane
	}

}
