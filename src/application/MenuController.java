package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
	//atributos
	private Connection c;	
	private String emailUsuario;
	
	//Recogemos los botones "jugar" que tiene cada juego
	@FXML private Button jugarPescamines;
	@FXML private Button jugarWordle;
	@FXML private Button jugarPixelArt;
	@FXML private Button jugarJocDeLaVida;
	
	@FXML private HBox root;
	
	//id de la variable para el nombre del usuario en el titulo
	@FXML private Label tituloNomUsuari;
	@FXML private ImageView imatgePerfilUsuari;
	
	//id de las imagenes de cada juego
	@FXML private ImageView fotoPescamines;
	@FXML private ImageView fotoWordle;
	@FXML private ImageView fotoJocDeLaVida;
	@FXML private ImageView fotoPixelArt;
	
	//items del MenuButton
	@FXML private MenuItem itemBaixa;
	@FXML private MenuItem itemLogout;
	@FXML
	private ImageView icono;

	@FXML
	private Slider slider;

	private MediaPlayer mediaPlayer;
	
	//array de ventanas abiertas
	public static ArrayList<Stage> juegosAbiertos=new ArrayList<Stage>();
	//hashmap para no poder abrir el mismo juego a la vez
	public static Map<String, Stage> juegosPorNombre = new HashMap<>();
	
	//instancia de pixelart para poder guardar
	public static PixelArtController controladorPixelArt = null;
	
	//instancia que se usará para poder guardar al cerrar menucontroller
	public static BuscaMinasController controladorBuscaMinas;
	
	//metodo que hace que se inicie 
	public void initialize(URL location, ResourceBundle resources) {
	this.emailUsuario=LoginController.EMAIL; //poner nombre archivo.nombreVariable del login de Moha;
		
		//funcion para cerrar todas las ventas abiertas a traves de Menu
		Platform.runLater(()->{
			Stage ventanaActual = (Stage) root.getScene().getWindow();
			
			ventanaActual.setOnCloseRequest(evt ->{
				tancarSesioApretado=true;
				System.out.println("Hola Mundo!");
				
				if(pixelartActivo) {
					System.out.println("Dentro del if menucontroller pixelart");

					Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
					alerta.setTitle("Eixida");
					alerta.setHeaderText("Vols guardar abans d'eixir?");
					alerta.setContentText("Selecciona una opció:");

					ButtonType botoGuardar = new ButtonType("Guardar");
					ButtonType botoEixir = new ButtonType("Eixir");
					ButtonType botoCancelar = new ButtonType("Cancelar");

					alerta.getButtonTypes().setAll(botoGuardar, botoEixir, botoCancelar);

					Optional<ButtonType> resultat = alerta.showAndWait();

					if (resultat.isPresent()) {
						if (resultat.get() == botoGuardar) {
							if (controladorPixelArt != null) {
								controladorPixelArt.guardarBDD(null);
							} else {
								System.err.println("⚠ No hi ha cap controladorPixelArt carregat.");
							}
						}
					}
				}

				//------------------------------------------------------------------------------------------
				System.out.println("fuera del if menucontroller pixelart");
				for (Stage s : new ArrayList<>(juegosAbiertos)) {
			        s.close();
			        
			        jocvidaActivo=false;
			        midajocvidaActivo=false;
			        
			        pescaminesActivo=false;
			        midapescaminesActivo=false;
			    	rankingpescaminesActivo=false;
			    	carregarpescaminesActivo=false;
			    	
			    	pixelartActivo=false;
			    	midapixelartActivo=false;
			    	desatspixelartActivo=false;
			    	
			    	wordleActivo=false;
			    	infowordleActivo=false;
			    }
			    juegosAbiertos.clear();
			    juegosPorNombre.clear();				
			});
		});
		
		
		
		try {
			// cargar el driver de MariaDB... con una vez sobra creo :) 
//	        Class.forName("org.mariadb.jdbc.Driver");
	        

			 this.c = ConexionBBDD.conectar();



			//----------------------------------------------------------------------------
			
			//llamamos a la funcion que comprueba el nombre de usuario
			String nombreBienvenida=ponerNombreUsuarioBD(c,emailUsuario);
			//cambiamos la bienvenida por la que contiene el nombre de usuario
			tituloNomUsuari.setText("A que t'apeteix jugar hui, "+nombreBienvenida +"?");
			
			//funcion para asignar la imagen de perfil de usuario al botón que la contiene
			asignarImagenPerfilUsuario(c,emailUsuario,imatgePerfilUsuari);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	    inicializarMusica();
	
	}
	
	
	//al pulsar esto, se borra la cuenta del usuario
	public void actionBaja(ActionEvent e){
		
		Alert alert = new Alert(AlertType.NONE);
		alert.setTitle("🚩 Confirmació");
		alert.getDialogPane().setPrefSize(500, 300);
		Image iconAlert = new Image("file:imagenes/danger.png");
		ImageView alertView = new ImageView(iconAlert);
		alertView.setFitWidth(200);
		alertView.setPreserveRatio(true);
		Label msg = new Label("Estàs segur que vols donar-te de baixa?\n Aquesta acció eliminarà el teu usuari per sempre.");
		msg.setTextAlignment(TextAlignment.CENTER);
		msg.setWrapText(true);
		msg.getStyleClass().add("msgAlertError");
		VBox content = new VBox(15, alertView, msg);
		content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(20));
		content.setPrefWidth(200);
		alert.getDialogPane().setContent(content);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
	    //botones personalizados
	    ButtonType botAceptar = new ButtonType("Acceptar", ButtonBar.ButtonData.OK_DONE);
	    ButtonType botCancelar = new ButtonType("Cancel·lar", ButtonBar.ButtonData.CANCEL_CLOSE);
	    alert.getButtonTypes().setAll(botAceptar,botCancelar);//añadir los botones
	    
	    Button botonAceptar = (Button) alert.getDialogPane().lookupButton(botAceptar);
	    botonAceptar.setStyle(
	        "-fx-background-color: #2a7963;" +     
	        "-fx-text-fill: #e8e8e8;" +           
	        "-fx-font-weight: bold;"
	    );
	    botonAceptar.setCursor(Cursor.HAND);


	    Button botonCancelar = (Button) alert.getDialogPane().lookupButton(botCancelar);
	    botonCancelar.setStyle(
	        "-fx-background-color: #f44336;" +     
	        "-fx-text-fill: #e8e8e8;" +
	        "-fx-font-weight: bold;"
	    );
	    botonCancelar.setCursor(Cursor.HAND);
	    
		
	    Optional<ButtonType> resultado = alert.showAndWait();
	    
	    if(resultado.isPresent() && resultado.get() == botAceptar) {//si pulsa ok -> adiós cuenta. Si pulsa no, pues nada
	    	String sentencia = "DELETE FROM usuari WHERE email =?";

		    try {
		        PreparedStatement ps = c.prepareStatement(sentencia);
		        ps.setString(1, emailUsuario);
	
		        int cant = ps.executeUpdate();
		        if (cant>0) {
		        	System.out.println("Usuari eliminat correctament.");
		        	actionLogout(e);//llamamos de nuevo a Login ya que si se ha dado de baja, deberia salir
		        } else {
		            System.out.println("Usuari no eliminat correctament... ");
		        }
		    } catch (SQLException e1) {
		        e1.printStackTrace();
		    }
	    }else {
		    System.out.println("Borrat Cancelat");
	    }
	    
	}
	public static boolean tancarSesioApretado=false;
	//metodo para cerrar sesión
	public void actionLogout(ActionEvent e) {
		tancarSesioApretado=true;
		//comprobar si el pixelart esta activo y preguntar para guardar
		if (pixelartActivo) {
			Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
			alerta.setTitle("Eixida");
			alerta.setHeaderText("Vols guardar abans de tancar sessió?");
			alerta.setContentText("Selecciona una opció:");

			ButtonType botoGuardar = new ButtonType("Guardar");
			ButtonType botoEixir = new ButtonType("Tancar sense guardar");
			ButtonType botoCancelar = new ButtonType("Cancelar");

			alerta.getButtonTypes().setAll(botoGuardar, botoEixir, botoCancelar);

			Optional<ButtonType> resultat = alerta.showAndWait();

			if (resultat.isPresent()) {
				if (resultat.get() == botoGuardar) {
					if (controladorPixelArt != null) {
						controladorPixelArt.guardarBDD(null);
					}
				} else if (resultat.get() == botoCancelar) {
					return; // CANCELA el logout
				}
			}
		}
		//---------------------------------------------------------------------------------
		
		//comprobar si pescamines esta activo y preguntar para guardar
		if (pescaminesActivo) {
//		    FXMLLoader loader = new FXMLLoader(getClass().getResource("BuscaMinas.fxml"));
		    try {
		        // IMPORTANTE: NO uses load() solo para llamar a alerta.
		        // Mejor usa la instancia real del controlador guardada cuando se abre el juego.
		    	if (controladorBuscaMinas != null) {
		    	    controladorBuscaMinas.alerta("Vols guardar la partida abans d'eixir?", "file:imagenes/alerta.png");
		    	}
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		}
		//-------------------------------------------------------------------------------
		try {
			
			for (Stage s : new ArrayList<>(juegosAbiertos)) {
			    s.close();
			    
		        jocvidaActivo=false;
		        midajocvidaActivo=false;
		        
		        pescaminesActivo=false;
		        midapescaminesActivo=false;
		    	rankingpescaminesActivo=false;
		    	carregarpescaminesActivo=false;
		    	
		    	pixelartActivo=false;
		    	midapixelartActivo=false;
		    	desatspixelartActivo=false;
		    	
		    	wordleActivo=false;
		    	infowordleActivo=false;
		    
			}
			juegosAbiertos.clear();
			juegosPorNombre.clear();

			
			//cerrar la ventana actual
			Stage ventanaActual = (Stage) itemLogout.getParentPopup().getOwnerWindow();
	        ventanaActual.close();
	        
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene escena= new Scene(root);
			Stage window = new Stage();
			window.setScene(escena);
			window.setTitle("Login");
			window.show();
	        
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mediaPlayer.stop();
	}

	
	public String ponerNombreUsuarioBD(Connection c, String emailUsuari) {
	    String sentencia = "SELECT nom FROM usuari WHERE email = ?;";
	    
	    try {
	        
	        PreparedStatement ps = c.prepareStatement(sentencia);
	        ps.setString(1, emailUsuari); // se coloca el valor en el primer '?'

	        ResultSet r = ps.executeQuery();
	        if (r.next()) {
	            String nomb = r.getString("nom");
	            return nomb;
	        } else {
	            return "Usuari no trobat... ";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error al averiguar el nombre de usuario";
	    }
	}

	
	public void asignarImagenPerfilUsuario(Connection c,String emailUsuari, ImageView imagenPerfil) {
	    String sentencia = "SELECT imatge FROM usuari WHERE email =?";
	   
	    try {
	        PreparedStatement s = c.prepareStatement(sentencia);
	        s.setString(1, emailUsuari);
	        ResultSet r = s.executeQuery();
	        if (r.next()) {
	        	System.out.println(r);
	            InputStream input = r.getBinaryStream("imatge");
	            if (input != null) {
	                Image imagen = new Image(input);
	                imagenPerfil.setImage(imagen);//añade la imagen directamente
	                //imagenPerfil.setFitWidth(110);
	                //imagenPerfil.setFitHeight(110);
	                
	                //------------------------------------------------
	                imagenPerfil.setImage(imagen);                                                                                    
	                imagenPerfil.setFitWidth(65);
	                imagenPerfil.setFitHeight(65);
	                imagenPerfil.setPreserveRatio(false);
	                imagenPerfil.setSmooth(true);
	        		double radius = imagenPerfil.getFitWidth() / 2;
	        		Circle clip = new Circle(radius, radius, radius);
	        		imagenPerfil.setClip(clip);
	        		//-----------------------------------------------------
	        		
	            }else {
	            	System.out.println("Imatge no trobada! ");
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static boolean jocvidaActivo=false;
	public static boolean midajocvidaActivo=false;
	
	public static boolean pescaminesActivo=false;
	public static boolean midapescaminesActivo=false;
	public static boolean rankingpescaminesActivo=false;
	public static boolean carregarpescaminesActivo=false;
	
	public static boolean pixelartActivo=false;
	public static boolean midapixelartActivo=false;
	public static boolean desatspixelartActivo=false;
	
	
	public static boolean wordleActivo=false;
	public static boolean infowordleActivo=false;
	
	
	//intento de "reciclar" codigo para los botones del menu-----------------------------
	public void abrirVentanaJuego(String rutaFXML, String tituloVentana, ActionEvent e) {
	    try {
	        if (juegosPorNombre.containsKey(rutaFXML)) {
	            Stage juego = juegosPorNombre.get(rutaFXML);
	            if (juego.isShowing()) {
	                juego.toFront();//trae el juego al frente
	                juego.requestFocus();
	                return;//no abre otro
	            } else {
	            	//si está cerrado lo quitamos de las listas
	                juegosPorNombre.remove(rutaFXML);
	                juegosAbiertos.remove(juego);
	            }
	        }

	        Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
	        Scene scene = new Scene(root, 600, 400);
	        Stage ventana = new Stage();
	        ventana.setScene(scene);
	        ventana.setTitle(tituloVentana);
	        ventana.setResizable(false);
	        ventana.show();
	        //añadir los juegos abiertos
	        juegosAbiertos.add(ventana);
	        juegosPorNombre.put(rutaFXML, ventana);

	        //Si cerramos manualmente tambien quitamos de las listats
	        ventana.setOnCloseRequest(event -> {
	            juegosAbiertos.remove(ventana);
	            juegosPorNombre.remove(rutaFXML);
	        });

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}

	
	//lo que hacen los botones es llamar a la función de arriba y darle la ventana a abrir y el titulo de esta
	public void actionPescamines(ActionEvent e) {
		if(pescaminesActivo || midapescaminesActivo || rankingpescaminesActivo || carregarpescaminesActivo) {//si hay alguna ventana en true, no entra al juego
			ventanaAlert alerta = new ventanaAlert();
			alerta.alert("Impossible obrir la finestra", "Atenció! Ja tens aquest joc obert.", "file:imagenes/equis.png", 200);
			return;
		}else {
			abrirVentanaJuego("Tamany.fxml", "Pescamines", e);
			midapescaminesActivo=true;
		}
	   
	}
	
	public void actionJocDeLaVida(ActionEvent e) {
	    if(jocvidaActivo || midajocvidaActivo) {
	    	ventanaAlert alerta = new ventanaAlert();
			alerta.alert("Impossible obrir la finestra", "Atenció! Ja tens aquest joc obert.", "file:imagenes/equis.png", 200);
			return;
		}else {
			 abrirVentanaJuego("Dificultad.fxml", "Joc de la Vida", e);
			 midajocvidaActivo=true;
		}
	}

	public void actionWordle(ActionEvent e) {
	    if(wordleActivo || infowordleActivo) {
	    	ventanaAlert alerta = new ventanaAlert();
			alerta.alert("Impossible obrir la finestra", "Atenció! Ja tens aquest joc obert.", "file:imagenes/equis.png", 200);
			return;
		}else {
			abrirVentanaJuego("wordleLogin.fxml", "Wordle", e);
			infowordleActivo=true;
		}
	}

	public void actionPixelArt(ActionEvent e) {
	    if(midapixelartActivo || pixelartActivo) {
	    	ventanaAlert alerta = new ventanaAlert();
			alerta.alert("Impossible obrir la finestra", "Atenció! Ja tens aquest joc obert.", "file:imagenes/equis.png", 200);
			return;
		}else {
			abrirVentanaJuego("PixelArtIniciFXML.fxml", "Pixel Art", e);
			midapixelartActivo=true;
		}
	    
	}
	
	public void inicializarMusica() {
	    // Icono inicial
	    Image icon = new Image("file:imagenes/speakerMedio.png");
	    icono.setImage(icon);

	    // Música
	    Media media = new Media(new File("media/inicio.mp3").toURI().toString());
	    mediaPlayer = new MediaPlayer(media);
	    mediaPlayer.play();

	    // Establecer el valor inicial del slider
	    slider.setValue(50);

	    // Añadir listener para el volumen
	    slider.valueProperty().addListener((obs, oldVal, newVal) -> {
	        double valor = newVal.doubleValue();

	        // Cambiar icono según el volumen
	        if (valor == 0) {
	            icono.setImage(new Image("file:imagenes/mute.png"));
	        } else if (valor <= 30) {
	            icono.setImage(new Image("file:imagenes/speakerMin.png"));
	        } else if (valor <= 60){
	            icono.setImage(new Image("file:imagenes/speakerMedio.png"));
	        } else {
	            icono.setImage(new Image("file:imagenes/speaker.png"));
	        }

	        // Ajustar volumen
	        if (mediaPlayer != null) {
	            mediaPlayer.setVolume(valor / 100.0);
	        }
	    });
	}



	@FXML
	private void mostrarSlider() {
	    slider.setVisible(true);
	}

	@FXML
	private void ocultarSlider() {
	    slider.setVisible(false);
	}
	
	@FXML
	 private void mute() {
        if (slider.getValue() == 0) {
            slider.setValue(50);
            icono.setImage(new Image("file:imagenes/speakerMedio.png"));
        } else {
            slider.setValue(0);
            icono.setImage(new Image("file:imagenes/mute.png"));
        }
    }

	
	/*
	 * Debido a que los 4 botones del menu van a hacer el mismo trabajo y solo cambiara la ventana a la que se refieren
	 * para abrir, dejo esto comentado tipo copia de seguridad para intentar hacer todo desde un mismo metodo y así no
	 * tener 4 metodos iguales. 
	public void actionPescamines(ActionEvent e) {
		try {
			HBox root2 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene2= new Scene(root2);
			Stage window = new Stage();
			window.setScene(scene2);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
			
			//window.show();
			//en vez de mostrar directamente, vamos a hacer que al abrir la nueva ventana, esta quede inactiva
			// y que solo vuelva a estar activa si se cierra la nueva ventana que se ha abierto
			 
			
			//Hacemos que esta ventana sea MODAL
	        window.initModality(Modality.WINDOW_MODAL);

	        //Establecemos que la ventana "dueña" es la del menu(la que pasa a ser inactiva)
	        Stage menuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	        window.initOwner(menuStage);
	        //Mostramos la ventana y bloqueamos la del menu hasta que se cierre esta
	        window.showAndWait();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	*/

	
}
