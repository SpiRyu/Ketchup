import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/* Alternative zu ShipA, allerdings nach Ausprobieren wurde festgestellt, dass es eher ungeeignet ist zum Spielen. Empfehle eher Schiff C
 * Es ist nicht verbuggt, aber es wirkt teilweise so, weil die instinktive Steuerung der Steuerung von Schiff C entspricht.
 * Dadurch drückt der Spieler ohne es zu merken falsche Tasten
 * 
 * Dieses Schiff hat nur eine Drüße, es kann also nicht nach mehreren Seiten gleichzeitig beschleunigen, um sich schneller zu bewegen.
 * Durch die Pfeiltasten lässt sich die Ausrichtung dieser Drüße ändern
 * Die aktuelle Ausrichtung der Drüße wird durch eine blaue Linie angezeigt
 * 
 * Links = Drüße auf die rechte Seite steuern
 * Rechts = Drüße auf die linke Seite steuern
 * Oben = Drüße auf die untere Seite steuern
 * Unten = Drüße auf die obere Seite steuern
 * Durch Kombination von Links und Oben kann die Drüße zum Beispiel nach Links Oben bewegt werden
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
	// Aktueller Winkel
	private float angle = 0;
	// Zielwinkel
	private float aimangle = 0;
	// Winkelgeschwindigkeit. Grad pro Schleifendurchlauf
	private float anglevelocity = 2;
	// Maximalgeschwindigkeit
	private float maxvelocity = 6;
	// Gibt an, ob vor Ändern des Winkels gebremst werden soll.
	// Dies sollte nur der Fall sein, wenn der Zielwinkel dem aktuellen Winkel
	// gegenüber liegt.
	private boolean firstbrake = false;

	// Gibt die aktuelle Geschwindigkeiten an
	private float velocity = 0;

	// Beschleunigung in Pixel pro Schleifendurchlauf pro Schleifendurchlauf
	private float acceleration = 0.05f;

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
		g.drawImage(img, (int) x, (int) y, null);
		g.setColor(Color.CYAN);
		double angleR = Math.toRadians(angle);
		int xm = (int) (x + width / 2);
		int ym = (int) (y + height / 2);
		g.drawLine(xm, ym, (int) (xm - Math.cos(angleR) * 50),
				(int) (ym - Math.sin(angleR) * 50));
	}

	// get Methode für die Schüsse
	public ArrayList<DefaultShoot> getShoots() {
		return shoots;
	}

	// Position updaten
	@Override
	public void update() {
		// Wenn ein Kommando aktiv ist, die Maximalgeschwindigkeit noch nicht
		// erreicht ist und nicht erst gebremst werden soll: Beschleunige!
		if ((direction[0] != 0 || direction[1] != 0) && velocity < maxvelocity
				&& !firstbrake) {
			velocity += acceleration;
			if (velocity > maxvelocity)
				velocity = maxvelocity;
		}
		// Ansonsten: Bremsen
		else {
			if (velocity > 0) {
				velocity -= acceleration;
			}
			if (velocity < 0) {
				velocity = 0;
			}
			if (velocity == 0 && firstbrake) {
				// FirstBremsung fertig. Ändere den Winkel!
				firstbrake = false;
				angle = aimangle;
			}
		}

		// Wenn sich Winkel und Zielwinkel unterscheiden, muss der Winkel
		// geändert werden
		if (angle != aimangle && !firstbrake) {
			// Deffiniere die Grenzen der linken und rechten Seite vom Schiff
			// aus gesehen
			float angleleft = angle + 180;
			float angleright = angle - 180;

			// Winkel muss sich im Bereich von Angleleft bis Angleright
			// befinden.
			// Ist dies nicht der Fall, wird so oft um 360° erhöht bzw
			// vermindert, bis es im Bereich ist
			while (aimangle > angleleft) {
				aimangle -= 360;
			}
			while (aimangle < angleright) {
				aimangle += 360;
			}

			// Ist der Zielwinkel im linken Bereich, muss nach links gedreht
			// werden.
			if (aimangle > angle && aimangle <= angleleft) {
				angle += anglevelocity;
			}
			// Ist der Zielwinkel im rechten Bereich, muss nach rechts gedreht
			// werden.
			else if (angleright <= aimangle && aimangle < angle) {
				angle -= anglevelocity;
			}
		}

		double angleR = Math.toRadians(angle);
		x += velocity * Math.cos(angleR);
		y += velocity * Math.sin(angleR);
	}

	/*
	 * Setzt das Directionarray und passt den Aimangle darauf an
	 * 
	 * @param xy 0 = X, 1 = Y
	 * 
	 * @param val -1, 0, 1
	 */
	private void setDirection(int xy, int val) {
		if (xy != 0 && xy != 1 || val < -1 && val > 1) {
			throw new RuntimeException("Illegal Arguments!");
		}
		direction[xy] = val;
		if (direction[0] == 1) {
			aimangle = 45 * direction[1];
		} else if (direction[0] == 0 && direction[1] != 0) {
			aimangle = 90 * direction[1];
		} else if (direction[0] == -1) {
			aimangle = 45 * direction[1] + 180;
		}
		if ((angle - aimangle) % 180 == 0 && (angle - aimangle) % 360 != 0) {
			firstbrake = true;
		}
		if (velocity == 0) {
			angle = aimangle;
		}
	}

	// Abfangen von Tastendrücken
	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			setDirection(0, -1);
			break;
		case KeyEvent.VK_RIGHT:
			setDirection(0, 1);
			break;
		case KeyEvent.VK_UP:
			setDirection(1, -1);
			break;
		case KeyEvent.VK_DOWN:
			setDirection(1, 1);
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
				setDirection(0, 0);
			break;
		case KeyEvent.VK_RIGHT:
			if (direction[0] == 1)
				setDirection(0, 0);
			break;
		case KeyEvent.VK_UP:
			if (direction[1] == -1)
				setDirection(1, 0);
		case KeyEvent.VK_DOWN:
			if (direction[1] == 1)
				setDirection(1, 0);
			break;

		}

	}

}
