package game_stage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player {
	
	private BufferedImage image = null;
	private int initialX;
	private int initialY;
	
	public Player(int x, int y, BufferedImage img){
		initialX = x;
		initialY = y;
		image = img;
	}

	public void renderPlayer(Graphics2D g, int sw, int sh){
		g.drawImage(image, initialX, initialY, null);
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getInitialX() {
		return initialX;
	}

	public void setInitialX(int initialX) {
		this.initialX = initialX;
	}

	public int getInitialY() {
		return initialY;
	}

	public void setInitialY(int initialY) {
		this.initialY = initialY;
	}
	
	
}