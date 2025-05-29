package application;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RankingController implements Initializable {

	@FXML
	private TableView<Partida> tablaRanking;
	@FXML
	private TableColumn<Partida, String> columnaTamanyo;

	@FXML
	private TableColumn<Partida, String> columnaTiempo;

	@FXML
	private TableColumn<Partida, String> columnaFecha;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		columnaTiempo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTiempo()));

		columnaFecha
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaFormateada()));

		columnaTamanyo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTamaño()));

		columnaTamanyo.setCellFactory(column -> new TableCell<Partida, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item);
					setStyle("-fx-alignment: CENTER;");
				}
			}
		});
		columnaTiempo.setCellFactory(column -> {
			return new TableCell<Partida, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item);
						setStyle("-fx-alignment: CENTER;");
					}
				}
			};
		});

		columnaFecha.setCellFactory(column -> {
			return new TableCell<Partida, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item);
						setStyle("-fx-alignment: CENTER;");
					}
				}
			};
		});

		Platform.runLater(() -> {
			Object userData = tablaRanking.getScene().getWindow().getUserData();
			if (userData instanceof ArrayList<?>) {
				ArrayList<Partida> listaRanking = (ArrayList<Partida>) userData;
				tablaRanking.setItems(FXCollections.observableArrayList(listaRanking));
			}
		});
	}

	public void volverATamany(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Tamany.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Seleccionar Tamaño");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
