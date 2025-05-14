package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class JocVidaController implements Initializable {

	@FXML
	private GridPane gridPane;

	private final int FILAS = 5;
	private final int COLUMNAS = 5;
	private char[][] matriz = new char[FILAS][COLUMNAS];
	private Label[][] etiquetas = new Label[FILAS][COLUMNAS];

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (int i = 0; i < FILAS; i++) {
			for (int j = 0; j < COLUMNAS; j++) {
				Label label = new Label(" ");
				label.setPrefSize(60, 60);
				label.setStyle("-fx-border-color: black; -fx-alignment: center;");
				etiquetas[i][j] = label;
				gridPane.add(label, j, i);
			}
		}


		llenar(8);
		actualizarGridPane();
	}

	private void llenar(int n) {
		matriz[0][0] = '*';
	}

	private void actualizarGridPane() {
	    for (int i = 0; i < FILAS; i++) {
	        for (int j = 0; j < COLUMNAS; j++) {
	            if (etiquetas[i][j] != null) {
	                if (matriz[i][j] == '*') {
	                    etiquetas[i][j].setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-alignment: center;");
	                } else {
	                    etiquetas[i][j].setStyle("-fx-background-color: white; -fx-border-color: black; -fx-alignment: center;");
	                }
	            }
	        }
	    }
	}


}
