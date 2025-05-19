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

	private Taulell taulell;

	// funcions
	public void jugar(ActionEvent e) {
		try {

			taulell.setAmple(Integer.parseInt(amplada.getText()));
			taulell.setAltura(Integer.parseInt(altura.getText()));
			DadesPixelArt.getInstancia().setTaulell(taulell);
			// carreguem el fitxer fxml
			Parent root = FXMLLoader.load(getClass().getResource("PixelArtFXML.fxml"));

			// creem un nou controlador de l'escena següent
			// establim el graf d'escena a l'escena
			Scene escena2 = new Scene(root);
			// obtenim la finestra a partir de l'esdeveniment
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			// establim el fitxer d'estils css (el mateix de l'actual)
			// establim l'escena a la finestra
			window.setScene(escena2);
			// establim el títol de l'escena
			window.setTitle("Pixel Art");
			window.setMaximized(true);
			System.out.println("Finestra maximitzada: " + window.isMaximized());
			// mostrem la finestra
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// CANVI DE PANTALLA A LA SELECCIO DE JOC
	public void enrere() {

	}

	// CANVI DE PANTALLA A LA SELECCIÓ DE DESATS
	public void desats() {

	}

	// MIDA DE 16X16
	public void triarXicotet() {
		amplada.setText("16");
		amplada.setStyle("-fx-text-fill: #e8e8e8; -fx-background-color: #365057;");
		altura.setText("16");
		altura.setStyle("-fx-text-fill: #e8e8e8; -fx-background-color: #365057;");
	}

	public void canviXicotet() {
		xicotet.setOnMouseEntered(e -> xicotet.setStyle("-fx-font-size: 20px;"));
		xicotet.setOnMouseExited(e -> xicotet.setStyle(""));
	}

	// MIDA DE 32X32
	public void triarMitja() {
		amplada.setText("32");
		amplada.setStyle("-fx-text-fill: #e8e8e8; -fx-background-color: #365057;");
		altura.setText("32");
		altura.setStyle("-fx-text-fill: #e8e8e8; -fx-background-color: #365057;");
	}

	public void canviMitja() {
		mitja.setOnMouseEntered(e -> mitja.setStyle("-fx-font-size: 20px;"));
		mitja.setOnMouseExited(e -> mitja.setStyle(""));
	}

	// MIDA DE 64X64
	public void triarGran() {
		amplada.setText("64");
		amplada.setStyle("-fx-text-fill: #e8e8e8; -fx-background-color: #365057;");
		altura.setText("64");
		altura.setStyle("-fx-text-fill: #e8e8e8; -fx-background-color: #365057;");
	}

	public void canviGran() {
		gran.setOnMouseEntered(e -> gran.setStyle("-fx-font-size: 20px;"));
		gran.setOnMouseExited(e -> gran.setStyle(""));

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		canviGran();
		canviMitja();
		canviXicotet();

		//pose una mida per defecte
		triarMitja();

		int ample = Integer.parseInt(amplada.getText());
		int alt = Integer.parseInt(altura.getText());
		taulell = new Taulell(ample, alt);

		DadesPixelArt.getInstancia().setTaulell(taulell);

	}
}
