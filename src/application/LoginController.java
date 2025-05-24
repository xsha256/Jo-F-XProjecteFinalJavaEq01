package application;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController implements Initializable {


	public static String EMAIL = "";
	@FXML
	private Label labelCorreu;
	@FXML
	private Label labelContrasenya;

	@FXML
	private TextField correutxt;
	@FXML
	private PasswordField contrasenyatxt;
	@FXML
	private Button accedirBoton;
	@FXML
	private HBox enter;

	private final String promptCorreu = "Correu electrònic";
	private final String promptContrasenya = "Contrasenya";

	public void enterKey(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			System.out.println("Has presionado ENTER");
			accedir(new ActionEvent(accedirBoton, null));
		} else {
			System.out.println("Has presionat: " + e.getCode());
		}

	}

	public void accedir(ActionEvent e) {
		System.out.println(e);
		boolean valid = false;
		String correu = correutxt.getText();
		String contrasenya = contrasenyatxt.getText();
		System.out.println(correutxt.getText());
		System.out.println(accedirBoton.getText());
		if (correu.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			labelCorreu.setText(promptCorreu);
			labelCorreu.setVisible(false);
			labelCorreu.setStyle("-fx-text-fill: #e8e8e8;");
			correutxt.setPromptText(promptCorreu);
			correutxt.setStyle("-fx-background-color: #365057; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
			if (contrasenya.trim() != "") {
				labelContrasenya.setText(promptContrasenya);
				labelContrasenya.setVisible(false);
				labelContrasenya.setStyle("-fx-text-fill: #e8e8e8;");
				contrasenyatxt.setPromptText(promptContrasenya);
				contrasenyatxt.setStyle(
						"-fx-background-color: #365057; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
				valid = consultaBBDD(correu, contrasenya, e);
			} else {
				labelContrasenya.setText("Fa falta una contrasenya");
				labelContrasenya.setStyle("-fx-text-fill: red;");
				contrasenyatxt.setPromptText("Fa falta una contrasenya");
				contrasenyatxt
						.setStyle("-fx-background-color: red; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
			}
		} else {

			labelCorreu.setText("Fa falta un correu");
			labelCorreu.setStyle("-fx-text-fill: red;");
			correutxt.setPromptText("Fa falta un correu");
			correutxt.setStyle("-fx-background-color: red; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
		}
		if (valid) {
			System.out.println("Bienvenido");
			EMAIL = correu;
			System.out.println(correu);

			try {

				Parent nuevaVista = FXMLLoader.load(getClass().getResource("Menu.fxml"));

				Scene nuevaEscena = new Scene(nuevaVista);
				Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

				window.setScene(nuevaEscena);
				window.show();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void accedirRegistre(ActionEvent e) {
		try {

			Parent nuevaVista = FXMLLoader.load(getClass().getResource("RegistreFXML.fxml"));

			Scene nuevaEscena = new Scene(nuevaVista);
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

			window.setScene(nuevaEscena);
			window.show();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean consultaBBDD(String email, String contrasenya, ActionEvent e) {
		boolean valid = false;

		try {

			Class.forName("org.mariadb.jdbc.Driver");
			String urlBaseDades = "jdbc:mariadb://localhost:3306/jofx";
			String user = "root";
			String pwd = "";
			Connection c = DriverManager.getConnection(urlBaseDades, user, pwd);
			
//			Connection c = ConexionBBDD.conectar();
			String sentencia = "SELECT email FROM usuari WHERE email = ?";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, email);
			ResultSet r = s.executeQuery();
			while (r.next()) {
				if (r.getString("email").equals(email)) {
					valid = true;
				}
			}
			if (valid) {
//				c = ConexionBBDD.conectar();
				sentencia = "SELECT contrasenya FROM usuari WHERE email = ?";
				s = c.prepareStatement(sentencia);
				s.setString(1, email);
				r = s.executeQuery();
				while (r.next()) {
					valid = verificarContrasenya(r.getString("contrasenya"), contrasenya);
				}
			} else {
				alerta("No tens compte!🕹️", 1, e,"file:imagenes/errorRegistre.png", "login");

			}
		}

		catch (Exception er) {
			System.out.println("Error: " + er);
		}
		return valid;
	}

	public boolean verificarContrasenya(String hashBBDD, String contrasenya) {
		boolean contrasenyaCorrecta = false;
		String hashContrasenya;
		// hashBBDD =
		// "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
		// contrasenya="123";
		System.out.println(hashBBDD);
		System.out.println(contrasenya);
		try {
			hashContrasenya = RegistreController.hashContrasenya(contrasenya);

			if (hashContrasenya.equals(hashBBDD)) {
				System.out.println("✅ Login correcto");
				contrasenyaCorrecta = true;
			} else {
				alerta("La contrasenya no es correcta️", 0, null,"file:imagenes/errorRegistre.png", "login");

			}
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		return contrasenyaCorrecta;
	}

	public void alerta(String msgParam, int op, ActionEvent e, String fotoPath, String controller ) {
		try {
			Alert alert = new Alert(AlertType.NONE);
			alert.setTitle("🚩 Error");
			alert.getDialogPane().setPrefSize(250, 530);
			Image iconAlert = new Image(fotoPath);
			ImageView alertView = new ImageView(iconAlert);
			alertView.setFitWidth(400);
			alertView.setPreserveRatio(true);
			Label msg = new Label(msgParam);
			msg.setMaxWidth(500);
			msg.setWrapText(true);
			msg.getStyleClass().add("msgAlertError");
			VBox content = new VBox(15, alertView, msg);
			content.setAlignment(Pos.CENTER);
			content.setPadding(new Insets(20));
			content.setPrefWidth(500);
			alert.getDialogPane().setContent(content);
			alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			if (op == 0) {
				alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
				Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
				okButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
				okButton.getStyleClass().add("boton-hover");
				alert.getDialogPane().getStyleClass().add("alertError");
				alert.showAndWait();
			} else {
				
				ButtonType login = new ButtonType("Login", ButtonBar.ButtonData.CANCEL_CLOSE);
				ButtonType registrar = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
				if (controller == "registre") {	
					 registrar = new ButtonType("Registrar", ButtonBar.ButtonData.CANCEL_CLOSE);
					 login = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
				}
				
				alert.getDialogPane().getButtonTypes().addAll(registrar, login);

				Button registrarButton = (Button) alert.getDialogPane().lookupButton(registrar);
				registrarButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
				registrarButton.getStyleClass().add("boton-hover");
				alert.getDialogPane().getStyleClass().add("alertError");
				Button loginButton = (Button) alert.getDialogPane().lookupButton(login);
				loginButton.setStyle("-fx-background-color: #2a7963; -fx-text-fill: #e8e8e8;");
				loginButton.getStyleClass().add("boton-hover");
				alert.getDialogPane().getStyleClass().add("alertError");
				Optional<ButtonType> resultado = alert.showAndWait();

				if (controller == "login") {
					if (resultado.isPresent() && resultado.get() == registrar) {
						accedirRegistre(e);
					}
					labelCorreu.setText(promptCorreu);
					labelCorreu.setStyle("-fx-text-fill: #e8e8e8;");
					correutxt.setText("");
					correutxt.setPromptText(promptCorreu);
					correutxt.setStyle(
							"-fx-background-color: #365057; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
					labelContrasenya.setText(promptContrasenya);
					labelContrasenya.setStyle("-fx-text-fill: #e8e8e8;");
					contrasenyatxt.setText("");
					contrasenyatxt.setPromptText(promptContrasenya);
					contrasenyatxt.setStyle(
							"-fx-background-color: #365057; -fx-prompt-text-fill: #e8e8e8; -fx-text-fill: #e8e8e8;");
					System.out.println("Aqio bajo");
				}else {
					if (resultado.isPresent() && resultado.get() == login) {
						RegistreController rcAccedirLogin = new RegistreController();
						rcAccedirLogin.accedirLogin(e);

					}
				}
			}

		} catch (Exception error) {
			System.out.println("Error: " + error);
		}
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
