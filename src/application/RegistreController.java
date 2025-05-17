package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;


public class RegistreController implements Initializable {
	@FXML
	private Label labelNom;
	@FXML
	private Label labelCognoms;
	@FXML
	private Label labelCorreu;
	@FXML
	private Label labelPoblacio;
	@FXML
	private Label labelContrasenya;

	@FXML
	private TextField nomtxt;
	@FXML
	private TextField cognomstxt;
	@FXML
	private TextField correutxt;
	@FXML
	private TextField poblaciotxt;
	@FXML
	private TextField contrasenyatxt;
	@FXML
	private ImageView addpic;

	private final String promptNom = "Nom";
	private final String promptCognoms = "Cognoms";
	private final String promptCorreu = "Correu electrònic";
	private final String promptPoblacio = "Població";
	private final String promptContrasenya = "Contrasenya";
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		nomtxt.setPromptText(promptNom);
		nomtxt.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				nomtxt.setPromptText("");
				labelNom.setVisible(true);
			} else {
				if (nomtxt.getText().isEmpty()) {
					nomtxt.setPromptText(promptNom);
					labelNom.setVisible(false);
				}
			}
		});

		nomtxt.textProperty().addListener((obs, oldVal, newVal) -> {
			labelNom.setVisible(!newVal.isEmpty() || nomtxt.isFocused());
		});

		
		cognomstxt.setPromptText(promptCognoms);

		cognomstxt.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				cognomstxt.setPromptText("");
				labelCognoms.setVisible(true);
			} else {
				if (cognomstxt.getText().isEmpty()) {
					cognomstxt.setPromptText(promptCognoms);
					labelCognoms.setVisible(false);
				}
			}
		});

		cognomstxt.textProperty().addListener((obs, oldVal, newVal) -> {
			labelCognoms.setVisible(!newVal.isEmpty() || cognomstxt.isFocused());
		});
		
		
		correutxt.setPromptText(promptCorreu);
		correutxt.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				correutxt.setPromptText("");
				labelCorreu.setVisible(true);
			} else {
				if (correutxt.getText().isEmpty()) {
					correutxt.setPromptText(promptCorreu);
					labelCorreu.setVisible(false);
				}
			}
		});
		
		correutxt.textProperty().addListener((obs, oldVal, newVal) -> {
			labelCorreu.setVisible(!newVal.isEmpty() || correutxt.isFocused());
		});
		
		poblaciotxt.setPromptText(promptPoblacio);
		poblaciotxt.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				poblaciotxt.setPromptText("");
				labelPoblacio.setVisible(true);
			} else {
				if (poblaciotxt.getText().isEmpty()) {
					poblaciotxt.setPromptText(promptPoblacio);
					labelPoblacio.setVisible(false);
				}
			}
		});
		
		poblaciotxt.textProperty().addListener((obs, oldVal, newVal) -> {
			labelPoblacio.setVisible(!newVal.isEmpty() || poblaciotxt.isFocused());
		});
		
		contrasenyatxt.setPromptText(promptContrasenya);
		
		contrasenyatxt.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				contrasenyatxt.setPromptText("");
				labelContrasenya.setVisible(true);
			} else {
				if (contrasenyatxt.getText().isEmpty()) {
					contrasenyatxt.setPromptText(promptContrasenya);
					labelContrasenya.setVisible(false);
				}
			}
		});
		
		contrasenyatxt.textProperty().addListener((obs, oldVal, newVal) -> {
			labelContrasenya.setVisible(!newVal.isEmpty() || contrasenyatxt.isFocused());
		});
		
		
	    double radius = 30;

	    Circle clip = new Circle(radius, radius, radius);
	    addpic.setClip(new Circle(
	            addpic.getFitWidth() / 2, 
	            addpic.getFitHeight() / 2, 
	            addpic.getFitWidth() / 2 
	        ));

	}
	
}
