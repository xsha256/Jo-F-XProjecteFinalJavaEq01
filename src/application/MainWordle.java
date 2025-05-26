package application;
	
import java.awt.Panel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class MainWordle extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox escena = (VBox)FXMLLoader.load(getClass().getResource("wordleLogin.fxml"));
			Scene scene = new Scene(escena);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			escena.prefWidthProperty().bind(escena.widthProperty());
			escena.prefHeightProperty().bind(escena.heightProperty());
			primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}