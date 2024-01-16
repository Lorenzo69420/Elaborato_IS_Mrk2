package frontEnd;

import java.util.Arrays;

public class SelectPage extends AbstractAppPane {
	public SelectPage() {
		Integer[] colSub = {25,25,25,25};
		Integer[] rowSub = {30,30,30};
		gridInit(Arrays.asList(colSub),Arrays.asList(rowSub));
		getStage().setScene(getScene());
		getStage().show();
	}
}
