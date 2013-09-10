import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/* Alternative zu ShipA.
 * 
 * Dieses Schiff hat genau wie Ship A an jeder Seite eine Drüße, es kann also nach mehreren Seiten gleichzeitig beschleunigen.
 * Es ist jedoch in sofern eingeschränkt, dass die Maximalgeschwindigkeit sich nicht auf die horizontale oder vertikale Geschwindigkeit jeweils isoliert bezieht,
 * sondern auf die Geschwindigkeit, die sich daraus gesamt ergibt.
 * Ship A: vx < vmax && vy < vmax
 * Ship B: vx + vy < vmax
 * 
 * Aktivierte Drüßen werden derzeit als blaue Linien angezeigt.
 * Die Summe der beiden Richtungen wird als grüne Linie angezeigt.
 * 
 * Tastenbefehle:
 * Links = Links beschleunigen (= Rechts bremsen)
 * Rechts = Rechts beschleunigen (= Links bremsen)
 * Oben = Vorne beschleunigen (= Hinten bremsen)
 * Unten = Hinten beschleunigen (= Vorne bremsen)
 * 
 * 
 * ACHTUNG, noch leicht verbuggt:
 * Manchmal springt das Schiff einfach in die entgegengesetzte Richtung zurück!!!!
 * 
 */
public class DefaultPlayerShipB extends PlayerShip {

	// Konstanten: Breite und Höhe des Schiffes
	public static final int DPS_WIDTH = 50;
	public static final int DPS_HEIGHT = 50;

	// Bild
	private Image img;

	private ArrayList<DefaultShoot> shoots;

	// Gibt die Kommandos in X bzw Y-Richtung an (z.B. X: -1 = Links, 0 =
	// Nichts, 1 = Rechts)
	private int[] direction = { 0, 0 };
	
	//X, Y - Geschwindigkeit
	private double[] velocities = {0, 0};
	
	//Maximalgeschwindigkeit
	private float maxvelocity = 6;
	
	// Beschleunigung in Pixel pro Schleifendurchlauf pro Schleifendurchlauf
	private double acceleration = 0.05f;

	// Konstruktor
	public DefaultPlayerShipB(float x, float y) throws IOException {
		super(x, y, DPS_WIDTH, DPS_HEIGHT);
		// Datei einlesen
		img = ImageIO.read(new File("PlayerShipB.png"));
		shoots = new ArrayList<DefaultShoot>();
	}

	// Zeichnen des Schiffes
	@Override
	public void paintShip(Graphics g) {
		//Bild
		g.drawImage(img, (int) x, (int) y, null);
		
		//Blaue XYLinien
		g.setColor(Color.CYAN);
		int xm = (int) (x + width / 2);
		int ym = (int) (y + height / 2);
		int xlength = -(int)(velocities[0]/maxvelocity * 70);
		int ylength = -(int)(velocities[1]/maxvelocity * 70);
		g.drawLine(xm, ym, xm+xlength, ym);
		g.drawLine(xm, ym, xm, ym+ylength);
		
		//Grüne Linie
		g.setColor(Color.GREEN);
		g.drawLine(xm, ym, xm+xlength, ym+ylength);
	}

	// get Methode für die Schüsse
	public ArrayList<DefaultShoot> getShoots() {
		return shoots;
	}

	// Position updaten
	@Override
	public void update() {
		for (int i = 0; i < 2; i++) {
			//Wenn ein Kommando aktiv ist und die Maximalgeschwindigkeit noch nicht
			//erreicht ist: Beschleunige!
			if (direction[i] != 0 && velocities[0]+velocities[1] < maxvelocity) {
				velocities[i] += acceleration*direction[i];
				//Überprüfung der Maximalgeschwindigkeit
				if (Math.abs(velocities[0])+Math.abs(velocities[1]) > maxvelocity)
					//Wenns größer ist, wird die aktuelle Geschwindigkeit reduziert
					velocities[i] = (maxvelocity - Math.abs(velocities[1-i]))*direction[i];
			}
			// Ansonsten: Bremsen
			else {
				//Geschwindigkeit näher an 0 heranbringen
				if (velocities[i] > 0) {
					velocities[i] -= acceleration;
				}
				if (velocities[i] < 0) {
					velocities[i] += acceleration;
				}
				//Überprüfung, ob sich die Geschwindigkeit aufgrund von z.B. Dezimalfehler irgendwo im Bereich [-acceleration, +acceleration] befindet.
				//Ist dies der Fall, wird die Geschwindigkeit auf 0 gesetzt
				if (velocities[i] < acceleration && velocities[i] > 0 || velocities[i] > -acceleration && velocities[i] < 0) {
					velocities[i] = 0;
				}
			}
		}
		//Sollte die Maximalgeschwindigkeit durch Aktivierung von zwei Drüßen erreicht werden, sollten deren Stärken so ausgeglichen werden,
		//dass jede Drüße für 50% der Geschwindigkeit sorgt, und so das Schiff im 45°-Winkel fliegt. 
		double absvel0 = Math.abs(velocities[0]);
		double absvel1 = Math.abs(velocities[1]);
		if (direction[0] != 0 && direction[1] != 0 && absvel0 + absvel1 == maxvelocity) {
			if (absvel0 > absvel1) {
				velocities[0] -= acceleration * direction[0];
				velocities[1] += acceleration * direction[1];
			}
			else if (absvel1 > absvel0) {
				velocities[1] -= acceleration * direction[1];
				velocities[0] += acceleration * direction[0];
			}
		}
		
		x += velocities[0];
		y += velocities[1];
	}

	// Abfangen von Tastendrücken
	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			direction[0] = -1;
			break;
		case KeyEvent.VK_RIGHT:
			direction[0] = 1;
			break;
		case KeyEvent.VK_UP:
			direction[1] = -1;
			break;
		case KeyEvent.VK_DOWN:
			direction[1] = 1;
			break;
		case KeyEvent.VK_SPACE:
			try {
				DefaultShoot shoot = new DefaultShoot(x, y);
				shoots.add(shoot);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// Abfangen von Tastendrücken
	@Override
	public void keyReleased(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction[0] == -1)
					direction[0] = 0;
				break;
			case KeyEvent.VK_RIGHT:
				if (direction[0] == 1)
					direction[0] = 0;
				break;
			case KeyEvent.VK_UP:
				if (direction[1] == -1)
					direction[1] = 0;
			case KeyEvent.VK_DOWN:
				if (direction[1] == 1)
					direction[1] = 0;
				break;

		}

	}

}
