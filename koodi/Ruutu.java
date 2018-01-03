
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
	/** Olkavartta liikuttavan C-moottorin kulma sensorin ollessa ruudun keskikohdassa. */
	private int moottorinKulmaC;
	/** Kyynärvartta liikuttavan B-moottorin kulma sensorin ollessa ruudun keskikohdassa. */
	private int moottorinKulmaB;
	
	public Ruutu(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setMoottorinKulmat(int asteC, int asteB) {
		this.moottorinKulmaC = asteC;
		this.moottorinKulmaB = asteB;
	}
	
	public int getMoottorinKulmaC() {
		return this.moottorinKulmaC;
	}
	
	public int getMoottorinKulmaB() {
		return this.moottorinKulmaB;
	}
	
}
