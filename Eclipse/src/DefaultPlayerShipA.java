import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/* Normales Player-Schiff
 * Dieses Schiff hat auf jeder seiner vier Seiten eine Drüße, so dass unabhängig voneinander horizontal und vertikal beschleunigt werden kann.
 * Aktivierte Drüßen werden derzeit als blaue Linien angezeigt.
 * Tastenbefehle:
 * Links = Links beschleunigen (= Rechts bremsen)
 * Rechts = Rechts beschleunigen (= Links bremsen)
 * Oben = Vorne beschleunigen (= Hinten bremsen)
 * Unten = Hinten beschleunigen (= Vorne bremsen)
 * 
 */
public class DefaultPlayerShipA extends PlayerShip {

	// Konstanten: Breite und Höhe des Schiffes
	public static final int DPS_WIDTH = 50;
	public static final int DPS_HEIGHT = 50;

	// Bild
	private Image img;

	private ArrayList<DefaultShoot> shoots;

	// Gibt die X/Y-Richtung an in die bewegt wird (z.B. X: -1 = Links, 0 =
	// Nichts, 1 = Rechts)
	private int[] direction = { 0, 0 };

	// Gibt die Geschwindigkeiten in X bzw. Y-Richtung an
	private float[] velocities = { 0, 0 };

	// Maximalgeschwindigkeit in Pixel pro Schleifendurchlauf
	private float maxvelocity = 6;

	// Beschleunigung in Pixel pro Schleifendurchlauf pro Schleifendurchlauf
	private float acceleration = 0.1f;

	// Konstruktor
	public DefaultPlayerShipA(float x, float y) throws IOException {
		super(x, y, DPS_WIDTH, DPS_HEIGHT);
		// Datei einlesen
		img = ImageIO.read(new File("PlayerShipA.png"));
		shoots = new ArrayList<DefaultShoot>();
	}

	// Zeichnen des Schiffes
	@Override
	public void paintShip(Graphics g) {
		g.drawImage(img, (int) x, (int) y, null);
		int xm = (int) (x + width / 2);
		int ym = (int) (y + height / 2);
		g.setColor(Color.CYAN);
		g.drawLine(xm, ym, xm - 50 * direction[0], ym);
		g.drawLine(xm, ym, xm, ym - 50 * direction[1]);
	}

	// get Methode für die Schüsse
	public ArrayList<DefaultShoot> getShoots() {
		return shoots;
	}

	// Position updaten
	@Override
	public void update() {
		// X und Y Achse durchgehen
		for (int i = 0; i <= 1; i++) {

			// Fall 1: Garnichts
			if (direction[i] == 0 && velocities[i] == 0) {
				continue;
			}
			// Fall 2: Kommando in eine Richtung ist da -> Beschleunigen
			else if (direction[i] != 0 && velocities[i] != maxvelocity) {
				velocities[i] += acceleration * direction[i];
				// Maximalgeschwindigkeit überschritten -> Reduzieren
				if (Math.abs(velocities[i]) > maxvelocity) {
					velocities[i] = maxvelocity * direction[i];
				}
			}
			// Fall 3: Kein Kommando, Geschwindigkeit da -> Abbremsen
			else if (velocities[i] != 0) {
				int brakeDirection = (velocities[i] > 0) ? 1 : -1;
				velocities[i] -= acceleration * brakeDirection;
				// Überprüfung: Wenn Geschwindigkeit in andere Richtung geht,
				// wurde zu viel gebremst. Auf 0 setzen!
				if (brakeDirection == -1 && velocities[i] > 0
						|| brakeDirection == 1 && velocities[i] < 0) {
					direction[i] = 0;
					velocities[i] = 0;
				}
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
				DefaultShoot shoot = new DefaultShoot((int) (x + height / 2), y);
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
