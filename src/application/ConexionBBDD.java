package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class  ConexionBBDD {
	public static Connection conectar() {
		
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");//per al ordenador de casa
		//Class.forName("org.mariadb.jdbc.Driver");//per a classe
		String urlBaseDades = "jdbc:mysql://localhost:3306/jofx";//"jdbc:mariadb://localhost:3306/jofx";
		String user = "root";
		String pwd = "helena";
		Connection c = DriverManager.getConnection(urlBaseDades, user, pwd);
		return c;
		} catch (Exception e) {
			System.out.println("Error conexion: "+ e);
		}
		System.out.println("hola");
		return null;
	}
} //Connection c= ConexionBBDD.conectar();