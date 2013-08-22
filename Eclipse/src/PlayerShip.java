import java.awt.Graphics;
import java.awt.event.KeyEvent;


public abstract class PlayerShip {
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	public PlayerShip(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public abstract void paintShip(Graphics g);
	
	public abstract void update();
	
	public abstract void keyPressed(KeyEvent event);
	
	public abstract void keyReleased(KeyEvent event);
	
}
