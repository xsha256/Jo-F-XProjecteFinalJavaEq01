package application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PixelArtController implements Initializable {

	@FXML
	private Button pinzell;
	@FXML
	private Button borrador;
	@FXML
	private BorderPane root;
	@FXML
	private GridPane graella;
	@FXML
	private ColorPicker color;
	@FXML
	private Button pantallaInici;
	@FXML
	private Button guardarImatge;

	private Taulell taulell;

	private int files;
	private int columnes;
	private int grandariaCelda;
	private Mode mode = Mode.PINTAR;
	private Casella [][] taulellCaselles;

	//ESTA FUNCIO ES GUARDARBDD
	public void guardarPNG(ActionEvent e) {
		try {


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(taulellCaselles);
            oos.close();
            byte[] imatgeSerialitzada = baos.toByteArray();

            Date diaDeJoc = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String data = format.format(diaDeJoc);

           
            //ESTO PA CONNECTAR DESPRES JA TREBALLAR NORMAL 
            Connection c= ConexionBBDD.conectar();

            String sentencia = "INSERT INTO pixelArt (idUsuari, data, dibuix) VALUES (?, ?, ?)";
            PreparedStatement s = c.prepareStatement(sentencia);
            s.setInt(1, 1);
            s.setString(2, data);
            s.setBytes(3, imatgeSerialitzada);
            s.executeUpdate();

            s.close();
            c.close();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Avis");
            alerta.setHeaderText(null);
            alerta.setContentText("Dibuix guardat satisfactoriament");
            alerta.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}
	
	//TORNA A LA PANTALLA DE SELECCIÓ DE MIDA
	public void tornarInici(ActionEvent e) {
		// carreguem el fitxer fxml

		try {
			VBox root = (VBox) FXMLLoader.load(getClass().getResource("PixelArtIniciFXML.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();		
			window.setScene(scene);
			window.setTitle("Inici Pixel Art");
			window.setMaximized(true);
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//ACTIVA EL BOTÓ DE BORRAR
	public void borrar(ActionEvent e) {
		mode = Mode.BORRAR;
		borrador.setStyle("-fx-background-color: #e85a71;");
		pinzell.setStyle("-fx-background-color:  #2a7963;");
	}

	//ACTIVA EL BOTÓ DE PINTAR
	public void pintar(ActionEvent e) {
		mode = Mode.PINTAR;
		pinzell.setStyle("-fx-background-color: #e85a71;");
		borrador.setStyle("-fx-background-color:  #2a7963;");
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		DadesPixelArt dades = DadesPixelArt.getInstancia();
		taulell = dades.getTaulell();
		
		//REVISAR ESTO
		if (taulell.getAmple() <= 16 && taulell.getAltura() <= 16) {
			this.grandariaCelda = 20;
		} else if (taulell.getAmple() <= 32 && taulell.getAltura() <= 32) {
			this.grandariaCelda = 15;
		} else {
			this.grandariaCelda = 10;
		}
		
		this.files = taulell.getAltura();
		this.columnes = taulell.getAmple();
		
		taulellCaselles = new Casella [this.files][this.columnes];

		root.prefWidthProperty().bind(root.widthProperty());
		root.prefHeightProperty().bind(root.heightProperty());

		int contador = 0;

		for (int fila = 0; fila < files; fila++) {
			for (int col = 0; col < columnes; col++) {
				if (contador % 2 == 0) {
					String colorBase = "white";
					Label celda = crearPanell(colorBase, fila, col);
					graella.add(celda, col, fila);
					taulellCaselles[fila][col] = new Casella (colorBase, false, this.grandariaCelda);
				} else {
					String colorBase = "#cccccc";
					Label celda = crearPanell(colorBase, fila, col);
					graella.add(celda, col, fila);
					taulellCaselles[fila][col] = new Casella (colorBase, false, this.grandariaCelda);
				}
				contador++;
			}
			contador++;
		}
	}

	private Label crearPanell(String colorBase, int fila, int col) {
		Label casella = new Label();
		casella.setPrefSize(grandariaCelda, grandariaCelda);
		casella.setMinSize(grandariaCelda, grandariaCelda);
		casella.setMaxSize(grandariaCelda, grandariaCelda);
		casella.setStyle("-fx-background-color:" + colorBase + "; -fx-alignment: center;");

		// drag enter pane
		casella.setOnMouseClicked(e -> {
			
			if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
				// agafe el valor que li he passat en el color picker
				casella.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");
				taulellCaselles[fila][col].setColor(colorString(color.getValue()));
				taulellCaselles[fila][col].setOcupat(true);
			} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {
				casella.setStyle("-fx-background-color:" + colorBase + ";");
				taulellCaselles[fila][col].setColor(colorBase);
				taulellCaselles[fila][col].setOcupat(false);
			} 

		});

		// Habilitar el drag en totes les cel·les
		casella.setOnDragDetected(e -> {
			casella.startFullDrag();
			if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
				casella.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");
				taulellCaselles[fila][col].setColor(colorString(color.getValue()));
				taulellCaselles[fila][col].setOcupat(true);
			} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {
				casella.setStyle("-fx-background-color:" + colorBase + ";");
				taulellCaselles[fila][col].setColor(colorBase);
				taulellCaselles[fila][col].setOcupat(false);
			} 
		});

		// mentre arrastre sobre altres cel.les
		casella.setOnMouseDragEntered(e -> {
			if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
				casella.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");
				taulellCaselles[fila][col].setColor(colorString(color.getValue()));
				taulellCaselles[fila][col].setOcupat(true);
			} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {
				casella.setStyle("-fx-background-color:" + colorBase + ";");
				taulellCaselles[fila][col].setColor(colorBase);
				taulellCaselles[fila][col].setOcupat(false);
			} 
		});

		return casella;
	}

	//ESTO NO ME FA FALTA
	private String colorString(Color c) {// converteix el color en una string per poder
															// interpretar-lo
		return "rgb(" + (int) (c.getRed() * 255) + "," + (int) (c.getGreen() * 255) + "," + (int) (c.getBlue() * 255)
				+ ")";
	}

}
