package main;

import java.awt.Color;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
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
//	public final int worldWidth = tileSize * maxWorldCol;
//	public final int worldHeight = tileSize * maxWorldRow;
	
	
	int FPS = 60;
	
	// SYSTEM
	TileManager tileM = new TileManager(this);
	// Must instantiate the KeyHandler object
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound(); 
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	// For making a clock, instantiate it
	Thread gameThread;
	
	// ENTITY AND OBJECT
	public Player player = new Player(this,keyH);
	public Entity obj[] = new Entity[10]; // YOU CAN CHANGE THIS NUMBER TO HOWEVER MANY OBJECT YOU WANT ON SCREEN
	public Entity npc [] = new Entity[10];
	public Entity monster[] = new Entity[20];
	ArrayList<Entity> entityList =  new ArrayList<>();
	
	// GAME STATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	

	
	
	//Constructor for game panel
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // setting screen size
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH); // will grab the key inputs
		this.setFocusable(true);
	}
	public void setupGame() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
//		playMusic(0);
		gameState = titleState;
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
		if (gameState == playState) {
			// PLAYER
			player.update();
			// NPC
			for(int i = 0; i<npc.length; i++) {
				if(npc[i]!=null) {
					npc[i].update();
				}
			}
			for(int i = 0; i<monster.length; i++) {
				if(monster[i]!=null) {
					monster[i].update();
				}
			}	
		}
		if (gameState == pauseState) {
			//nothing
		}
		
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //necessary for painting on JPanel
		Graphics2D g2 = (Graphics2D)g;
		
		//DEBUG
		long drawStart = 0;
		if(keyH.checkDrawTime == true) {
			drawStart = System.nanoTime();
		}
		
		//TITLE SCREEN
		if (gameState == titleState) {
			ui.draw(g2);
		}
		//OTHERS
		else {
			//TILE
			tileM.draw(g2);
			
			//Add ENTITIES TO LIST
			entityList.add(player);
			
			for (int i = 0; i < npc.length;i++) {
				if(npc[i]!= null) {
					entityList.add(npc[i]);
				}
			}
			for (int i = 0; i < obj.length;i++) {
				if(obj[i]!= null) {
					entityList.add(obj[i]);
				}
			}
			for (int i = 0; i < monster.length;i++) {
				if(monster[i]!= null) {
					entityList.add(monster[i]);
				}
			}
			//SORT
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					int result = Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
			
			});
			
			// DRAW ENTITIES
			for (int i = 0; i <entityList.size();i++) {
				entityList.get(i).draw(g2);
			}
			// EMPTY ENTITY LIST
			entityList.clear();
			
			//UI
			ui.draw(g2);
			
		}

		//DEBUG
		if(keyH.checkDrawTime == true) {
			long drawEnd =System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time: " + passed, 10, 400);
			System.out.println("Draw Time: " + passed);
		}
		
		g2.dispose(); //gets rid of any unnecessary resources
		
		
	}
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		music.stop();
	}
	//sound effect
	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}
}
