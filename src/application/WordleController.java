package application;

import java.io.BufferedReader;
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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import javafx.scene.control.TextArea;

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

	private Label[][] caselles = new Label[6][5];
	private String paraula = paraulaAleatoria();
	private int columnaActual = 0;
	private int filaActual = 0;
	private Stage finestra;

	private int[] comptadorIntents = new int[7];// del 1 al 6
	private int intents = 1;
	private int encertats = 0;
	private int vegades = 0;
	// private String emailUsauri=LoginController.EMAIL;

	private EventHandler<KeyEvent> tecla = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent e) {
			String lletra = e.getText().toUpperCase();
			KeyCode tecla = e.getCode();

			// [comprovació extra] per assegurar que no s'accedeiz a una fila o columna
			// inexistent
			if (filaActual >= caselles.length || columnaActual >= caselles[0].length) {
				return;
			}

			if (lletra.matches("[A-Z]") && columnaActual < 5) {// Per a que sols es puga escriurer lletres en majuscula
				caselles[filaActual][columnaActual].setText(lletra);
				if (columnaActual < 4) {
					columnaActual++;
				}

			} else if (tecla == KeyCode.BACK_SPACE && columnaActual >= 0) {// ESBORRAR
				borrarLletres();

			} else if (tecla == KeyCode.ENTER && columnaActual == 4) {// ENTER
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
					intents++;// aumentem els intents
					filaActual++;// pasem a la fila de baix
					columnaActual = 0;

				}
				if (filaActual == 4 && columnaActual == 4) {
					filaActual = 0;
					columnaActual = 0;
				}
				return;

			}

		}
	};

	public void initialize(URL arg0, ResourceBundle arg1) {// creacio de caselles
		// funcion que cambia el estado de los booleans para poder duplicados abiertos
		// del mismo juego
		Platform.runLater(() -> {
			Stage ventanaActual = (Stage) root.getScene().getWindow();
			if (ventanaActual.isShowing()) {
				MenuController.wordleActivo = true;
				System.out.println("La ventana joc-wordle esta activa. Boolean: " + MenuController.wordleActivo);
				System.out.println("BooleanInfoWordle: " + MenuController.infowordleActivo);
			}
			ventanaActual.setOnHidden(evt -> {
				MenuController.wordleActivo = false;
				System.out.println("La ventana joc-wordle se cerró. Boolean: " + MenuController.wordleActivo);
				System.out.println("BooleanInfoWordle: " + MenuController.infowordleActivo);
			});
		});

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
				celda.setStyle("-fx-background-color: #F0F0F00D;" + "-fx-background-radius: 10;" + "-fx-font-size: 20;"
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
							intents++;// aumentem els intents
							filaActual++;// pasem a la fila de baix
							columnaActual = 0;

						}

						if (filaActual == 4 && columnaActual == 4) {
							filaActual = 0;
							columnaActual = 0;
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
				celda.setStyle(
						"-fx-background-color: #43a047; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 10;");
				actualitzarColorTeclat(lletraUsuari, "#43a047");// metodo per pintar el teclat
				comptadorIntents[intents]++;
			} else if (lletraUsuari != lletraCorrecta && paraula.contains(String.valueOf(lletraUsuari))) {
				int totalLletraParaula = comptarLletra(paraula, lletraUsuari);
				int totalLletraUsuari = comptarLletraUsuari(fila, lletraUsuari, columna);

				if (totalLletraUsuari >= totalLletraParaula) {
					// Ja has posat aquesta lletra més vegades de les que hi ha a la paraula
					celda.setStyle(
							"-fx-background-color: lightgrey; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight:bold; -fx-background-radius: 10;");
					actualitzarColorTeclat(lletraUsuari, "lightgrey");

				} else {
					// Groc: lletra està a la paraula però no en aquesta posició i encara no s'ha
					// exhaurit el seu compte
					celda.setStyle(
							"-fx-background-color: #e4a81d; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 10;");
					actualitzarColorTeclat(lletraUsuari, "#e4a81d");
				}
				encertada = false;
			} else {
				// Gris: lletra no està en la paraula
				celda.setStyle(
						"-fx-background-color: lightgrey; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight:bold; -fx-background-radius: 10;");
				actualitzarColorTeclat(lletraUsuari, "lightgrey");
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
			InsertarBBDD();
			try {
				
				Alert alert = new Alert(AlertType.NONE);
				
				alert.setTitle("Incorrecte");
				// alert.setHeaderText("INCORRECTE"); // text al costat de la icona
				alert.getDialogPane().setPrefSize(650, 600);
				 Image iconAlert = new
				 Image("file:imagenes/equis.png");
				 ImageView alertView = new ImageView(iconAlert);
				 alertView.setFitWidth(150);
				 alertView.setPreserveRatio(true);
				Label msg = new Label(
						"INCORRECTE:\n\nNo has adivinant la paraula. \nLa paraula correcta es: \n" + paraula.toUpperCase());
				// msg.setMaxWidth(300);
				msg.setWrapText(true);
				msg.getStyleClass().add("msgAlertError");

				Label est = new Label();
				est.setText(estadistica());
				est.setWrapText(true);
				// est.setMaxWidth(300);
				// est.setMaxHeight(300);
				est.getStyleClass().add("msgAlertError");

				VBox content = new VBox(15, alertView, msg, est);
				content.setAlignment(Pos.CENTER);
				content.setPadding(new Insets(20));
				content.setPrefWidth(500);
				alert.getDialogPane().setContent(content);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
				Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
				okButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
				okButton.getStyleClass().add("boton-hover");
				
				
				alert.getDialogPane().getStyleClass().add("alertError");
				
				// tornar a la pantalla de inici
				Optional<ButtonType> result = alert.showAndWait();

				if (result.isPresent() && result.get() == ButtonType.OK) {
					MenuController.wordleActivo = false;
					MenuController.infowordleActivo = true;
					try {
						Stage stage = (Stage) root.getScene().getWindow();
						stage.close();

						Parent escena = FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));
						String rutaFXML="wordleLogin.fxml";
						Scene escena2 = new Scene(escena, 600, 400);
						Stage window = new Stage();// (Stage) ((Node) e.getSource()).getScene().getWindow();
						window.setScene(escena2);
						window.setTitle("Wordle");

						window.show();
						// añadir los juegos abiertos
						MenuController.juegosAbiertos.add(window);
						MenuController.juegosPorNombre.put(rutaFXML, window);
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
		else if (encertada) {
			estadistica();
			InsertarBBDD();
			try {

				encertats++;

				Alert alert = new Alert(AlertType.NONE);
				alert.setTitle("Correcte");
				alert.getDialogPane().setPrefSize(550, 500);
				 Image iconAlert = new
				 Image("file:imagenes/win.png");
				 ImageView alertView = new ImageView(iconAlert);
				 alertView.setFitWidth(200);
				 alertView.setPreserveRatio(true);

				Label msg = new Label("Correcte:\n\nENHORABONA HAS ENDEVINAT LA PARAULA !!!!!!");
				msg.setMaxWidth(500);
				msg.setWrapText(true);
				msg.getStyleClass().add("msgAlertError");
				System.out.println();
				System.out.println();
				System.out.println("-------------------");
				System.out.println(estadistica());
				Label est = new Label();
				est.setText(estadistica());
				est.setWrapText(true);
				est.setMaxWidth(460);
				est.setMaxHeight(600);
				est.setStyle("-fx-text-fill:white;");
				est.getStyleClass().add("msgAlertError");
				
//				TextArea est = new TextArea(estadistica());
//				est.setWrapText(true);
//				est.setEditable(false); // que no se pueda modificar
//				est.setMaxWidth(460);
//				est.setMaxHeight(600);
//				est.setStyle(
//					    "-fx-control-inner-background: #0d262e;" +  /* fondo oscuro como el resto del juego */
//					    "-fx-font-size: 14px;" +
//					    "-fx-text-fill: white;" +
//					    "-fx-border-color: transparent;" +
//					    "-fx-background-insets: 0;" +
//					    "-fx-focus-color: transparent;" +
//					    "-fx-faint-focus-color: transparent;" +
//					    "-fx-highlight-fill: #2a7963;" +  /* color de selección de texto */
//					    "-fx-highlight-text-fill: white;" /* color del texto seleccionado */
//					);

				VBox content = new VBox(15, alertView, msg, est);
				content.setAlignment(Pos.CENTER);
				content.setPadding(new Insets(20));
				content.setPrefWidth(500);

				alert.getDialogPane().setContent(content);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
				Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
				okButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: white;");
				okButton.getStyleClass().add("boton-hover");
				alert.getDialogPane().getStyleClass().add("alertError");

				alert.setContentText(estadistica());
				// tornar a la pantalla de inici
				Optional<ButtonType> result = alert.showAndWait();

				if (result.isPresent() && result.get() == ButtonType.OK) {
					MenuController.wordleActivo = false;
					MenuController.infowordleActivo = true;
					try {
						/*
						 * VBox root = FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));
						 * 
						 * // En comptes de usar 'escena', que és null, agafem l'stage des d'un node //
						 * existent Stage stage = (Stage) grid1.getScene().getWindow();
						 * 
						 * Scene scene = new Scene(root); stage.setScene(scene);
						 * stage.setMaximized(true); stage.show();
						 * 
						 */

						Stage stage = (Stage) root.getScene().getWindow();
						stage.close();

						Parent escena = FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));
						String rutaFXML="wordleLogin.fxml";
						Scene escena2 = new Scene(escena, 600, 400);
						Stage window = new Stage();// (Stage) ((Node) e.getSource()).getScene().getWindow();
						window.setScene(escena2);
						window.setTitle("Wordle");

						window.show();
						// añadir los juegos abiertos
						MenuController.juegosAbiertos.add(window);
						MenuController.juegosPorNombre.put(rutaFXML, window);
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
		ArrayList<String> paraulesUtilitzades = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("paraules.txt"));// llig el fitxer
			String linea = "";
			Connection c = ConexionBBDD.conectar();
			String sql = "SELECT paraula FROM paraulesFetes";// select per mostrar totes les paraules
			PreparedStatement pst = c.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				paraulesUtilitzades.add(rs.getString("paraula").toUpperCase());// guardar en un array totes les paraules
																				// utilitzades
			}

			while ((linea = reader.readLine()) != null) {
				if (!paraulesUtilitzades.contains(linea)) {// si encara no se ha jugat la paraula
					paraules.add(linea);
				}

			}
			c.close();
			reader.close();

			int index = aleatori.nextInt(0, 300);
			paraulaAleatoria = paraules.get(index);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("La palabra para el wordle es: "+paraulaAleatoria);
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
		MenuController.wordleActivo = false;
		try {
			Stage stage = (Stage) root.getScene().getWindow();
			stage.close();

			Parent escena = FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));
			String rutaFXML="wordleLogin.fxml";
			Scene escena2 = new Scene(escena, 600, 400);
			// obtenim la finestra de l'aplicació actual
			Stage window = new Stage();// (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Wordle");
			window.show();
//			window.setMaximized(true);
			
			// añadir los juegos abiertos
			MenuController.juegosAbiertos.add(window);
			MenuController.juegosPorNombre.put(rutaFXML, window);
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

	private void actualitzarColorTeclat(char lletra, String color) {
		for (Node node : grid2.getChildren()) {
			if (node instanceof Button) {
				Button boto = (Button) node;
				if (boto.getText().equalsIgnoreCase(String.valueOf(lletra))) {
					// Només actualitza si el color actual és "menys prioritari"
					String estilActual = boto.getStyle();
					if (estilActual.contains("#43a047"))
						return; // si ja es verd no el torna a pintar
					if (estilActual.contains("#e4a81d") && color.equals("lightgrey"))
						return; // No rebaixem groc a gris

					boto.setStyle("-fx-background-color: " + color
							+ "; -fx-text-fill:white; -fx-font-size: 20px; -fx-font-weight: bold;");// pintem la tecla
																									// del color q pasem
																									// per parametre

					return;
				}
			}
		}
	}

	public int consultarId() {
		int idUsuari = 0;

		try {
			Connection c = ConexionBBDD.conectar();
			String consultaInsert = "SELECT id FROM usuari WHERE email = ?";
			PreparedStatement psInsert = c.prepareStatement(consultaInsert);
			psInsert.setString(1, LoginController.EMAIL);
			ResultSet rs = psInsert.executeQuery();
			while (rs.next()) {
				idUsuari = rs.getInt("id");
			}
			return idUsuari;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return idUsuari;
	}

	public void InsertarBBDD() {

		try {
			Connection c = ConexionBBDD.conectar();
			String consultaInsert = "INSERT INTO wordle (idUsuari,intents,fallades,encertats) VALUES (?,?,?,?)";
			String insertParaula = "INSERT INTO paraulesfetes (idWordle, paraula) VALUES (?,?)";
			PreparedStatement psInsert = c.prepareStatement(consultaInsert);

			psInsert.setInt(1, consultarId());
			psInsert.setInt(2, intents);
			psInsert.setInt(3, 0);
			psInsert.setInt(4, encertats);
			psInsert.executeUpdate();
			int idWordle = 0;
			String consultaIdParaula = "SELECT MAX(id) FROM wordle WHERE idUsuari = ?";
			PreparedStatement psconsultaId = c.prepareStatement(consultaIdParaula);
			psconsultaId.setInt(1, consultarId());// posar el id correcte
			ResultSet rs = psconsultaId.executeQuery();
			while (rs.next()) {
				idWordle = rs.getInt("MAX(id)");
			}
			System.out.println("idWordle: " + idWordle);

			// guardar la paraula que se ha ejecutat
			PreparedStatement psParaula = c.prepareStatement(insertParaula);
			psParaula.setInt(1, idWordle);// posar el id correcte
			psParaula.setString(2, paraula);
			psParaula.executeUpdate();

			psInsert.close();
			psParaula.close();
			c.close();

		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}

	}

	public String estadistica() {
		int encertades = 0;
		int guanyats = 0;
		try {
			Connection c = ConexionBBDD.conectar();

			// comptar total de partides jugades
			String consultaSelect = "SELECT COUNT(?) AS comptar FROM wordle GROUP BY (?)";
			PreparedStatement psSelect = c.prepareStatement(consultaSelect);
			psSelect.setString(1, "idUsuari");
			psSelect.setString(2, "idUsuari");
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				//vegades = rs.getInt("comptar") + 1;// +1 prq no compta el ultimo partido prq no esta en bd
				vegades = rs.getInt("comptar") ;

			}
			String consultaSelectGuanyats = "SELECT COUNT(?) AS comptarGuanyats FROM wordle WHERE encertats = ? GROUP BY (?)";
			PreparedStatement psSelectGuanyats = c.prepareStatement(consultaSelectGuanyats);
			psSelectGuanyats.setString(1, "encertats");
			psSelectGuanyats.setInt(2, 1);
			psSelectGuanyats.setString(3, "idUsuari");
			ResultSet rsGuanyats = psSelectGuanyats.executeQuery();
			while (rsGuanyats.next()) {
				//vegades = rs.getInt("comptar") + 1;// +1 prq no compta el ultimo partido prq no esta en bd
				guanyats = rsGuanyats.getInt("comptarGuanyats") ;
				
			}


		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}

		int posEncertats = 0;
		int vegadesEncertats = 0;
		int totalEncertats = 0;
		int index = 0;
		float[] posEncertatsArray = new float[6];
		float[] vegadesEncertatsArray = new float[6];
		float[] totalEncertatsPerArray = new float[6];

		try {
			Connection c = ConexionBBDD.conectar();


			String consultaSumaEncertats = "SELECT COUNT(encertats) AS TotalEncertats FROM wordle WHERE encertats = ?";
			PreparedStatement psSumaEncertats = c.prepareStatement(consultaSumaEncertats);
			psSumaEncertats.setInt(1, 1);
			ResultSet rsEncertats = psSumaEncertats.executeQuery();
			while (rsEncertats.next()) {
				totalEncertats = rsEncertats.getInt("TotalEncertats");

			}

			// comptar total de partides jugades

			String consultaSelect = "SELECT intents, COUNT(encertats) AS encertatsFila FROM wordle WHERE encertats = 1 GROUP BY (intents)";
			PreparedStatement psSelect = c.prepareStatement(consultaSelect);
			ResultSet rs = psSelect.executeQuery();
			float totalEncertatsPer = 0.0f;
			while (rs.next()) {
				posEncertats = rs.getInt("intents");
				posEncertatsArray[index] = (float) posEncertats;
				vegadesEncertats = rs.getInt("encertatsFila");
				vegadesEncertatsArray[index] = (float)vegadesEncertats;

				totalEncertatsPer = ( (float)vegadesEncertats /  (float)totalEncertats) * 100;
				totalEncertatsPerArray[index] = totalEncertatsPer;
				index++;
			}

		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
		
		String simbol = "#";
		StringBuilder resultat = new StringBuilder();
		resultat.append("\n+---------- ESTADÍSTICA -----------+" + "\n");
		resultat.append("|    Partides: " + vegades + "     |  Guanyats: " + guanyats +"   |\n");
		resultat.append("+------------------------------------+\n");
		
		for (int i = 0; i < posEncertatsArray.length; i++) {

			if (posEncertatsArray[i]!= 0.0) {
				
				resultat.append("|  "+i + ":   " + simbol.repeat(((int)totalEncertatsPerArray[i]) / 10) + " "+ vegadesEncertatsArray[i]+ " (" + totalEncertatsPerArray[i] + "% )                   |\n");

			}
		}
		resultat.append("+------------------------------------+");
		



		
		return resultat.toString();

	}

}
