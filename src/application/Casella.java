package application;

public class Casella {

	//ATRIBUTS
	private String color;
	private boolean ocupat;
	private int altura;
	private int amplada;
	public String getColor() {
		return color;
	}
	public boolean isOcupat() {
		return ocupat;
	}
	public int getAltura() {
		return altura;
	}
	public int getAmplada() {
		return amplada;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setOcupat(boolean ocupat) {
		this.ocupat = ocupat;
	}
	public void setAltura(int altura) {
		this.altura = altura;
	}
	public void setAmplada(int amplada) {
		this.amplada = amplada;
	}
	public Casella(String color, boolean ocupat, int altura, int amplada) {
		super();
		this.color = color;
		this.ocupat = ocupat;
		this.altura = altura;
		this.amplada = amplada;
	}
	
	
	
	
}
