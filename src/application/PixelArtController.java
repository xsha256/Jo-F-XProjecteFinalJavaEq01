package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class PixelArtController implements Initializable {

	@FXML
	private Button pinzell;
	@FXML
	private Button borrador;
	@FXML
	private Pane root;
	@FXML
	private GridPane graella;
	@FXML
	private ColorPicker color;
	
	int files = 50;
	int columnes = 50;
	int grandariaCelda = 15;

	@Override

	public void initialize(URL arg0, ResourceBundle arg1) {

		// ACÍ HA DE TRIAR EL USUARI LA MIDA

		int contador = 0;

		for (int fila = 0; fila < files; fila++) {
			for (int col = 0; col < columnes; col++) {
				if (contador % 2 == 0) {
					String colorBase = "white";
					Pane celda = crearPanell(colorBase);
					graella.add(celda, col, fila);
				} else {
					String colorBase = "#cccccc";
					Pane celda = crearPanell(colorBase);
					graella.add(celda, col, fila);
				}
				contador++;
			}
			contador++;
		}
	}

	private Pane crearPanell(String colorBase) {
		Pane celda = new Pane();
		celda.setPrefSize(grandariaCelda, grandariaCelda);
		celda.setStyle("-fx-background-color:" + colorBase + ";");

		celda.setOnMouseClicked(e -> {// drag enter pane
			celda.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");// agafe el valor que li he
																							// passat en el color picker
		});

		// Cuando empieza el drag
		celda.setOnDragDetected(e -> {
			celda.startFullDrag(); // Muy importante para habilitar drag en celdas vecinas
			celda.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");
		});

		// Cuando se pasa por encima de otra celda mientras se arrastra
		celda.setOnMouseDragEntered(e -> {
			celda.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");
		});
		return celda;
	}

	private String colorString(javafx.scene.paint.Color c) {// converteix el color en una string per poder
															// interpretar-lo
		return "rgb(" + (int) (c.getRed() * 255) + "," + (int) (c.getGreen() * 255) + "," + (int) (c.getBlue() * 255)
				+ ")";
	}

}
