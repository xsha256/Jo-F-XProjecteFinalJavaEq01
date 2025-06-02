package application;

public class DadesPixelArt {

private Taulell taulell;
	
	private final static DadesPixelArt INSTANCIA =  new DadesPixelArt();
	
	private DadesPixelArt() {} //constructor privat, no es pot utilitzar externament
	
	public static DadesPixelArt getInstancia() {
		return INSTANCIA;
	}
	
	public void setTaulell(Taulell t) {
		this.taulell = t;
	}
	
	public Taulell getTaulell() {
		return this.taulell;
	}
}
