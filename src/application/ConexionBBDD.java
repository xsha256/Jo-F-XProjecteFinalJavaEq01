package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class  ConexionBBDD {
	public static Connection conectar() {
		
		try {
		Class.forName("org.mariadb.jdbc.Driver");
		String urlBaseDades = "jdbc:mariadb://localhost:3306/jofx";
		String user = "root";
		String pwd = "root";
		Connection c = DriverManager.getConnection(urlBaseDades, user, pwd);
		return c;
		} catch (Exception e) {
			System.out.println("Error conexion: "+ e);
		}
		return null;
	}
}
