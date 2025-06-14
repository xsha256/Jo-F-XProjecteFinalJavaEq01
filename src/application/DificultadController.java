package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DificultadController implements Initializable {

	@FXML
	Button play;
	@FXML
	private VBox root;
	@FXML
	private ToggleGroup opcionesGrupo;
	@FXML
	private Button botoEnrere;
	@FXML Button jugar;
	
	//Variables para guardar estilos originales
    private String jugarStyleOriginal;
    private String botoEnrereStyleOriginal;
	
	
	public void anarEnrere(ActionEvent e) {
		//cerrar la ventana actual primero y luego abrir la de abajo como ventana NUEVA
		Stage ventanaActual = (Stage) ((Node) e.getSource()).getScene().getWindow();
        ventanaActual.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//funcion que cambia el estado de los booleans para poder duplicados abiertos del mismo juego
		Platform.runLater(()->{
			Stage ventanaActual = (Stage) root.getScene().getWindow();
			if(ventanaActual.isShowing()) {
				MenuController.midajocvidaActivo=true;
				System.out.println("La ventana mida-jocVida esta activa. Boolean: "+MenuController.midajocvidaActivo);
				System.out.println("BooleanJuegoActivo: "+MenuController.jocvidaActivo);
			}
			ventanaActual.setOnHidden(evt ->{
				MenuController.midajocvidaActivo=false;
				System.out.println("La ventana mida-jocVida se cerró. Boolean: "+MenuController.midajocvidaActivo);
				System.out.println("BooleanJuegoActivo: "+MenuController.jocvidaActivo);
			});
		});
		
		//Guardamos estilos originales para poder restaurarlos luego
//        jugarStyleOriginal = jugar.getStyle();
//        botoEnrereStyleOriginal = botoEnrere.getStyle();
//        
//
//			//boton jugar
//		 	jugar.setOnMouseEntered(e -> {
//		        jugar.setStyle(jugarStyleOriginal +"; -fx-background-color: #159a91; -fx-cursor: hand;");
//		        jugar.setScaleX(1.1); 
//		        jugar.setScaleY(1.1);
//		    });
//		    jugar.setOnMouseExited(e -> {
//		        jugar.setStyle(jugarStyleOriginal+ "; -fx-cursor: default;"); // vuelve al estilo original
//		        jugar.setScaleX(1);
//		        jugar.setScaleY(1);
//		    });
//
//		    //boton botoEnrere
//		    botoEnrere.setOnMouseEntered(e -> {
//		        botoEnrere.setStyle(jugarStyleOriginal +"; -fx-background-color: #53808c; -fx-cursor: hand;");
//		        botoEnrere.setScaleX(1.1);
//		        botoEnrere.setScaleY(1.1);
//		    });
//		    botoEnrere.setOnMouseExited(e -> {
//		        botoEnrere.setStyle(botoEnrereStyleOriginal+ "; -fx-cursor: default;");
//		        botoEnrere.setScaleX(1);
//		        botoEnrere.setScaleY(1);
//		    });
	}

	public void canviaEscena(ActionEvent e) {
		Toggle selectedToggle = opcionesGrupo.getSelectedToggle();
		if (selectedToggle != null) {
			RadioButton seleccionado = (RadioButton) selectedToggle;
			String opcion = seleccionado.getText();
			try {

				Stage ventanaActual =  (Stage) ((Node) e.getSource()).getScene().getWindow();
				ventanaActual.close();
				
				VBox root2 = FXMLLoader.load(getClass().getResource("JocVida.fxml"));
				String rutaFXML= "JocVida.fxml";
				Scene escena2 = new Scene(root2);
				escena2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

				Stage window = new Stage(); //(Stage) ((Node) e.getSource()).getScene().getWindow();

				window.setUserData(opcion);
				window.setScene(escena2);
				window.setTitle("Joc de la Vida");
		        window.setMaximized(true);//lo abrimos en maximizado
		        
				window.show();
				
				//añadir los juegos abiertos
		        MenuController.juegosAbiertos.add(window);
		        MenuController.juegosPorNombre.put(rutaFXML, window);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else {
			ventanaAlert alerta = new ventanaAlert();
			alerta.alert("Atenció ","Per favor, selecciona una opció.", "file:imagenes/alerta.png", 100);
//			alerta.showAndWait();
		}
		

	}

}
