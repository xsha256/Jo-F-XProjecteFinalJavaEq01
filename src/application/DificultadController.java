package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	public void canviaEscena(ActionEvent e) {
		Toggle selectedToggle = opcionesGrupo.getSelectedToggle();
		if (selectedToggle != null) {
			RadioButton seleccionado = (RadioButton) selectedToggle;
			String opcion = seleccionado.getText();
			try {
				VBox root2 = FXMLLoader.load(getClass().getResource("JocVida.fxml"));
				
				Scene escena2 = new Scene(root2); //1000,800
				Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
				window.setUserData(opcion);
				window.setScene(escena2);
				window.setTitle("Juego de la Vida");
		        window.setMaximized(true);//lo abrimos en maximizado

				window.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			Alert alerta = new Alert(Alert.AlertType.WARNING);
			alerta.setTitle("Aviso");
			alerta.setHeaderText(null);
			alerta.setContentText("Por favor, selecciona una opción.");
			alerta.showAndWait();
		}
		

	}

}
