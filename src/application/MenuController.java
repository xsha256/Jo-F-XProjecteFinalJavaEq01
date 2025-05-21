package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController {
	
	//Recogemos los botones "jugar" que tiene cada juego
	@FXML private Button jugarPescamines;
	@FXML private Button jugarWordle;
	@FXML private Button jugarPixelArt;
	@FXML private Button jugarJocDeLaVida;
	
	@FXML private HBox root;

	
	//Ponemos el "onAction" de cada botón jugar al ser pulsado
	public void actionPescamines(ActionEvent e) { //boton jugar Pescamines
		try {
			HBox root2 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene2= new Scene(root2);
			Stage window = new Stage();//(Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene2);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
//			window.show();
			/*en vez de mostrar directamente, vamos a hacer que al abrir la nueva ventana, esta quede inactiva
			 * y que solo vuelva a estar activa si se cierra la nueva ventana que se ha abierto
			 */
			
			//Hacemos que esta ventana sea MODAL
	        window.initModality(Modality.WINDOW_MODAL);

	        //Establecemos que la ventana "dueña" es la del menu(la que pasa a ser inactiva)
	        Stage menuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	        window.initOwner(menuStage);

	        //Mostramos la ventana y bloqueamos la del menu hasta que se cierre esta
	        window.showAndWait();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
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
