import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import java.util.Random;

/**
 * Tämä luokka toteuttaa ristinollapelin. Se muistaa pelitilanteen ja ohjaa robottia tekemään tilanteeseen sopivan siirron.
 * 
 * @author mshroom
 *
 */
public class Peli {
	/** Ruudukkoon on tallennettu pelitilanne. 0 = tyhjä ruutu, 1 = risti (robotti), 2 = nolla (ihminen). */
	private int[][] ruudukko;
	/** Pelilaudan kautta hallitaan fyysistä pelialuetta ja robottia. */
	private Pelilauta pelilauta;
	/** Pelianalyysin apuna käytetään tietoa siitä, kuinka mones siirto pelissä on menossa. 1 = aloitussiirto, 2 = vastapelaajan ensimmäinen siirto jne. */
	private int vuoro;
	/** Tulos kertoo pelin voittajan. -1 = peli kesken, 0 = tasapeli, 1 = risti, 2 = nolla. */
	private int tulos;
	/** Vaikeustaso 1 = helppo, 2 = vaikea. */
	private int vaikeustaso;
	
	/**
	 * Konstruktorissa alustetaan pelitilanne ja pelilauta.
	 */
	public Peli(int vaikeustaso) {
		this.ruudukko = new int[3][3];
		this.pelilauta = new Pelilauta();
		this.vuoro = 1;
		this.tulos = -1;
		this.vaikeustaso = vaikeustaso;
	}
	
	/**
	 * Pyydetään pelilautaa suorittamaan valosensorin kalibrointi.
	 */
	public void kalibroiPelilauta() {
		this.pelilauta.kalibroiSensori();
	}
	
	/**
	 * Toteutetaan siirto, jonka robotti on päättänyt tehdä päivittämällä pelimuuttujat ja antamalla pelilaudalle käsky viedä pelimerkki ruutuun.
	 * @param x rivi (0, 1 tai 2 ylhäältä alas)
	 * @param y sarake (0, 1 tai 2 vasemmalta oikealle)
	 */
	public void risti(int x, int y) {
		this.ruudukko[x][y] = 1;
		this.vuoro ++;
		pelilauta.viePelimerkki(x, y);
	}
	
	/**
	 * Käydään läpi kaikki tyhjänä olleet ruudut ja tarkistetaan, onko niissä vastustajan pelimerkkiä. Jos merkki löytyy, päivitetään pelimuuttujat.
	 * @return true, mikäli uusi pelimerkki havaitaan.
	 */
	public boolean paivitaPelitilanne() {
		LCD.clear();
		LCD.drawString("Tutkitaan", 0, 0);
		for (int x = 0; x < 3; x ++) {
			for (int y = 0; y < 3; y ++) {
				if (this.ruudukko[x][y] == 0) {
					pelilauta.kuljeRuutuun(x, y);
					Delay.msDelay(2000);
					if (pelilauta.onkoVastustajanMerkki()) {
						this.ruudukko[x][y] = 2;
						this.vuoro ++;
						Delay.msDelay(2000);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Tehdään robotin siirto. Jos vaikeustaso on helppo, arvotaan tehdäänkö satunnainen siirto vai optimoitu siirto. Optimoidussa siirrossa käydään ensin läpi erikoistilanteet. Jos ne eivät aiheuta toimenpiteitä, analysoidaan paras siirto ja tehdään se.
	 */
	public void siirto() {
		LCD.clear();
		LCD.drawString("Robotin vuoro", 0, 0);
		if (this.vaikeustaso == 1) {
			Random arpa = new Random();
			int arvottu = arpa.nextInt(2);
			if (arvottu == 0) {
				this.randomSiirto();
				return;
			}
		}
		if (this.vuoro == 1) {
			this.aloitusSiirto();
			return;
		} else if (this.vuoro == 2) {
			this.tokaSiirto();
			return;
		} else if (this.vuoro == 3) {
			if (this.ruudukko[0][0] == 1 || this.ruudukko[0][2] == 1 || this.ruudukko[2][0] == 1 || this.ruudukko[2][2] == 1) {
				if (this.ruudukko[0][0] == 2 || this.ruudukko[0][2] == 2 || this.ruudukko[2][0] == 2 || this.ruudukko[2][2] == 2) {
					this.kulmaSiirto();
					return;
				}				
			}					
		}
		this.laskeParasSiirto();				
	}
	
	/**
	 * Tehdään satunnainen siirto johonkin tyhjään ruutuun.
	 */
	public void randomSiirto() {
		Random arpa = new Random();
		int arvottu = arpa.nextInt(this.vapaitaRuutuja());
		for (int x = 0; x < 3; x ++) {
			for (int y = 0; y < 3; y ++) {
				if (this.ruudukko[x][y] == 0) {
					if (arvottu == 0) {
						this.risti(x, y);
						return;
					} else {
						arvottu --;
					}
				}
			}
		}
	}
	
	/**
	 * Kun robotti aloittaa pelin, se tekee aloitussiirron. Robotti aloittaa pelin joko keskeltä tai jostain kulmasta.
	 */
	private void aloitusSiirto() {
		Random arpa = new Random();
		if (arpa.nextInt(2) == 1) {
			this.risti(1, 1);
		} else {
			int kulma = arpa.nextInt(4);
			if (kulma == 0) {
				this.risti(2, 2);
			} else if (kulma == 1) {
				this.risti(2, 0);
			} else if (kulma == 2) {
				this.risti(0, 2);
			} else {
				this.risti(0, 0);
			}
		}
	}
	
	/**
	 * Jos ihminen on aloittanut pelin keskeltä, robotti laittaa ensimmäisen merkkinsä johonkin kulmaan. Muussa tapauksessa se laittaa merkin keskelle.
	 */
	private void tokaSiirto() {
		Random arpa = new Random();
		if (this.ruudukko[1][1] == 2) {
			int kulma = arpa.nextInt(4);
			if (kulma == 0) {
				this.risti(2, 2);
			} else if (kulma == 1) {
				this.risti(2, 0);
			} else if (kulma == 2) {
				this.risti(0, 2);
			} else {
				this.risti(0, 0);
			}
		} else {
			this.risti(1, 1);
		}
	}
	
	/**
	 * Kulmasiirrossa robotti laittaa merkin mihin tahansa tyhjään kulmaan. Siirto toimii vain jos pelissä on tyhjiä kulmia.
	 */
	private void kulmaSiirto() {
		if (this.ruudukko[0][0] == 0) {
			this.risti(0, 0);
			return;
		} else if (this.ruudukko[0][2] == 0) {
			this.risti(0, 2);
			return;
		} else if (this.ruudukko[2][0] == 0) {
			this.risti(2, 0);
			return;
		} else if (this.ruudukko[2][2] == 0) {
			this.risti(2, 2);
		}
	}
	
	/**
	 * Lasketaan ja suoritetaan paras siirto. Paras siirto lasketaan pisteyttämällä tyhjät ruudut sen mukaan, kuinka paljon leikkaavilla suorilla on omia ja vastustajan pelimerkkejä.
	 */
	private void laskeParasSiirto() {
		int parasX = -1;
		int parasY = -1;
		int parhaatPisteet = 0;
		
		for (int x = 0; x < 3; x ++) {
			for (int y = 0; y < 3; y ++) {				
				if (ruudukko[x][y] == 0) {
					int pisteet = 0;
					if (parasX == -1) {
						parasX = x;
						parasY = y;
					}
					if (this.rivillaMerkkeja(x, 1, 2)) {
						pisteet += 20;
					}
					if (this.sarakkeessaMerkkeja(y, 1, 2)) {
						pisteet += 20;
					}
					if (this.rivillaMerkkeja(x, 2, 2)) {
						pisteet += 10;
					}
					if (this.sarakkeessaMerkkeja(y, 2, 2)) {
						pisteet += 10;
					}
					if (this.rivillaMerkkeja(x, 1, 1) && this.rivillaMerkkeja(x, 2, 0)) {
						pisteet += 2;
					}
					if (this.sarakkeessaMerkkeja(y, 1, 1) && this.sarakkeessaMerkkeja(y, 2, 0)) {
						pisteet += 2;
					}
					if (this.rivillaMerkkeja(x, 1, 0) && this.rivillaMerkkeja(x, 2, 1)) {
						pisteet ++;
					}
					if (this.sarakkeessaMerkkeja(y, 1, 0) && this.sarakkeessaMerkkeja(y, 2, 1)) {
						pisteet ++;
					}
					if (this.rivillaMerkkeja(x, 0, 3)) {
						pisteet ++;
					}
					if (this.sarakkeessaMerkkeja(y, 0, 3)) {
						pisteet ++;
					}
					if ((x == 0 && y == 2) || (x == 2 && y == 0) || (x == 1 && y == 1)) {
						if (this.vinottainYlosMerkkeja(1, 2)) {
							pisteet += 20;
						}
						if (this.vinottainYlosMerkkeja(2, 2)) {
							pisteet += 10;
						}
						if (this.vinottainYlosMerkkeja(1, 1) && this.vinottainYlosMerkkeja(0, 2)) {
							pisteet += 2;
						}
						if (this.vinottainYlosMerkkeja(2, 1) && this.vinottainYlosMerkkeja(0, 2)) {
							pisteet ++;
						}
						if (this.vinottainYlosMerkkeja(0, 3)) {
							pisteet ++;
						}
					}
					if ((x == 0 && y == 0) || (x == 2 && y == 2) || (x == 1 && y == 1)) {
						if (this.vinottainAlasMerkkeja(1, 2)) {
							pisteet += 20;
						}
						if (this.vinottainAlasMerkkeja(2, 2)) {
							pisteet += 10;
						}
						if (this.vinottainAlasMerkkeja(1, 1) && this.vinottainAlasMerkkeja(0, 2)) {
							pisteet += 2;
						}
						if (this.vinottainAlasMerkkeja(2, 1) && this.vinottainAlasMerkkeja(0, 2)) {
							pisteet ++;
						}
						if (this.vinottainAlasMerkkeja(3, 0)) {
							pisteet ++;
						}
					}					
					if (pisteet > parhaatPisteet) {
						parhaatPisteet = pisteet;
						parasX = x;
						parasY = y;
					}
				}
				
			}			
		}		
		this.risti(parasX, parasY);					
	}
	
	/**
	 * Tutkitaan, onko tarkasteltavaa merkkiä jokin tietty määrä tietyllä rivillä.
	 * @param rivi Tarkasteltava rivi
	 * @param merkki Tarkasteltava merkki (0, 1 tai 2)
	 * @param maara Kysytty määrä (0, 1, 2 tai 3)
	 * @return true, jos rivillä on kyseisiä merkkejä tasan oikea määrä
	 */
	private boolean rivillaMerkkeja(int rivi, int merkki, int maara) {
		int summa = 0;
		for (int i = 0; i < 3; i ++) {
			if (this.ruudukko[rivi][i] == merkki) {
				summa ++;
			}
		}
		if (summa == maara) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tutkitaan, onko tarkasteltavaa merkkiä jokin tietty määrä tietyssä sarakkeessa.
	 * @param sarake Tarkasteltava sarake
	 * @param merkki Tarkasteltava merkki (0, 1 tai 2)
	 * @param maara Kysytty määrä (0, 1, 2 tai 3)
	 * @return true, jos sarakkeessa on kyseisiä merkkejä tasan oikea määrä.
	 */
	private boolean sarakkeessaMerkkeja(int sarake, int merkki, int maara) {
		int summa = 0;
		for (int i = 0; i < 3; i ++) {
			if (this.ruudukko[i][sarake] == merkki) {
				summa ++;
			}
		}
		if (summa == maara) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tutkitaan, onko tarkasteltavaa merkkiä jokin tietty määrä vasemmalta ylhäältä oikealle alas kulkevalla suoralla
	 * @param merkki Tarkasteltava merkki (0, 1 tai 2)
	 * @param maara (0, 1, 2 tai 3)
	 * @return true, jos vinorivillä on kyseisiä merkkejä tasan oikea määrä.
	 */
	private boolean vinottainAlasMerkkeja(int merkki, int maara) {
		int summa = 0;
		if (this.ruudukko[0][0] == merkki) {
			summa ++;
		}
		if (this.ruudukko[1][1] == merkki) {
			summa ++;
		}
		if (this.ruudukko[2][2] == merkki) {
			summa ++;
		}
		if (summa == maara) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tutkitaan, onko tarkasteltavaa merkkiä jokin tietty määrä vasemmalta alhaalta oikealle ylös kulkevalla suoralla
	 * @param merkki Tarkasteltava merkki (0, 1 tai 2)
	 * @param maara (0, 1, 2 tai 3)
	 * @return true, jos vinorivillä on kyseisiä merkkejä tasan oikea määrä.
	 */
	private boolean vinottainYlosMerkkeja(int merkki, int maara) {
		int summa = 0;
		if (this.ruudukko[0][2] == merkki) {
			summa ++;
		}
		if (this.ruudukko[1][1] == merkki) {
			summa ++;
		}
		if (this.ruudukko[2][0] == merkki) {
			summa ++;
		}
		if (summa == maara) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tutkitaan, onko peli ohi eli onko jompikumpi pelaaja voittanut tai onko ruudukko täysi.
	 * @return true, jos peli on päättynyt.
	 */
	public boolean peliOhi() {
		if (this.voittosuora(1)) {
			this.tulos = 1;
			return true;
		} else if (this.voittosuora(2)) {
			this.tulos = 2;
			return true;
		} else if (this.vapaitaRuutuja() == 0) {
			this.tulos = 0;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tarkistetaan, onko jossain pelilaudalla kolme peräkkäistä samaa pelimerkkiä.
	 * @param merkki Tarkasteltava merkki
	 * @return true, jos kolmen saman pelimerkin suora löytyy.
	 */
	private boolean voittosuora(int merkki) {		
		for (int i = 0; i < 3; i ++) {
			if (this.rivillaMerkkeja(i, merkki, 3)) {
				return true;
			}
			if (this.sarakkeessaMerkkeja(i, merkki, 3)) {
				return true;
			}
		}
		if (this.vinottainAlasMerkkeja(merkki, 3)) {
			return true;
		}
		if (this.vinottainYlosMerkkeja(merkki, 3)) {
			return true;
		}
		return false;
	}
	

	
	/**
	 * Tutkitaan, kuinka monta vapaata ruutua on jäljellä.
	 * @return vapaiden ruutujen määrä.
	 */
	private int vapaitaRuutuja() {
		int vapaita = 0;
		for (int x = 0; x < 3; x ++) {
			for (int y = 0; y < 3; y ++) {
				if (this.ruudukko[x][y] == 0) {
					vapaita ++;
				}
			}
		}
		return vapaita;
	}
	
	/**
	 * Virheilmoitus. Käytetään testauksessa sekä silloin, kun robotti ei löydä vastustajan pelimerkkiä.
	 */
	public void ilmoitaVirheesta() {
		this.pelilauta.palaaKotiin();
		Sound.buzz();
	}	
	
	/**
	 * Ilmoitetaan pelin lopputulos merkkiäänellä ja näytölle tulostettavalla tekstillä.
	 */
	public void kerroTulos() {
		LCD.clear();
		this.pelilauta.palaaKotiin();
		if (this.tulos == 0) {
			  LCD.drawString("Tasapeli", 0, 0);
			  Sound.playTone(294, 100);
			  Sound.pause(105);
			  Sound.playTone(311, 100);
			  Sound.pause(105);
			  Sound.playTone(349, 100);
			  Sound.pause(105);
			  Sound.playTone(294, 100);
			  Sound.pause(105);
			  Sound.playTone(262, 100);
			  Sound.pause(105);
			  Sound.playTone(233, 100);
			  Sound.pause(105);
			  Sound.playTone(294, 300);
		  } else if (this.tulos == 1) {
			  LCD.drawString("Rist-0 voitti!", 0, 0);
			  Sound.playTone(294, 120);
			  Sound.pause(200);
			  Sound.playTone(294, 100);
			  Sound.pause(105);
			  Sound.playTone(349, 100);
			  Sound.pause(105);
			  Sound.playTone(294, 100);
			  Sound.pause(105);
			  Sound.playTone(349, 100);
			  Sound.pause(105);
			  Sound.playTone(466, 300);
		  } else if (this.tulos == 2) {		  
			  LCD.drawString("Voitit pelin!", 0, 0);
			  Sound.playTone(294, 100);	
			  Sound.pause(105);
			  Sound.playTone(277, 100);
			  Sound.pause(105);
			  Sound.playTone(294, 100);
			  Sound.pause(105);
			  Sound.playTone(233, 100);
			  Sound.pause(105);
			  Sound.playTone(220, 100);
			  Sound.pause(105);
			  Sound.playTone(233, 100);
			  Sound.pause(105);
			  Sound.playTone(196, 300);
		  }
		  Delay.msDelay(3000);
	}
}
