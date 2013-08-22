import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener {
	
	private final static int PANEL_WIDTH = 1000;
	private final static int PANEL_HEIGHT = 750;

	private static final int START_X = 500;
	private static final int START_Y = 600;
	
	private PlayerShip player = null;
	private boolean gameRunning = false;

	public GamePanel() {
		super();
		setBackground(Color.BLACK);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
	}
	
	public void startGame() {
		new Thread() {
			@Override
			public void run() {
				runGame();
			}
		}.start();
	}
	
	private void runGame() {
		
		try {
			player = new DefaultPlayerShip(START_X, START_Y);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			return;
		}
		gameRunning = true;
		addKeyListener(this);
		requestFocus();
		
		while(true) {
			player.update();
			repaint();
			try {
				Thread.sleep(25);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		//removeKeyListener(this);
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
		if (gameRunning) {
			player.keyPressed(arg0);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (gameRunning) {
			player.keyReleased(arg0);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}
