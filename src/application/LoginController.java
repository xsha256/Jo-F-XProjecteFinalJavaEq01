package application;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoginController {
	@FXML private ImageView loginimg;
	private Image imatge;

	
//	public void initialize() {	
//		
//		try {
//			imatge = new Image(new FileInputStream("./img/login.png"));
//			loginimg.setImage(imatge);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
}
