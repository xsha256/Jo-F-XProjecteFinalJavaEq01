package application;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ventanaAlert {
	
	public void alert(String texto, String rutaFoto) {
		//Estilo de las alertas por defecto
		Alert alert = new Alert(AlertType.NONE);
		alert.setTitle("Bucle Detectado");
		alert.getDialogPane().setPrefSize(500, 300);
		Image iconAlert = new Image(rutaFoto);//file:imagenes/danger.png
		ImageView alertView = new ImageView(iconAlert);
		alertView.setFitWidth(200);
		alertView.setPreserveRatio(true);
		Label msg = new Label(texto);
		msg.setTextAlignment(TextAlignment.CENTER);
		msg.setWrapText(true);
		msg.getStyleClass().add("msgAlertError");
		VBox content = new VBox(15, alertView, msg);
		content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(20));
		content.setPrefWidth(200);
		alert.getDialogPane().setContent(content);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
	    //botones personalizados
	    ButtonType botAceptar = new ButtonType("Acceptar", ButtonBar.ButtonData.OK_DONE);
	    ButtonType botCancelar = new ButtonType("Cancel·lar", ButtonBar.ButtonData.CANCEL_CLOSE);
	    alert.getButtonTypes().setAll(botAceptar,botCancelar);//añadir los botones
	    
	    Button botonAceptar = (Button) alert.getDialogPane().lookupButton(botAceptar);
	    botonAceptar.setStyle(
	        "-fx-background-color: #2a7963;" +     
	        "-fx-text-fill: #e8e8e8;" +           
	        "-fx-font-weight: bold;"
	    );
	    botonAceptar.setCursor(Cursor.HAND);
	
	
	    Button botonCancelar = (Button) alert.getDialogPane().lookupButton(botCancelar);
	    botonCancelar.setStyle(
	        "-fx-background-color: #f44336;" +     
	        "-fx-text-fill: #e8e8e8;" +
	        "-fx-font-weight: bold;"
	    );
	    botonCancelar.setCursor(Cursor.HAND);
	    Optional<ButtonType> resultado = alert.showAndWait();
	    
	    
	}
	
}












/*
 * 
							//-------------------------
							Alert alert = new Alert(AlertType.NONE);
							alert.setTitle("Bucle Detectado");
							alert.getDialogPane().setPrefSize(500, 300);
							Image iconAlert = new Image("file:imagenes/danger.png");
							ImageView alertView = new ImageView(iconAlert);
							alertView.setFitWidth(200);
							alertView.setPreserveRatio(true);
							Label msg = new Label("El juego ha entrado en un bucle. Has llegado hasta la generacion " + tabla.getGeneraciones());
							msg.setTextAlignment(TextAlignment.CENTER);
							msg.setWrapText(true);
							msg.getStyleClass().add("msgAlertError");
							VBox content = new VBox(15, alertView, msg);
							content.setAlignment(Pos.CENTER);
							content.setPadding(new Insets(20));
							content.setPrefWidth(200);
							alert.getDialogPane().setContent(content);
							alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
							
						    //botones personalizados
						    ButtonType botAceptar = new ButtonType("Acceptar", ButtonBar.ButtonData.OK_DONE);
						    ButtonType botCancelar = new ButtonType("Cancel·lar", ButtonBar.ButtonData.CANCEL_CLOSE);
						    alert.getButtonTypes().setAll(botAceptar,botCancelar);//añadir los botones
						    
						    Button botonAceptar = (Button) alert.getDialogPane().lookupButton(botAceptar);
						    botonAceptar.setStyle(
						        "-fx-background-color: #2a7963;" +     
						        "-fx-text-fill: #e8e8e8;" +           
						        "-fx-font-weight: bold;"
						    );
						    botonAceptar.setCursor(Cursor.HAND);


						    Button botonCancelar = (Button) alert.getDialogPane().lookupButton(botCancelar);
						    botonCancelar.setStyle(
						        "-fx-background-color: #f44336;" +     
						        "-fx-text-fill: #e8e8e8;" +
						        "-fx-font-weight: bold;"
						    );
						    botonCancelar.setCursor(Cursor.HAND);
						    Optional<ButtonType> resultado = alert.showAndWait();
							//-------------------------
 */
