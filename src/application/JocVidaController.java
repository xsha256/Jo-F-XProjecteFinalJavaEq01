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
import javafx.scene.Parent;
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

	private final int MAX_HISTORIAL = 10;
	private String[][][] historial = new String[MAX_HISTORIAL][][];
	private int numEstadosGuardados = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//funcion que cambia el estado de los booleans para poder duplicados abiertos del mismo juego
		Platform.runLater(()->{
			Stage ventanaActual = (Stage) root.getScene().getWindow();
			if(ventanaActual.isShowing()) {
				MenuController.jocvidaActivo=true;
				System.out.println("El juego jocVida esta activa. Boolean: "+MenuController.jocvidaActivo);
			}
			ventanaActual.setOnHidden(evt ->{
				MenuController.jocvidaActivo=false;
				System.out.println("El juego jocVida se cerró. Boolean: "+MenuController.jocvidaActivo);
				
			});
		});

		Platform.runLater(() -> {
			Stage window = (Stage) root.getScene().getWindow();
			
			//SOLUCIÓN A QUE EL JUEGO CONTINUE EN SEGUNDO PLANO UNA VEZ CERRADA LA PANTALLA
		    window.setOnCloseRequest(event -> {
		        continuar = false;
		        synchronized (lock) {
		            lock.notify(); //por si sse queda esperando
		        }
		    });
		    
		    //----------------------------------------------------------------------------
			String opcion = (String) window.getUserData();

			if (opcion.equals("Petita")) {
				tabla = new Tabla(10, 30, 40);
			} else if (opcion.equals("Mitjana")) {
				tabla = new Tabla(20, 75, 100);
			} else {
				tabla = new Tabla(28, 200, 300);// 30,200,300 <-- Tamaño original, pero no se ven los botones con ese tamaño
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
					while (true) {
						synchronized (lock) {
							while (!continuar) {
								lock.wait();
							}
						}
						Thread.sleep(tiempoEspera);

						tabla.cambiarMatriz();

						Platform.runLater(() -> actualizar());

						if (!detectarBucle()) {
							continuar = false;
						}
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
				switch (estado) {
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
			continuar=false;
			tabla.reiniciar();

			try {
				// cerrar la ventana actual primero y luego abrir la de abajo como ventana NUEVA
				Stage ventanaActual = (Stage) ((Node) e.getSource()).getScene().getWindow();
				ventanaActual.close();

				Parent root = FXMLLoader.load(getClass().getResource("Dificultad.fxml"));
				Scene scene = new Scene(root, 600, 400);// ponemos la medida ya que es una ventana con poca información
														// anchoXalto
				String rutaFXML="Dificultad.fxml";
				Stage window = new Stage();
				window.setScene(scene);
				window.setTitle("Elecció Dificultat");
				window.show();
				//añadir los juegos abiertos
		        MenuController.juegosAbiertos.add(window);
		        MenuController.juegosPorNombre.put(rutaFXML, window);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			/*
			 * //cerrar la ventana actual primero y luego abrir la de abajo como ventana
			 * NUEVA
			 * Stage ventanaActual = (Stage) ((Node) e.getSource()).getScene().getWindow();
			 * ventanaActual.close();
			 * 
			 * Parent root2 = FXMLLoader.load(getClass().getResource("Dificultad.fxml"));
			 * Scene escena2 = new Scene(root2,600,500);
			 * Stage window = new Stage();
			 * window.setScene(escena2);
			 * window.setTitle("Elecció de Dificultat");
			 * window.show();
			 */

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public void reiniciar(ActionEvent e) {
		try {
			continuar = false;
			tabla.reiniciar();
			// cerrar la ventana actual primero y luego abrir la de abajo como ventana NUEVA
			Stage ventanaActual = (Stage) ((Node) e.getSource()).getScene().getWindow();
			ventanaActual.close();

			Parent root2 = FXMLLoader.load(getClass().getResource("JocVida.fxml"));
			Scene escena2 = new Scene(root2);
			String rutaFXML="JocVida.fxml";
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Joc De La Vida");
			window.show();
			//añadir los juegos abiertos
	        MenuController.juegosAbiertos.add(window);
	        MenuController.juegosPorNombre.put(rutaFXML, window);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void x2() {
		tiempoEspera = Math.max(1, tiempoEspera / 2);
	}

	public void E2() {
		tiempoEspera = tiempoEspera * 2;
	}

	private boolean detectarBucle() {
		String[][] actual = tabla.getMatriu_actual();

		if (numEstadosGuardados < MAX_HISTORIAL) {
			historial[numEstadosGuardados] = copiarMatriz(actual);
			numEstadosGuardados++;
			return true;
		} else {
			for (int i = 0; i < MAX_HISTORIAL - 1; i++) {
				historial[i] = historial[i + 1];
			}
			historial[MAX_HISTORIAL - 1] = copiarMatriz(actual);
		}

		for (int i = 1; i <= MAX_HISTORIAL / 2; i++) {
			boolean bucle = true;

			for (int j = 0; j < MAX_HISTORIAL - i; j += i) {
				for (int k = 0; k < i; k++) {
					if (!matricesIguales(historial[j + k], historial[j + i + k])) {
						bucle = false;
						break;
					}
				}
				if (bucle) {
					continuar=false;
					if (tabla.contarCelulas() == 0) {
						Platform.runLater(() -> {
							// llamada a la alerta
							int aux = tabla.getGeneraciones();
							ventanaAlert alerta = new ventanaAlert();
							alerta.alert("Bucle trobat",
									"S'han mort totes les cel·lules. Has arribat fins la generació " + aux,
									"file:imagenes/alerta.png", 100);
						});
					} else {
						Platform.runLater(() -> {
							// llamada a la alerta
							int aux = tabla.getGeneraciones();
							ventanaAlert alerta = new ventanaAlert();
							alerta.alert("Bucle trobat ",
									"El joc ha entrat en bucle. Has arribat fins a la generació " + aux,
									"file:imagenes/alerta.png", 100);
						});
					}
					return false;
				}
				bucle = true;
			}
		}

		return true;
	}

	private boolean matricesIguales(String[][] m1, String[][] m2) {
		for (int i = 0; i < FILAS; i++) {
			for (int j = 0; j < COLUMNAS; j++) {
				String s1 = m1[i][j];
				String s2 = m2[i][j];
				if (s1 == null && s2 == null) {
					continue;
				}
				if (s1 == null || s2 == null) {
					return false;
				}
				if (!s1.equals(s2)) {
					return false;
				}
			}
		}
		return true;
	}

	private String[][] copiarMatriz(String[][] actual) {
		if (actual == null) {
			return null;
		}
		String[][] copia = new String[FILAS][COLUMNAS];
		for (int i = 0; i < FILAS; i++) {
			for (int j = 0; j < COLUMNAS; j++) {
				copia[i][j] = actual[i][j];
			}
		}
		return copia;
	}
}
