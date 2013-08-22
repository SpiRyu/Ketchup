import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/* Das Panel, in dem das eigentliche Spiel abläuft
 * 
 */
public class GamePanel extends JPanel implements KeyListener {
	
	//Konstanten. Breite und Höhe
	private final static int PANEL_WIDTH = 1000;
	private final static int PANEL_HEIGHT = 750;

	//Start-Position des Schiffes
	private static final float START_X = 500;
	private static final float START_Y = 600;
	
	private PlayerShip player = null;
	private boolean gameRunning = false;

	//Konstruktor
	public GamePanel() {
		super();
		setBackground(Color.BLACK);
	}
	
	//Damit das Fenster die richtige Größe bekommt
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
	}
	
	//Startet das Spiel. Ruft runGame in neuem Thread auf
	public void startGame() {
		new Thread() {
			@Override
			public void run() {
				runGame();
			}
		}.start();
	}
	
	//Die eigentliche Spielprozedur
	private void runGame() {
		
		try {
			//Zunächst: Neues Schiff erzeugen
			player = new DefaultPlayerShip(START_X, START_Y);
		} catch (IOException ex) {
			//Fehler -> Meldung ausgeben
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			return;
		}
		//GameRunning auf True setzen und KeyListener hinzufügen
		gameRunning = true;
		addKeyListener(this);
		requestFocus();
		
		//Spielschleife
		while(gameRunning) {
			//Alle Objekte updaten
			player.update();
			//CollisionDetection etc. kommt hier auch noch hin
			//Spielfeld neu Zeichnen
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		//removeKeyListener(this);
		//gameRunning = false;
	}
	
	//Zeichnen-Methode
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (gameRunning) {
			player.paintShip(g);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		//Events wird an das Player-Schiff weitergeleitet
		if (gameRunning) {
			player.keyPressed(arg0);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameRunning = false;
			return;
		}
		if (gameRunning) {
			player.keyReleased(arg0);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}
