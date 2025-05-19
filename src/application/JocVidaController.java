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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JocVidaController implements Initializable {

	@FXML
	private VBox root;
	@FXML
	private GridPane gridPane;
	@FXML
	private Text textoGeneraciones;
	@FXML
	private Text textoTotal;
	@FXML
	private Text textoVivas;
	@FXML
	private Text textoMuertas;
	@FXML
	private Button botonPausa;
	@FXML
	private Button botonReanudar;
	@FXML
	private Button botonStop;
	@FXML
	private Button botonX2;
	@FXML
	private Button botonE2;

	private int FILAS;
	private int COLUMNAS;
	private Label[][] etiquetas;
	private Tabla tabla;

	private int tiempoEspera = 1000;
	private boolean continuar = true;

	private final Object lock = new Object();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {
			Stage window = (Stage) root.getScene().getWindow();
			String opcion = (String) window.getUserData();

			if (opcion.equals("Petita")) {
				tabla = new Tabla(10, 200, 25, 50);
			} else if (opcion.equals("Mitjana")) {
				tabla = new Tabla(20, 400, 175, 200);
			} else {
				tabla = new Tabla(30, 600, 450, 500);
			}

			FILAS = tabla.getLongMatriz();
			COLUMNAS = tabla.getLongMatriz();

			etiquetas = new Label[FILAS][COLUMNAS];

			gridPane.getChildren().clear();

			tabla.llenarMatrizInicial();

			for (int i = 0; i < FILAS; i++) {
				for (int j = 0; j < COLUMNAS; j++) {
					Label label = new Label();
					label.setPrefSize(20, 20);
					label.setStyle("-fx-border-color: black; -fx-alignment: center;");
					etiquetas[i][j] = label;
					gridPane.add(label, j, i);
				}
			}

			actualizar();

			Thread hilo = new Thread(() -> {
				try {
					while (tabla.getGeneraciones() < tabla.getMaxGen()) {
						synchronized (lock) {
							while (!continuar) {
								lock.wait();
							}
						}
						Thread.sleep(tiempoEspera);

						tabla.cambiarMatriz();

						Platform.runLater(() -> actualizar());
					}
					if (tabla.getGeneraciones() == tabla.getMaxGen()) {
						Platform.runLater(() -> {
							Alert alerta = new Alert(Alert.AlertType.INFORMATION);
							alerta.setTitle("Aviso");
							alerta.setHeaderText(null);
							alerta.setContentText("Has llegado a la maxima generacion, Felicidades!!!");
							alerta.showAndWait();
						});
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			hilo.setDaemon(true);
			hilo.start();
		});
	}

	public void actualizar() {

		int generaciones = tabla.getGeneraciones();
		String[] aux = textoGeneraciones.getText().split(":");
		textoGeneraciones.setText(aux[0] + ": " + generaciones);

		int totales = tabla.getContadorCreadas();
		aux = textoTotal.getText().split(":");
		textoTotal.setText(aux[0] + ": " + totales);

		int vivas = tabla.contarCelulas();
		aux = textoVivas.getText().split(":");
		textoVivas.setText(aux[0] + ": " + vivas);

		int muertas = tabla.getContadorMuertes();
		aux = textoMuertas.getText().split(":");
		textoMuertas.setText(aux[0] + ": " + muertas);

		String[][] estados = tabla.getEstados();

		for (int i = 0; i < FILAS; i++) {
			for (int j = 0; j < COLUMNAS; j++) {
				String estado = estados[i][j];
				if (estado == null) {
					etiquetas[i][j].setStyle("-fx-background-color:white; -fx-border-color: black;");
					continue;
				}
				switch (estados[i][j]) {
				case "viva":
					etiquetas[i][j].setStyle("-fx-background-color:green; -fx-border-color: black;");
					break;
				case "recienNacida":
					etiquetas[i][j].setStyle("-fx-background-color:lightgreen; -fx-border-color: black;");
					break;
				case "muerta":
					etiquetas[i][j].setStyle("-fx-background-color:white; -fx-border-color: black;");
					break;
				case "recienMuerta":
					etiquetas[i][j].setStyle("-fx-background-color:gray; -fx-border-color: black;");
					break;
				}

			}
		}
	}

	public void pausar() {
		continuar = false;
	}

	public void reanudar() {
		continuar = true;
		synchronized (lock) {
			lock.notify();
		}
	}

	public void acabar(ActionEvent e) {
		try {
			tabla.reiniciar();
			VBox root2 = FXMLLoader.load(getClass().getResource("Dificultad.fxml"));
			Scene escena2 = new Scene(root2, 600, 500);
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Eleccion de Dificultad");
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void x2() {
		tiempoEspera = tiempoEspera / 2;
	}

	public void E2() {
		tiempoEspera = tiempoEspera * 2;
	}
}
