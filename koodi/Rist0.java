import lejos.nxt.*;
import lejos.util.Delay;
import java.util.Random;

/**
 * Pääluokka tällä hetkellä testaa robotin käsivarren ja valosensorin toimintaa. Robotti kulkee peliruudukossa ja pudottaa pelimerkin jokaiseen ruutuun, jossa ei ole vastustajan pelimerkkiä.
 * 
 * @author mshroom
 * 
 */
public class Rist0 {
	public static void main (String[] aArg) throws Exception  {
		Pelilauta peli = new Pelilauta();
		LightSensor light = new LightSensor(SensorPort.S1);
		
	// Kalibroidaan valosensori mustassa ja valkoisessa ruudussa
	light.calibrateLow();
	peli.kuljeRuutuun(2, 2);
	Delay.msDelay(1000);
	light.calibrateHigh();
	Delay.msDelay(1000);
	
		
	for (int x = 0; x < 3; x ++) {
		for (int y = 0; y < 3; y ++) {
			peli.kuljeRuutuun(x, y);
			Delay.msDelay(1000);
			if (light.getLightValue() > 50) {
				peli.jataPelimerkki();
			}
			Delay.msDelay(2000);
		}
	}
	peli.palaaKotiin();
	}
}
