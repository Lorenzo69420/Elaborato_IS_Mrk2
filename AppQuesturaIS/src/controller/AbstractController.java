package controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public abstract class AbstractController {
	private MainController mainController;
	private FXMLLoader loader;
	private Scene scene;
	protected void setMainController(MainController MC) {
		this.mainController = MC;
	}
	protected AbstractController(String fileName, MainController MC) {
		loader = new FXMLLoader(getClass().getResource("../view/" + fileName + ".fxml"));
		loader.setController(this);
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setMainController(MC);
	}
	protected MainController getMC() {
		return mainController;
	}
	protected Scene getScene() {
		return scene;
	}
}
