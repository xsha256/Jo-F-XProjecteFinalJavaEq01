package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CargarPartidaController implements Initializable {

	@FXML
	private VBox root;
	
	@FXML
	private TableView<Partida> tableViewPartidas;

	@FXML
	private TableColumn<Partida, String> colTamaño;

	@FXML
	private TableColumn<Partida, String> colTiempo;

	@FXML
	private TableColumn<Partida, Integer> colCasillasDescubiertas;

	@FXML
	private TableColumn<Partida, Integer> colBanderasRestantes;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		colTamaño.setCellValueFactory(new PropertyValueFactory<>("tamaño"));
		colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
		colCasillasDescubiertas.setCellValueFactory(new PropertyValueFactory<>("contadorCasillasAbiertas"));
		colBanderasRestantes.setCellValueFactory(new PropertyValueFactory<>("banderasRestantes"));


	    colTamaño.setCellFactory(column -> new TableCell<Partida, String>() {
	        @Override
	        protected void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            setText((empty || item == null) ? null : item);
	            setStyle("-fx-alignment: CENTER;");
	        }
	    });

	    colTiempo.setCellFactory(column -> new TableCell<Partida, String>() {
	        @Override
	        protected void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                try {
	                    double tiempo = Double.parseDouble(item);
	                    int minutos = (int) Math.floor(tiempo);
	                    int segundos = (int) Math.round((tiempo - minutos) * 60);
	                    setText(String.format("%02d:%02d", minutos, segundos));
	                } catch (NumberFormatException e) {
	                    setText("??:??");
	                }
	            }
	            setStyle("-fx-alignment: CENTER;");
	        }
	    });



	    colCasillasDescubiertas.setCellFactory(column -> new TableCell<Partida, Integer>() {
	        @Override
	        protected void updateItem(Integer item, boolean empty) {
	            super.updateItem(item, empty);
	            setText((empty || item == null) ? null : item.toString());
	            setStyle("-fx-alignment: CENTER;");
	        }
	    });

	    colBanderasRestantes.setCellFactory(column -> new TableCell<Partida, Integer>() {
	        @Override
	        protected void updateItem(Integer item, boolean empty) {
	            super.updateItem(item, empty);
	            setText((empty || item == null) ? null : item.toString());
	            setStyle("-fx-alignment: CENTER;");
	        }
	    });

	    Platform.runLater(() -> {
	        Stage window = (Stage) root.getScene().getWindow();
	        ArrayList<Partida> partidas = (ArrayList<Partida>) window.getUserData();
	        tableViewPartidas.setItems(FXCollections.observableArrayList(partidas));
	    });

	}



	public void volver(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Tamany.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene escena2 = new Scene(root, 600, 500);
			stage.setScene(escena2);
			stage.setTitle("Seleccionar Mida");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void cargarPartida(ActionEvent event) {
	    Partida partidaSeleccionada = tableViewPartidas.getSelectionModel().getSelectedItem();

	    if (partidaSeleccionada != null) {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("BuscaMinas.fxml"));
	            Parent root = loader.load();
				Scene escena2 = new Scene(root);
	            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            window.setUserData(partidaSeleccionada);
	            window.setScene(escena2);
	            window.setTitle("Pescamines");
	            window.setMaximized(true);
	            window.show();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } else {
			ventanaAlert alerta = new ventanaAlert();
			alerta.alert("Notificació","No has seleccionat cap partida.", "file:imagenes/alerta.png", 150);
	    }
	}

}
