import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/* Stellt das Schiff eines menschlichen Spielers dar
 * 
 */
public abstract class PlayerShip {

	protected float x;
	protected float y;
	protected int width;
	protected int height;

	// Konstruktor. Selbsterklärend!?
	// X und Y sind als Floats, um Zwischenwerte zu erhalten, die für
	// Berechnungen wichtig sind, jedoch nicht angezeigt werden
	public PlayerShip(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	// Zeichen-Methode.
	public abstract void paintShip(Graphics g);

	// Updated die Position des Schiffes
	public abstract void update();

	public abstract void keyPressed(KeyEvent event);

	public abstract void keyReleased(KeyEvent event);

	public abstract ArrayList<DefaultShoot> getShoots();

}
