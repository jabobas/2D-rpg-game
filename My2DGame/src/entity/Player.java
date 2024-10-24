package entity;

import java.awt.Color;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	

	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		setDefaultValues();
		getPlayerImage();
	}

	public void setDefaultValues() {
		worldX = gp.tileSize * 23 ;
		worldY = gp.tileSize * 21 ;
		speed = 4;
		direction = "down";

	}
	public void getPlayerImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/wlk_bk_left_foot.png.png")); 
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/wlk_bk_right_foot.png.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/wlk_fr_left_foot.png.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/wlk_fr_right_foot.png.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/st_left.png.png")); //stand left
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/wlk_left.png.png")); //walk left
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/st_right.png.png")); //stand right
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/wlk_right.png.png")); //walk right
			
			
		}
		catch(IOException e){
			e.printStackTrace();
			
		}
	}

	public void update() {
		// this if statement will stop the character from moving when standing still
		if (keyH.upPressed == true || keyH.downPressed == true || 
				keyH.leftPressed == true || keyH.rightPressed == true) {
			// changing the player position
			// ** X increases to the right **
			// ** Y increases down **
			if (keyH.upPressed == true) {
				direction = "up";
				worldY -= speed;
			} else if (keyH.downPressed == true) {
				direction = "down";
				worldY += speed;
			} else if (keyH.leftPressed == true) {
				direction = "left";
				worldX -= speed;
			} else if (keyH.rightPressed == true) {
				direction = "right";
				worldX += speed;
			}
			
			spriteCounter++;  //used to animate walking
			if(spriteCounter > 15) {
				if (spriteNum == 1) {
					spriteNum = 2;
				}
				else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}
		
	}

	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white);
//
//		// making the player
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize); // makes a rectangle and fills it with a specified color

		
		BufferedImage image = null;
		
		switch(direction) {
		case "up" :
			if(spriteNum == 1) {
				image = up1;
			}
			if(spriteNum == 2) {
				image = up2;
			}
			break;
		case "down" :
			if(spriteNum == 1) {
				image = down1;
			}
			if(spriteNum == 2) {
				image = down2;
			}
			break;
		case "left" :
			if(spriteNum == 1) {
				image = left1;
			}
			if(spriteNum == 2) {
				image = left2;
			}
			break;
		case "right" :
			if(spriteNum == 1) {
				image = right1;
			}
			if(spriteNum == 2) {
				image = right2;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}

}
