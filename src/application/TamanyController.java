package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TamanyController implements Initializable{
	private Connection c;
	@FXML
	ToggleGroup opcionesGrupo;
	@FXML
	private VBox root;
	// recoger idUsuario y guardarlo
	private static int idUsuari = 0;
	public static String emailMoha = LoginController.EMAIL;
	
	public void initialize(URL location, ResourceBundle resources) {
		//funcion que cambia el estado de los booleans para poder duplicados abiertos del mismo juego
		Platform.runLater(()->{
			Stage ventanaActual = (Stage) root.getScene().getWindow();
			if(ventanaActual.isShowing()) {
				MenuController.midapescaminesActivo=true;
				System.out.println("La ventana mida-pescaminas esta activa. Boolean: "+MenuController.midapescaminesActivo);
				System.out.println("BooleanMida: "+MenuController.midapescaminesActivo);
				System.out.println("BooleanRanking: "+MenuController.rankingpescaminesActivo);
				System.out.println("BooleanJoc: "+MenuController.pescaminesActivo);
				System.out.println("BooleanCarregar: "+MenuController.carregarpescaminesActivo);
			}
			ventanaActual.setOnHidden(evt ->{
				MenuController.midapescaminesActivo=false;
				System.out.println("La ventana mida-pescaminas se cerró. Boolean: "+MenuController.midapescaminesActivo);
				System.out.println("BooleanMida: "+MenuController.midapescaminesActivo);
				System.out.println("BooleanRanking: "+MenuController.rankingpescaminesActivo);
				System.out.println("BooleanJoc: "+MenuController.pescaminesActivo);
				System.out.println("BooleanCarregar: "+MenuController.carregarpescaminesActivo);
			});
		});
	}
	
	public void recogerIdUsuario(String emailUsuario) {
	    this.c = ConexionBBDD.conectar();
	    
	    try {
	        String sentencia = "SELECT id FROM usuari WHERE email = ?";
	        PreparedStatement s = c.prepareStatement(sentencia);
	        s.setString(1, emailUsuario);
	        ResultSet rs = s.executeQuery();

	        if (rs.next()) {
	            idUsuari = rs.getInt("id");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public void canviaEscena(ActionEvent e) {
		
		Toggle selectedToggle = opcionesGrupo.getSelectedToggle();
		if (selectedToggle != null) {
			RadioButton seleccionado = (RadioButton) selectedToggle;
			String opcion = seleccionado.getId();
			try {
				Stage ventanaActual = (Stage) ((Node) e.getSource()).getScene().getWindow();
				ventanaActual.close();
				VBox root2 = FXMLLoader.load(getClass().getResource("BuscaMinas.fxml"));
				String rutaFXML="BuscaMinas.fxml";
				Scene escena2 = new Scene(root2);
				escena2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Stage window = new Stage();// (Stage) ((Node) e.getSource()).getScene().getWindow();
				window.setUserData(opcion);
				window.setScene(escena2);
				window.setTitle("Pescamines");
				window.setMaximized(true);// lo abrimos en maximizado
				window.show();
				
				//añadir los juegos abiertos
		        MenuController.juegosAbiertos.add(window);
		        MenuController.juegosPorNombre.put(rutaFXML, window);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			ventanaAlert alerta = new ventanaAlert();
			alerta.alert("Atenció ", "Por favor, selecciona una opción.", "file:imagenes/alerta.png", 100);
		}
	}

	public void enrere(ActionEvent e) {
		Stage ventanaActual = (Stage) ((Node) e.getSource()).getScene().getWindow();
		ventanaActual.close();
		
	}

	public void ranking(ActionEvent event) {
		
		recogerIdUsuario(emailMoha);
		try {
			Connection c = ConexionBBDD.conectar();
			String sentencia = "SELECT id FROM usuari WHERE email = ?";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, LoginController.EMAIL);
			ResultSet r = s.executeQuery();
			while (r.next()) {
				idUsuari = r.getInt("id");
			}

			// String sentencia = "SELECT pescaMines.id, temps, data , tamany FROM
			// pescaMines, usuari WHERE idUsuari = usuari.id AND acabat = 'Si' ORDER BY
			// temps";
			sentencia = "SELECT id, temps, data , tamany FROM pescaMines WHERE idUsuari = ? AND acabat = 'Si' ORDER BY temps";
			s = c.prepareStatement(sentencia);
			s.setInt(1, idUsuari);
			r = s.executeQuery();

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

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Ranking.fxml"));
			Parent root = loader.load();
			String rutaFXML="Ranking.fxml";
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setUserData(listaRanking);
			stage.setScene(new Scene(root));
			stage.show();
			//añadir los juegos abiertos
	        MenuController.juegosAbiertos.add(stage);
	        MenuController.juegosPorNombre.put(rutaFXML, stage);
	        
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}

	}

}
