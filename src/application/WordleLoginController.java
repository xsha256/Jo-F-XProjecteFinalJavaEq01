package application;



import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class WordleLoginController implements Initializable {

	@FXML
	private Button botoenrere;
	
	public void enrere() {
		Stage stage = (Stage)botoenrere.getScene().getWindow();
		stage.close();
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		
			

	}
			
	
	
	
}
