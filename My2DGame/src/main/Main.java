package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //can close window
		window.setResizable(false); // can't resize the window
		window.setTitle("2D Adventure"); //title of the window
		
		GamePanel gamePanel = new GamePanel(); // making the game panel object
		window.add(gamePanel);
		
		window.pack(); //pack() causes the frame to fit the game panel
		
		window.setLocationRelativeTo(null); // window is open at center of screen
		window.setVisible(true); // you can see the window
		
		((GamePanel) gamePanel).startGameThread();

	}

}
