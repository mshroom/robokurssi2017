import lejos.nxt.*;
import lejos.util.Delay;
import java.util.Random;

/**
 * Pääluokka tällä hetkellä lähinnä testaa robotin käsivarren toimintaa.
 * 
 * @author mshroom
 * 
 */
public class Rist0 {
	public static void main (String[] aArg) throws Exception  {
		Pelilauta peli = new Pelilauta();
		for (int i = 0; i < 10; i ++) {
			Random arpa = new Random();
			int x = arpa.nextInt(3);
			int y = arpa.nextInt(3);
			peli.kuljeRuutuun(x, y);
			Delay.msDelay(2000);
		}
		peli.palaaKotiin();
	}
}
