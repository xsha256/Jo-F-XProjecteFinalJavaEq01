package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController implements Initializable {
	
	//Recogemos los botones "jugar" que tiene cada juego
	@FXML private Button jugarPescamines;
	@FXML private Button jugarWordle;
	@FXML private Button jugarPixelArt;
	@FXML private Button jugarJocDeLaVida;
	
	@FXML private HBox root;
	
	//id de la variable para el nombre del usuario en el titulo e Bie
	@FXML private Label tituloNomUsuari;
	@FXML private ImageView imatgePerfilUsuari;
	
	//metodo que hace que se inicie 
	public void initialize(URL location, ResourceBundle resources) {
		String emailUsuario="";//poner nombre archivo.nombreVariable del login de Moha;
		
		try {
			// cargar el driver de MariaDB... con una vez sobra creo :) 
	        Class.forName("org.mariadb.jdbc.Driver");
	        
			//Conexion BBDD--------------------------------------------------------------
			String urlBaseDatos = "jdbc:mariadb://localhost:3306/jofxs";
			String usuario = "root";
			String contra = "";
			Connection c = DriverManager.getConnection(urlBaseDatos, usuario, contra);
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
	    String sentencia = "SELECT imagen FROM usuari WHERE email = ?";
	    
	    try {
	        PreparedStatement s = c.prepareStatement(sentencia);
	        s.setString(1, emailUsuari);
	        ResultSet r = s.executeQuery();

	        if (r.next()) {
	            InputStream input = r.getBinaryStream("imagen");
	            if (input != null) {
	                Image imagen = new Image(input);
	                imagenPerfil.setImage(imagen);//añade la imagen directamente
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	//intento de "reciclar" codigo para los botones del menu-----------------------------
	private void abrirVentanaJuego(String rutaFXML, String tituloVentana, ActionEvent e) {
	    try {
	        HBox root = FXMLLoader.load(getClass().getResource(rutaFXML));
	        Scene scene = new Scene(root);
	        Stage window = new Stage();
	        window.setScene(scene);
	        window.setTitle(tituloVentana);
	        window.setMaximized(true);

	        window.initModality(Modality.WINDOW_MODAL);
	        Stage menuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	        window.initOwner(menuStage);
	        window.showAndWait();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}

	
	//lo que hacen los botones es llamar a la función de arriba y darle la ventana a abrir y el titulo de esta
	public void actionPescamines(ActionEvent e) {
	    abrirVentanaJuego("pescamines.fxml", "Pescamines", e);
	}

	public void actionWordle(ActionEvent e) {
	    abrirVentanaJuego("wordle.fxml", "Wordle", e);
	}

	public void actionPixelArt(ActionEvent e) {
	    abrirVentanaJuego("pixelart.fxml", "Pixel Art", e);
	}

	public void actionJocDeLaVida(ActionEvent e) {
	    abrirVentanaJuego("jocDeLaVida.fxml", "Joc de la Vida", e);
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
