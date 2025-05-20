package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BuscaMinasController implements Initializable {

	@FXML
	private VBox root;
	@FXML
	private GridPane gridPane;

	private int FILAS;
	private int banderas;
	private int COLUMNAS;
	private Label[][] etiquetas;
	private Tablero tablero;
	EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent e) {
			Node node = (Node) e.getTarget();
			while (node != null && !(node instanceof Label)) {
				node = node.getParent();
			}

			if (node instanceof Label) {
				if (e.getButton() == MouseButton.PRIMARY) {
					if (node.getId().equals("mina")) {
						Alert alerta = new Alert(Alert.AlertType.WARNING);
						alerta.setTitle("Aviso");
						alerta.setHeaderText(null);
						alerta.setContentText("Has explotado una mina");
						alerta.showAndWait();

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

					} else {
					}
				} else if (e.getButton() == MouseButton.SECONDARY) {
					if (((Label) node).getGraphic() != null) {
						((Label) node).setGraphic(null);
						banderas--;
					} else {
						try {
							if (banderas == tablero.getMinas()) {
								Alert alerta = new Alert(Alert.AlertType.WARNING);
								alerta.setTitle("Aviso");
								alerta.setHeaderText(null);
								alerta.setContentText("No puedes poner mas banderas");
								alerta.showAndWait();
							} else {
								Image bandera1 = new Image(new FileInputStream("imagenes/bandera.png"));
								ImageView bandera = new ImageView(bandera1);
								bandera.setFitWidth(17);
								bandera.setFitHeight(17);

								bandera.setPreserveRatio(true);
								((Label) node).setGraphic(bandera);
								banderas++;
							}

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}

				}
			}
		}

	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			Stage window = (Stage) root.getScene().getWindow();
			String opcion = (String) window.getUserData();

			if (opcion.equals("petita")) {
				tablero = new Tablero(10, 10, 10);
			} else if (opcion.equals("media")) {
				tablero = new Tablero(18, 18, 40);
			} else {
				tablero = new Tablero(25, 25, 99);
			}

			FILAS = tablero.getLongitudHorizontal();
			COLUMNAS = tablero.getLongitudVertical();

			etiquetas = new Label[FILAS][COLUMNAS];

			gridPane.getChildren().clear();

			tablero.llenarMinas();
			tablero.asignarNumeros();
			String[][] matriz_arriba = tablero.getMatriz_arriba();
			String[][] matriz_abajo = tablero.getMatriz_abajo();

			for (int i = 0; i < FILAS; i++) {
				for (int j = 0; j < COLUMNAS; j++) {
					Label label = new Label();
					label.setPrefSize(20, 20);
					label.setStyle("-fx-border-color: white; -fx-alignment: center;");
					label.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

					if (matriz_arriba[i][j].equals("*")) {
						label.setStyle("-fx-background-color: green;-fx-border-color: black;");
					} else if (matriz_arriba[i][j].equals("+")) {
						label.setStyle("-fx-background-color: lightgreen;-fx-border-color: black;");
					}
					if (matriz_abajo[i][j].equals("x")) {
						label.setId("mina");
					} else {
						label.setId("noMina");
					}
					etiquetas[i][j] = label;
					gridPane.add(label, j, i);
				}
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
