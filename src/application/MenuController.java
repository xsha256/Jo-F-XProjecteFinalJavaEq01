package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	
	//metodo que hace que se inicie 
	public void initialize(URL location, ResourceBundle resources) {
		//String nomUsuari=//poner nombre archivo.nombreVariable del login de Moha;
		String nom= "Yordan";
		//Conexion BBDD
		String urlBaseDatos = "jdbc:mariadb://localhost:3306/jofxs";
		String usuario = "root";
		String contra = "";
		String sentencia = "SELECT imatge FROM usuari WHERE email ='"+LoginController.EMAIL+"'";
		
		try {
			// cargar el driver de MariaDb
			Class.forName("org.mariadb.jdbc.Driver");
			Connection c = DriverManager.getConnection(urlBaseDatos, usuario, contra);
			Statement s = c.createStatement();
			s.executeUpdate(sentencia);
		}catch(ClassNotFoundException | SQLException e){
			
		}
		
		
		tituloNomUsuari.setText("A que t'apeteix jugar hui, "+nom +"?");
	}
	
	//Ponemos el "onAction" de cada botón jugar al ser pulsado
	public void actionPescamines(ActionEvent e) { //boton jugar Pescamines
		try {
			HBox root2 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene2= new Scene(root2);
			Stage window = new Stage();//(Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene2);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
//			window.show();
			/*en vez de mostrar directamente, vamos a hacer que al abrir la nueva ventana, esta quede inactiva
			 * y que solo vuelva a estar activa si se cierra la nueva ventana que se ha abierto
			 */
			
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
	
	
	public void actionWordle(ActionEvent e) {//boton jugar Wordle
		try {
			HBox root2 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene2= new Scene(root2);
			Stage window = new Stage();//(Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene2);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
//			window.show();
			/*en vez de mostrar directamente, vamos a hacer que al abrir la nueva ventana, esta quede inactiva
			 * y que solo vuelva a estar activa si se cierra la nueva ventana que se ha abierto
			 */
			
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
	
	public void actionPixelArt(ActionEvent e) {//boton jugar Pixel Art
		try {
			HBox root2 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene2= new Scene(root2);
			Stage window = new Stage();//(Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene2);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
//			window.show();
			/*en vez de mostrar directamente, vamos a hacer que al abrir la nueva ventana, esta quede inactiva
			 * y que solo vuelva a estar activa si se cierra la nueva ventana que se ha abierto
			 */
			
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
	
	public void actionJocDeLaVida(ActionEvent e) {//boton jugar Joc de la Vida
		try {
			HBox root2 = (HBox)FXMLLoader.load(getClass().getResource("ventanaNueva.fxml"));
			Scene scene2= new Scene(root2);
			Stage window = new Stage();//(Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene2);
			window.setTitle("VentanaNueva");
			window.setMaximized(true);
//			window.show();
			/*en vez de mostrar directamente, vamos a hacer que al abrir la nueva ventana, esta quede inactiva
			 * y que solo vuelva a estar activa si se cierra la nueva ventana que se ha abierto
			 */
			
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
	
	
	
	
}
