package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

	private Taulell taulell;

	private int files;
	private int columnes;
	private int grandariaCelda = 15;

	@Override

	public void initialize(URL arg0, ResourceBundle arg1) {

		DadesPixelArt dades = DadesPixelArt.getInstancia();
		taulell = dades.getTaulell();
		this.files = taulell.getAltura();
		this.columnes = taulell.getAmple();

		root.prefWidthProperty().bind(root.widthProperty());
		root.prefHeightProperty().bind(root.heightProperty());
		
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

		
		celda.setOnDragDetected(e -> {
			celda.startFullDrag(); //Habilitar el drag en totes les cel·les
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
