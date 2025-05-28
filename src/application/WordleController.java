package application;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javax.security.auth.login.LoginContext;

import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WordleController implements Initializable {

	@FXML
	private GridPane grid1; // tabla de paraules
	@FXML
	private GridPane grid2;// teclat
	@FXML
	private VBox escena;// log
	@FXML
	private HBox root;
	@FXML
	private Button q;
		
	
	
	private Label[][] caselles = new Label[6][5];
	private String paraula = paraulaAleatoria();
	private int columnaActual = 0;
	private int filaActual = 0;
	private Stage finestra;

	private int intents=0;
	private int fallades=0;
	private int encertatLletra=0;
	private int encertatParaula=0;
	//private String emailUsauri=LoginController.EMAIL;
	
	private EventHandler<KeyEvent> tecla = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent e) {
			String lletra = e.getText().toUpperCase();
			KeyCode tecla = e.getCode();

			//[comprovació extra] per assegurar que no s'accedeiz a una fila o columna inexistent
			if (filaActual >= caselles.length || columnaActual >= caselles[0].length) {
			    return;
			}

			
			
			if(lletra.matches("[A-Z]") && columnaActual <5) {// Per a que sols es puga escriurer lletres en majuscula
				caselles[filaActual][columnaActual].setText(lletra);
				if (columnaActual<4) {
					columnaActual++; 
				}

			}else if (tecla == KeyCode.BACK_SPACE && columnaActual >=0) {//ESBORRAR
				borrarLletres();
				
			}else if (tecla == KeyCode.ENTER && columnaActual==4) {//ENTER
				boolean filaCompleta = true;
				for (int i = 0; i < 5; i++) {
					String casellesText = caselles[filaActual][i].getText();// agafa el text de la casella
					if (casellesText == null || lletra.isEmpty()) {// si esta buit es false i no podra pasar de
																	// linea
						filaCompleta = false;
						break;
					}
				}

				if (filaCompleta) {
					comprovarFila(filaActual);// enter
					filaActual++;// pasem a la fila de baix
					columnaActual = 0;
				}
				if (filaActual==4 && columnaActual==4) {
					filaActual=0;
					columnaActual=0;
				}
				return;

			}

			
		}
	};

	public void initialize(URL arg0, ResourceBundle arg1) {// creacio de caselles
		Platform.runLater(() -> {
			finestra = (Stage) root.getScene().getWindow();
			finestra.addEventFilter(KeyEvent.KEY_PRESSED, tecla);
		});

		for (int fila = 0; fila < 6; fila++) {
			for (int columna = 0; columna < 5; columna++) {
				Label celda = new Label();
				// donar mida i forma a les caselles
				celda.setPrefWidth(65);
				celda.setPrefHeight(65);
				celda.setStyle("-fx-background-color: white;" + "-fx-border-radius: 5;" + "-fx-font-size: 20;"
						+ "-fx-font-weight: bold;");
				celda.setAlignment(Pos.CENTER);
				celda.setId("escriu");

				
				caselles[fila][columna] = celda;// guardar en la matriu
				grid1.add(celda, columna, fila);

				/* ENTER NOMES A LA ULTIMA CASELLA */
				celda.setOnKeyTyped(e -> {

					if (!celda.getText().isEmpty()) {
						if (columnaActual < 4) {
							caselles[filaActual][columnaActual + 1].requestFocus(); // Avanza a siguiente casilla en la
																					// fila
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

		/* FER TECLAT */

		EventHandler<MouseEvent> escriuTeclatPantalla = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Node node = (Node) arg0.getTarget();
				while (node != null && !(node instanceof Button)) {
					node = node.getParent();
				}
				if (node instanceof Button) {

					String text = ((Button) node).getText();
					String id = node.getId();

					if ("delete".equals(id)) {
						borrarLletres();
						return;
					}

					if ("check".equals(id)) {
						boolean filaCompleta = true;
						for (int i = 0; i < 5; i++) {
							String casellesText = caselles[filaActual][i].getText();// agafa el text de la casella
							if (casellesText == null || text.isEmpty()) {// si esta buit es false i no podra pasar de
																			// linea
								filaCompleta = false;
								break;
							}
						}

						if (filaCompleta) {
							comprovarFila(filaActual);// enter
							filaActual++;// pasem a la fila de baix
							columnaActual = 0;
						}
						
						if (filaActual==4 && columnaActual==4) {
							filaActual=0;
							columnaActual=0;
						}
						return;

					}
					escriureLletra(text);
					return;

				}

			}

		};

		grid2.addEventFilter(MouseEvent.MOUSE_CLICKED, escriuTeclatPantalla);

	}

	//// *MÈTODES*/////

	public void comprovarFila(int fila) {
		boolean encertada = true;
		for (int columna = 0; columna < 5; columna++) {
			Label celda = caselles[fila][columna];
			String text = celda.getText();// agafem la lletra del usuari
			char lletraUsuari = text.charAt(0);// la convertim a char
			char lletraCorrecta = paraula.charAt(columna);// lletra a comprovar

			if (lletraUsuari == lletraCorrecta) {
				// Verd: lletra correcta i posició correcta
				celda.setStyle("-fx-background-color: #43a047; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
				q.setStyle("-fx-background-color: #43a047; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
			} else if (lletraUsuari != lletraCorrecta && paraula.contains(String.valueOf(lletraUsuari))) {
				int totalLletraParaula = comptarLletra(paraula, lletraUsuari);
				int totalLletraUsuari = comptarLletraUsuari(fila, lletraUsuari, columna);

				if (totalLletraUsuari >= totalLletraParaula) {
					// Ja has posat aquesta lletra més vegades de les que hi ha a la paraula
					celda.setStyle("-fx-background-color: lightgrey; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight:bold;");
					q.setStyle("-fx-background-color: lightgrey; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight:bold;");
				} else {
					// Groc: lletra està a la paraula però no en aquesta posició i encara no s'ha
					// exhaurit el seu compte
					celda.setStyle("-fx-background-color: #e4a81d; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");
					q.setStyle("-fx-background-color: #e4a81d; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");

				}
				encertada = false;
			} else {
				// Gris: lletra no està en la paraula
				celda.setStyle("-fx-background-color: lightgrey; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight:bold;");
				encertada = false;
			}

			if (fila < 5) {
				caselles[fila + 1][0].requestFocus();
			} else {
				// Última fila: el foco se queda en la última casilla
				caselles[fila][4].requestFocus();
			}

			columnaActual = columna;
		}

		/* ALERT INCORRECTE */
		if ((fila == 5 && columnaActual == 4) && !encertada) {// si estem en la fila 5 i no la ha acertat

			try {

				Alert alert = new Alert(AlertType.NONE);
				alert.setTitle("Incorrecte");
				alert.setHeaderText("INCORRECTE"); // text al costat de la icona
				alert.getDialogPane().setPrefSize(250, 500);
				// Image iconAlert = new
				// Image(getClass().getResourceAsStream("/Jo-F-XProjecteFinalJavaEq01/src/images/icona.png"));
				// ImageView alertView = new ImageView(iconAlert);
				// alertView.setFitWidth(200);
				// alertView.setPreserveRatio(true);

				Label msg = new Label(
						"No has adivinant la paraula. \n La paraula correcta es: " + paraula.toUpperCase());
				msg.setMaxWidth(500);
				msg.setWrapText(true);
				msg.getStyleClass().add("msgAlertError");

				VBox content = new VBox(15, msg);
				content.setAlignment(Pos.CENTER);
				content.setPadding(new Insets(20));
				content.setPrefWidth(500);

				alert.getDialogPane().setContent(content);
				// alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
				Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
				okButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
				okButton.getStyleClass().add("boton-hover");
				alert.getDialogPane().getStyleClass().add("alertError");
				// tornar a la pantalla de inici
				Optional<ButtonType> result = alert.showAndWait();

				if (result.isPresent() && result.get() == ButtonType.OK) {
					try {
						VBox root = FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));

						// En comptes de usar 'escena', que és null, agafem l'stage des d'un node
						// existent
						Stage stage = (Stage) grid1.getScene().getWindow();

						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.setMaximized(true);
						stage.show();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				

			} catch (Exception er) {
				System.out.println("Error: " + er);
				System.out.println(er.getLocalizedMessage());
			}

		}

		/* ALERT CORRECTE */
		if (encertada) {
			try {
				
				encertatParaula++;

				Alert alert = new Alert(AlertType.NONE);
				alert.setTitle("Correcte");
				alert.setHeaderText("Correcte"); // text al costat de la icona
				alert.getDialogPane().setPrefSize(250, 500);
				// Image iconAlert = new
				// Image(getClass().getResourceAsStream("/Jo-F-XProjecteFinalJavaEq01/src/images/icona.png"));
				// ImageView alertView = new ImageView(iconAlert);
				// alertView.setFitWidth(200);
				// alertView.setPreserveRatio(true);

				Label msg = new Label("ENHORABONA HAS ADIVINAT LA PARAULA !!!!!!");
				msg.setMaxWidth(500);
				msg.setWrapText(true);
				msg.getStyleClass().add("msgAlertError");

				VBox content = new VBox(15, msg);
				content.setAlignment(Pos.CENTER);
				content.setPadding(new Insets(20));
				content.setPrefWidth(500);

				alert.getDialogPane().setContent(content);
				// alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
				Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
				okButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
				okButton.getStyleClass().add("boton-hover");
				alert.getDialogPane().getStyleClass().add("alertError");

				// tornar a la pantalla de inici
				Optional<ButtonType> result = alert.showAndWait();

				if (result.isPresent() && result.get() == ButtonType.OK) {
					try {
						VBox root = FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));

						// En comptes de usar 'escena', que és null, agafem l'stage des d'un node
						// existent
						Stage stage = (Stage) grid1.getScene().getWindow();

						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.setMaximized(true);
						stage.show();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception er) {
				System.out.println("Error: " + er);
				System.out.println(er.getLocalizedMessage());
			}
		}

		// resetejar a 0 si la linea es completa i es pasa a la següent
		columnaActual = 0;

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
		if (filaActual < 6 && columnaActual < 5) {
			Label celda = caselles[filaActual][columnaActual];
			if (celda.getText().isEmpty()) {
				celda.setText(lletra);
				if (columnaActual < 4) {
					columnaActual++;
				}
				if (columnaActual < 5) {
					caselles[filaActual][columnaActual].requestFocus();
				}
			}
		}
	}

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

	public int comptarLletra(String paraula, char lletra) {
		int comptador = 0;
		for (int i = 0; i < paraula.length(); i++) {
			if (paraula.charAt(i) == lletra) {
				comptador++;
			}
		}
		return comptador;
	}

	public int comptarLletraUsuari(int fila, char lletra, int columnaActual) {
		int comptador = 0;
		for (int col = 0; col < columnaActual; col++) {
			String text = caselles[fila][col].getText();
			if (text.length() > 0 && text.charAt(0) == lletra) {
				comptador++;
			}
		}
		return comptador;
	}

	public void borrarLletres() {
		if (filaActual < 6) {
			if (columnaActual == 0) {
				// Estem a la primera columna de la fila
				Label celda = caselles[filaActual][columnaActual];
				if (!celda.getText().isEmpty()) {
					// Si la casella té text, l'esborrem
					celda.setText("");
					celda.requestFocus();
				}
				// Si està buida i som a la primera columna, no fem res més
			} else {
				// Estem més enllà de la primera columna (columnaActual > 0)
				// Retrocedim la columna, esborrem la casella anterior i posem focus allà
				if (columnaActual == 4) {
					if (caselles[filaActual][columnaActual].getText() == "") {
						columnaActual--;
						Label celda = caselles[filaActual][columnaActual];
						celda.setText("");
						celda.requestFocus();
					} else {
						Label celda = caselles[filaActual][columnaActual];
						celda.setText("");
						celda.requestFocus();
					}

				} else {
					columnaActual--;
					Label celda = caselles[filaActual][columnaActual];
					celda.setText("");
					celda.requestFocus();
				}

			}
		}
	}
	
	
	/*public void consultarId() {
		try {
			int idUsuari=0;
			 Connection c= ConexionBBDD.conectar();
			 String consultaInsert="SELECT id FROM usuari WHERE email = "+emailUsuario;		 
			 PreparedStatement psInsert = c.prepareStatement(consultaInsert);			 
			 ResultSet rs = psInsert.executeQuery(consultaInsert);
			 while (rs.next()) {
				idUsuari=rs.getInt("id");
			}		 
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/	
	
	
	public void InsertarBBDD() {

		 try {
			 Connection c= ConexionBBDD.conectar();
			 String consultaInsert="INSERT INTO wordle (idUsuari,intents,fallades,encertats) VALUES (?,?,?,?)";		 
			 PreparedStatement psInsert = c.prepareStatement(consultaInsert);
					 
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
	}

}
