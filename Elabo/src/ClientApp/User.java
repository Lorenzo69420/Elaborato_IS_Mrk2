package ClientApp;

import java.util.HashMap;

public class User {
	private HashMap<UserDataTypes,String> UserData = new HashMap<>();
	public User (HashMap<UserDataTypes,String> UserData) {
		this.UserData=UserData;
	}
	public boolean Login(String name, String pass) {
		if (name.equals(UserData.get(UserDataTypes.Nome)) && pass.equals(UserData.get(UserDataTypes.Password))) {
			return true;
		}
		return false;
	}
	public String getName() {
		return UserData.get(UserDataTypes.Nome);
	}
	public String getPassword() {
		return UserData.get(UserDataTypes.Password);
	}
}
