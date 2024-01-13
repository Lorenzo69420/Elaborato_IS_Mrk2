package ClientApp;

import java.util.ArrayList;

public class UserList {
	private ArrayList<User> Users = new ArrayList<>();
	public void addUser(User o) throws ExistingUserException{
		for (User U : Users) {
			if (o.Login(U.getName(), U.getPassword())) {
				throw new ExistingUserException();
			}
		}
		Users.add(o);
	}
}
