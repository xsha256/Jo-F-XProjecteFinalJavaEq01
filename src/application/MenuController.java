package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuController {
	
	//Recogemos los botones "jugar" que tiene cada juego
	@FXML private Button jugarPescamines;
	@FXML private Button jugarWordle;
	@FXML private Button jugarPixelArt;
	@FXML private Button jugarJocDeLaVida;
	
	@FXML private HBox root;

	private boolean juegoActivo =false;
	
	//Ponemos el "onAction" de cada botón jugar al ser pulsado
	public void actionPescamines(ActionEvent e) { //boton jugar Pescamines
		//comprobación de si hay un juego activo o no
		
		if(juegoActivo==false) {
			juegoActivo=true;
			try {
				HBox root2 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
				Scene scene2= new Scene(root2);
				Stage window = new Stage();//(Stage) ((Node) e.getSource()).getScene().getWindow();
				window.setScene(scene2);
				window.setTitle("VentanaNueva");
				window.setMaximized(true);
				window.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else {
			System.out.println("No se puede acceder a otro juego mientras hay uno en marcha!");
		};
		juegoActivo=false;
	}
	
	
	public void actionWordle(ActionEvent e) {//boton jugar Wordle
		try {
			HBox root3 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene3= new Scene(root3);
			Stage window = new Stage(); //(Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene3);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void actionPixelArt(ActionEvent e) {//boton jugar Pixel Art
		try {
			HBox root4 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene4= new Scene(root4);
			Stage window = new Stage();// (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene4);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void actionJocDeLaVida(ActionEvent e) {//boton jugar Joc de la Vida
		try {
			HBox root5 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene5= new Scene(root5);
			Stage window = new Stage();// (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene5);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
}
