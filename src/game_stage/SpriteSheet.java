package game_stage;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private BufferedImage img = null;
	private static int IMG_SIZE;

	public SpriteSheet(BufferedImage img, int imgSz) {
		this.img = img;
		IMG_SIZE = imgSz;
	}

	public BufferedImage[] getRow(int row, int picsNr) {
		BufferedImage imgRow[] = new BufferedImage[picsNr];
		for (int i = 0; i < picsNr; i++) {
			imgRow[i] = img.getSubimage(i * IMG_SIZE, row * IMG_SIZE, IMG_SIZE, IMG_SIZE);
		}
		return imgRow;
	}
}
