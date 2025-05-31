package application;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WordleLoginController implements Initializable {

	@FXML
	private VBox escena;
	@FXML
	private Button botoenrere;
	@FXML
	private Button botojugar;
	
	
	//metode per a tancar la finestra
	public void enrere() {
		Stage stage = (Stage)botoenrere.getScene().getWindow();
		stage.close();
	}

	public void jugar(ActionEvent e) {
		try {
			Parent escena = FXMLLoader.load(getClass().getResource("wordle.fxml"));
			Scene escena2 = new Scene(escena);
			//obtenim la finestra de l'aplicació actual
			 Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();			
			 window.setScene(escena2);
			 window.setTitle("Wordle");
			 //window.setMaximized(true);

			 window.show();
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	
			
	
	
	
}