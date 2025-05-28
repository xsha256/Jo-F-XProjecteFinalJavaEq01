package application;

import java.util.Random;

public class Tablero {

	private String[][] matriz_arriba;
	private String[][] matriz_abajo;
	private int longitudHorizontal;
	private int longitudVertical;
	private int minas;

	public int getMinas() {
		return minas;
	}

	public void setMinas(int minas) {
		this.minas = minas;
	}

	public String[][] getMatriz_arriba() {
		return matriz_arriba;
	}

	public String[][] getMatriz_abajo() {
		return matriz_abajo;
	}

	public void setMatriz_arriba(String[][] matriz_arriba) {
		this.matriz_arriba = matriz_arriba;
	}

	public void setMatriz_abajo(String[][] matriz_abajo) {
		this.matriz_abajo = matriz_abajo;
	}

	public int getLongitudHorizontal() {
		return longitudHorizontal;
	}

	public int getLongitudVertical() {
		return longitudVertical;
	}

	public void setLongitudHorizontal(int longitudHorizontal) {
		this.longitudHorizontal = longitudHorizontal;
	}

	public void setLongitudVertical(int longitudVertical) {
		this.longitudVertical = longitudVertical;
	}

	public Tablero(int longitudHorizontal, int longitudVertical, int minas) {
		super();
		this.longitudHorizontal = longitudHorizontal;
		this.longitudVertical = longitudVertical;
		this.minas = minas;

		this.matriz_abajo = new String[longitudHorizontal][longitudVertical];
		this.matriz_arriba = new String[longitudHorizontal][longitudVertical];
	}
	public Tablero(String [][] matriz_abajo) {
		this.matriz_abajo = matriz_abajo;
	}

	public void asignarColores() {
		for (int i = 0; i < matriz_arriba.length; i++) {
			for (int j = 0; j < matriz_arriba.length; j++) {
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						matriz_arriba[i][j] = "*";
					} else {
						matriz_arriba[i][j] = "+";
					}
				} else {
					if (j % 2 != 0) {
						matriz_arriba[i][j] = "*";
					} else {
						matriz_arriba[i][j] = "+";
					}
				}
			}
		}
	}

	public void llenarMinas() {
		Random aleatorio = new Random();
		for (int i = 0; i < minas; i++) {
			int numero = aleatorio.nextInt(longitudVertical);
			int numero2 = aleatorio.nextInt(longitudHorizontal);
			if (matriz_abajo[numero2][numero] != null) {
				i--;
			} else {
				matriz_abajo[numero2][numero] = "x";
			}
		}
	}

	public void llenarMinasIniciales(int fila,int columna) {
		Random aleatorio = new Random();
		for (int i = 0; i < minas; i++) {
			int numero = aleatorio.nextInt(longitudVertical);
			int numero2 = aleatorio.nextInt(longitudHorizontal);
			if (matriz_abajo[numero2][numero] != null || (numero2 == fila && numero == columna)) {
				i--;
			} else {
				matriz_abajo[numero2][numero] = "x";
			}
		}
	}

	public void asignarNumeros() {
		for (int i = 0; i < matriz_abajo.length; i++) {
			for (int j = 0; j < matriz_abajo.length; j++) {
				if (matriz_abajo[i][j] == null) {
					matriz_abajo[i][j] = contarMinas(i, j);
				}
			}
		}
	}

	public String contarMinas(int x, int y) {
		int numero = 0;

		int filas = matriz_abajo.length;
		int columnas = matriz_abajo[0].length;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0)
					continue;

				int nx = x + i;
				int ny = y + j;
				if (nx >= 0 && nx < filas && ny >= 0 && ny < columnas) {
					if (matriz_abajo[nx][ny] != null && !matriz_abajo[nx][ny].equals("+")
							&& !matriz_abajo[nx][ny].matches("[0-9]")) {
						numero++;
					}
				}
			}
		}

		return "" + numero;

	}

}
