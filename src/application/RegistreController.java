package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
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
	@FXML
	private Hyperlink accedirURL;

	private Stage stage;

	private final String promptNom = "Nom*";
	private final String promptCognoms = "Cognoms*";
	private final String promptCorreu = "Correu electrònic*";
	private final String promptPoblacio = "Població*";
	private final String promptContrasenya = "Contrasenya*";
	private final String promptConfContrasenya = "Confirmació de contrasenya*";

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void registrar(ActionEvent e) {

		if (verificarCampos(labelNom, nomtxt, promptNom, "El nom és obligatori", 0)) {
			if (verificarCampos(labelCognoms, cognomstxt, promptCognoms, "Els cognoms són obligatoris", 0)) {
				if (verificarCampos(labelCorreu, correutxt, promptCorreu, "Correu no vàlid ex: email@emailcom", 1)) {
					if (verificarCampos(labelPoblacio, poblaciotxt, promptPoblacio, "El poble és obligatori", 0)) {
						if (contrasenyatxt.getText().trim() != ""
								&& contrasenyatxt.getText().equals(confcontrasenyatxt.getText())) {
							inserirBBDD(nomtxt.getText(), cognomstxt.getText(), correutxt.getText(),
									poblaciotxt.getText(), contrasenyatxt.getText(), imginput.getText(), e);
						} else {
							labelContrasenya.setText("La contrasenya no es la mateixa");
							labelContrasenya.setStyle("-fx-text-fill: red;");
							labelConfContrasenya.setText("La contrasenya no es la mateixa");
							labelConfContrasenya.setStyle("-fx-text-fill: red;");
							contrasenyatxt.setPromptText("La contrasenya no es la mateixa");
							contrasenyatxt.setStyle(
									"-fx-background-color: red; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
							confcontrasenyatxt.setPromptText("La contrasenya no es la mateixa");
							confcontrasenyatxt.setStyle(
									"-fx-background-color: red; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
						}
					}
				}
			}

		}

	}

	public boolean verificarCampos(Label labelparam, TextField paramtxt, String promptParam, String msg, int op) {
		boolean validar = false;
		if (op == 0) {
			if (paramtxt.getText().trim() != "") {
				labelparam.setText(promptParam);
				labelparam.setVisible(false);
				labelparam.setStyle("-fx-text-fill: #e8e8e8;");
				paramtxt.setPromptText(promptParam);
				paramtxt.setStyle(
						"-fx-background-color: #365057; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
				validar = true;
			} else {
				labelparam.setText(msg);
				labelparam.setStyle("-fx-text-fill: red;");
				paramtxt.setPromptText(msg);
				paramtxt.setStyle("-fx-background-color: red; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
			}
		} else if (op == 1) {
			if (paramtxt.getText().trim().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				labelparam.setText(promptParam);
				labelparam.setVisible(false);
				labelparam.setStyle("-fx-text-fill: #e8e8e8;");
				paramtxt.setPromptText(promptParam);
				paramtxt.setStyle(
						"-fx-background-color: #365057; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
				validar = true;
			} else {
				labelparam.setText(msg);
				labelparam.setStyle("-fx-text-fill: red;");
				paramtxt.setPromptText(msg);
				paramtxt.setStyle("-fx-background-color: red; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
			}
		}
		return validar;
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
		confcontrasenyatxt.setText("");
		nomtxt.setPromptText("Nom*");
		cognomstxt.setPromptText("Cognoms*");
		correutxt.setPromptText("Correu electrònic*");
		poblaciotxt.setPromptText("Poblaciò*");
		contrasenyatxt.setPromptText("Contrasenya*");
		confcontrasenyatxt.setPromptText("Confirmació de contrasenya*");
		Image imageShow = new Image("file:imagenes/imgdefault.jpg");
		showPic.setImage(imageShow);
		imginput.setText("Pujar imatge");

	}

	public void inserirBBDD(String nom, String cognoms, String email, String poblacio, String contrasenya, String img,
			ActionEvent e) {

		try {

			Class.forName("org.mariadb.jdbc.Driver");
			String urlBaseDades = "jdbc:mariadb://localhost:3308/jofx";
			String user = "root";
			String pwd = "root";
			boolean valid = comprobarEmail(email, urlBaseDades, user, pwd);
			if (valid) {
				if (img.equals("Pujar imatge")) {
					img = "imagenes/imgdefault.jpg";
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
				try {

					Alert alert = new Alert(AlertType.NONE);
					alert.setTitle("🎮 Per fi!");
					alert.getDialogPane().setPrefSize(250, 500);
					Image iconAlert = new Image("file:imagenes/creatUsuari.png");
					ImageView alertView = new ImageView(iconAlert);
					alertView.setFitWidth(200);
					alertView.setPreserveRatio(true);

					Label msg = new Label("El compte s'ha creat perfectament! 🎮");
					msg.setMaxWidth(500);
					msg.setWrapText(true);
					msg.getStyleClass().add("msgAlertError");

					VBox content = new VBox(15, alertView, msg);
					content.setAlignment(Pos.CENTER);
					content.setPadding(new Insets(20));
					content.setPrefWidth(500);

					alert.getDialogPane().setContent(content);
					alert.getDialogPane().getStylesheets()
							.add(getClass().getResource("application.css").toExternalForm());
					alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
					Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
					okButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
					okButton.getStyleClass().add("boton-hover");
					alert.getDialogPane().getStyleClass().add("alertError");
					alert.showAndWait();
					accedirLogin(e);
				} catch (Exception er) {
					System.out.println("Error: " + er);
				}
			} else {

				try {

					Alert alert = new Alert(AlertType.NONE);
					alert.setTitle("🚩 Error");
					alert.getDialogPane().setPrefSize(250, 530);

					Image iconAlert = new Image("file:imagenes/errorRegistre.png");
					ImageView alertView = new ImageView(iconAlert);
					alertView.setFitWidth(400);
					alertView.setPreserveRatio(true);
					Label msg = new Label("Ya tens un compte!🕹️");
					msg.setMaxWidth(500);
					msg.setWrapText(true);
					msg.getStyleClass().add("msgAlertError");
					VBox content = new VBox(15, alertView, msg);
					content.setAlignment(Pos.CENTER);
					content.setPadding(new Insets(20));
					content.setPrefWidth(500);
					alert.getDialogPane().setContent(content);
					alert.getDialogPane().getStylesheets()
							.add(getClass().getResource("application.css").toExternalForm());

					ButtonType registrar = new ButtonType("Registrar", ButtonBar.ButtonData.CANCEL_CLOSE);
					ButtonType login = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
					alert.getDialogPane().getButtonTypes().addAll(registrar, login);

					Button registrarButton = (Button) alert.getDialogPane().lookupButton(registrar);
					registrarButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
					registrarButton.getStyleClass().add("boton-hover");
					registrarButton.getStyleClass().add("boton-hover");
					alert.getDialogPane().getStyleClass().add("alertError");
					Button loginButton = (Button) alert.getDialogPane().lookupButton(login);
					loginButton.getStyleClass().add("boton-hover");
					loginButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
					loginButton.getStyleClass().add("boton-hover");
					alert.getDialogPane().getStyleClass().add("alertError");
					Optional<ButtonType> resultado = alert.showAndWait();

					if (resultado.isPresent() && resultado.get() == login) {
						accedirLogin(e);

					}

				} catch (Exception er) {
					System.out.println("Error: " + er);
				}
			}

		} catch (Exception er) {
			System.out.println("Error: " + er);
		}
	}

	public boolean comprobarEmail(String email, String urlBaseDades, String user, String pwd) {
		boolean valid = true;
		try {

			Connection c = DriverManager.getConnection(urlBaseDades, user, pwd);
			String sentencia = "SELECT email FROM usuari WHERE email = ?";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, email);
			ResultSet r = s.executeQuery();
			while (r.next()) {
				System.out.println("dentro while");
				if (r.getString("email").equals(email)) {
					valid = false;
				}
			}
		}

		catch (Exception e) {
			System.out.println("Error: " + e);
		}
		return valid;
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

	public void accedirLogin(ActionEvent e) {
		try {
			Parent nuevaVista = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene nuevaEscena = new Scene(nuevaVista);
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(nuevaEscena);
			window.show();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
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

		Image imageAdd = new Image("file:imagenes/addIcon.png");
		imgurl.setImage(imageAdd);

		Image imageShow = new Image("file:imagenes/imgdefault.jpg");
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
