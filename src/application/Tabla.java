package application;

import java.util.Random;

public class Tabla {

	public static final int MAX_GEN = 200;
	public static final int LONG_MATRIZ = 10;

	private int generaciones = 1;
	private String[][] matriu_actual = new String[LONG_MATRIZ][LONG_MATRIZ];
	private String[][] matriu_posterior = new String[LONG_MATRIZ][LONG_MATRIZ];

	public int contadorCreadas;
	public int contadorMuertes;

	
	
	public int getContadorCreadas() {
		return contadorCreadas;
	}

	public int getContadorMuertes() {
		return contadorMuertes;
	}

	public void setContadorCreadas(int contadorCreadas) {
		this.contadorCreadas = contadorCreadas;
	}

	public void setContadorMuertes(int contadorMuertes) {
		this.contadorMuertes = contadorMuertes;
	}

	public int getGeneraciones() {
		return generaciones;
	}

	public String[][] getMatriu_actual() {
		return matriu_actual;
	}

	public String[][] getMatriu_posterior() {
		return matriu_posterior;
	}

	public void setGeneraciones(int generaciones) {
		this.generaciones = generaciones;
	}

	public void setMatriu_actual(String[][] matriu_actual) {
		this.matriu_actual = matriu_actual;
	}

	public void setMatriu_posterior(String[][] matriu_posterior) {
		this.matriu_posterior = matriu_posterior;
	}

	public Tabla() {

	}

	public void llenarMatrizInicial() {
		Random aleatorio = new Random();
		for (int i = 0; i < aleatorio.nextInt(25,50); i++) {
			int numero = aleatorio.nextInt(0, matriu_actual.length);
			int numero2 = aleatorio.nextInt(0, matriu_actual.length);
			if (matriu_actual[numero][numero2] != "*") {
			    matriu_actual[numero][numero2] = "*";
			    contadorCreadas++;
			} else {
			    i--;
			}


		}
	}

	public void cambiarMatriz() {
		int filas = matriu_actual.length;
		int columnas = matriu_actual[0].length;
		int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };
		for (int i = 0; i < matriu_actual.length; i++) {
			for (int j = 0; j < matriu_actual.length; j++) {
				int contadorVecinas = 0;
				for (int k = 0; k < 8; k++) {
					int ni = i + dx[k];
					int nj = j + dy[k];

					if (ni >= 0 && ni < filas && nj >= 0 && nj < columnas && matriu_actual[ni][nj] != null) {
						contadorVecinas++;
					}
				}
				if (matriu_actual[i][j] == null) {
					if (contadorVecinas == 3) {
						matriu_posterior[i][j] = "*";
						contadorCreadas++;
					} else {
						matriu_posterior[i][j] = null;
					}
				} else {
					if (contadorVecinas < 2 || contadorVecinas > 3) {
						matriu_posterior[i][j] = null;
						contadorMuertes++;
					} else {
						matriu_posterior[i][j] = "*";
					}
				}
			}
		}
		for (int i = 0; i < matriu_actual.length; i++) {
			for (int j = 0; j < matriu_actual[i].length; j++) {
				matriu_actual[i][j] = matriu_posterior[i][j];
			}
		}
		generaciones++;
	}


	public int contarCelulas() {
		int numero = 0;
		for (int i = 0; i < matriu_actual.length; i++) {
			for (int j = 0; j < matriu_actual[i].length; j++) {
				if (matriu_actual[i][j] != null) {
					numero++;
				}
			}
		}
		return numero;
	}


}
