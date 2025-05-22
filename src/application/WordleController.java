package application;



import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import javafx.scene.control.TextFormatter;

import javafx.scene.layout.GridPane;

public class WordleController implements Initializable {


	@FXML private GridPane grid1; //tabla de paraules
	@FXML private GridPane grid2;//teclat
	
	
	private TextField[][] caselles = new TextField[6][5];
	private String paraula="ENVIO";


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//posar forma a la tabla de les paraules
				grid1.setAlignment(Pos.CENTER);
				grid1.setHgap(10);
				grid1.setVgap(10);
		for (int fila = 0; fila < 6; fila++) {
			for (int columna = 0; columna < 5; columna++) {
				TextField celda = new TextField();

				celda.setPrefWidth(65);//donar mida a la casella
				celda.setPrefHeight(65);
				celda.setStyle("-fx-background-color: white;"
						+"-fx-border-radius: 5;"
						+ "-fx-font-size: 20px;"
						+ "-fx-font-weight: bold;");
				celda.setAlignment(Pos.CENTER);
				
				//format per a que sols es puga anyadir una lletra en cada casella
				celda.setTextFormatter(new TextFormatter<>(c -> {
					String text = c.getControlNewText();
					if (text.length()>1 || !text.matches("[a-zA-Z]")) {//no permet mes d'un caracter i que siga una mayuscula
						return null;
					}
					c.setText(c.getText().toUpperCase());
					return c;
				}));
				
				
				if (columna==4) {
					final int finalFila = fila;
					celda.setOnAction(e-> comprovarFila(finalFila));//quan l'usuari esta en l'ultima casella i prem ENTER
				}
				
				caselles[fila][columna]=celda;//guardar en la matriu
				grid1.add(celda, columna, fila);
				
			final int c =columna; //guardem la columna en una variable final per a utilitzar-la en la lambda
			final int f =fila;//guardem la fila en una variable final per a utilitzar-la en la lambda
						
			celda.setOnKeyTyped(e-> {
							
					if (!celda.getText().isEmpty()) {//Si la celda no esta buida
	                    
	                    if (c < 4) {//si no es la ultima casella
	                        caselles[f][c+1].requestFocus();
	                    }else {
	                    	if (f<5) {//si es la ultima casella
	                    		
	                    		caselles[f+1][0].requestFocus();//mourer el foco a la primera casella de la següent fila
							} else {
		                    	celda.requestFocus();//si es la ultima casella i la ultima fila, deixem el foco ahi
							}
	                    }
				}});
			}	
		}
		
		
		
	}
	public void comprovarFila(int fila) {
		for (int columna = 0; columna < 5; columna++) {
			TextField celda = caselles[fila][columna];
			String text = celda.getText();//agafem la lletra del usuari
			char lletraUsuari = text.charAt(0);//la convertim a char
			char lletraCorrecta = paraula.charAt(columna);//lletra a comprovar
				
			if (lletraUsuari == lletraCorrecta) {
		            // Verd: lletra correcta i posició correcta
		            celda.setStyle("-fx-background-color: #43a047; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
		        } else if (paraula.contains(String.valueOf(lletraUsuari))) {
		            // Groc: lletra està en la paraula però en altra posició
		            celda.setStyle("-fx-background-color: #e4a81d; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
		        } else {
		            // Gris: lletra no està en la paraula
		            celda.setStyle("-fx-background-color: lightgrey; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
		        }
				
		

				
				
		}
			} 
	}
			
	
	
	

