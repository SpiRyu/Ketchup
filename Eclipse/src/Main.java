import java.awt.FlowLayout;

import javax.swing.JFrame;


public class Main extends JFrame {

	public static void main(String[] args) {
		 new Main();
	}
	
	private GamePanel gamePanel;
	
	public Main() {
		super("Ketchup");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(50, 50);
		
		gamePanel = new GamePanel();
		getContentPane().add(gamePanel);
		pack();
		
		setVisible(true);
		
		gamePanel.startGame();
	}
}
