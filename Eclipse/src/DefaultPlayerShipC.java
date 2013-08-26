import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/* Weitere, gute Möglichkeit der Steuerung des Schiffes. Wahrscheinlich eh die Beste.
 * Dieses Schiff funktioniert wie ein Auto. Antrieb ist hinten und man kann nach Links und Rechts lenken
 * Tastendrücke:
 * Oben = Beschleunigen
 * Unten = Bremsen
 * Links = Nach links lenken
 * Rechts = Nach rechts lenken
 * 
 */
public class DefaultPlayerShipC extends PlayerShip {

	// Konstanten: Breite und Höhe des Schiffes
	public static final int DPS_WIDTH = 50;
	public static final int DPS_HEIGHT = 50;

	// Bild
	private Image img;

	private ArrayList<DefaultShoot> shoots;

	// Gibt das Links/Nichts/Rechts-Kommando an (-1, 0, 1)
	private int xdirection = 0;
	// Aktueller Winkel
	private float angle = 90;
	// Winkelgeschwindigkeit. Grad pro Schleifendurchlauf
	private float anglevelocity = 2;
	// Maximalgeschwindigkeit
	private float maxvelocity = 5;

	// Gibt an, ob beschleunigt werden soll
	private boolean speeding = false;
	// Gibt an, ob künstlich gebremst werden soll
	private boolean braking = false;

	// Gibt die aktuelle Geschwindigkeiten an
	private float velocity = 0;

	// Beschleunigung in Pixel pro Schleifendurchlauf pro Schleifendurchlauf
	private float acceleration = 0.05f;

	// Konstruktor
	public DefaultPlayerShipC(float x, float y) throws IOException {
		super(x, y, DPS_WIDTH, DPS_HEIGHT);
		// Datei einlesen
		img = ImageIO.read(new File("PlayerShipC.png"));
		shoots = new ArrayList<DefaultShoot>();
	}

	// Zeichnen des Schiffes
	@Override
	public void paintShip(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// Das Bild drehen!
		AffineTransform trans = new AffineTransform();
		trans.translate(x, y);
		trans.rotate(Math.toRadians(angle - 90), width / 2, height / 2);
		g2d.drawImage(img, trans, null);
	}

	// get Methode für die Schüsse
	public ArrayList<DefaultShoot> getShoots() {
		return shoots;
	}

	// Position updaten
	@Override
	public void update() {
		// Wenn beschleunigt werden soll ist, die Maximalgeschwindigkeit noch
		// nicht erreicht ist: Beschleunige!
		if (speeding && velocity < maxvelocity) {
			velocity += acceleration;
			if (velocity > maxvelocity)
				velocity = maxvelocity;
		}
		// Wenn gebremst werden soll
		else if (braking && velocity > 0) {
			velocity -= 2 * acceleration;
			if (velocity < 0)
				velocity = 0;
		}
		// Ansonsten: Natürliches Verlangsamen
		else if (velocity != 0) {
			if (velocity > 0) {
				velocity -= acceleration;
			}
			if (velocity < 0) {
				velocity = 0;
			}
		}

		// Winkel anpassen
		angle += anglevelocity * xdirection;

		double angleR = Math.toRadians(angle);
		x -= velocity * Math.cos(angleR);
		y -= velocity * Math.sin(angleR);
	}

	// Abfangen von Tastendrücken
	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			xdirection = -1;
			break;
		case KeyEvent.VK_RIGHT:
			xdirection = 1;
			break;
		case KeyEvent.VK_UP:
			speeding = true;
			break;
		case KeyEvent.VK_DOWN:
			braking = true;
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
			if (xdirection == -1) {
				xdirection = 0;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (xdirection == 1) {
				xdirection = 0;
			}
			break;
		case KeyEvent.VK_UP:
			if (speeding) {
				speeding = false;
			}
		case KeyEvent.VK_DOWN:
			if (braking) {
				braking = false;
			}
			break;

		}

	}
}
