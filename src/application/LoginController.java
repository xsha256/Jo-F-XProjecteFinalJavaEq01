package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
//	@FXML private ImageView loginimg;
//	private Image imatge;

	@FXML
	private Label labelCorreu;
	@FXML
	private Label labelContrasenya;

	@FXML
	private TextField correutxt;
	@FXML
	private TextField contrasenyatxt;
	@FXML
	private Button accedirBoton;

	private final String promptCorreu = "Correu electrònic";
	private final String promptContrasenya = "Contrasenya";

	public void accedir(ActionEvent e) {
		System.out.println(correutxt.getText());
		System.out.println(accedirBoton.getText());
		boolean valid = consultaBBDD(correutxt.getText(), contrasenyatxt.getText());
		if (valid) {
			System.out.println("Bienvenido");
		}

	}

	public boolean consultaBBDD(String email, String contrasenya) {
		boolean valid = false;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String urlBaseDades = "jdbc:mariadb://localhost:3308/jofx";
			String user = "root";
			String pwd = "root";
			Connection c = DriverManager.getConnection(urlBaseDades, user, pwd);
			String sentencia = "SELECT email FROM usuari WHERE email = ?";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, email);
			ResultSet r = s.executeQuery();
			while (r.next()) {
				System.out.println("Dentro while");
				if (r.getString("email").equals(email)) {
					valid = true;
				}
			}
			if (valid) {
				System.out.println("Dentro if");

				c = DriverManager.getConnection(urlBaseDades, user, pwd);
				sentencia = "SELECT contrasenya FROM usuari WHERE email = ?";
				s = c.prepareStatement(sentencia);
				s.setString(1, email);
				r = s.executeQuery();
				while (r.next()) {
					valid = verificarContrasenya(r.getString("contrasenya"), contrasenya);
				}
			} else {
				System.out.println("Dentro else ");
				try {
					Alert alert = new Alert(AlertType.NONE);
					alert.setTitle("🚩 Error");
					alert.getDialogPane().setPrefSize(250, 530);

					Image iconAlert = new Image(getClass().getResourceAsStream("/errorRegistre.png"));
					ImageView alertView = new ImageView(iconAlert);
					alertView.setFitWidth(400);
					alertView.setPreserveRatio(true);

					Label msg = new Label("No tens Compte️");
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
				} catch (Exception e) {
					System.out.println("Error fsdf: " + e);
				}
			}
		}

		catch (Exception e) {
			System.out.println("Error: " + e);
		}
		return valid;
	}

	public boolean verificarContrasenya(String hashBBDD, String contrasenya) {
		boolean contrasenyaCorrecta = false;
		String hashContrasenya;
		//hashBBDD = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
		//contrasenya="123";
		System.out.println(hashBBDD);
		System.out.println(contrasenya);
		try {
			hashContrasenya = hashContrasenya(contrasenya);

			if (hashContrasenya.equals(hashBBDD)) {
				System.out.println("✅ Login correcto");
				contrasenyaCorrecta = true;
			} else {
				try {

					Alert alert = new Alert(AlertType.NONE);
					alert.setTitle("🚩 Error");
					alert.getDialogPane().setPrefSize(250, 530);

					Image iconAlert = new Image(getClass().getResourceAsStream("/errorRegistre.png"));
					ImageView alertView = new ImageView(iconAlert);
					alertView.setFitWidth(400);
					alertView.setPreserveRatio(true);

					Label msg = new Label("La contrasenya no es correcta️");
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
				} catch (Exception error) {
					System.out.println("Error: " + error);
				}
			}
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		return contrasenyaCorrecta;
	}

	public static String hashContrasenya(String password) throws NoSuchAlgorithmException {
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

		contrasenyatxt.textProperty().addListener((obs, oldVal, newVal) -> {
			labelContrasenya.setVisible(!newVal.isEmpty() || contrasenyatxt.isFocused());
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

	}
}
