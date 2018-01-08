import lejos.nxt.*;
import lejos.util.Delay;

/**
 * Pelilauta-luokka hallinnoi pelialueen ruutuja ja ohjaa käsivartta, jonka avulla robotti liikkuu pelilaudalla ja jättää pelimerkkejään. 
 * 
 * @author mshroom
 *
 */
public class Pelilauta {

	private Ruutu[][] ruudukko;
	private Kasivarsi kasi;
	private LightSensor valosensori;
		
	/**
	 * Konstruktorissa alustetaan käsivarsi sekä 3 * 3 ruudun kokoinen pelialue. Jokaista ruutua varten luodaan oma Ruutu-olio. 
	 */
	public Pelilauta() {
		this.ruudukko = new Ruutu[3][3];
		for (int x = 0; x < this.ruudukko.length; x ++) {
			for (int y = 0; y < this.ruudukko.length; y ++) {
				this.ruudukko[x][y] = new Ruutu(x, y);
			}
		}
		this.alustaRuudut();
		this.kasi = new Kasivarsi();
		this.valosensori = new LightSensor(SensorPort.S1);
	}
	
	/**
	 * Kalibroidaan valosensori mustalla alustalla (kotiruutu) ja valkoisella alustalla (ensimmäinen peliruutu).
	 */
	public void kalibroiSensori() {
		valosensori.calibrateLow();
		this.kuljeRuutuun(2, 2);
		Delay.msDelay(1000);
		valosensori.calibrateHigh();
		Delay.msDelay(1000);
		this.palaaKotiin();
		Delay.msDelay(1000);
	}

	/**
	 * Alustetaan pelialueen ruudut asettamalla jokaiselle Ruutu-oliolle ruudun keskipistettä vastaavat moottorien kulmat.
	 */	
	private void alustaRuudut() {
		this.ruudukko[0][0].setMoottorinKulmat(102, -87);
		this.ruudukko[0][1].setMoottorinKulmat(77, -67);
		this.ruudukko[0][2].setMoottorinKulmat(60, -73);
		this.ruudukko[1][0].setMoottorinKulmat(95, -32);
		this.ruudukko[1][1].setMoottorinKulmat(67, -15);
		this.ruudukko[1][2].setMoottorinKulmat(37, -15);
		this.ruudukko[2][0].setMoottorinKulmat(100, 10);
		this.ruudukko[2][1].setMoottorinKulmat(75, 33);
		this.ruudukko[2][2].setMoottorinKulmat(28, 28);
	}
	
	/**
	 * Ohjataan käsivarsi liikkumaan haluttuun ruutuun.
	 * @param x rivi (0, 1 tai 2 ylhäältä alas)
	 * @param y sarake (0, 1 tai 2 vasemmalta oikealle)
	 */
	public void kuljeRuutuun(int x, int y) {
		Ruutu ruutu = this.ruudukko[x][y];
		kasi.liiku(ruutu.getMoottorinKulmaC(), ruutu.getMoottorinKulmaB());
	}
	
	/**
	 * Palautetaan käsivarsi alkuasentoon pelilaudan ulkopuolelle.
	 */
	public void palaaKotiin() {
		kasi.liiku(0, 0);
	}
	
	/**
	 * Jätetään pelimerkki siihen ruutuun, jossa käsivarsi parhaillaan on.
	 */
	public void jataPelimerkki() {
		kasi.pudotaMerkki();
	}
	
	/**
	 * Viedään pelimerkki ruutuun, jonka koordinaatit annetaan parametreina.
	 * @param x rivi (0, 1 tai 2 ylhäältä alas)
	 * @param y sarake (0, 1 tai 2 vasemmalta oikealle)
	 */
	public void viePelimerkki(int x, int y) {
		this.kuljeRuutuun(x, y);
		Delay.msDelay(1000);
		this.jataPelimerkki();
		Delay.msDelay(1000);
		this.palaaKotiin();
		Delay.msDelay(1000);
	}
	
	/**
	 * Tutkitaan onko siinä ruudussa, jossa käsivarsi parhaillaan on, vastustajan pelimerkkiä.
	 * @return true, jos valosensori tunnistaa ruudussa pelimerkin.
	 */
	public boolean onkoVastustajanMerkki() {
		if (valosensori.getLightValue() < 80) {
			return true;
		} else {
			return false;
		}
	}
	
}
