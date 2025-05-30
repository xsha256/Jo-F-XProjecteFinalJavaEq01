package application;

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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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

	//ESTE ES PER AL SINGLETON
	private Taulell taulell;
	
	public void setTaulell(Taulell taulell) {
	    this.taulell = taulell;
	}

	private int files;
	private int columnes;
	private int grandariaCelda; 
	private Mode mode = Mode.PINTAR;
	private Casella [][] taulellCaselles;


	public void carregarTaulell() {
	    if (taulell == null || taulell.getCaselles() == null) {
	        System.out.println("No hi ha taulell per carregar.");
	        return;
	    }
	    
	    Casella[][] caselles = taulell.getCaselles();
	    int files = taulell.getAltura();
	    int columnes = taulell.getAmple();
	    
	    graella.getChildren().clear();
	    
	    
	    int grandariaCelda = 18;
	    
	    taulellCaselles = new Casella[files][columnes];
	    
	    for (int i = 0; i < files; i++) {
	        for (int j = 0; j < columnes; j++) {
	            Casella casella = caselles[i][j];
	            Label label = new Label();
	            label.setPrefSize(grandariaCelda, grandariaCelda);
	            label.setStyle("-fx-background-color:" + casella.getColor() + ";");
	            
	            taulellCaselles[i][j] = casella;
	           
	            final int fila = i;
	            final int col = j;
	            label.setOnMouseClicked(e -> {
	                if (mode == Mode.PINTAR) {
	                    Color c = color.getValue();
	                    String colorString = colorString(c);
	                    label.setStyle("-fx-background-color:" + colorString + ";");
	                    taulellCaselles[fila][col].setColor(colorString);
	                    taulellCaselles[fila][col].setOcupat(true);
	                } else if (mode == Mode.BORRAR) {
	                    label.setStyle("-fx-background-color:" + casella.getColor() + ";"); // suposant que guardes el color base
	                    taulellCaselles[fila][col].setColor(casella.getColor());
	                    taulellCaselles[fila][col].setOcupat(false);
	                }
	            });
	            
	            graella.add(label, j, i);
	        }
	    }
	}


	public BufferedImage crearImatgePNG(Casella[][] taulellCaselles) {
	    int files = taulellCaselles.length;
	    int columnes = taulellCaselles[0].length;
	    int midaCasella = 20;

	    BufferedImage image = new BufferedImage(columnes * midaCasella, files * midaCasella, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2d = image.createGraphics();

	    for (int i = 0; i < files; i++) {
	        for (int j = 0; j < columnes; j++) {
	            Casella c = taulellCaselles[i][j];

	            Color color;
	            if (c.isOcupat()) {
	            	//IMPORTANT!!! 
	            	//SI TINC DOS COLORS IMPORTATS, JO PUC ESPECIFICAR AIXÍ QUIN COLOR VULL GASTAR
	                color = javafx.scene.paint.Color.web(c.getColor());
	            } else {
	                color = javafx.scene.paint.Color.WHITE;
	            }

	            java.awt.Color awtColor = new java.awt.Color(
	                (float) color.getRed(),
	                (float) color.getGreen(),
	                (float) color.getBlue()
	            );

	            g2d.setColor(awtColor);
	            g2d.fillRect(j * midaCasella, i * midaCasella, midaCasella, midaCasella);
	        }
	    }

	    g2d.dispose();
	    return image;
	}
	
	//ESTA FUNCIÓ GUARDA LA IMATGE EN PNG... ME FALTA
	public void descarregar(ActionEvent e) {
	    Date diaDeJoc = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("dd_HH_mm_ss");
	    String data = format.format(diaDeJoc);
	    File outputFile = new File("PixelArt_" + data + ".png");

	    try {
	        BufferedImage image = crearImatgePNG(taulellCaselles);
	        javax.imageio.ImageIO.write(image, "png", outputFile);

	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Imatge Guardada");
	        alert.setHeaderText(null);
	        alert.setContentText("La imatge s'ha guardat com a: " + outputFile.getName());
	        alert.showAndWait();

	    } catch (IOException e1) {
	        e1.printStackTrace();
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("No s'ha pogut guardar la imatge");
	        alert.setContentText(e1.getMessage());
	        alert.showAndWait();
	    }
	}
	
	//ESTA FUNCIÓ TANCA EL PIXEL ART PER COMPLET, PERO PRIMER PREGUNTA SI ESTAS SEGUR
	public void tancarPixelArt(Stage stage) {
	    stage.setOnCloseRequest(event -> {
	        
	        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
	        alerta.setTitle("Eixida");
	        //PREGUNTA SI TANCA LA FINESTRA
	        alerta.setHeaderText("Estas segur de que vols tancar el PixelArt? Es pedrà tot allò que no s'haja guardat");
	        alerta.setContentText("Selecciona una opció:");

	  
	        ButtonType botoEixir = new ButtonType("Eixir");
	        ButtonType botoCancelar = new ButtonType("Cancelar");

	        alerta.getButtonTypes().setAll(botoEixir, botoCancelar);
	        
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
	
	//SERIALITZA I GUARDA A LA BDD
	public void guardarBDD(ActionEvent e) {
		try {

			//SERIALITZACIO
			Taulell t1 = this.taulell;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(t1);
            oos.close();
            byte[] imatgeSerialitzada = baos.toByteArray();

            //ESTO NO FA FALTA PERO COM JA ESTAVA EN LA BDD HO HE DEIXAT
            Date diaDeJoc = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String data = format.format(diaDeJoc);

           
            //ESTO PA CONNECTAR EN LA CLASSE CONEXIONBDD
            //DESPRES JA TREBALLAR COM SI LA CONNECTION ESTIGUERA ACI
            Connection c= ConexionBBDD.conectar();

            String sentenciaObtenirID="SELECT id FROM usuari WHERE email LIKE ?";
            PreparedStatement psID=c.prepareStatement(sentenciaObtenirID);
            psID.setString(1, "joan.garcia@example.com"/*LoginController.EMAIL*/ );
			ResultSet r = psID.executeQuery();
			int id=0;
			while (r.next()) {
				id=r.getInt("id");
			}
            
            String sentencia = "INSERT INTO pixelArt (idUsuari, data, dibuix) VALUES (?, ?, ?)";
            PreparedStatement s = c.prepareStatement(sentencia);
            s.setInt(1, id);
            s.setString(2, data);
            s.setBytes(3, imatgeSerialitzada);
            s.executeUpdate();

            s.close();
            c.close();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Avis");
            alerta.setHeaderText(null);
            alerta.setContentText("Dibuix guardat satisfactoriament");
            alerta.showAndWait();
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}
	
	//TORNA A LA PANTALLA DE SELECCIÓ DE MIDA
	public void tornarInici() {
		pantallaInici.setOnMouseClicked(event -> {
	        
	        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
	        alerta.setTitle("Eixida");
	        //AÇÒ FA EL MATEIX QUE EL BOTÓ DE GUARDAR, PERÒ DESPRES TANCA LA FINESTRA
	        alerta.setHeaderText("Vols guardar abans d'eixir?");
	        alerta.setContentText("Selecciona una opció:");

	        ButtonType botoGuardar = new ButtonType("Guardar");
	        ButtonType botoEixir = new ButtonType("Eixir");
	        ButtonType botoCancelar = new ButtonType("Cancelar");

	        alerta.getButtonTypes().setAll(botoGuardar, botoEixir, botoCancelar);

	        //UNA ESPECIA DE ARRAYLIST DELS BOTONS QUE HI HA EN EL POPUP
	        //DEPENENT DEL RESULTAT FA UNA COSA
	        Optional<ButtonType> resultat = alerta.showAndWait();

	        if (resultat.isPresent()) {
	            if (resultat.get() == botoGuardar) {
	                //SERIALITZA Y TANCA
	                guardarBDD(null);
	                try {
	          	        VBox rootInici = (VBox) FXMLLoader.load(getClass().getResource("PixelArtIniciFXML.fxml"));
	          	        Scene scene = new Scene(rootInici);
	          	        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	          	        Stage window = (Stage) this.root.getScene().getWindow();

	          	        window.setScene(scene);
	          	        window.setTitle("Inici Pixel Art");
	          	        window.setMaximized(true);
	          	        window.show();
	          	    } catch (IOException e1) {
	          	        e1.printStackTrace();
	          	    }
	            } else if (resultat.get() == botoEixir) {	                
	            	  try {
	          	        VBox rootInici = (VBox) FXMLLoader.load(getClass().getResource("PixelArtIniciFXML.fxml"));
	          	        Scene scene = new Scene(rootInici);
	          	        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	          	        Stage window = (Stage) this.root.getScene().getWindow();

	          	        window.setScene(scene);
	          	        window.setTitle("Inici Pixel Art");
	          	        window.setMaximized(true);
	          	        window.show();
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

	//ACTIVA EL BOTÓ DE BORRAR
	public void borrar(ActionEvent e) {
		mode = Mode.BORRAR;
		borrador.setStyle("-fx-background-color: #e85a71;");
		pinzell.setStyle("-fx-background-color:  #2a7963;");
	}

	//ACTIVA EL BOTÓ DE PINTAR
	public void pintar(ActionEvent e) {
		mode = Mode.PINTAR;
		pinzell.setStyle("-fx-background-color: #e85a71;");
		borrador.setStyle("-fx-background-color:  #2a7963;");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		
		DadesPixelArt dades = DadesPixelArt.getInstancia();
		
		//AGAFA LES DADES QUE LI HA PASSAT PEL SETTEXT DEL INICI
		taulell = dades.getTaulell();
		
		//SI NO HI HA TAULER EL CREA
		if (taulell == null || taulell.getCaselles() == null) {

		    if (taulell.getAmple() <= 16 && taulell.getAltura() <= 16) {
		        this.grandariaCelda = 20;
		    } else if (taulell.getAmple() <= 32 && taulell.getAltura() <= 32) {
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

		    carregarTaulell();
		}
	}

	//PINTA EN ELS PANELLS I ASSIGNA ATRIBUTS
	private Label crearPanell(String colorBase, int fila, int col) {
		Label casella = new Label();
		casella.setPrefSize(grandariaCelda, grandariaCelda);
		casella.setMinSize(grandariaCelda, grandariaCelda);
		casella.setMaxSize(grandariaCelda, grandariaCelda);
		casella.setStyle("-fx-background-color:" + colorBase + "; -fx-alignment: center;");

		casella.setOnMouseClicked(e -> {
			
			if (e.getButton() == MouseButton.PRIMARY && mode == Mode.PINTAR) {
				// agafe el valor que li he passat en el color picker
				casella.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");
				taulellCaselles[fila][col].setColor(colorString(color.getValue()));
				taulellCaselles[fila][col].setOcupat(true);
			} else if (e.getButton() == MouseButton.SECONDARY || mode == Mode.BORRAR) {
				casella.setStyle("-fx-background-color:" + colorBase + ";");
				taulellCaselles[fila][col].setColor(colorBase);
				taulellCaselles[fila][col].setOcupat(false);
			} 

		});

		//HABILITA EL DRAG EN TOTES LES CEL·LES
		casella.setOnDragDetected(e -> {
			casella.startFullDrag();
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

		//MENTRE ARRASTRE TOTES ES PINTEN
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

	//CONVERTEIX EL COLOR A RGB PER PINTAR
	private String colorString(Color c) {
															
		return "rgb(" + (int) (c.getRed() * 255) + "," + (int) (c.getGreen() * 255) + "," + (int) (c.getBlue() * 255)
				+ ")";
	}

}
