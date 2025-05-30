package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	private String opcion;
	private Timeline timeline;
	private Object data;
	private int id;
	private boolean partidaCargada = false;

	private int clicks = 0;
	private boolean cargar = false;
	private static Connection c = ConexionBBDD.conectar();
	private static int idUsuari = 0;
	
	//recoger idUsuario y guardarlo
	public static String emailMoha=LoginController.EMAIL;
	
	public void recogerIdUsuario(String emailUsuario) {
		try {
			String sentencia = "SELECT id FROM usuari where email = ?";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, emailUsuario);
			ResultSet rs = s.executeQuery(sentencia);
			idUsuari = rs.getInt("id");
		} catch (  SQLException e) {
			e.printStackTrace();
		}
	}
	
	EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
		
		
		
		@Override
		public void handle(MouseEvent e) {
			Node node = (Node) e.getTarget();
			while (node != null && !(node instanceof Label)) {
				node = node.getParent();
			}
			if (node instanceof Label) {
				if (e.getButton() == MouseButton.PRIMARY) {
					if (((Label) node).getGraphic() == null) {
						if (clicks == 0) {
							if (!cargar) {
								tablero.llenarMinasIniciales(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
								tablero.asignarNumeros();
								String[][] matriz_abajo = tablero.getMatriz_abajo();
								for (int i = 0; i < tablero.getLongitudHorizontal(); i++) {
									for (int j = 0; j < tablero.getLongitudHorizontal(); j++) {
										if (matriz_abajo[i][j].equals("x")) {
											etiquetas[i][j].setId("mina");
										} else {
											etiquetas[i][j].setId("noMina");
										}
									}
								}
							}
							liberarCuadrados(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
							String[] aux = casillasAbiertas.getText().split(":");
							casillasAbiertas.setText(aux[0] + ": " + contarCuadrados() + "/"
									+ (tablero.getLongitudHorizontal() * tablero.getLongitudVertical()));
							casillasAbiertas.setStyle("-fx-font-weight: bold;");
							clicks++;
						} else {
							if (node.getId().equals("mina")) {
								timeline.stop();
								mostrarMinas();
							
								ventanaAlert alerta = new ventanaAlert();
								alerta.alert("Game Over","Has esclatat una mina!", "file:imagenes/boom.png", 300);
								
								if (partidaCargada) {
									System.out.println("Perder");
									try {
										String sentencia = "DELETE FROM pescaMines where id = ?";
										PreparedStatement s = c.prepareStatement(sentencia);
										s.setInt(1, id);
										s.executeUpdate();
									} catch ( SQLException e1) {
										e1.printStackTrace();
									}
									partidaCargada = false;
								}
								canviaEscena();
							} else {
								liberarCuadrados(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
								String[] aux = casillasAbiertas.getText().split(":");
								casillasAbiertas.setText(aux[0] + ": " + contarCuadrados() + "/"
										+ (tablero.getLongitudHorizontal() * tablero.getLongitudVertical()));
								casillasAbiertas.setStyle("-fx-font-weight: bold;");
							}
						}
					}
				} else if (e.getButton() == MouseButton.SECONDARY) {
					if (clicks == 0) {
						if (!cargar) {
							tablero.llenarMinasIniciales(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
							tablero.asignarNumeros();
							String[][] matriz_abajo = tablero.getMatriz_abajo();
							for (int i = 0; i < tablero.getLongitudHorizontal(); i++) {
								for (int j = 0; j < tablero.getLongitudHorizontal(); j++) {
									if (matriz_abajo[i][j].equals("x")) {
										etiquetas[i][j].setId("mina");
									} else {
										etiquetas[i][j].setId("noMina");
									}
								}
							}
						}
						Image bandera1;
						try {
							bandera1 = new Image(new FileInputStream("imagenes/bandera.png"));
							ImageView bandera = new ImageView(bandera1);
							bandera.setFitWidth(17);
							bandera.setFitHeight(17);

							bandera.setPreserveRatio(true);
							((Label) node).setGraphic(bandera);
							clicks++;
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						
					}else {
						if (!node.getId().equals("noMinaVista")
								&& (node.getId().equals("noMina") || node.getId() == null || node.getId().equals("mina"))) {
							if (((Label) node).getGraphic() != null) {
								((Label) node).setGraphic(null);
								banderasPuestas--;
								banderasRestantes++;
								textoMinas.setText("" + banderasRestantes);
								textoMinas.setStyle("-fx-font-weight: bold;");
							} else {
								try {
									if (banderasPuestas == tablero.getMinas()) {
										ventanaAlert alerta = new ventanaAlert();
										alerta.alert("Atenció","No pots posar més banderes", "file:imagenes/equis.png", 300);
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
			}
		}

	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			Stage window = (Stage) root.getScene().getWindow();
			data = window.getUserData();

			if (data instanceof String) {
				opcion = (String) data;
				if (opcion.equals("petita")) {
					tablero = new Tablero(10, 10, 12);
				} else if (opcion.equals("media")) {
					tablero = new Tablero(18, 18, 45);
				} else {
					tablero = new Tablero(25, 25, 100);
				}

				FILAS = tablero.getLongitudHorizontal();
				COLUMNAS = tablero.getLongitudVertical();

				etiquetas = new Label[FILAS][COLUMNAS];

				gridPane.getChildren().clear();
				tablero.asignarColores();
				String[][] matriz_arriba = tablero.getMatriz_arriba();

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

			} else if (data instanceof Partida) {
				Partida partida = (Partida) data;
				etiquetas = new Label[partida.getMatriz_abajo().length][partida.getMatriz_abajo().length];
				banderasRestantes = partida.getBanderasRestantes();
				partidaCargada = true;
				if (partida.getTamaño().equals("petita")) {
					banderasPuestas = 12 - partida.getBanderasRestantes();
				} else if (partida.getTamaño().equals("media")) {
					banderasPuestas = 45 - partida.getBanderasRestantes();
				} else {
					banderasPuestas = 100 - partida.getBanderasRestantes();
				}
				id = partida.getId();
				cargarPartida(partida, partida.getTamaño());
			} else {

			}
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
			
			

		});
		try {
		String sentencia = "SELECT id FROM usuari WHERE email = ?";
		PreparedStatement s = c.prepareStatement(sentencia);
		s.setString(1, LoginController.EMAIL);
		ResultSet r;

			r = s.executeQuery();
		
		while (r.next()) {
			idUsuari = r.getInt("id");
		}
		} catch (SQLException e) {
			System.out.println(e);
		}
		

		
	}

	public void canviaEscena() {

		try {

			Stage ventanaActual = (Stage) root.getScene().getWindow();
			ventanaActual.close();
			VBox root2 = FXMLLoader.load(getClass().getResource("Tamany.fxml"));
			Scene escena2 = new Scene(root2);
			String rutaFXML="Tamany.fxml";
			Stage window = new Stage(); //(Stage) root.getScene().getWindow();

			window.setScene(escena2);
			window.setTitle("Juego de la Vida");
			window.show();
			//añadir los juegos abiertos
	        MenuController.juegosAbiertos.add(window);
	        MenuController.juegosPorNombre.put(rutaFXML, window);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void reiniciar() {

		try {
			VBox root2 = FXMLLoader.load(getClass().getResource("BuscaMinas.fxml"));
			Scene escena2 = new Scene(root2);
			String rutaFXML="BuscaMinas.fxml";
			Stage window = (Stage) root.getScene().getWindow();
			window.setScene(escena2);
			window.setTitle("Juego de la Vida");
			window.setMaximized(true);
			window.show();
			//añadir los juegos abiertos
	        MenuController.juegosAbiertos.add(window);
	        MenuController.juegosPorNombre.put(rutaFXML, window);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void mostrarMinas() {
		String[][] matriz_abajo = tablero.getMatriz_abajo();
		for (int i = 0; i < tablero.getLongitudHorizontal(); i++) {
			for (int j = 0; j < tablero.getLongitudHorizontal(); j++) {
				if (matriz_abajo[i][j].equals("x")) {
					Image bandera1;
					try {
						bandera1 = new Image(new FileInputStream("imagenes/bomba.png"));
						ImageView bomba = new ImageView(bandera1);
						bomba.setFitWidth(17);
						bomba.setFitHeight(17);

						bomba.setPreserveRatio(true);
						etiquetas[i][j].setGraphic(bomba);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	public void liberarCuadrados(int x, int y) {
		String[][] matriz_abajo = tablero.getMatriz_abajo();
		if (etiquetas[x][y].getGraphic() != null) {
			
		}else {
			if (matriz_abajo[x][y].equals("0")) {
				etiquetas[x][y].setStyle("-fx-background-color: white;-fx-border-color: black;");
				etiquetas[x][y].setId("noMinaVista");
				comprobarVecinas(x, y);
			} else if (!matriz_abajo[x][y].equals("x")) {
				etiquetas[x][y].setStyle("-fx-background-color: white;-fx-border-color: black;");
				etiquetas[x][y].setText(matriz_abajo[x][y]);
				etiquetas[x][y].setAlignment(Pos.CENTER);
				etiquetas[x][y].setId("noMinaVista");
			}
		}
	
		
		if (contarCuadrados() == ((tablero.getLongitudHorizontal() * tablero.getLongitudVertical())
				- tablero.getMinas())) {
			timeline.stop();
			
			ventanaAlert alerta = new ventanaAlert();
			alerta.alert("You Win","Has guanyat!!", "file:imagenes/win.png", 200);
			if (partidaCargada) {
				System.out.println("Ganar");
				try {
					String sentencia = "DELETE FROM pescaMines where id = ?";
					PreparedStatement s = c.prepareStatement(sentencia);
					s.setInt(1, id);
					s.executeUpdate();
				} catch (  SQLException e1) {
					e1.printStackTrace();
				}
				partidaCargada = false;
			}
			guardarPartida2();
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

			if (ni >= 0 && ni < tablero.getLongitudHorizontal() && nj >= 0 && nj < tablero.getLongitudHorizontal()) {
				if (!etiquetas[ni][nj].getId().equals("noMinaVista") ) {
					if (matriz_abajo[ni][nj].equals("0")) {
						liberarCuadrados(ni, nj);
					} else if (!matriz_abajo[ni][nj].equals("x")) {
						if (etiquetas[ni][nj].getGraphic() != null) {
							
						}else {
							etiquetas[ni][nj].setStyle("-fx-background-color: white;-fx-border-color: black;");
							etiquetas[ni][nj].setText(matriz_abajo[ni][nj]);
							etiquetas[ni][nj].setAlignment(Pos.CENTER);
							etiquetas[ni][nj].setId("noMinaVista");
						}
						
					}
				}
			}
		}
	}

	public int contarCuadrados() {
		int contador = 0;
		for (int i = 0; i < tablero.getLongitudHorizontal(); i++) {
			for (int j = 0; j < tablero.getLongitudHorizontal(); j++) {
				if (etiquetas[i][j].getId().equals("noMinaVista") && etiquetas[i][j] !=null) {
					contador++;
				}
			}
		}

		return contador;
	}
	
	
	public void guardarPartida() {
		recogerIdUsuario(emailMoha);
		if(partidaCargada) {
			try {

				String sentencia = "DELETE FROM pescaMines where id = ?";
				PreparedStatement s = c.prepareStatement(sentencia);
				s.setInt(1, id);
				s.executeUpdate();
			} catch (  SQLException e) {
				e.printStackTrace();
			}
			partidaCargada = false;
			
		}
		if (tablero.getMatriz_abajo() == null) {
			ventanaAlert alerta = new ventanaAlert();
			alerta.alert("Atenció ","No has iniciat cap partida..", "file:imagenes/alerta.png", 100);
			
			return;
		} else {
			try {
				
				Partida partida = new Partida(contarCuadrados(), banderasRestantes, opcion, tablero.getMatriz_abajo());
				
				partida.setEtiquetas(etiquetas);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(partida);
				oos.close();
				byte[] datosSerializados = baos.toByteArray();
				
				Date hoy = new Date();
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String fecha = formato.format(hoy);
				System.out.println("dentro del else3");
				
				String sentencia = "INSERT INTO pescaMines (idUsuari, data, sesioJoc, tamany, temps, acabat) VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement s = c.prepareStatement(sentencia);
				s.setInt(1, idUsuari);
				s.setString(2, fecha);
				s.setBytes(3, datosSerializados);
				s.setString(4, opcion);
				s.setDouble(5, (minutos + segundos / 60.0));
				s.setString(6, "No");
				s.executeUpdate();

//				s.close();
//				c.close();
				
				ventanaAlert alerta = new ventanaAlert();
				alerta.alert("Desar Partida ","Partida desada amb èxit.", "file:imagenes/saved.png", 100);
			} catch (Exception e) {//IOException  | SQLException e
				e.printStackTrace();
			}
		}

	}

	public void guardarPartida2() {
		recogerIdUsuario(emailMoha);
		try {
			Partida partida = new Partida(contarCuadrados(), banderasRestantes, opcion, tablero.getMatriz_abajo());
			partida.setEtiquetas(etiquetas);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(partida);
			oos.close();
			byte[] datosSerializados = baos.toByteArray();

			Date hoy = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fecha = formato.format(hoy);


			String sentencia = "INSERT INTO pescaMines (idUsuari, data, sesioJoc, tamany, temps, acabat) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setInt(1, idUsuari);
			s.setString(2, fecha);
			s.setBytes(3, datosSerializados);
			s.setString(4, opcion);
			s.setDouble(5, (minutos + segundos / 60.0));
			s.setString(6, "Si");
			s.executeUpdate();

//			s.close();
//			c.close();

		} catch (IOException  | SQLException e) {
			e.printStackTrace();
		}

	}

	public void ranking(ActionEvent event) {
		recogerIdUsuario(emailMoha);
		try {

			//String sentencia = "SELECT pescaMines.id, temps, data , tamany FROM pescaMines, usuari WHERE idUsuari = usuari.id AND acabat = 'Si' ORDER BY temps";
			String sentencia = "SELECT id, temps, data , tamany FROM pescaMines WHERE idUsuari = ? AND acabat LIKE 'Si' ORDER BY temps";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setInt(1, idUsuari);
			System.out.println(idUsuari);
			ResultSet r = s.executeQuery();

			ArrayList<Partida> listaRanking = new ArrayList<>();

			while (r.next()) {
				Timestamp fecha = r.getTimestamp("data");
				double tiempo = r.getDouble("temps");
				String tamaño = r.getString("tamany");
				String tiempo2 = "";
				int id = r.getInt("id");
				if (tiempo < 1.0) {
					double seg = tiempo * 60;
					int seg2 = (int) seg;
					if (seg2 < 10) {
						tiempo2 = "00:0" + seg2;
					} else {
						tiempo2 = "00:" + seg2;
					}
				} else {
					int min = (int) Math.floor(tiempo);
					double seg = (tiempo - min) * 60;
					int seg2 = (int) seg;
					if (min < 10) {
						if (seg2 < 10) {
							tiempo2 = "0" + min + ":0" + seg2;
						} else {
							tiempo2 = "0" + min + ":" + seg2;
						}
					} else {
						if (seg2 < 10) {
							tiempo2 = min + ":0" + seg2;
						} else {
							tiempo2 = min + ":" + seg2;
						}
					}
				}

				String fechaFormateada = fecha.toLocalDateTime()
						.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

				listaRanking.add(new Partida(tiempo2, fechaFormateada, tamaño, id));
			}

			Stage ventanaActual =  (Stage) ((Node) event.getSource()).getScene().getWindow();
			ventanaActual.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Ranking.fxml"));
			Parent root = loader.load();
			String rutaFXML="Ranking.fxml";
			Stage stage = new Stage(); //(Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setUserData(listaRanking);
			stage.setScene(new Scene(root));
			stage.show();
			//añadir los juegos abiertos
	        MenuController.juegosAbiertos.add(stage);
	        MenuController.juegosPorNombre.put(rutaFXML, stage);
		} catch (  SQLException | IOException e) {
			e.printStackTrace();
		}

	}

	public void cargar(ActionEvent event) {
		recogerIdUsuario(emailMoha);
		try {
			
			String sentencia = "SELECT id, data, sesioJoc, temps FROM pescaMines WHERE idUsuari = ? AND acabat = 'No'";
			//Statement s = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			PreparedStatement s = c.prepareStatement(sentencia, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			s.setInt(1, idUsuari); 
			ResultSet r = s.executeQuery();

			ArrayList<Partida> partidas = new ArrayList<>();

			System.out.println("dentro fuera while");
			System.out.println("ID usuari: " + idUsuari);
			while (r.next()) {
				System.out.println("dentro  while");
				Timestamp fecha = r.getTimestamp("data");
				System.out.println(fecha);
				byte[] datosSerializados = r.getBytes("sesioJoc");
				System.out.println(datosSerializados);
				double tiempo = r.getDouble("temps");
				System.out.println(tiempo);
				int id = r.getInt("id");
				ByteArrayInputStream bais = new ByteArrayInputStream(datosSerializados);
				ObjectInputStream ois = new ObjectInputStream(bais);
				Partida partida = (Partida) ois.readObject();
				ois.close();

				partida.setFechaFormateada("" + fecha);
				partida.setTiempo("" + tiempo);
				partida.setId(id);
				partidas.add(partida);
			}

//			r.close();
//			s.close();
//			c.close();


			
			
			Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
			ventanaActual.close();
			VBox root2 = FXMLLoader.load(getClass().getResource("CargarPartida.fxml"));
			Scene escena2 = new Scene(root2);
			String rutaFXML="CargarPartida.fxml";
			escena2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage window = new Stage(); //(Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setUserData(partidas);
			window.setScene(escena2);
			window.setTitle("Busca Mines");
			window.show();
			//añadir los juegos abiertos
	        MenuController.juegosAbiertos.add(window);
	        MenuController.juegosPorNombre.put(rutaFXML, window);
		} catch (SQLException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void cargarPartida(Partida partida, String tamaño) {
		this.tablero = new Tablero(partida.getMatriz_abajo());
		if (tamaño.equals("petita")) {
			opcion = "petita";
			tablero.setMinas(12);
			tablero.setLongitudHorizontal(10);
			tablero.setLongitudVertical(10);
			banderasPuestas = 12 - partida.getBanderasRestantes();
		} else if (tamaño.equals("media")) {
			opcion = "media";
			tablero.setMinas(45);
			tablero.setLongitudHorizontal(18);
			tablero.setLongitudVertical(18);
			banderasPuestas = 45 - partida.getBanderasRestantes();
		} else {
			opcion = "gran";
			tablero.setMinas(100);
			tablero.setLongitudHorizontal(25);
			tablero.setLongitudVertical(25);
			banderasPuestas = 100 - partida.getBanderasRestantes();
		}
		reconstruirTableroDesdePartida(partida);
	}

	public void reconstruirTableroDesdePartida(Partida partida) {
		String[][] id = partida.getEtiquetasId();
		String[][] estilos = partida.getEtiquetasStyle();
		String[][] graphic = partida.getEtiquetasGraphic();
		String[][] matriz_abajo = tablero.getMatriz_abajo();

		if (id == null || id.length == 0 || estilos == null || graphic == null || estilos.length == 0) {
			System.out.println("Error: etiquetas nulas o vacías.");
			return;
		}

		int filas = id.length;
		int columnas = id[0].length;

		gridPane.getChildren().clear();

		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				Label label = new Label();
				label.setId(id[i][j]);

				if ("noMinaVista".equals(label.getId()) && !matriz_abajo[i][j].equals("x")
						&& !matriz_abajo[i][j].equals("0")) {
					label.setText(matriz_abajo[i][j]);
				}
				label.setStyle(estilos[i][j]);
				label.setPrefSize(20, 20);
				if (graphic[i][j] != null && graphic[i][j].equals("*")) {
					Image bandera1;
					try {
						bandera1 = new Image(new FileInputStream("imagenes/bandera.png"));
						ImageView bandera = new ImageView(bandera1);
						bandera.setFitWidth(17);
						bandera.setFitHeight(17);
						bandera.setPreserveRatio(true);
						label.setGraphic(bandera);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				label.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
				etiquetas[i][j] = label;
				gridPane.add(label, j, i);
			}
		}
		String[] aux = casillasAbiertas.getText().split(":");
		casillasAbiertas.setText(
				aux[0] + ": " + partida.getContadorCasillasAbiertas() + "/" + (estilos.length * estilos.length));
		casillasAbiertas.setStyle("-fx-font-weight: bold;");

		textoMinas.setText("" + partida.getBanderasRestantes());
		textoMinas.setStyle("-fx-font-weight: bold;");
		String[] aux2 = tiempo.getText().split(":");
		double temps = Double.parseDouble(partida.getTiempo());
		if (temps < 1) {
			segundos = (int) (temps * 60);
		} else {
			minutos = (int) Math.floor(temps);
			segundos = (int) temps - minutos;
			if (minutos < 10) {
				if (segundos < 10) {
					tiempo.setText(aux2[0] + ": 0" + minutos + ":0" + segundos);
					tiempo.setStyle("-fx-font-weight: bold;");
				} else {
					tiempo.setText(aux2[0] + ": 0" + minutos + ":" + segundos);
					tiempo.setStyle("-fx-font-weight: bold;");
				}
			} else {
				if (segundos < 10) {
					tiempo.setText(aux2[0] + ": " + minutos + ":0" + segundos);
					tiempo.setStyle("-fx-font-weight: bold;");
				} else {
					tiempo.setText(aux2[0] + ": " + minutos + ":" + segundos);
					tiempo.setStyle("-fx-font-weight: bold;");
				}
			}
		}
		cargar = true;
	}

}
