package frontEnd;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
public class LoginPage extends AbstractAppPane {
	private ArrayList<Button> ButtonList = new ArrayList<>();
	private ArrayList<Node> nodeList = new ArrayList<>();
	private Label errorLabel;
	private
	public LoginPage () {
		int x = 0;
		Integer colP[] = {33,33,33};
		Integer rowP[] = {50,50};
		gridInit(Arrays.asList(colP),Arrays.asList(rowP));
		setNodes();
		for (var N : nodeList) {
			getPane().add(N, x, 0);
			getPane().setHalignment(N, HPos.CENTER);
			x++;
		}
		x = 0;
		setButtons();
		for (var N :ButtonList) {
			getPane().add(N, x, 1);
			getPane().setHalignment(N, HPos.CENTER);
			x++;
		}
		getStage().setScene(getScene());
		getStage().show();
		
	}
	enum ButtonNames {
		Register,
		AdminLogin,
		Login;
	}
	private void setButtons() {
		for (var x: ButtonNames.values()) {
			String s = x.toString();
			if (x.equals(ButtonNames.AdminLogin)) {
				s = "Admin Login";
			}
			Button B = new Button(s);
			ButtonList.add(B);
			B.setTextAlignment(TextAlignment.CENTER);
			B.setOnAction( e -> {
				switch (B.getText()) {
				case "Register" :
					new RegisterPage();
					closeStage();
				}
			});
		}
	}
	private void setNodes() {
		nodeList.add(new Text("Codice \nFiscale"));
		nodeList.add(new TextField());
		nodeList.add(new Label(""));
		for (var x: nodeList) {
			if (x instanceof Text ) {
				((Text)x).setTextAlignment(TextAlignment.CENTER);
			}
			if (x instanceof Label) {
				errorLabel = ((Label)x);
				errorLabel.setAlignment(Pos.CENTER);
			}
		}
	}
}


