package application;

import java.util.Random;

public class Tabla {

	public int contadorCreadas;
	public int contadorMuertes;

	private int maxGen;
	private int longMatriz;
	private int minimoCelulas;
	private int maximoCelulas;

	private int generaciones = 1;
	private String[][] matriu_actual;
	private String[][] matriu_posterior;
	private String[][] matriu_anterior;
	private String[][] estados;

	public Tabla(int longMatriz, int maxGen, int minimoCelulas, int maximoCelulas) {
		this.longMatriz = longMatriz;
		this.maxGen = maxGen;
		this.minimoCelulas = minimoCelulas;
		this.maximoCelulas = maximoCelulas;

		this.matriu_actual = new String[longMatriz][longMatriz];
		this.matriu_posterior = new String[longMatriz][longMatriz];
		this.matriu_anterior = new String[longMatriz][longMatriz];
		this.estados = new String[longMatriz][longMatriz];

	}

	
	
	public String[][] getMatriu_anterior() {
		return matriu_anterior;
	}



	public void setMatriu_anterior(String[][] matriu_anterior) {
		this.matriu_anterior = matriu_anterior;
	}



	public String[][] getEstados() {
		return estados;
	}


	public void setEstados(String[][] estados) {
		this.estados = estados;
	}

	public int getLongMatriz() {
		return longMatriz;
	}

	public int getMaxGen() {
		return maxGen;
	}

	public int getMinimoCelulas() {
		return minimoCelulas;
	}

	public int getMaximoCelulas() {
		return maximoCelulas;
	}

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
		for (int i = 0; i < aleatorio.nextInt(minimoCelulas, maximoCelulas); i++) {
			int numero = aleatorio.nextInt(0, matriu_actual.length);
			int numero2 = aleatorio.nextInt(0, matriu_actual.length);
			if (matriu_actual[numero][numero2] != "*") {
				matriu_actual[numero][numero2] = "*";
				estados[numero][numero2] = "recienNacida";
				contadorCreadas++;
			} else {
				i--;
			}

		}
		for(int i = 0;i<matriu_actual.length;i++) {
			for(int j = 0;j<matriu_actual.length;j++) {
				if(matriu_actual[i][j] == null) {
					estados[i][j] = "muerta";
				}
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
						estados[i][j] = "recienNacida";
						contadorCreadas++;
					} else {
						matriu_posterior[i][j] = null;
						estados[i][j] = "muerta";
					}
				} else {
					if (contadorVecinas < 2 || contadorVecinas > 3) {
						matriu_posterior[i][j] = null;
						estados[i][j] = "recienMuerta";
						contadorMuertes++;
					} else {
						matriu_posterior[i][j] = "*";
						estados[i][j] = "viva";
					}
				}
			}
		}
		for (int i = 0; i < matriu_actual.length; i++) {
			for (int j = 0; j < matriu_actual[i].length; j++) {
				matriu_anterior[i][j] = matriu_actual[i][j];
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

	public void reiniciar() {
		for (int i = 0; i < matriu_actual.length; i++) {
			for (int j = 0; j < matriu_actual[i].length; j++) {
				if (matriu_actual[i][j] != null) {
					matriu_actual[i][j] = null;
					matriu_posterior[i][j] = null;
				}
			}
		}
		generaciones = 1;
		contadorCreadas = 0;
		contadorMuertes = 0;
	}

	public boolean comparar() {

		boolean continuar = true;
		int contador = 0;
		for (int i = 0; i < matriu_actual.length; i++) {
			for (int j = 0; j < matriu_actual[i].length; j++) {
				if (matriu_actual[i][j] == matriu_anterior[i][j]) {
					contador++;
					System.out.println(contador);
				}
			}
		}
		if(contador == Math.pow(matriu_actual.length, 2)) {
			continuar = false;
		}
		contador = 0;
		return continuar;
	
	}
}
