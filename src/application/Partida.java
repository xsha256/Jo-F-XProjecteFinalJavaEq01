package application;

import java.io.Serializable;

import javafx.scene.control.Label;

public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;

    private int contadorCasillasAbiertas;
    private int banderasRestantes;
    private String tamaño;
    private String[][] matriz_abajo;
    private int id;

    private transient Label[][] etiquetas;
    private String[][] etiquetasId;
    private String[][] etiquetasStyle;
    private String[][] etiquetasGraphic;

    private String tiempo;
    private String fechaFormateada;

    public Partida(int contadorCasillasAbiertas, int banderasRestantes, String tamaño, String[][] matriz_abajo) {
        this.contadorCasillasAbiertas = contadorCasillasAbiertas;
        this.banderasRestantes = banderasRestantes;
        this.tamaño = tamaño;
        this.matriz_abajo = matriz_abajo;
    }

    public Partida(String tiempo, String fechaFormateada, String tamaño,int id) {
        this.tiempo = tiempo;
        this.fechaFormateada = fechaFormateada;
        this.tamaño = tamaño;
        this.id = id;
    }

    
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }


    public int getContadorCasillasAbiertas() {
        return contadorCasillasAbiertas;
    }

    public int getBanderasRestantes() {
        return banderasRestantes;
    }

    public String getTamaño() {
        return tamaño;
    }

    public String[][] getMatriz_abajo() {
        return matriz_abajo;
    }

    public Label[][] getEtiquetas() {
        return etiquetas;
    }

    public void setContadorCasillasAbiertas(int contadorCasillasAbiertas) {
        this.contadorCasillasAbiertas = contadorCasillasAbiertas;
    }

    public void setBanderasRestantes(int banderasRestantes) {
        this.banderasRestantes = banderasRestantes;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }

    public void setMatriz_abajo(String[][] matriz_abajo) {
        this.matriz_abajo = matriz_abajo;
    }

    public void setEtiquetas(Label[][] etiquetas) {
        this.etiquetas = etiquetas;

        if (etiquetas != null) {
            int filas = etiquetas.length;
            int columnas = etiquetas[0].length;
            etiquetasId = new String[filas][columnas];
            etiquetasStyle = new String[filas][columnas];
            etiquetasGraphic = new String[filas][columnas];
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    etiquetasId[i][j] = etiquetas[i][j].getId();
                    etiquetasStyle[i][j] = etiquetas[i][j].getStyle();
                    if(etiquetas[i][j].getGraphic() != null) {
                    	etiquetasGraphic[i][j] = "*";
                    }
                }
            }
        }
    }

    
    
    public String[][] getEtiquetasGraphic() {
		return etiquetasGraphic;
	}

	public void setEtiquetasGraphic(String[][] etiquetasGraphic) {
		this.etiquetasGraphic = etiquetasGraphic;
	}

	public String[][] getEtiquetasId() {
        return etiquetasId;
    }

    public String[][] getEtiquetasStyle() {
        return etiquetasStyle;
    }
}
