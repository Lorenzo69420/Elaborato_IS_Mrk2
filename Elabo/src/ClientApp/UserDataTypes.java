package ClientApp;

import java.util.ArrayList;

public enum UserDataTypes {
	Nome,
	Cognome,
	Luogo,
	Cittàl,
	Data,
	Password,
	RPassword;
	
	public static ArrayList<String> Names() {
		ArrayList<String> names = new ArrayList<>();
		for (var N: UserDataTypes.values()) {
			names.add(N.toString());
		}
		return names;
	}
}
