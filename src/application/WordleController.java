package application;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class WordleController implements Initializable {


	@FXML private GridPane grid1; //tabla de paraules
	@FXML private GridPane grid2;//teclat
	
	@FXML
	private HBox escena;
	
	private TextField[][] caselles = new TextField[6][5];
	
	private String paraula=paraulaAleatoria();
	
	public void enrereInici(ActionEvent e) {
		try {
			Parent escena = FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));
			Scene escena2 = new Scene(escena);
			//obtenim la finestra de l'aplicació actual
			 Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();			
			 window.setScene(escena2);
			 window.setTitle("Wordle");
			 window.show();		
		} catch (IOException error) {
			error.printStackTrace();
		}
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
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
		
		
		//posar forma a la tabla de les paraules
		grid1.setAlignment(Pos.CENTER);
		grid1.setHgap(10);
		grid1.setVgap(10);
		
		
	}
	public void comprovarFila(int fila) {
		boolean encertada =true;
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
		            encertada =false;
		        } else {
		            // Gris: lletra no està en la paraula
		            celda.setStyle("-fx-background-color: lightgrey; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
		            encertada =false;
		        }
				if ((fila==5 &&columna==4)&&!encertada) {//si estem en la fila 5 i no la ha acertat
					
					try {
						
						File fitxer=new File("icona.png");
						Image iconaAlert;
						iconaAlert = new Image(new FileInputStream(fitxer));
						 ImageView alertView = new ImageView(iconaAlert);
						 alertView.setFitWidth(100);
						 alertView.setPreserveRatio(true);
						Alert alert = new Alert(AlertType.NONE);
						alert.setTitle("Resultat"); // text en la bara de la finestra
						alert.setHeaderText("INCORRECTE"); // text al costat de la icona
						alert.setContentText("No has adivinant la paraula.\n La paraula correcta es: "+paraula.toUpperCase()); // missatge
						alert.setResizable(true); // es pot canviar la mida o no?
						alert.getDialogPane().setPrefSize(300, 300); // mida del diàleg
						alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
						 alert.setGraphic(alertView);

						alert.showAndWait(); 
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
			
			
				
	}
	
	
	public String paraulaAleatoria() {
		Random aleatori = new Random();
		String paraulaAleatoria="";
		ArrayList<String> paraules=new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("paraules.txt"));
			String linea ="";
			
			while ((linea=reader.readLine())!=null) {
				paraules.add(linea);
			}
			reader.close();
			int index=aleatori.nextInt(0,200);
			paraulaAleatoria=paraules.get(index);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return paraulaAleatoria;


		
	}
			
	
	
	
}
