import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DefaultShoot extends Shoot {

	private static final int DS_WIDTH = 6;
	private static final int DS_HEIGHT = 14;
	
	// Bild
	private Image img;

	// Gibt die X/Y-Richtung an in die bewegt wird (z.B. X: -1 = Links, 0 =
	// Nichts, 1 = Rechts)
	private int[] direction = { 0, 0 };

	public DefaultShoot(float x, float y) throws IOException {
		super(x-DS_WIDTH/2, y, DS_WIDTH, DS_HEIGHT);
		// Datei einlesen
		img = ImageIO.read(new File("PlayerShoot.png"));
	}

	// Zeichnen des Schusses
	@Override
	public void paintShoot(Graphics g) {
		g.drawImage(img, (int) x, (int) y, null);

	}

	@Override
	public void update() {
		y = y - 3;

	}
}
