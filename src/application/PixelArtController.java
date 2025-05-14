package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class PixelArtController implements Initializable {

	@FXML private Button pinzell;
	@FXML private Button borrador;
	@FXML private Pane root;
	@FXML private GridPane graella;
	@FXML private ColorPicker color;
		
	@Override
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		int files = 100; 
	    int columnes = 100;
	    int grandariaCelda = 10;
	    int contador = 0;

	    for (int fila = 0; fila < files; fila++) {
	        for (int col = 0; col < columnes; col++) {
	        	
	        	if(contador%2==0) {
	        		Pane celda = new Pane();
		            celda.setPrefSize(grandariaCelda, grandariaCelda);
		            celda.setStyle("-fx-background-color: white;");
		            
		            celda.setOnMousePressed(e -> {
		                celda.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");//agafe el valor que li he passat en el color picker
		            });
		            
		            graella.add(celda, col, fila);
		            
	        	}else {
	        		  Pane celda = new Pane();
	  	            celda.setPrefSize(grandariaCelda, grandariaCelda);
	  	            celda.setStyle("-fx-background-color: grey;");
	  	            
	  	            celda.setOnMousePressed(e -> {
	  	                celda.setStyle("-fx-background-color: " + colorString(color.getValue()) + ";");//agafe el valor que li he passat en el color picker
	  	            });	  	            

	  	            graella.add(celda, col, fila);//afegisc la celda amb el event
	        	}
	        	
	          contador++;
	        }
	        contador++;
	    }
		
	}
	
	private String colorString(javafx.scene.paint.Color c) {//converteix el color en una string per poder interpretar-lo
	    return "rgb("
	        + (int)(c.getRed()*255) + ","
	        + (int)(c.getGreen()*255) + ","
	        + (int)(c.getBlue()*255) + ")";
	}
	
}


