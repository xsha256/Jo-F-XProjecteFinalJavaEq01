package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ventanaNuevaController {
	@FXML private Button volver;
	
	@FXML private HBox root;
	public static int NUM=12;
	
	public void actionVolver(ActionEvent e) { 
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();//recoge la ventana abierta
		window.close();//hace que se cierre la ventana abierta
	}
	
	
	

	
	
}
