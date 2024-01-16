package frontEnd;

import java.util.Arrays;

public class RegisterPage extends AbstractAppPane {
	public RegisterPage() {
		Integer[] rowSub = {12,12,12,12,12,12,12,12};
		Integer[] colSub = {50,50};
		gridInit(Arrays.asList(colSub),Arrays.asList(rowSub));
		getStage().setScene(getScene());
		getStage().show();
	}
}
