
import lejos.nxt.*;

/**
 * Tämä luokka toteuttaa robotin käsivarren liikkeet.
 * 
 * @author mshroom
 *
 */

public class Kasivarsi {
	
	/** Moottori A on kytketty A-porttiin ja liikuttaa robotin olkavartta. */
	private NXTRegulatedMotor moottoriA;
	/** Moottori B on kytketty B-porttiin ja liikuttaa robotin kyynärvartta. */
	private NXTRegulatedMotor moottoriB;
	
	/**
	 * Konstruktorissa alustetaan robotin käsivartta liikuttavat moottorit.
	 */
	public Kasivarsi() {
		this.moottoriA = Motor.A;
		this.moottoriB = Motor.B;
		moottoriA.setSpeed(50);
		moottoriB.setSpeed(70);
	}
	
	/**
	 * Liikutetaan käsivartta kääntämällä molemmat moottorit haluttuun asentoon.
	 * @param a moottori A:n haluttu kulma
	 * @param b moottori B:n haluttu kulma
	 */
	public void liiku(int a, int b) {
		moottoriA.rotateTo(a);
		moottoriB.rotateTo(b);		
	}
	
	/**
	 * Liikutetaan olkavartta kääntämällä moottoria A.
	 * @param a moottori A:n haluttu kulma
	 */
	public void liikuSuunnassaA(int a) {
		moottoriA.rotateTo(a);
	}
	
	/**
	 * Liikutetaan kyynärvartta kääntämällä moottoria B.
	 * @param b moottori B:n haluttu kulma
	 */
	public void liikuSuunnassaB(int b) {
		moottoriB.rotateTo(b);
	}
}
