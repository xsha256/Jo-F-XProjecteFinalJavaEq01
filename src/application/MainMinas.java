package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMinas extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox) FXMLLoader.load(getClass().getResource("Tamany.fxml"));
			Scene scene = new Scene(root, 600, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
