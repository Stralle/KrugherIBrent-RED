package game_stage;

import java.awt.image.BufferedImage;

public class Field {

	private int fldWidth;
	private int fldHeight;
	
	private int type;
	private boolean isSpecial;
	
	private BufferedImage image = null;
	
	public Field(int fldWidth, int fldHeight, BufferedImage image, boolean special){
		this.fldHeight = fldHeight;
		this.fldWidth = fldWidth;
		this.image = image;
		this.isSpecial = special;
	}
	
	public Field(int fldWidth, int fldHeight){
		this.fldHeight = fldHeight;
		this.fldWidth = fldWidth;
	}

	public int getFldWidth() {
		return fldWidth;
	}

	public void setFldWidth(int fldWidth) {
		this.fldWidth = fldWidth;
	}

	public int getFldHeight() {
		return fldHeight;
	}

	public void setFldHeight(int fldHeight) {
		this.fldHeight = fldHeight;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
}
