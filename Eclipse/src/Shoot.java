import java.awt.Graphics;

public abstract class Shoot {

	protected float x;
	protected float y;
	protected int width;
	protected int height;

	// Konstruktor. Selbsterklärend!?
	// X und Y sind als Floats, um Zwischenwerte zu erhalten, die für
	// Berechnungen wichtig sind, jedoch nicht angezeigt werden
	public Shoot(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	// Zeichen-Methode.
	public abstract void paintShoot(Graphics g);

	// Updated die Position des Schusses
	public abstract void update();
}
