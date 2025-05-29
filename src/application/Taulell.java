package application;

import java.io.Serializable;

public class Taulell implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ample;
	private int altura;
	private Casella[][] caselles;

	public int getAmple() {
		return this.ample;
	}
	public int getAltura() {
		return this.altura;
	}
	public void setAmple(int ample) {
		this.ample = ample;
	}
	public void setAltura(int altura) {
		this.altura = altura;
	}
	
	public Casella[][] getCaselles() {
		return caselles;
	}
	public void setCaselles(Casella[][] caselles) {
		this.caselles = caselles;
	}
	
	public Taulell(int ample, int altura) {

		this.ample = ample;
		this.altura = altura;
	}
	public Taulell(Casella[][] caselles) {
	    this.altura = caselles.length;
	    this.ample = caselles[0].length;
	    this.setCaselles(caselles);
	}


}
