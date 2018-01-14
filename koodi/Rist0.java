import lejos.nxt.*;
import lejos.util.Delay;

/**
 * Pääluokka suorittaa yhden pelikerran alusta loppuun. Ihminen saa valita vaikeustason ja aloittavan pelaajan.
 * 
 * @author mshroom
 * 
 */
public class Rist0 {
  public static void main (String[] aArg) throws Exception  {
	  
	  int vaikeustaso = 0;
	  while (true) {
		  LCD.drawString("Vaikeustaso", 0, 0);
		  LCD.drawString("< Helppo", 0, 2);
		  LCD.drawString("> Vaikea", 0, 4);
		  if (Button.LEFT.isPressed()) {
			  LCD.clear();
			  vaikeustaso = 1;
			  break;
		  }
		  if (Button.RIGHT.isPressed()) {
			  LCD.clear();
			  vaikeustaso = 2;
			  break;
		  }
	  }
	  
	  LCD.drawString("Kalibroidaan", 0, 0);
	  Peli peli = new Peli(vaikeustaso);
	  peli.kalibroiPelilauta();
	  LCD.clear();
	  
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
		  LCD.clear();
		  LCD.drawString("Sinun vuorosi", 0, 0);
		  Delay.msDelay(5000);
		  if (peli.paivitaPelitilanne()) {
			  if (!peli.peliOhi()) {
				  peli.siirto();
			  }			  
		  } else {
			  peli.ilmoitaVirheesta();
		  }
	  }
	  	  
	  peli.kerroTulos();
  }
}
