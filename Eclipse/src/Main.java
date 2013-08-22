import java.awt.FlowLayout;

import javax.swing.JFrame;

/* Das Fenster
 * 
 */
public class Main extends JFrame {

	public static void main(String[] args) {
		 new Main();
	}
	
	private GamePanel gamePanel;
	
	
	//Konstruktor
	public Main() {
		super("Ketchup");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(50, 50);
		setResizable(false);
		
		//Erzeuge GamePanel und füge es dem Fenster hinzu.
		gamePanel = new GamePanel();
		getContentPane().add(gamePanel);
		pack();
		
		setVisible(true);
		
		//Starte das Spiel
		gamePanel.startGame();
	}
}
