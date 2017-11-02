package game_stage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player {
	
	private BufferedImage image = null;
	private int x;
	private int y;
	
	int velX = 0;
	int velY = 0;
	
	public Player(int x, int y, BufferedImage img){
		this.x = x;
		this.y = y;
		image = img;
	}

	public void updatePlayer(int velX, int velY){
		if(x+velX<0){
			x = 0;
		}
		
		if(y+velY<0){
			y = 0;
		}
		
		x+=velX;
		y+=velY;
	}
	
	public void renderPlayer(Graphics2D g, int sw, int sh){
		g.drawImage(image, x, y, null);
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getX() {
		return x;
	}

	public void setX(int X) {
		this.x = X;
	}

	public int getY() {
		return y;
	}

	public void setY(int Y) {
		this.y = Y;
	}
	
	
}