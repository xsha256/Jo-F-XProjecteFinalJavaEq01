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
import javafx.scene.control.Alert;
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

	//COMPROVA QUE LA ENTRADA SIGUEN NUMEROS I MULTIPLES DE 16
	public boolean comprovarEntrada(String s1, String s2) {

	
		if (s1.matches("\\d+") && s2.matches("\\d+")) {
			int ample = Integer.parseInt(s1);
	        int alt = Integer.parseInt(s2);
	        //MULTIPLES DE 16, QUE SI NO LA QUADRICULA ES PETA
			return ample % 16 == 0 && alt % 16 == 0 && ample <= 128 && alt <= 64;
		} else {
			return false;
		}
	}

	//FA UN RECULL DE LES DADES PASSADES PER SETTEXT, LES COMPROVA I CREA EL NOU PANELL
	public void jugar(ActionEvent e) {
		
		try {
			
			 boolean valorsValids = comprovarEntrada(amplada.getText(), altura.getText());
		        int ample = 0;
		        int alt = 0;
		        boolean ajustat = false;

		        if (valorsValids) {
		            ample = Integer.parseInt(amplada.getText());
		            alt = Integer.parseInt(altura.getText());

		            if (ample > 128 || ample==0) {
		                ample = 128;
		                ajustat = true;
		            }

		            if (alt > 64 || alt==0) {
		                alt = 64;
		                ajustat = true;
		            }

		            if (ajustat) {
		                Alert alerta = new Alert(Alert.AlertType.WARNING);
		                alerta.setTitle("Límit excedit");
		                alerta.setHeaderText(null);
		                alerta.setContentText("Els valors màxims són 128 d'amplada i 64 d'altura. S'han ajustat automàticament.");
		                alerta.showAndWait();
		            }

		        } else {
		            ample = 32;
		            alt = 32;
		        }
			taulell.setAmple(ample);
			taulell.setAltura(alt);
			
			DadesPixelArt.getInstancia().setTaulell(taulell);
			Parent root = FXMLLoader.load(getClass().getResource("PixelArtFXML.fxml"));
			Scene escena2 = new Scene(root);
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Pixel Art");
			window.setMaximized(true);
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// CANVI DE PANTALLA A LA SELECCIO DE JOC LA TE YORDAN

	public void enrere() {

		Stage stage = (Stage) botoEnrere.getScene().getWindow();
		stage.close();

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

		root.prefWidthProperty().bind(root.widthProperty());
		root.prefHeightProperty().bind(root.heightProperty());
		canviGran();
		canviMitja();
		canviXicotet();

		// pose una mida per defecte
		triarMitja();

		int ample = Integer.parseInt(amplada.getText());
		int alt = Integer.parseInt(altura.getText());
		
		if (ample > 128) ample = 128;

		if (alt > 64) alt = 64;
		
		taulell = new Taulell(ample, alt);

		DadesPixelArt.getInstancia().setTaulell(taulell);

	}
}
