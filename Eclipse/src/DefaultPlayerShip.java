import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DefaultPlayerShip extends PlayerShip {

	public static final int DPS_WIDTH = 75;
	public static final int DPS_HEIGHT = 75;

	private Image img;

	private int[] direction = { 0, 0 };

	public DefaultPlayerShip(int x, int y) throws IOException {
		super(x, y, 75, 75);
		img = ImageIO.read(new File("PlayerShip.png"));
	}

	@Override
	public void paintShip(Graphics g) {
		g.drawImage(img, x, y, null);
	}

	@Override
	public void update() {
		x += direction[0] * 3;
		y += direction[1] * 3;
	}

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

	@Override
	public void keyReleased(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
			direction[0] = 0;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			direction[1] = 0;
			break;
		}

	}

}
