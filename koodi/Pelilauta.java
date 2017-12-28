
/**
 * Pelilauta-luokka hallinnoi pelialueen ruutuja ja ohjaa käsivartta, jonka avulla robotti liikkuu pelilaudalla. 
 * 
 * @author mshroom
 *
 */
public class Pelilauta {

	private Ruutu[][] ruudukko;
	private Kasivarsi kasi;
		
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
	}
	
	/**
	 * Alustetaan pelialueen ruudut asettamalla jokaiselle Ruutu-oliolle ruudun keskipistettä vastaavat moottorien kulmat.
	 */	
	private void alustaRuudut() {
		this.ruudukko[0][0].setMoottorinKulmat(95, -115);
		this.ruudukko[0][1].setMoottorinKulmat(75, -110);
		this.ruudukko[0][2].setMoottorinKulmat(60, -110);
		this.ruudukko[1][0].setMoottorinKulmat(80, -70);
		this.ruudukko[1][1].setMoottorinKulmat(55, -55);
		this.ruudukko[1][2].setMoottorinKulmat(35, -55);
		this.ruudukko[2][0].setMoottorinKulmat(85, -40);
		this.ruudukko[2][1].setMoottorinKulmat(55, -25);
		this.ruudukko[2][2].setMoottorinKulmat(20, -25);
	}
	
	/**
	 * Ohjataan käsivarsi liikkumaan haluttuun ruutuun.
	 * @param x rivi (0, 1 tai 2 ylhäältä alas)
	 * @param y sarake (0, 1 tai 2 vasemmalta oikealle)
	 */
	public void kuljeRuutuun(int x, int y) {
		Ruutu ruutu = this.ruudukko[x][y];
		kasi.liiku(ruutu.getMoottorinKulmaA(), ruutu.getMoottorinKulmaB());
	}
  
	/**
	 * Palautetaan käsivarsi alkuasentoon pelilaudan ulkopuolelle.
	 */
	public void palaaKotiin() {
		kasi.liiku(0, 0);
	}
	
}
