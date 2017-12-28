
/**
 * Ruutu-luokan avulla hallitaan peliruudun tietoja.
 * 
 * @author mshroom
 *
 */

public class Ruutu {

	/** Rivi (0, 1 tai 2 ylhäältä alas). */
	private int x;
	/** Sarake (0, 1 tai 2 vasemmalta oikealle). */
	private int y;
	/** Olkavartta liikuttavan A-moottorin kulma sensorin ollessa ruudun keskikohdassa. */
	private int moottorinKulmaA;
	/** Kyynärvartta liikuttavan B-moottorin kulma sensorin ollessa ruudun keskikohdassa. */
	private int moottorinKulmaB;
	
	public Ruutu(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setMoottorinKulmat(int asteA, int asteB) {
		this.moottorinKulmaA = asteA;
		this.moottorinKulmaB = asteB;
	}
	
	public int getMoottorinKulmaA() {
		return this.moottorinKulmaA;
	}
	
	public int getMoottorinKulmaB() {
		return this.moottorinKulmaB;
	}
	
}
