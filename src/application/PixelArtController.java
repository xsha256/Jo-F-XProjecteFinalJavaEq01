package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

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
	@FXML
	private Button imatgePNG;

	// ESTE ES PER AL SINGLETON
	private Taulell taulell;

	private int files;
	private int columnes;
	private int grandariaCelda;
	private Mode mode = Mode.PINTAR;
	private Casella[][] taulellCaselles;

	// EL TAULELL QUE TENIEM GUARDAR I DESERIALITZAT
	// HEM DE POSAR TOTS ELS EVENTS DE NOU
	public void carregarTaulellUltimaSessio(Taulell taulell) {
		this.taulell = taulell;
		Casella[][] caselles = taulell.getCaselles();

		this.taulellCaselles = caselles;
		graella.getChildren().clear();

		for (int i = 0; i < caselles.length; i++) {
			for (int j = 0; j < caselles[i].length; j++) {
				final int fila = i;
				final int col = j;
				Casella casellaModel = caselles[i][j];
				Label label = new Label();

				label.setPrefSize(casellaModel.getGrandaria(), casellaModel.getGrandaria());
				label.setMinSize(casellaModel.getGrandaria(), casellaModel.getGrandaria());
				label.setMaxSize(casellaModel.getGrandaria(), casellaModel.getGrandaria());
				label.setStyle("-fx-background-color:" + casellaModel.getColor() + "; -fx-alignment: center;");

				// ESDEVENIMENTS! 
				label.setOnMouseClicked(e -> {
					if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
						casellaModel.setColor(colorString(color.getValue())); // actualitza model
						label.setStyle("-fx-background-color:" + colorString(color.getValue()) + ";");
					} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {
						String colorBuit = ((fila + col) % 2 == 0) ? "#ffffff" : "#cccccc";
						casellaModel.setColor(colorBuit);
						label.setStyle("-fx-background-color:" + colorBuit + ";");
					}
				});

				label.setOnDragDetected(e -> {
					label.startFullDrag();
					if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
						casellaModel.setColor(colorString(color.getValue()));
						label.setStyle("-fx-background-color:" + colorString(color.getValue()) + ";");
					} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {
						String colorBuit = ((fila + col) % 2 == 0) ? "#ffffff" : "#cccccc";
						casellaModel.setColor(colorBuit);
						label.setStyle("-fx-background-color:" + colorBuit + ";");
					}
				});

				label.setOnMouseDragEntered(e -> {
					if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
						casellaModel.setColor(colorString(color.getValue()));
						label.setStyle("-fx-background-color:" + colorString(color.getValue()) + ";");
					} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {

						String colorBuit = ((fila + col) % 2 == 0) ? "#ffffff" : "#cccccc";
						casellaModel.setColor(colorBuit);
						label.setStyle("-fx-background-color:" + colorBuit + ";");
					}
				});

				graella.add(label, j, i);
			}
		}
	}

	// ESTA CREA EL BUFFEREDIMAGE QUE DESPRÉS LI PASSAREM A LA FUNCIÓ DESCARREGAR
	// PER GUARDAR-LA
	public BufferedImage crearImatgePNG(Casella[][] taulellCaselles) {
		int files = taulellCaselles.length;
		int columnes = taulellCaselles[0].length;
		int midaCasella = 20;

		BufferedImage image = new BufferedImage(columnes * midaCasella, files * midaCasella,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();

		for (int i = 0; i < files; i++) {
			for (int j = 0; j < columnes; j++) {
				Casella c = taulellCaselles[i][j];

				Color color;
				if (c.isOcupat()) {
					// IMPORTANT!!!
					// SI TINC DOS COLORS IMPORTATS, JO PUC ESPECIFICAR AIXÍ QUIN COLOR VULL GASTAR
					color = javafx.scene.paint.Color.web(c.getColor());
				} else {
					color = javafx.scene.paint.Color.WHITE;
				}

				java.awt.Color awtColor = new java.awt.Color((float) color.getRed(), (float) color.getGreen(),
						(float) color.getBlue());

				g2d.setColor(awtColor);
				g2d.fillRect(j * midaCasella, i * midaCasella, midaCasella, midaCasella);
			}
		}

		g2d.dispose();
		return image;
	}

	// ESTA FUNCIÓ GUARDA LA IMATGE EN PNG
	public void descarregar(ActionEvent e) {
		Date diaDeJoc = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd_HH_mm_ss");
		String data = format.format(diaDeJoc);
		File outputFile = new File("PixelArt_" + data + ".png");

		try {
			BufferedImage image = crearImatgePNG(taulellCaselles);
			javax.imageio.ImageIO.write(image, "png", outputFile);
			/*
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Imatge Guardada");
			alert.setHeaderText(null);
			alert.setContentText("La imatge s'ha guardat com a: " + outputFile.getName());
			alert.showAndWait();
			*/
			
			Alert alert = new Alert(Alert.AlertType.NONE);
		    alert.setTitle("Imatge Guardada");
		    alert.getDialogPane().setPrefSize(500, 300);
		    alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		    ImageView icon = new ImageView(new Image("file:imagenes/alerta.png"));
		    icon.setFitWidth(100);
		    icon.setPreserveRatio(true);

		    Label msg = new Label("La imatge s'ha guardat com a: " + outputFile.getName());
		    msg.setWrapText(true);
		    msg.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

		    VBox content = new VBox(15, icon, msg);
		    content.setAlignment(Pos.CENTER);
		    content.setPadding(new Insets(20));
		    alert.getDialogPane().setContent(content);

		    ButtonType botoAcceptar = new ButtonType("Acceptar", ButtonBar.ButtonData.OK_DONE);
		    alert.getButtonTypes().setAll(botoAcceptar);

		    Button botonAceptar = (Button) alert.getDialogPane().lookupButton(botoAcceptar);
		    botonAceptar.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8; -fx-font-weight: bold;");
		    botonAceptar.setCursor(Cursor.HAND);

		    alert.showAndWait();
		    
		} catch (IOException e1) {
			e1.printStackTrace();
			/*
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No s'ha pogut guardar la imatge");
			alert.setContentText(e1.getMessage());
			alert.showAndWait();
			*/
			
			Alert alert = new Alert(Alert.AlertType.NONE);
		    alert.setTitle("Error");
		    alert.getDialogPane().setPrefSize(500, 300);
		    alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		    ImageView icon = new ImageView(new Image("file:imagenes/alerta.png"));
		    icon.setFitWidth(100);
		    icon.setPreserveRatio(true);

		    Label msg = new Label(e1.getMessage());
		    msg.setWrapText(true);
		    msg.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

		    VBox content = new VBox(15, icon, msg);
		    content.setAlignment(Pos.CENTER);
		    content.setPadding(new Insets(20));
		    alert.getDialogPane().setContent(content);

		    ButtonType botoAcceptar = new ButtonType("Acceptar", ButtonBar.ButtonData.OK_DONE);
		    alert.getButtonTypes().setAll(botoAcceptar);

		    Button botonAceptar = (Button) alert.getDialogPane().lookupButton(botoAcceptar);
		    botonAceptar.setStyle("-fx-background-color: #f44336; -fx-text-fill: #e8e8e8; -fx-font-weight: bold;");
		    botonAceptar.setCursor(Cursor.HAND);

		    alert.showAndWait();
		}
	}

	// ESTA FUNCIÓ TANCA EL PIXEL ART PER COMPLET, PERO PRIMER PREGUNTA SI ESTAS
	// SEGUR
	// NO GUARDA EL DIBUIX ACTUAL, SOLAMENT AVISA AL USUARI
	public void tancarPixelArt(Stage stage) {
		
		stage.setOnCloseRequest(event -> {
			MenuController.pixelartActivo=false;
			
			/*
			Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
			alerta.setTitle("Eixida");
			// PREGUNTA SI TANCA LA FINESTRA
			alerta.setHeaderText("Estas segur de que vols tancar el PixelArt? Es pedrà tot allò que no s'haja guardat");
			alerta.setContentText("Selecciona una opció:");

			ButtonType botoEixir = new ButtonType("Eixir");
			ButtonType botoCancelar = new ButtonType("Cancelar");

			alerta.getButtonTypes().setAll(botoEixir, botoCancelar);
			*/
			
			Alert alerta = new Alert(Alert.AlertType.NONE);
		    alerta.setTitle("Eixida");
		    alerta.getDialogPane().setPrefSize(500, 300);
		    alerta.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		    ImageView icon = new ImageView(new Image("file:imagenes/alerta.png"));
		    icon.setFitWidth(100);
		    icon.setPreserveRatio(true);

		    Label msg = new Label("Estas segur de que vols tancar el PixelArt? Es pedrà tot allò que no s'haja guardat");
		    msg.setWrapText(true);
		    msg.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

		    VBox content = new VBox(15, icon, msg);
		    content.setAlignment(Pos.CENTER);
		    content.setPadding(new Insets(20));
		    alerta.getDialogPane().setContent(content);

		    ButtonType botoEixir = new ButtonType("Eixir", ButtonBar.ButtonData.YES);
		    ButtonType botoCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
		    alerta.getButtonTypes().setAll(botoEixir, botoCancelar);

		    Button botonEixir = (Button) alerta.getDialogPane().lookupButton(botoEixir);
		    botonEixir.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8; -fx-font-weight: bold;");
		    botonEixir.setCursor(Cursor.HAND);

		    Button botonCancelar = (Button) alerta.getDialogPane().lookupButton(botoCancelar);
		    botonCancelar.setStyle("-fx-background-color: #f44336; -fx-text-fill: #e8e8e8; -fx-font-weight: bold;");
		    botonCancelar.setCursor(Cursor.HAND);

		    Optional<ButtonType> resultat = alerta.showAndWait();


			if (resultat.isPresent()) {
				if (resultat.get() == botoEixir) {
					stage.close();
				} else {
					event.consume();
				}
			} else {
				event.consume();
			}
		});
	}

	// SERIALITZA I GUARDA A LA BDD
	public void guardarBDD(ActionEvent e) {
		if (taulellCaselles == null) {
			System.err.println("⚠ No es pot guardar. taulellCaselles = null");
			return;
		}

		try {

			// SERIALITZACIO
			Taulell t1 = new Taulell(taulellCaselles);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(t1);
			oos.close();
			byte[] imatgeSerialitzada = baos.toByteArray();

			// ESTO NO FA FALTA PERO COM JA ESTAVA EN LA BDD HO HE DEIXAT
			Date diaDeJoc = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String data = format.format(diaDeJoc);

			// ESTO PA' CONNECTAR EN LA CLASSE CONEXIONBDD
			// DESPRES JA TREBALLAR COM SI LA CONNECTION ESTIGUERA ACI
			Connection c = ConexionBBDD.conectar();

			String sentenciaObtenirID = "SELECT id FROM usuari WHERE email LIKE ?";
			PreparedStatement psID = c.prepareStatement(sentenciaObtenirID);
			psID.setString(1, LoginController.EMAIL );
			ResultSet r = psID.executeQuery();
			int id = 0;
			while (r.next()) {
				id = r.getInt("id");
			}

			String sentencia = "INSERT INTO pixelArt (idUsuari, data, dibuix) VALUES (?, ?, ?)";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setInt(1, id);
			s.setString(2, data);
			s.setBytes(3, imatgeSerialitzada);
			s.executeUpdate();

			s.close();
			c.close();
			/*
			Alert alerta = new Alert(Alert.AlertType.INFORMATION);
			alerta.setTitle("Avis");
			alerta.setHeaderText(null);
			alerta.setContentText("Dibuix guardat satisfactoriament");
			alerta.showAndWait();
			*/
			
			Alert alerta = new Alert(Alert.AlertType.NONE);
			alerta.setTitle("Avis");
			alerta.getDialogPane().setPrefSize(500, 300);
			alerta.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			ImageView icon = new ImageView(new Image("file:imagenes/alerta.png"));
			icon.setFitWidth(100);
			icon.setPreserveRatio(true);

			Label msg = new Label("Dibuix guardat satisfactoriament");
			msg.setWrapText(true);
			msg.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

			VBox content = new VBox(15, icon, msg);
			content.setAlignment(Pos.CENTER);
			content.setPadding(new Insets(20));
			alerta.getDialogPane().setContent(content);

			ButtonType botoAcceptar = new ButtonType("Acceptar", ButtonBar.ButtonData.OK_DONE);
			alerta.getButtonTypes().setAll(botoAcceptar);

			Button botonAceptar = (Button) alerta.getDialogPane().lookupButton(botoAcceptar);
			botonAceptar.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8; -fx-font-weight: bold;");
			botonAceptar.setCursor(Cursor.HAND);

			alerta.showAndWait();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	// TORNA A LA PANTALLA DE SELECCIÓ DE MIDA
	// PREGUNTA ABANS SI VOL GUARDAR
	public void tornarInici() {
		
		pantallaInici.setOnMouseClicked(event -> {
			/*
			Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
			alerta.setTitle("Eixida");
			// AÇÒ FA EL MATEIX QUE EL BOTÓ DE GUARDAR, PERÒ DESPRES TANCA LA FINESTRA
			alerta.setHeaderText("Vols guardar abans d'eixir?");
			alerta.setContentText("Selecciona una opció:");

			ButtonType botoGuardar = new ButtonType("Guardar");
			ButtonType botoEixir = new ButtonType("Eixir");
			ButtonType botoCancelar = new ButtonType("Cancelar");

			alerta.getButtonTypes().setAll(botoGuardar, botoEixir, botoCancelar);

			// UNA ESPECIA DE ARRAYLIST DELS BOTONS QUE HI HA EN EL POPUP
			// DEPENENT DEL RESULTAT FA UNA COSA
			Optional<ButtonType> resultat = alerta.showAndWait();
			*/
			
			Alert alertaTornar = new Alert(Alert.AlertType.NONE);
			alertaTornar.setTitle("Eixida");
			alertaTornar.getDialogPane().setPrefSize(500, 300);
			alertaTornar.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			ImageView iconTornar = new ImageView(new Image("file:imagenes/alerta.png"));
			iconTornar.setFitWidth(100);
			iconTornar.setPreserveRatio(true);

			Label msgTornar = new Label("Vols guardar abans d'eixir?");
			msgTornar.setWrapText(true);
			msgTornar.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

			VBox contentTornar = new VBox(15, iconTornar, msgTornar);
			contentTornar.setAlignment(Pos.CENTER);
			contentTornar.setPadding(new Insets(20));
			alertaTornar.getDialogPane().setContent(contentTornar);

			ButtonType botoGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.YES);
			ButtonType botoEixir = new ButtonType("Eixir", ButtonBar.ButtonData.NO);
			ButtonType botoCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
			alertaTornar.getButtonTypes().setAll(botoGuardar, botoEixir, botoCancelar);

			((Button) alertaTornar.getDialogPane().lookupButton(botoGuardar)).setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8; -fx-font-weight: bold;");
			((Button) alertaTornar.getDialogPane().lookupButton(botoGuardar)).setCursor(Cursor.HAND);
			((Button) alertaTornar.getDialogPane().lookupButton(botoEixir)).setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8; -fx-font-weight: bold;");
			((Button) alertaTornar.getDialogPane().lookupButton(botoEixir)).setCursor(Cursor.HAND);
			((Button) alertaTornar.getDialogPane().lookupButton(botoCancelar)).setStyle("-fx-background-color: #f44336; -fx-text-fill: #e8e8e8; -fx-font-weight: bold;");
			((Button) alertaTornar.getDialogPane().lookupButton(botoCancelar)).setCursor(Cursor.HAND);

			Optional<ButtonType> resultatTornar = alertaTornar.showAndWait();
			
			if (resultatTornar.isPresent()) {
			    MenuController.pixelartActivo = false;
			    if (resultatTornar.get() == botoGuardar) {
			
//			if (resultat.isPresent()) {
//				MenuController.pixelartActivo=false;
//				if (resultat.get() == botoGuardar) {
					// SERIALITZA Y TANCA
					guardarBDD(null);
					try {
						Stage ventanaActual = (Stage) root.getScene().getWindow();
						ventanaActual.close();
						
						VBox rootInici = (VBox) FXMLLoader.load(getClass().getResource("PixelArtIniciFXML.fxml"));
						String rutaFXML="PixelArtIniciFXML.fxml";
						Scene scene = new Scene(rootInici, 600, 400);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

						Stage window = new Stage();//(Stage) this.root.getScene().getWindow();
						window.setScene(scene);
						window.setTitle("Inici Pixel Art");
//						window.setMaximized(true);
						window.setResizable(false);
						window.show();
						
						// añadir los juegos abiertos
						MenuController.juegosAbiertos.add(window);
						MenuController.juegosPorNombre.put(rutaFXML, window);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    } else if (resultatTornar.get() == botoEixir) {	
//				} else if (resultat.get() == botoEixir) {
					MenuController.pixelartActivo=false;
					try {
						Stage ventanaActual = (Stage) root.getScene().getWindow();
						ventanaActual.close();
						
						VBox rootInici = (VBox) FXMLLoader.load(getClass().getResource("PixelArtIniciFXML.fxml"));
						String rutaFXML="PixelArtIniciFXML.fxml";
						Scene scene = new Scene(rootInici, 600, 400);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

						Stage window = new Stage();//(Stage) this.root.getScene().getWindow();

						window.setScene(scene);
						window.setTitle("Inici Pixel Art");
//						window.setMaximized(true);
						window.setResizable(false);
						window.show();
						
						// añadir los juegos abiertos
						MenuController.juegosAbiertos.add(window);
						MenuController.juegosPorNombre.put(rutaFXML, window);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else {
					event.consume();
				}
			} else {
				event.consume();
			}
		});
	}

	// CANVIA EL MODE DEL BOTO A BORRAR I POSA ATRIBUTS
	public void borrar(ActionEvent e) {
		mode = Mode.BORRAR;
		borrador.setStyle("-fx-background-color: #e85a71;");
		pinzell.setStyle("-fx-background-color:  #2a7963;");

	}

	// CANVIA EL MODE DEL BOTO A PINTAR I POSA ATRIBUTS
	public void pintar(ActionEvent e) {
		mode = Mode.PINTAR;
		pinzell.setStyle("-fx-background-color: #e85a71;");
		borrador.setStyle("-fx-background-color:  #2a7963;");
	}

	// PINTA EN ELS PANELLS I ASSIGNA ATRIBUTS I FUNCIONALITAT
	private Label crearPanell(String colorBase, int fila, int col) {
		Label casella = new Label();
		casella.setPrefSize(grandariaCelda, grandariaCelda);
		casella.setMinSize(grandariaCelda, grandariaCelda);
		casella.setMaxSize(grandariaCelda, grandariaCelda);
		casella.setStyle("-fx-background-color:" + colorBase + "; -fx-alignment: center;");

		casella.setOnMouseClicked(e -> {

			if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
				// AGAFE EL COLOR QUE LI HE PASSAT AL COLORPICKER
				casella.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");
				taulellCaselles[fila][col].setColor(colorString(color.getValue()));
				taulellCaselles[fila][col].setOcupat(true);
			} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {
				casella.setStyle("-fx-background-color:" + colorBase + ";");
				taulellCaselles[fila][col].setColor(colorBase);
				taulellCaselles[fila][col].setOcupat(false);
			}

		});

		// HABILITA EL DRAG EN TOTES LES CEL·LES
		casella.setOnDragDetected(e -> {
			casella.startFullDrag();

			if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
				casella.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");
				taulellCaselles[fila][col].setColor(colorString(color.getValue()));
				taulellCaselles[fila][col].setOcupat(true);
				Image image = new Image("file:imagenes/Pinzell.png", 32, 32, true, true); // Ruta relativa
				ImageCursor customCursor = new ImageCursor(image);
				casella.setCursor(customCursor);
			} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {
				casella.setStyle("-fx-background-color:" + colorBase + ";");
				taulellCaselles[fila][col].setColor(colorBase);
				taulellCaselles[fila][col].setOcupat(false);
				Image image = new Image("file:imagenes/goma.png", 32, 32, true, true); // Ruta relativa
				ImageCursor customCursor = new ImageCursor(image);
				casella.setCursor(customCursor);

			}
		});

		// MENTRE ARRASTRE TOTES ES PINTEN
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

	// CONVERTEIX EL COLOR A RGB PER PINTAR, ESTO CREC QUE HO VAIG FER PERQUE NO ME
	// FUNCIONAVA EL HEXADECIMAL
	private String colorString(Color c) {

		return "rgb(" + (int) (c.getRed() * 255) + "," + (int) (c.getGreen() * 255) + "," + (int) (c.getBlue() * 255)
				+ ")";
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//funcion que cambia el estado de los booleans para poder duplicados abiertos del mismo juego
		Platform.runLater(()->{
			Stage ventanaActual = (Stage) root.getScene().getWindow();
			if(ventanaActual.isShowing()) {
				MenuController.pixelartActivo=true;
				System.out.println("El juego Pixelart esta activo. Boolean: "+MenuController.pixelartActivo);
				System.out.println("BooleanMida: "+MenuController.midapixelartActivo);
				System.out.println("BooleaDesats: "+MenuController.desatspixelartActivo);
			}
			ventanaActual.setOnHidden(evt ->{
				MenuController.pixelartActivo=false;
				System.out.println("El juego Pixelart se cerró. Boolean: "+MenuController.pixelartActivo);
				System.out.println("BooleanMida: "+MenuController.midapixelartActivo);
				System.out.println("BooleaDesats: "+MenuController.desatspixelartActivo);
			});
		});
		
		
		DadesPixelArt dades = DadesPixelArt.getInstancia();

		// AGAFA LES DADES QUE LI HA PASSAT PEL SETTEXT DEL INICI
		taulell = dades.getTaulell();

		// SI NO HI HA TAULER EL CREA
		if (taulell == null || taulell.getCaselles() == null) {

			if (taulell.getAmple() <= 32 && taulell.getAltura() <= 16) {
				this.grandariaCelda = 20;
			} else if (taulell.getAmple() <= 64 && taulell.getAltura() <= 32) {
				this.grandariaCelda = 15;
			} else {
				this.grandariaCelda = 10;
			}

			this.files = taulell.getAltura();
			this.columnes = taulell.getAmple();
			taulellCaselles = new Casella[this.files][this.columnes];

			root.prefWidthProperty().bind(root.widthProperty());
			root.prefHeightProperty().bind(root.heightProperty());

			int contador = 0;

			for (int fila = 0; fila < files; fila++) {
				for (int col = 0; col < columnes; col++) {
					if (contador % 2 == 0) {
						String colorBase = "white";
						Label celda = crearPanell(colorBase, fila, col);
						graella.add(celda, col, fila);
						taulellCaselles[fila][col] = new Casella(colorBase, false, this.grandariaCelda);
					} else {
						String colorBase = "#cccccc";
						Label celda = crearPanell(colorBase, fila, col);
						graella.add(celda, col, fila);
						taulellCaselles[fila][col] = new Casella(colorBase, false, this.grandariaCelda);
					}
					contador++;
				}
				contador++;
			}
		} else {

			root.prefWidthProperty().bind(root.widthProperty());
			root.prefHeightProperty().bind(root.heightProperty());

			carregarTaulellUltimaSessio(taulell);

			pinzell.setOnAction(e -> pintar(e));
			borrador.setOnAction(e -> borrar(e));
			guardarImatge.setOnAction(e -> guardarBDD(e));
			imatgePNG.setOnAction(e -> descarregar(e));
			pantallaInici.setOnMouseClicked(e -> tornarInici());
		}
	}
}
