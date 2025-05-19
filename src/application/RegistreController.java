package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	private Label labelConfContrasenya;

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
	private TextField confcontrasenyatxt;
	@FXML
	private TextField imginput;

	@FXML
	private ImageView showPic;
	@FXML
	private ImageView imgurl;

	private Stage stage;

	private final String promptNom = "Nom";
	private final String promptCognoms = "Cognoms";
	private final String promptCorreu = "Correu electrònic";
	private final String promptPoblacio = "Població";
	private final String promptContrasenya = "Contrasenya";
	private final String promptConfContrasenya = "Confirmació de contrasenya";

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void registrar(ActionEvent e) {

		System.out.println(nomtxt.getText());
		System.out.println(cognomstxt.getText());
		System.out.println(correutxt.getText());
		System.out.println(poblaciotxt.getText());
		System.out.println(contrasenyatxt.getText());
		System.out.println(confcontrasenyatxt.getText());
		System.out.println(imginput.getText());
		String nom = nomtxt.getText();
		String cognoms = cognomstxt.getText();
		String correu = correutxt.getText();
		String poblacio = poblaciotxt.getText();
		String contrasenya = contrasenyatxt.getText();
		String confcontrasenya = confcontrasenyatxt.getText();
		if (nom.matches("[\\w]+")) {
			labelNom.setText(promptNom);
			labelNom.setVisible(false);
			labelNom.setStyle("-fx-text-fill: #e8e8e8;");
			nomtxt.setPromptText(promptNom);
			nomtxt.setStyle("-fx-background-color: #0d262e;");
			if (cognoms.matches("[\\w]+")) {
				labelCognoms.setText(promptCognoms);
				labelCognoms.setVisible(false);
				labelCognoms.setStyle("-fx-text-fill: #e8e8e8;");
				if (poblacio.matches("[\\w]+")) {
					labelPoblacio.setText(promptPoblacio);
					labelPoblacio.setVisible(false);
					labelPoblacio.setStyle("-fx-text-fill: #e8e8e8;");
					if (correu.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
						labelCorreu.setText(promptCorreu);
						labelCorreu.setVisible(false);
						labelCorreu.setStyle("-fx-text-fill: #e8e8e8;");
						if (contrasenya.equals(confcontrasenya)) {
							inserirBBDD(nom, cognoms, correu, poblacio, contrasenya, imginput.getText());
							nomtxt.setText("");
							nomtxt.setText("");
							cognomstxt.setText("");
							correutxt.setText("");
							poblaciotxt.setText("");
							contrasenyatxt.setText("");
							confcontrasenyatxt.setText("");
							nomtxt.setPromptText("Nom");
							cognomstxt.setPromptText("Cognoms");
							correutxt.setPromptText("Correu electrònic");
							poblaciotxt.setPromptText("Poblaciò");
							contrasenyatxt.setPromptText("Contrasenya");
							confcontrasenyatxt.setPromptText("Confirmació de contrasenya");
							labelContrasenya.setText(promptContrasenya);
							labelContrasenya.setVisible(false);
							labelContrasenya.setStyle("-fx-text-fill: #e8e8e8;");
							labelConfContrasenya.setText(promptConfContrasenya);
							labelConfContrasenya.setStyle("-fx-text-fill: #e8e8e8;");
							labelConfContrasenya.setVisible(false);
							Image imageShow = new Image(getClass().getResourceAsStream("/imgdefault.jpg"));
							showPic.setImage(imageShow);
							imginput.setText("Pujar imatge");

						} else {
							labelContrasenya.setText("La contrasenya no es la mateixa");
							labelContrasenya.setStyle("-fx-text-fill: red;");
							labelConfContrasenya.setText("La contrasenya no es la mateixa");
							labelConfContrasenya.setStyle("-fx-text-fill: red;");
						}
					} else {
						labelCorreu.setText("Correu no vàlid ex: email@emailcom");
						labelCorreu.setStyle("-fx-text-fill: red;");
					}
				} else {
					labelCognoms.setText("El poble és obligatori");
					labelCognoms.setStyle("-fx-text-fill: red;");
				}
			} else {
				labelCognoms.setText("Els cognoms són obligatoris");
				labelCognoms.setStyle("-fx-text-fill: red;");
			}

		} else {
			labelNom.setText("El nom és obligatori");
			labelNom.setStyle("-fx-text-fill: red;");
			nomtxt.setPromptText("El nom és obligatori");
			nomtxt.setStyle("-fx-background-color: red;");
		}

	}

	public void addimg() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecciona una imatge");
		File archivo = fileChooser.showOpenDialog(stage); 
		if (archivo != null) {
			System.out.println("Archivo seleccionado: " + archivo.getAbsolutePath());
			imginput.setText(archivo.toString());
			Image imageShow = new Image("file:" + archivo.toString());
			showPic.setImage(imageShow);

		}
	}

	public void netejar(ActionEvent e) {
		nomtxt.setText("");
		nomtxt.setText("");
		cognomstxt.setText("");
		correutxt.setText("");
		poblaciotxt.setText("");
		contrasenyatxt.setText("");
		nomtxt.setPromptText("Nom");
		cognomstxt.setPromptText("Cognoms");
		correutxt.setPromptText("Correu electrònic");
		poblaciotxt.setPromptText("Poblaciò");
		contrasenyatxt.setPromptText("Contrasenya");
		confcontrasenyatxt.setPromptText("Confirmació de contrasenya");
		Image imageShow = new Image(getClass().getResourceAsStream("/imgdefault.jpg"));
		showPic.setImage(imageShow);
		imginput.setText("Pujar imatge");

	}

	public void inserirBBDD(String nom, String cognoms, String email, String poblacio, String contrasenya, String img) {
		try {

			Class.forName("org.mariadb.jdbc.Driver");
			String urlBaseDades = "jdbc:mariadb://localhost:3308/jofx";
			String user = "root";
			String pwd = "root";
			if (img.equals("Pujar imatge")) {
				img = "bin/imgdefault.jpg";
			}
			System.out.println(img);

			File imagen = new File(img);
			FileInputStream fis = new FileInputStream(imagen);
			String contrasenyaCifString = hashPassword(contrasenya);
			Connection c = DriverManager.getConnection(urlBaseDades, user, pwd);
			String sentencia = "INSERT INTO usuari(nom, cognoms, email, imatge, contrasenya, poblacio) VALUES (?,?,?,?,?,?)";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, nom);
			s.setString(2, cognoms);
			s.setString(3, email);
			s.setBinaryStream(4, fis, (int) imagen.length());
			s.setString(5, contrasenyaCifString);
			s.setString(6, poblacio);
			s.executeUpdate();

		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	public static String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = md.digest(password.getBytes());

		StringBuilder sb = new StringBuilder();
		for (byte b : hashBytes) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

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

		confcontrasenyatxt.setPromptText(promptConfContrasenya);

		confcontrasenyatxt.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				confcontrasenyatxt.setPromptText("");
				labelConfContrasenya.setVisible(true);
			} else {
				if (confcontrasenyatxt.getText().isEmpty()) {
					confcontrasenyatxt.setPromptText(promptConfContrasenya);
					labelConfContrasenya.setVisible(false);
				}
			}
		});

		confcontrasenyatxt.textProperty().addListener((obs, oldVal, newVal) -> {
			labelConfContrasenya.setVisible(!newVal.isEmpty() || confcontrasenyatxt.isFocused());
		});

		Image imageAdd = new Image(getClass().getResourceAsStream("/addIcon.png"));
		imgurl.setImage(imageAdd);

		Image imageShow = new Image(getClass().getResourceAsStream("/imgdefault.jpg"));
		showPic.setImage(imageShow);
		showPic.setFitWidth(60);
		showPic.setFitHeight(60);
		showPic.setPreserveRatio(false);
		showPic.setSmooth(true);
		double radius = showPic.getFitWidth() / 2;
		Circle clip = new Circle(radius, radius, radius);
		showPic.setClip(clip);

	}

}
