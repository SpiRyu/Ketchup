import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/* Normales Player-Schiff
 * 
 */
public class DefaultPlayerShip extends PlayerShip {

	//Konstanten: Breite und Höhe des Schiffes
	public static final int DPS_WIDTH = 75;
	public static final int DPS_HEIGHT = 75;

	//Bild
	private Image img;

	//Gibt an, ob in X oder Y-Richtung bewegt wird (z.B. X: -1 = Links, 0 = Nichts, 1 = Rechts)
	private int[] direction = { 0, 0 };

	//Konstruktor
	public DefaultPlayerShip(int x, int y) throws IOException {
		super(x, y, 75, 75);
		//Datei einlesen
		img = ImageIO.read(new File("PlayerShip.png"));
	}

	//Zeichnen des Schiffes
	@Override
	public void paintShip(Graphics g) {
		g.drawImage(img, x, y, null);
	}

	//Position updaten
	@Override
	public void update() {
		x += direction[0] * 3;
		y += direction[1] * 3;
	}

	//Abfangen von Tastendrücken
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
		}

	}

	//Abfangen von Tastendrücken
	@Override
	public void keyReleased(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (direction[0] == -1) direction[0] = 0;
			break;
		case KeyEvent.VK_RIGHT:
			if (direction[0] == 1) direction[0] = 0;
			break;
		case KeyEvent.VK_UP:
			if (direction[1] == -1) direction[1] = 0;
		case KeyEvent.VK_DOWN:
			if (direction[1] == 1) direction[1] = 0;
			break;
		}

	}

}
