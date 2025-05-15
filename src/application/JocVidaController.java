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

	        // Crear la tabla con parámetros según dificultad
	        if (opcion.equals("Pequeño")) {
	            tabla = new Tabla(10, 200, 25, 50);
	        } else if (opcion.equals("Mediano")) {
	            tabla = new Tabla(15, 300, 75, 100);
	        } else {
	            tabla = new Tabla(20, 400, 100, 150);
	        }

	        FILAS = tabla.getLongMatriz();
	        COLUMNAS = tabla.getLongMatriz();

	        // Inicializar matriz de etiquetas
	        etiquetas = new Label[FILAS][COLUMNAS];

	        // Limpiar gridPane para evitar superposición de nodos
	        gridPane.getChildren().clear();

	        // Inicializar matriz inicial con células
	        tabla.llenarMatrizInicial();

	        // Crear etiquetas y agregarlas al gridPane
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

	        // Crear y arrancar hilo que actualiza el juego
	        Thread hilo = new Thread(() -> {
	            try {
	                while (tabla.getGeneraciones() < tabla.getMaxGen() && (tabla.contarCelulas() != 0)) {
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
	    });}

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
		synchronized (lock) {
			lock.notify();
		}
	}

	public void acabar(ActionEvent e) {
		try {
			tabla.reiniciar();
			 VBox root2 = FXMLLoader.load(getClass().getResource("Dificultad.fxml"));
			 Scene escena2 = new Scene(root2,1000,1000);
			 Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			 window.setScene(escena2);
			 window.setTitle("Eleccion de Dificultad");
			 window.show();
			 } catch (IOException e1) {
			 e1.printStackTrace();
			 }	}

	public void x2() {
		tiempoEspera = tiempoEspera / 2;
	}

	public void E2() {
		tiempoEspera = tiempoEspera * 2;
	}
}
