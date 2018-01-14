
import lejos.nxt.*;

/**
 * Tämä luokka toteuttaa robotin käsivarren liikkeet.
 * 
 * @author mshroom
 *
 */

public class Kasivarsi {
	
	/** Moottori A on kytketty A-porttiin ja vapauttaa robotin kourasta yhden pelimerkin. */
	private NXTRegulatedMotor moottoriA;
	/** Moottori B on kytketty B-porttiin ja liikuttaa robotin kyynärvartta. */
	private NXTRegulatedMotor moottoriB;
	/** Moottori C on kytketty C-porttiin ja liikuttaa robotin olkavartta. */
	private NXTRegulatedMotor moottoriC;
	
	/**
	 * Konstruktorissa alustetaan robotin käsivartta liikuttavat moottorit.
	 */
	public Kasivarsi() {
		this.moottoriA = Motor.A;
		this.moottoriB = Motor.B;
		this.moottoriC = Motor.C;
		moottoriA.setSpeed(20);
		moottoriB.setSpeed(50);
		moottoriC.setSpeed(30);
	}
	
	/**
	 * Liikutetaan käsivartta kääntämällä molemmat moottorit haluttuun asentoon.
	 * @param c moottori C:n haluttu kulma
	 * @param b moottori B:n haluttu kulma
	 */
	public void liiku(int c, int b) {
		moottoriC.rotateTo(c);
		moottoriB.rotateTo(b);		
	}
	
	/**
	 * Liikutetaan olkavartta kääntämällä moottoria C.
	 * @param c moottori C:n haluttu kulma
	 */
	public void liikuSuunnassaC(int c) {
		moottoriC.rotateTo(c);
	}
	
	/**
	 * Liikutetaan kyynärvartta kääntämällä moottoria B.
	 * @param b moottori B:n haluttu kulma
	 */
	public void liikuSuunnassaB(int b) {
		moottoriB.rotateTo(b);
	}
	
	/**
	 * Vapautetaan kourasta yksi pelimerkki pyöräyttämällä moottoria A.
	 */
	public void pudotaMerkki() {
		moottoriA.rotate(-90);
	}
}
