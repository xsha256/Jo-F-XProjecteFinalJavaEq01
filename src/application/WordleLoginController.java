package application;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
			//si le damos a jugar, cierra la ventana de inicio y muestra el juego
			Stage stage = (Stage)botoenrere.getScene().getWindow();
			stage.close();
			
			Parent escena = FXMLLoader.load(getClass().getResource("wordle.fxml"));
			Scene escena2 = new Scene(escena);
			//obtenim la finestra de l'aplicació actual
			 Stage window = new Stage();//(Stage) ((Node) e.getSource()).getScene().getWindow();			
			 window.setScene(escena2);
			 window.setTitle("Wordle");
			 window.setMaximized(true);
			 
			 window.show();
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//funcion que cambia el estado de los booleans para poder duplicados abiertos del mismo juego
		Platform.runLater(()->{
			Stage ventanaActual = (Stage) escena.getScene().getWindow();
			if(ventanaActual.isShowing()) {
				MenuController.infowordleActivo=true;
				System.out.println("La ventana info-wordle esta activa. Boolean: "+MenuController.infowordleActivo);
				System.out.println("BooleanJuegoActivo: "+MenuController.wordleActivo);
			}
			ventanaActual.setOnHidden(evt ->{
				MenuController.infowordleActivo=false;
				System.out.println("La ventana info-wordle se cerró. Boolean: "+MenuController.infowordleActivo);
				System.out.println("BooleanJuegoActivo: "+MenuController.wordleActivo);
			});
		});

	}

	
			
	
	
	
}