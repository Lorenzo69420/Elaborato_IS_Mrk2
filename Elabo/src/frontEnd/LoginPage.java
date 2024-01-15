package frontEnd;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
public class LoginPage extends AbstractAppPane {
	private ArrayList<Button> ButtonList = new ArrayList<>();
	public LoginPage () {
		Text errMessage = new Text("");
		
		Integer colP[] = {33,33,33};
		Integer rowP[] = {50,50};
		gridSetup(Arrays.asList(colP),Arrays.asList(rowP));
		MainPane.add(new Text("Codice \nFiscale"), 0, 0); 
		MainPane.add(new TextField(), 1, 0);
		MainPane.add(errMessage, 2, 0);
		int x = 0;
		for (var N :getButtonNames()) {
			Button B = new Button(N);
			MainPane.add(B, x, 1);
			x++;
		}
		
	}
	enum ButtonNames {
		Register,
		AdminLogin,
		Login;
	}
	private List<String> getButtonNames() {
		ArrayList<String> Names = new ArrayList<>();
		for (var x: ButtonNames.values()) {
			String s = x.toString();
			if (x.equals(ButtonNames.AdminLogin)) {
				s = "Admin Login";
			}
			Names.add(s);
		}
		return Names;
	}
}


