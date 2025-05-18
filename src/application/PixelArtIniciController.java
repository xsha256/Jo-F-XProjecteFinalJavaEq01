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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PixelArtIniciController implements Initializable {

	@FXML
	private VBox root;
	@FXML
	private Button botoJugar;
	@FXML
	private Button botoEnrere;
	@FXML
	private Button botoDesats;
	@FXML
	private Label xicotet;
	@FXML
	private Label mitja;
	@FXML
	private Label gran;
	@FXML
	private TextField amplada;
	@FXML
	private TextField altura;


	//funcions
	public void jugar(ActionEvent e) {
		try {
			//carreguem el fitxer fxml
			Parent root = FXMLLoader.load(getClass().getResource("PixelArtFXML.fxml"));
			//obtenim la finestra a partir de l'esdeveniment
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			//creem un nou controlador de l'escena següent
			//establim el graf d'escena a l'escena
			Scene escena2 = new Scene(root);
			//establim el fitxer d'estils css (el mateix de l'actual)
			escena2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//establim l'escena a la finestra
			window.setScene(escena2);
			//establim el títol de l'escena
			window.setTitle("Pixel Art");
			//mostrem la finestra
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//CANVI DE PANTALLA A LA SELECCIO DE JOC
	public void enrere() {
		
	}
	
	
	//CANVI DE PANTALLA A LA SELECCIÓ DE DESATS
	public void desats() {
		
	}
	
	//MIDA DE 16X16
	public void triarXicotet() {
		
	}
	
	//MIDA DE 32X32
	public void triarMitja() {
		
	}
	
	//MIDA DE 64X64
	public void triarGran() {
		
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// ACÍ HA DE TRIAR EL USUARI LA MIDA

		


	}
}
