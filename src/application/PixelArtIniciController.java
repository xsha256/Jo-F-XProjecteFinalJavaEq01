package application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
	
	//variable para marcar las casillas de medida
	private Label seleccionatLabel = null;

	// TAULELL SERVEIX PER CANVIAR DE DADES ENTRE FINESTRES
	private Taulell taulell;
	
	//funcion para marcar las casillas marcadas
	private void marcarLabel(Label labelSeleccionat) {
		//Resetea el estilo a todos
		xicotet.setStyle("");
		mitja.setStyle("");
		gran.setStyle("");

		//Marca el seleccionado
		labelSeleccionat.setStyle("-fx-background-color: #e4a81d; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight:bold;");
		seleccionatLabel = labelSeleccionat;
	}

	// COMPROVA QUE LA ENTRADA SIGUEN NUMEROS I MULTIPLES DE 16
	public boolean comprovarEntrada(String s1, String s2) {

		if (s1.matches("\\d+") && s2.matches("\\d+")) {
			int ample = Integer.parseInt(s1);
			int alt = Integer.parseInt(s2);
			// MULTIPLES DE 16, QUE SI NO LA QUADRICULA ES PETA
			return ample % 16 == 0 && alt % 16 == 0 && ample <= 128 && alt <= 64;
		} else {
			return false;
		}
	}

	// FA UN RECULL DE LES DADES PASSADES PER SETTEXT, LES COMPROVA I CREA EL NOU
	// PANELL COMPROVAR QUE NO POSA TEXT, SI POSA TEXT 32X32 PREDEFINIT
	public void jugar(ActionEvent e) {
		MenuController.midapixelartActivo=false;
		MenuController.pixelartActivo=true;
		try {

			boolean valorsValids = comprovarEntrada(amplada.getText(), altura.getText());
			int ample = 0;
			int alt = 0;
			boolean ajustat = false;

			if (valorsValids) {
				ample = Integer.parseInt(amplada.getText());
				alt = Integer.parseInt(altura.getText());

				if (ample > 128 || ample <= 0) {
					ample = 128;
					ajustat = true;
				}

				if (alt > 64 || alt <= 0) {
					alt = 64;
					ajustat = true;
				}

				if (ajustat) {
					Alert alerta = new Alert(Alert.AlertType.WARNING);
					alerta.setTitle("Límit excedit");
					alerta.setHeaderText(null);
					alerta.setContentText(
							"Els valors màxims són 128 d'amplada i 64 d'altura. S'han ajustat automàticament.");
					alerta.showAndWait();
				}

			} else {
				ample = 32;
				alt = 32;
			}

			taulell.setAmple(ample);
			taulell.setAltura(alt);

			DadesPixelArt.getInstancia().setTaulell(taulell);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("PixelArtFXML.fxml"));
			String rutaFXML="PixelArtFXML.fxml";
			Parent root = loader.load();
			PixelArtController controlador = loader.getController();
			
			MenuController.controladorPixelArt = controlador;//lo necesita yordan para guardar!! no borrar

			Scene escena2 = new Scene(root);
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Pixel Art");
			window.setMaximized(true);

			controlador.tancarPixelArt(window);

			window.show();
			
			// añadir los juegos abiertos
			MenuController.juegosAbiertos.add(window);
			MenuController.juegosPorNombre.put(rutaFXML, window);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// TANCA LA FINESTRA I TORNA A LA PANTALLA DE SELECCIO DE JOC
	public void enrere() {
		MenuController.midapixelartActivo=false;
		Stage stage = (Stage) botoEnrere.getScene().getWindow();
		stage.close();

	}

	// DESERIALITZA I CARREGA EL ULTIM PANELL
	public void desats() {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement psEliminar = null;
		ResultSet rs = null;

		try {

			conn = ConexionBBDD.conectar();

			// JO VULL TREURE EL ULTIM ELEMENT
			String sql = "SELECT dibuix FROM pixelArt ORDER BY id DESC LIMIT 1";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				byte[] dades = rs.getBytes("dibuix");
				if (dades == null) {
					System.out.println("No hi ha dades per a aquest id.");
					return;
				}

				// DESERIALITZAR
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dades));
				Object obj = ois.readObject();
				ois.close();

				// JO SERIALITZE UN OBJECTE TAULELL QUE DINS TE UNA MATRIU DE CASELLES LLAVORS
				// FAIG UN CASTING DE L'OBJECTE PER TINDRE UN TAULELL

				Taulell taulellDesat = (Taulell) obj;

				DadesPixelArt.getInstancia().setTaulell(taulellDesat);

				FXMLLoader loader = new FXMLLoader(getClass().getResource("PixelArtFXML.fxml"));
				String rutaFXML="PixelArtFXML.fxml";
				Parent root = loader.load();

				PixelArtController controlador = loader.getController();
				MenuController.controladorPixelArt = controlador;//lo necesita yordan para guardar!

				controlador.carregarTaulellUltimaSessio(taulellDesat);

				Scene escena = new Scene(root);
				Stage window = (Stage) botoDesats.getScene().getWindow();

				window.setScene(escena);
				window.setTitle("Pixel Art (Desat)");
				window.setMaximized(true);
				window.show();
				// añadir los juegos abiertos
				MenuController.juegosAbiertos.add(window);
				MenuController.juegosPorNombre.put(rutaFXML, window);
			} else {
				System.out.println("No s'ha trobat cap fila amb l'id indicat.");
			}

			String eliminar="DELETE FROM pixelart";
			psEliminar = conn.prepareStatement(eliminar);
			psEliminar.executeUpdate();
			
			conn.close();
			ps.close();
			rs.close();
			psEliminar.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// MIDA DE 16X16
	public void triarXicotet() {
		amplada.setText("16");
		amplada.setStyle("-fx-text-fill: white; -fx-background-color: #365057;");
		altura.setText("16");
		altura.setStyle("-fx-text-fill: white; -fx-background-color: #365057;");
		marcarLabel(xicotet);
	}

	public void canviXicotet() {
//		xicotet.setOnMouseEntered(e -> xicotet.setStyle("-fx-font-size: 20px;"));
//		xicotet.setOnMouseExited(e -> xicotet.setStyle(""));
		
	}

	// MIDA DE 32X32
	public void triarMitja() {
		amplada.setText("32");
		amplada.setStyle("-fx-text-fill: white; -fx-background-color: #365057;");
		altura.setText("32");
		altura.setStyle("-fx-text-fill: white; -fx-background-color: #365057;");
		marcarLabel(mitja);
	}

	public void canviMitja() {
//		mitja.setOnMouseEntered(e -> mitja.setStyle("-fx-font-size: 20px;"));
//		mitja.setOnMouseExited(e -> mitja.setStyle(""));
	}

	// MIDA DE 64X64
	public void triarGran() {
		amplada.setText("64");
		amplada.setStyle("-fx-text-fill: white; -fx-background-color: #365057;");
		altura.setText("64");
		altura.setStyle("-fx-text-fill: white; -fx-background-color: #365057;");
		marcarLabel(gran);
	}

	public void canviGran() {
//		gran.setOnMouseEntered(e -> gran.setStyle("-fx-font-size: 20px;"));
//		gran.setOnMouseExited(e -> gran.setStyle(""));

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//funcion que cambia el estado de los booleans para poder duplicados abiertos del mismo juego
		Platform.runLater(()->{
			Stage ventanaActual = (Stage) root.getScene().getWindow();
			if(ventanaActual.isShowing()) {
				MenuController.midapixelartActivo=true;
				System.out.println("La ventana mida-pixelart esta activa. Boolean: "+MenuController.midapixelartActivo);
				System.out.println("BooleaJoc: "+MenuController.pixelartActivo);
				System.out.println("BooleaDesats: "+MenuController.desatspixelartActivo);
				
			}
			ventanaActual.setOnHidden(evt ->{
				MenuController.midapixelartActivo=false;
				System.out.println("La ventana mida-pixelart se cerró. Boolean: "+MenuController.midapixelartActivo);
				System.out.println("BooleanJoc: "+MenuController.pixelartActivo);
				System.out.println("BooleaDesats: "+MenuController.desatspixelartActivo);
			});
		});
		
		root.prefWidthProperty().bind(root.widthProperty());
		root.prefHeightProperty().bind(root.heightProperty());
		canviGran();
		canviMitja();
		canviXicotet();

		// pose una mida per defecte
		triarMitja();

		int ample = Integer.parseInt(amplada.getText());
		int alt = Integer.parseInt(altura.getText());

		if (ample > 128)
			ample = 128;

		if (alt > 64)
			alt = 64;

		taulell = new Taulell(ample, alt);

		DadesPixelArt.getInstancia().setTaulell(taulell);

	}
}
