package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3; // Time to scale the 16x16 to the computer
	
	public final int tileSize = originalTileSize * scale;  // 48x48 tile
	public final int maxScreenCol = 16; // right-left
	public final int maxScreenRow = 12; // up-down
	public final int screenWidth = tileSize * maxScreenCol; //768 pixels
	public final int screenHeight = tileSize * maxScreenRow; //576 pixels
	
	//WORLD SETTINGS - these can be changed to have a bigger or smaller world map
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	
	// Setting the FPS 
	int FPS = 60;
	
	
	TileManager tileM = new TileManager(this);
	// Must instantiate the KeyHandler object
	KeyHandler keyH = new KeyHandler();
	
	// For making a clock, instantiate it
	Thread gameThread;
	
	// Instantiating player 
	public Player player = new Player(this,keyH);
	
	

	
	
	//Constructor for game panel
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // setting screen size
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH); // will grab the key inputs
		this.setFocusable(true);
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	//making a game loop
	@Override
	public void run() {
		// making a sleep method
		
		double drawInterval = 1000_000_000/FPS;  //1 billion nano seconds / 60 
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		
		
		while(gameThread != null) {
			
			long currentTime = System.nanoTime();
			//setting the FPS
			
			// 1 UPDATE: update information such as character positions
			update(); //calls update
			
			// 2 DRAW: draw the screen with the updated information
			repaint(); //calls painComponent
			
			
			try {
				double remainingTime = nextDrawTime - System.nanoTime(); // we need this variable to make the thread sleep after its done repainting
				remainingTime = remainingTime /1000000; //conversion to milli
				
				if (remainingTime < 0) {
					remainingTime = 0;
				}
				
				Thread.sleep((long)remainingTime); //this is in milliseconds and must be convert the nano to milli
				
				nextDrawTime += drawInterval;
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	public void update() {
		player.update();
		
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //necessary for painting on JPanel
		
		Graphics2D g2 = (Graphics2D)g;
		
		tileM.draw(g2);
		
		player.draw(g2);
		g2.dispose(); //gets rid of any unnecessary resources
		
		
	}
}
