package application;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class WordleController implements Initializable {

	@FXML private GridPane grid1; //tabla de palabras
	@FXML private GridPane grid2;//teclado
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (int fila = 0; fila < 6; fila++) {
			for (int columna = 0; columna < 5; columna++) {
				TextField celda = new TextField();
				celda.setPrefWidth(80);//donar mida a la casella
				celda.setPrefHeight(80);
				celda.setStyle("-fx-background-color: white;"
						+ "-fx-border-color: grey;"
						+"-fx-border-radius: 5;");
				celda.setAlignment(Pos.CENTER);
				
				grid1.add(celda, columna, fila);
				
			}
			
		}
		grid1.setAlignment(Pos.CENTER);
		grid1.setHgap(10);
		grid1.setVgap(10);
	}
	
	
	
}
