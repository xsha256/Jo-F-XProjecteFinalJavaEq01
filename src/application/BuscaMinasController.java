package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BuscaMinasController implements Initializable {

	@FXML
	private VBox root;
	@FXML
	private GridPane gridPane;
	@FXML
	private Text textoMinas;
	@FXML
	private Text casillasAbiertas;
	@FXML
	private Text tiempo;

	private int FILAS;
	private int banderasPuestas;
	private int banderasRestantes;
	private int COLUMNAS;
	private Label[][] etiquetas;
	private Tablero tablero;
	private int segundos;
	private int minutos;
	private Timeline timeline;

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
						timeline.stop();
						mostrarMinas();
						Alert alerta = new Alert(Alert.AlertType.WARNING);
						alerta.setTitle("Aviso");
						alerta.setHeaderText(null);
						alerta.setContentText("Has explotado una mina");
						alerta.showAndWait();
						canviaEscena();
					} else {
						liberarCuadrados(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
						String[] aux = casillasAbiertas.getText().split(":");
						casillasAbiertas.setText(aux[0] + ": " + contarCuadrados() + "/"
								+ (tablero.getLongitudHorizontal() * tablero.getLongitudVertical()));
						casillasAbiertas.setStyle("-fx-font-weight: bold;");
					}
				} else if (e.getButton() == MouseButton.SECONDARY) {
					if (((Label) node).getGraphic() != null) {
						((Label) node).setGraphic(null);
						banderasPuestas--;
						banderasRestantes++;
						textoMinas.setText("" + banderasRestantes);
						textoMinas.setStyle("-fx-font-weight: bold;");
					} else {
						try {
							if (banderasPuestas == tablero.getMinas()) {
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
								banderasPuestas++;
								banderasRestantes--;
								textoMinas.setText("" + banderasRestantes);
								textoMinas.setStyle("-fx-font-weight: bold;");
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
				tablero = new Tablero(10, 10, 12);
			} else if (opcion.equals("media")) {
				tablero = new Tablero(18, 18, 45);
			} else {
				tablero = new Tablero(25, 25, 110);
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
			banderasRestantes = tablero.getMinas();
			textoMinas.setText("" + banderasRestantes);
			textoMinas.setStyle("-fx-font-weight: bold;");
			casillasAbiertas.setText(casillasAbiertas.getText() + " 0/"
					+ (tablero.getLongitudHorizontal() * tablero.getLongitudVertical()));
			casillasAbiertas.setStyle("-fx-font-weight: bold;");
				String[] aux = tiempo.getText().split(":");
				timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
					if (segundos == 59) {
						segundos = 0;
						minutos++;
					} else {
						segundos++;
					}
					if (minutos < 10) {
						if (segundos < 10) {
							tiempo.setText(aux[0] + ": 0" + minutos + ":0" + segundos);
							tiempo.setStyle("-fx-font-weight: bold;");
						} else {
							tiempo.setText(aux[0] + ": 0" + minutos + ":" + segundos);
							tiempo.setStyle("-fx-font-weight: bold;");
						}
					} else {
						if (segundos < 10) {
							tiempo.setText(aux[0] + ": " + minutos + ":0" + segundos);
							tiempo.setStyle("-fx-font-weight: bold;");
						} else {
							tiempo.setText(aux[0] + ": " + minutos + ":" + segundos);
							tiempo.setStyle("-fx-font-weight: bold;");
						}
					}
				}));
				timeline.setCycleCount(Timeline.INDEFINITE);
		        timeline.play();
				
			
			;
		});
	}

	public void canviaEscena() {

		try {
			VBox root2 = FXMLLoader.load(getClass().getResource("Tamaño.fxml"));
			Scene escena2 = new Scene(root2, 600, 500);
			Stage window = (Stage) root.getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Juego de la Vida");
			window.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void mostrarMinas() {
		String[][] matriz_abajo = tablero.getMatriz_abajo();
		for (int i = 0; i < FILAS; i++) {
			for (int j = 0; j < COLUMNAS; j++) {
				if (matriz_abajo[i][j].equals("x")) {
					Image bandera1;
					try {
						bandera1 = new Image(new FileInputStream("imagenes/bomba.png"));
						ImageView bandera = new ImageView(bandera1);
						bandera.setFitWidth(17);
						bandera.setFitHeight(17);

						bandera.setPreserveRatio(true);
						etiquetas[i][j].setGraphic(bandera);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					
					}
			}
		}
	}

	public void liberarCuadrados(int x, int y) {
		String[][] matriz_abajo = tablero.getMatriz_abajo();
		if (matriz_abajo[x][y].equals("0")) {
			etiquetas[x][y].setStyle("-fx-background-color: white;-fx-border-color: black;");
			etiquetas[x][y].setId("noMinaVista");
			comprobarVecinas(x, y);
		} else if (!matriz_abajo[x][y].equals("x")) {
			etiquetas[x][y].setStyle("-fx-background-color: white;-fx-border-color: black;");
			etiquetas[x][y].setText(matriz_abajo[x][y]);
			etiquetas[x][y].setId("noMinaVista");
		}
		if (contarCuadrados() == ((tablero.getLongitudHorizontal() * tablero.getLongitudVertical())
				- tablero.getMinas())) {
			timeline.stop();
			Alert alerta = new Alert(Alert.AlertType.WARNING);
			alerta.setTitle("Aviso");
			alerta.setHeaderText(null);
			alerta.setContentText("Has ganado, Felicidades!!!");
			alerta.showAndWait();
			guardarPartida();
			canviaEscena();
		}

	}

	public void comprobarVecinas(int x, int y) {
		String[][] matriz_abajo = tablero.getMatriz_abajo();
		int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };

		for (int i = 0; i < 8; i++) {
			int ni = x + dx[i];
			int nj = y + dy[i];

			if (ni >= 0 && ni < FILAS && nj >= 0 && nj < COLUMNAS) {
				if (!etiquetas[ni][nj].getId().equals("noMinaVista")) {
					if (matriz_abajo[ni][nj].equals("0")) {
						liberarCuadrados(ni, nj);
					} else if (!matriz_abajo[ni][nj].equals("x")) {
						etiquetas[ni][nj].setStyle("-fx-background-color: white;-fx-border-color: black;");
						etiquetas[ni][nj].setText(matriz_abajo[ni][nj]);
						etiquetas[ni][nj].setId("noMinaVista");
					}
				}
			}
		}
	}

	public int contarCuadrados() {
		int contador = 0;
		for (int i = 0; i < FILAS; i++) {
			for (int j = 0; j < COLUMNAS; j++) {
				if (etiquetas[i][j].getId().equals("noMinaVista")) {
					contador++;
				}
			}
		}

		return contador;
	}

	public void guardarPartida() {
		
	}
}
