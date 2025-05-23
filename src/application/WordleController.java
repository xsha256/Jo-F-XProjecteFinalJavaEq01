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
import javafx.event.EventHandler;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class WordleController implements Initializable {

	@FXML
	private GridPane grid1; // tabla de paraules
	@FXML
	private GridPane grid2;//teclat /*cambiar el nom de id del teclat*/

	@FXML
	private HBox escena;

	private TextField[][] caselles = new TextField[6][5];
	private String paraula = paraulaAleatoria();
	private int columnaActual=0;
	private int filaActual=0;
	
	
	
	
	
	public void enrereInici(ActionEvent e) {
		try {
			Parent escena = FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));
			Scene escena2 = new Scene(escena);
			// obtenim la finestra de l'aplicació actual
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Wordle");
			window.show();
			window.setMaximized(true);

		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	public void initialize(URL arg0, ResourceBundle arg1) {//creacio de caselles
		
		for (int fila = 0; fila < 6; fila++) {
			for (int columna = 0; columna < 5; columna++) {
				TextField celda = new TextField();
				//donar mida i forma a les caselles
				celda.setPrefWidth(65);
				celda.setPrefHeight(65);
				celda.setStyle("-fx-background-color: white;" + "-fx-border-radius: 5;" + "-fx-font-size: 20px;"
						+ "-fx-font-weight: bold;");
				celda.setAlignment(Pos.CENTER);
				
				
				final int columnaActual = columna; // guardem la columna en una variable final per a utilitzar-la en la lambda
				final int filaActual = fila;// guardem la fila en una variable final per a utilitzar-la en la lambda

				
				/*LIMITEM A UNA SOLA LLETRA I QUE ESTIGA EN MAYUSCULES*/
				celda.setTextFormatter(new TextFormatter<>(e -> {
					String text = e.getControlNewText();
					if (text.length() > 1 || !text.matches("[a-zA-Z]")) {
																			
						return null;
					}
					e.setText(e.getText().toUpperCase());
					return e;
				}));
				
				
				/*AVANÇAR AUTOMATICAMENT*/
				if (columna == 4) {//si estem en la ultima casella de la fila
					final int finalFila = fila;
					celda.setOnAction(e -> {

						boolean filaCompleta =true;
						for (int i = 0; i < 5; i++) {
							if (caselles[finalFila][i].getText().isEmpty()) {
								filaCompleta=false;
								break;
							}
							
						}
						
						if (filaCompleta) {// si les caselles estan totes plenes
							comprovarFila(finalFila); // Es crida al pulsar ENTER ENTER en la última casella
						} else {// si la ultima casilla no esta llena
							celda.requestFocus();
						}
					});
				}
					caselles[fila][columna] = celda;// guardar en la matriu
					grid1.add(celda, columna, fila);

					
					
					
				/*ESBORRAR AMB BACKSPACE*/
					celda.setOnKeyPressed(event -> {
					    if (event.getCode() == KeyCode.BACK_SPACE) {
					        if (!celda.getText().isEmpty()) {
					            celda.clear();
					        } else if (columnaActual > 0) {
					            TextField anterior = caselles[filaActual][columnaActual - 1];
					            anterior.requestFocus();
					            anterior.clear();
					        }
					        event.consume();
					    }
					});
					

					/*ENTER NOMES A LA ULTIMA CASELLA*/
					celda.setOnKeyTyped(e -> {
						
						if (!celda.getText().isEmpty()) {
							if (columnaActual < 4) {
								caselles[filaActual][columnaActual + 1].requestFocus(); // Avanza a siguiente casilla en la fila
							}
							// En la última casilla no hacemos nada (el foco se queda ahí)
						}
					});

				

			}
		}
		// posar forma a la tabla de les paraules
		grid1.setAlignment(Pos.CENTER);
		grid1.setHgap(10);
		grid1.setVgap(10);
		
		/*FER TECLAT*/
		
		EventHandler<MouseEvent> escriuTeclat=new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Node node =(Node) arg0.getTarget();
				while (node != null && !(node instanceof Button)) {
					node = node.getParent();
				}
				if (node instanceof Button) {
					
					String letra =((Button) node).getText();
					escriureLletra(letra);
				
					if (node.getId()=="delete") {
						
					}
					
					if (node.getId()=="check") {
						String enter =((Button) node).getText();
						
						EventHandler<MouseEvent> ferEnter = new EventHandler<MouseEvent>() {
							
							@Override
							public void handle(MouseEvent arg0) {
								if (!enter.isEmpty()) {
									if (columnaActual < 4) {
										caselles[filaActual][columnaActual + 1].requestFocus(); // Avanza a siguiente casilla en la fila
									}
									// En la última casilla no hacemos nada (el foco se queda ahí)
								}								
							}
						};
						
						node.addEventHandler(MouseEvent.MOUSE_CLICKED, ferEnter);
							
						
					}
				
				}
			
				
			}
			
			
			
		};

		grid2.addEventFilter(MouseEvent.MOUSE_CLICKED, escriuTeclat);
		
		
		
	}
	

	

	public void comprovarFila(int fila) {
		boolean encertada = true;
		for (int columna = 0; columna < 5; columna++) {
			TextField celda = caselles[fila][columna];
			String text = celda.getText();// agafem la lletra del usuari
			char lletraUsuari = text.charAt(0);// la convertim a char
			char lletraCorrecta = paraula.charAt(columna);// lletra a comprovar

			if (lletraUsuari == lletraCorrecta) {
				// Verd: lletra correcta i posició correcta
				celda.setStyle(
						"-fx-background-color: #43a047; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
			} else if (paraula.contains(String.valueOf(lletraUsuari))) {
				// Groc: lletra està en la paraula però en altra posició
				celda.setStyle(
						"-fx-background-color: #e4a81d; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
				encertada = false;
			} else {
				// Gris: lletra no està en la paraula
				celda.setStyle(
						"-fx-background-color: lightgrey; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
				encertada = false;
			}
			if (fila < 5) {
				caselles[fila + 1][0].requestFocus();
			} else {
				// Última fila: el foco se queda en la última casilla o donde quieras
				caselles[fila][4].requestFocus();
			}
			if ((fila == 5 && columna == 4) && !encertada) {// si estem en la fila 5 i no la ha acertat

				try {

					File fitxer = new File("icona.png");
					Image iconaAlert;
					iconaAlert = new Image(new FileInputStream(fitxer));
					ImageView alertView = new ImageView(iconaAlert);
					alertView.setFitWidth(100);
					alertView.setPreserveRatio(true);
					Alert alert = new Alert(AlertType.NONE);
					alert.setTitle("Resultat"); // text en la bara de la finestra
					alert.setHeaderText("INCORRECTE"); // text al costat de la icona
					alert.setContentText("No has adivinant la paraula.\n La paraula correcta es: " + paraula.toUpperCase()); // missatge
					alert.setResizable(true); // es pot canviar la mida o no?
					alert.getDialogPane().setPrefSize(300, 300); // mida del diàleg
					alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
					alert.setGraphic(alertView);

					alert.showAndWait();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}

		}

	}

	public String paraulaAleatoria() {
		Random aleatori = new Random();
		String paraulaAleatoria = "";
		ArrayList<String> paraules = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("paraules.txt"));
			String linea = "";

			while ((linea = reader.readLine()) != null) {
				paraules.add(linea);
			}
			reader.close();
			int index = aleatori.nextInt(0, 200);
			paraulaAleatoria = paraules.get(index);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return paraulaAleatoria;

	}
	
	public void escriureLletra(String lletra) {
		if (filaActual<6 && columnaActual<5) {
			TextField celda = caselles[filaActual][columnaActual];
			if (celda.getText().isEmpty()) {
				celda.setText(lletra);
				if (columnaActual <4) {
					columnaActual++;
				}
				//posar focus a la següent casella
				if (columnaActual<5) {
					caselles[filaActual][columnaActual].requestFocus();
				}
			}
		}
	}
	
	
	
	

}