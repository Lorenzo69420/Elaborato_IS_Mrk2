package controller;

import org.junit.jupiter.api.Test;

//import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.*;


import javafx.stage.Stage;

class JUnitControllerTest {
	private MainController MC = new MainController(new Stage());
	private static String DESCRIPTIONUSER = "Benvenuto nel sistema di prenotazione per l'emissione e il ritiro dei passaporti";
	private static String DESCRIPTIONADMIN = "Benvenuto nel sistema di inserimento degli appuntamenti da prendere in questura";
	@Test
	void testActivityAdminController() {
		var controller = new ActivityAdminController(MC);
		// The object is not null
		assertNotNull(controller, () -> "Controller null");
		// The setDescription method inserts the right string
		assertEquals(controller.getDescriptionText().getText(), DESCRIPTIONADMIN);
		assertEquals(controller.getSaveButton().getText(), "Conferma");
		
		
	}
	@Test
	void testActivityUserController() {
		
	}
	@Test 
	void testAddTutorController() {
		
	}
	@Test
	void testLoginController() {
		
	}
	@Test
	void testMainController() {
		
	}
	@Test 
	void testMessagePromptController() {
		
	}
	@Test 
	void testRegisterController() {
		
	}

}
