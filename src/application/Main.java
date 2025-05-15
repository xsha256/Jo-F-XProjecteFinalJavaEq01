package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("Dificultad.fxml"));
			Scene scene = new Scene(root,1000,1000);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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

//preguntar como pausar y como reanudar el juego
//las casillas aleatorias entre que numeros deben estar
//preguntar que tiene que hacer el boton de stop
//como puedo ver si esta muerta de antes,acaba de morir, acaba de nacer o estaba viva de antes
//el maximo de generaciones tiene que variar dependiendo el tamaño?