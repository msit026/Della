package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class democontroller {
	
	@FXML
	private TextField f1;
	
	public democontroller() {
		
		f1 = new TextField();
		f1.setText("Manoj");
		System.out.println("from demo");
		
	}
	
	
}
