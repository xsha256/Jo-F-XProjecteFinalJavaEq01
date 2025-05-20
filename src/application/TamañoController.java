package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TamañoController {
	
	@FXML ToggleGroup opcionesGrupo;
	
	public void canviaEscena(ActionEvent e) {
        Toggle selectedToggle = opcionesGrupo.getSelectedToggle();
        if (selectedToggle != null) {
            RadioButton seleccionado = (RadioButton) selectedToggle;
            String opcion = seleccionado.getId();
            try {
				VBox root2 = FXMLLoader.load(getClass().getResource("BuscaMinas.fxml"));
				
				Scene escena2 = new Scene(root2, 1000, 800);
				Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
				window.setUserData(opcion);
				window.setScene(escena2);
				window.setTitle("PescaMines");
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
