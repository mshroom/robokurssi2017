import lejos.nxt.*;

/**
 * Pääluokka suorittaa yhden pelikerran alusta loppuun. Ihminen saa valita kumpi aloittaa.
 * 
 * @author mshroom
 * 
 */
public class Rist0 {
	public static void main (String[] aArg) throws Exception  {
	Peli peli = new Peli();
	  peli.kalibroiPelilauta();
	  while (true) {
		  LCD.drawString("Kumpi aloittaa?", 0, 0);
		  LCD.drawString("< Robotti", 0, 2);
		  LCD.drawString("> Ihminen", 0, 4);
		  if (Button.LEFT.isPressed()) {
			  LCD.clear();
			  peli.siirto();
			  break;
		  }
		  if (Button.RIGHT.isPressed()) {
			  LCD.clear();
			  break;
		  }  
	  }
	  
	  while (!peli.peliOhi()) {
		  Delay.msDelay(5000);
		  if (peli.paivitaPelitilanne()) {
			  if (!peli.peliOhi()) {
				  peli.siirto();
			  }			  
		  } else {
			  peli.ilmoitaVirheesta();
		  }
	  }
}
