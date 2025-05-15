package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class JocVidaController implements Initializable {

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

	private final int FILAS = Tabla.LONG_MATRIZ;
	private final int COLUMNAS = Tabla.LONG_MATRIZ;

	private Label[][] etiquetas = new Label[FILAS][COLUMNAS];
	private Tabla tabla;
	private int tiempoEspera = 1000;
	private boolean continuar = true;
	
    private final Object lock = new Object();


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tabla = new Tabla();
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
				while (tabla.getGeneraciones() < Tabla.MAX_GEN) {
					synchronized (lock) {
                        while (!continuar) {
                            lock.wait();
                        }
                    }
					Thread.sleep(tiempoEspera);

					tabla.cambiarMatriz();

					Platform.runLater(() -> actualizar());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		hilo.setDaemon(true);
		hilo.start();
	}

	public void actualizar() {
		String[][] matriz = tabla.getMatriu_actual();

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
		for (int i = 0; i < FILAS; i++) {
			for (int j = 0; j < COLUMNAS; j++) {
				if ("*".equals(matriz[i][j])) {
					etiquetas[i][j].setStyle("-fx-background-color: red; -fx-border-color: black;");
				} else {
					etiquetas[i][j].setStyle("-fx-background-color: white; -fx-border-color: black;");
				}
			}
		}
	}

	public void pausar() {
		continuar = false;
	}

	public void reanudar() {
		continuar = true;
		// Despierta el hilo cuando se reanuda
		synchronized (lock) {
			lock.notify(); // Notifica al hilo que puede continuar
		}
	}

	public void acabar() {
		Platform.exit();
	}

	public void x2() {
		tiempoEspera = tiempoEspera / 2;
	}

	public void E2() {
		tiempoEspera = tiempoEspera * 2;
	}
}
