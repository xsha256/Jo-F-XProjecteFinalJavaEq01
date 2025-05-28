package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TamañoController {

	@FXML
	ToggleGroup opcionesGrupo;
	private String urlBaseDades = "jdbc:mariadb://localhost:3306/jofx";
	private String usuari = "root";
	private String contrasenya = "root";

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

	public void ranking(ActionEvent event) {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection c = DriverManager.getConnection(urlBaseDades, usuari, contrasenya);

			String sentencia = "SELECT pescaMines.id, temps, data , tamany FROM pescaMines, usuari WHERE idUsuari = usuari.id AND acabat = 'Si' ORDER BY temps";
			PreparedStatement s = c.prepareStatement(sentencia);
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

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Ranking.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setUserData(listaRanking);
			stage.setScene(new Scene(root));
			stage.show();

		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}

	}

}
