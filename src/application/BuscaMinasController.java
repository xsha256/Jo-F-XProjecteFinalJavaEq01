package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BuscaMinasController implements Initializable {

	@FXML
	private VBox root;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			Stage window = (Stage) root.getScene().getWindow();
			String opcion = (String) window.getUserData();

			if (opcion.equals("petita")) {
				
			} else if (opcion.equals("media")) {
				
			} else {
				
			}
		});
	}

	public void canviaEscena(ActionEvent e) {

		try {
			VBox root2 = FXMLLoader.load(getClass().getResource("Tamaño.fxml"));
			Scene escena2 = new Scene(root2, 600, 500);
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Juego de la Vida");
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
