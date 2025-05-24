package application;

public class Casella {

	//ATRIBUTS
	private String color;
	private boolean ocupat;
	private int grandaria;
	
	
	public String getColor() {
		return color;
	}
	public boolean isOcupat() {
		return ocupat;
	}
	public int getGrandaria() {
		return grandaria;
	}

	public void setColor(String color) {
		this.color = color;
	}
	public void setOcupat(boolean ocupat) {
		this.ocupat = ocupat;
	}
	public void setGrandaria(int grandaria) {
		this.grandaria = grandaria;
	}

	public Casella(String color, boolean ocupat, int grandaria) {

		this.color = color;
		this.ocupat = ocupat;
		this.grandaria = grandaria;
		
	}
	
	
	
	
}
