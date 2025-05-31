package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PixelArtMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("PixelArtIniciFXML.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setMaximized(true);
			root.prefWidthProperty().bind(root.widthProperty());
			root.prefHeightProperty().bind(root.heightProperty());
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
			//Comentari
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
