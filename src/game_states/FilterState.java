package game_states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Random;
import main.Model;
import rafgfxlib.GameHost;
import rafgfxlib.GameHost.GFMouseButton;
import rafgfxlib.GameState;
import rafgfxlib.Util;


public class FilterState extends GameState
{
	public static enum FilterType
	{
		WAVES,
		FLIP,
		NEGATIVE,
		BINARY,
		GRAYSCALE,
		POSTERIZE,
		CONTRAST,
		EDGEDETECT,
		COLOREDNOISE,
		BLUR,
		SHARPEN,
		EMBOSS,
		BRIGHTNESS,
	}

	private BufferedImage imageLeft;
	private BufferedImage imageRight;
	private BufferedImage imageSpiral;

	private int screenWidth;
	private int screenHeight;
	
	private int imageWidth;
	private int imageHeight;
	
	private int xL;
	private int yL;
	private int xR;
	private int yR;	
	
	private int moveX;
	private Pixel pixel;

	public void resetStats()
	{
		moveX = xL;
		pixel = new Pixel(imageWidth, imageHeight);
		imageSpiral = this.imageBlack(imageLeft);
	}
	
	public void loadImage()
	{
		setImageLeft(Model.getGlobalImage());
		
		if(imageLeft.getWidth() > ((5 * this.screenWidth) / 12) || imageLeft.getHeight() > ((5 * this.screenWidth) / 12))
		{

			this.imageLeft = imageScale(imageLeft);
		}
		/*
		this.imageRight = imageLeft;
		for(FilterType filterType: Model.selectedFilters)
			this.imageRight = makeImage(imageRight, filterType);	*/
		ColorModel cm = imageLeft.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = imageLeft.copyData(null);
		imageRight = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		for(FilterType filterType: Model.selectedFilters)
			this.imageRight = makeImage(imageRight, filterType);
		
		imageWidth = imageLeft.getWidth();
		imageHeight = imageLeft.getHeight();
	
	
		
		xL = (screenWidth / 4) - (imageWidth / 2);
		yL = (screenHeight / 2) - (imageHeight / 2);
		xR = 3*(screenWidth / 4) - (imageWidth / 2);
		yR = (screenHeight / 2) - (imageHeight / 2);
		
		moveX = xL;
		
		this.pixel = new Pixel(imageWidth, imageHeight);
		this.imageSpiral = this.imageBlack(imageLeft);		
	}
	
	public FilterState(GameHost host)
	{
		super(host);
		
		screenWidth = this.host.getWidth();
		screenHeight = this.host.getHeight();
		
	}
	
	
	@Override
	public void render(Graphics2D g, int sw, int sh)
	{	
		
		if(!Model.isFiltered){
		    //Sledece cetiri linije samo kopiraju sadrzaj iz right u left image
			ColorModel cm = imageLeft.getColorModel();
			boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
			WritableRaster raster = imageLeft.copyData(null);
			imageRight = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
			for(FilterType filterType: Model.selectedFilters)
				this.imageRight = makeImage(imageRight, filterType);
			Model.isFiltered = true;
		}
		
		if(moveX < xR)
		{
			g.drawImage(this.imageAlpha(imageLeft, imageRight), moveX,  yL, null);
			
		}
			
		else
		{
			if(pixel.move != Move.STOP)
			{
				this.imageSpiral = this.imageSpiral(imageSpiral, imageLeft);
				g.drawImage(this.imageSpiral, xL, yL, null);
				
				g.setColor(Color.red);
				g.drawImage(imageRight, xR, yR, null);
				Font font = new Font("Serif" , Font.BOLD, 24);
				g.setFont(font);
				g.drawRect(xR - 1, yR - 1, imageWidth + 2, imageHeight + 2);
				g.drawString("After Krugher&Brant RED", xR, yR / 2);
				
			}
			else
			{
			g.drawImage(imageLeft, xL, yL, null);
			g.drawImage(imageRight, xR, yR, null);
			
			g.setColor(Color.red);
			
			g.drawRect(xR - 1, yR - 1, imageWidth + 2, imageHeight + 2);
			g.drawRect(xL - 1, yL - 1, imageWidth + 2, imageHeight + 2);
		
			Font font = new Font("Serif" , Font.BOLD, 24);
			g.setFont(font);
			
			g.drawString("Before Krugher&Brant RED", xL, yL / 2);
			g.drawString("After Krugher&Brant RED", xR, yR / 2);
			}
		}
		

	
		
	}
	
	@Override
	public void update()
	{
	    if(moveX < xR)
	    	moveX += 10;
	    else moveX = xR;
	}
	
	public enum Move
	{
		UP,
		RIGHT,
		LEFT,
		DOWN,
		STOP,
	}
	
	public BufferedImage imageScale(BufferedImage image)
	{
		WritableRaster source = image.getRaster() ;
		
		int widthNew = (5 * this.screenWidth) / 12 ;
		int heightNew = (5 * this.screenHeight) / 12;
	
		
		WritableRaster target = Util.createRaster(widthNew, heightNew, false);
		
		double xRatio = source.getWidth() / (double) widthNew;
		double yRatio = source.getHeight() / (double) heightNew;
		
		int px,py;
		
		int rgb[] = new int[3];
		
		for(int i = 0; i < heightNew; i++)
		{
			for(int j = 0; j < widthNew; j++)
			{
				px = (int) Math.floor(j * xRatio);
				py = (int) Math.floor(i * yRatio);
				
				source.getPixel(px, py, rgb);
				target.setPixel(j, i, rgb);
				
			}
		}
		
		return Util.rasterToImage(target);
	}
	
	
	private BufferedImage imageSpiral(BufferedImage imageSpiral, BufferedImage imageOriginal)
	{
		WritableRaster source= imageOriginal.getRaster() ;
		WritableRaster target = imageSpiral.getRaster();
		
		for(int i = 0 ; i < 5000 && pixel.move != Move.STOP; i++)
		{
		
			int rgb[] = new int[3];
			source.getPixel(this.pixel.x, this.pixel.y, rgb);
		
			
			target.setPixel(this.pixel.x, this.pixel.y, rgb);
			this.pixel.move();
			//System.out.println(pixel);
		}
		
		return Util.rasterToImage(target);
	}
	
	public class Pixel
	{
		public int x;
		private int y;
		private int xLeft;
		private int xRight;
		private int yTop;
		private int yBottom;
		private Move move;

		public Pixel(int imageWidth, int imageHeight)
		{
			this.x = 0;
			this.y = 0;
			this.xLeft = -1;
			this.xRight = imageWidth;
			
			this.yTop = -1;
			this.yBottom = imageHeight;
			this.move = Move.DOWN;
			//System.out.println(this);
		}
		
		public void move()
		{
			
			//if(!(xRight - x == x - xLeft && yBottom - y ==  y - yTop ))
			if(!(check(x, xLeft) && check(xRight, x) && check(yBottom, y)  &&  check(y, yTop) ) && !(xRight - x == x - xLeft && yBottom - y ==  y - yTop ))
			{
				switch(move)
				{
				case UP: 
					this.y --; 
					if(y == yTop + 1)
					{
						move = Move.LEFT;
						this.xRight --;
					}
					break;
				case DOWN:
					this.y ++;
					if(y == yBottom - 1)
						{
						move = Move.RIGHT;
						this.xLeft ++;
						}	
					break;
				case LEFT:
					this.x --; 
					if(x == xLeft + 1)
					{
						move = Move.DOWN;
						this.yTop ++;
					}
					break;
				case RIGHT:
					this.x ++; 
					if(x == xRight - 1)
					{
						move = Move.UP;
						this.yBottom --;
					}
					break;
				default:
					break;
				}
			}
			else
			{
				move = Move.STOP;

			}
		}
		
		public String toString()
		{
			return this.move + "xL: " + this.xLeft + ", xR:" + this.xRight + ", x:" + this.x + 
					"|yT: " + this.yTop + ", yB:" + this.yBottom + ", y:" + this.y;
		}
		
	}
	
	
	private BufferedImage imageBlack(BufferedImage image)
	{
		WritableRaster source = image.getRaster();
		WritableRaster target = Util.createRaster(source.getWidth(), source.getHeight(), false);
		
		int rgb[] = new int[3];
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		
		for(int y = 0; y < source.getHeight() ; y++)
		{
			for(int x = 0; x < source.getWidth(); x++)
			{	
				target.setPixel(x, y, rgb);
			}
		}
		
		return Util.rasterToImage(target);
	}

	private BufferedImage imageAlpha(BufferedImage imageLeft, BufferedImage imageRight)
	{
		WritableRaster sourceLeft = imageLeft.getRaster();
		WritableRaster sourceRight = imageRight.getRaster();
		WritableRaster target = Util.createRaster(sourceLeft.getWidth(), sourceLeft.getHeight(), false);
		
		int rgbLeft[] = new int[3];
		int rgbRight[] = new int[3];
		
		int width = this.xR - this.xL;
		float alpha = (float) (moveX - this.xL) / (float) width; 
		if(alpha > 1.0f)
			alpha = 1.0f;
		
		for(int y = 0; y < sourceLeft.getHeight(); y++)
		{
			for(int x = 0; x < sourceLeft.getWidth(); x++)
			{
				sourceLeft.getPixel(x, y, rgbLeft);
				sourceRight.getPixel(x, y, rgbRight);
				
				rgbLeft[0] = Util.lerpI(rgbLeft[0], rgbRight[0], alpha);
				rgbLeft[1] = Util.lerpI(rgbLeft[1], rgbRight[1], alpha);
				rgbLeft[2] = Util.lerpI(rgbLeft[2], rgbRight[2], alpha);
				
				target.setPixel(x, y, rgbLeft);
			}	
		}
		
		return Util.rasterToImage(target);
	}
	
	private BufferedImage makeImage(BufferedImage image, FilterType filterType)
	{
		switch(filterType)
		{
		case EDGEDETECT: case BLUR: case SHARPEN: case EMBOSS:
			return this.makeImageFromMatrix(image, filterType);
		default:
			return this.makeImageRGB(image, filterType);
		}
	}
	
	private BufferedImage makeImageRGB(BufferedImage image, FilterType filterType)
	{
		
		WritableRaster source = image.getRaster();
		WritableRaster target = Util.createRaster(image.getWidth(), image.getHeight(), false);
		
		int rgb[] = new int[3];
		int i;
		
		
		for(int y = 0; y < image.getHeight(); y++)
		{			
			for(int x = 0; x < image.getWidth(); x++)
			{
				switch(filterType)
				{
				case WAVES: 
					float power = 8.0f;
					float size = 0.07f;
					float srcX = (float)(x + Math.sin(y * size) * power);
					float srcY = (float)(y + Math.cos(x * size) * power);
					Util.bilSample(source, srcX, srcY, rgb);
					target.setPixel(x, y, rgb); 
					break;
				
				case FLIP:
					source.getPixel(x, y, rgb);
					target.setPixel(source.getWidth() - x - 1, y, rgb);
					break;
				
				case NEGATIVE:
					source.getPixel(x, y, rgb);
					rgb[0] = 255 - rgb[0];
					rgb[1] = 255 - rgb[1];
					rgb[2] = 255 - rgb[2];
					target.setPixel(x, y, rgb);
					break;
					
				case BINARY:
					source.getPixel(x, y, rgb);
					i = (int)(rgb[0] * 0.30 + rgb[1] * 0.59 + rgb[2] * 0.11);
					if(i > 160) i = 255;
					else i = 0;					
					rgb[0] = i;
					rgb[1] = i;
					rgb[2] = i;
					target.setPixel(x, y, rgb);
					break;
					
				case GRAYSCALE:
					source.getPixel(x, y, rgb);
					i = (int)(rgb[0] * 0.30 + rgb[1] * 0.59 + rgb[2] * 0.11);
					rgb[0] = i;
					rgb[1] = i;
					rgb[2] = i;
					target.setPixel(x, y, rgb);
					break;
				
				case POSTERIZE:
					source.getPixel(x, y, rgb);
					rgb[0] = (rgb[0] / 50) * 50;
					rgb[1] = (rgb[1] / 50) * 50;
					rgb[2] = (rgb[2] / 50) * 50;
					target.setPixel(x, y, rgb);
					break;
				
				case CONTRAST:
					source.getPixel(x, y, rgb);
					double contrast = 1.5;
					rgb[0] = saturate((int)((rgb[0] - 128) * contrast + 128));
					rgb[1] = saturate((int)((rgb[1] - 128) * contrast + 128));
					rgb[2] = saturate((int)((rgb[2] - 128) * contrast + 128));
					target.setPixel(x, y, rgb);
					break;
				
				case COLOREDNOISE:
					int noise = 50;
					Random rnd = new Random();
					source.getPixel(x, y, rgb);
					rgb[0] = saturate(rgb[0] + rnd.nextInt(noise) - noise / 2);
					rgb[1] = saturate(rgb[1] + rnd.nextInt(noise) - noise / 2);
					rgb[2] = saturate(rgb[2] + rnd.nextInt(noise) - noise / 2);
					target.setPixel(x, y, rgb);
					break;
					
				case BRIGHTNESS:
					source.getPixel(x, y, rgb);
					int brightness = 50;
					rgb[0] = saturate(rgb[0] + brightness);
					rgb[1] = saturate(rgb[1] + brightness);
					rgb[2] = saturate(rgb[2] + brightness);
					target.setPixel(x, y, rgb);
					break;
				
				default:break;
				}
				
				
			}
		}
		
		return Util.rasterToImage(target);
	}
	
	private BufferedImage makeImageFromMatrix(BufferedImage image, FilterType filterType)
	{
		float[][] matrix = getMatrix(filterType);
		
		WritableRaster source = image.getRaster();
		WritableRaster target = Util.createRaster(source.getWidth(), source.getHeight(), false);
		
		int rgb[] = new int[3];
		int pixel[] = new int[3];
		
		for(int y = 1; y < source.getHeight() - 1; y++)
		{
			for(int x = 1; x < source.getWidth() - 1; x++)
			{
				pixel[0] = 0;
				pixel[1] = 0;
				pixel[2] = 0;
				
				for(int Y = 0; Y < 3; Y++)
				{
					for(int X = 0; X < 3; X++)
					{
						
						pixel[0] += (int) (rgb[0] * matrix[X][Y]);
						pixel[1] += (int) (rgb[1] * matrix[X][Y]);
						pixel[2] += (int) (rgb[2] * matrix[X][Y]);
					}
				}
				
				pixel[0] = saturate(pixel[0]);
				pixel[1] = saturate(pixel[1]);
				pixel[2] = saturate(pixel[2]);
				
				target.setPixel(x, y, pixel);
			}
		}
		
		return Util.rasterToImage(target);
	}
	
	private float[][] getMatrix(FilterType filterType) 
	{
		float[][] matrix;
		switch(filterType)
		{
		case BLUR: matrix = new float[][] 
				{
					{ 0, 0.2f, 0 },
					{ 0.2f, 0.2f, 0.2f },
					{ 0, 0.2f, 0 }
				}; break;
		case EDGEDETECT: matrix = new float[][] 
				{
					{ -1, -1, -1 },
					{ -1, 8, -1 },
					{ -1, -1, -1 }
				}; break;
		case SHARPEN: matrix = new float[][] 
				{
					{ -1, -1, -1 },
					{ -1, 9, -1 },
					{ -1, -1, -1 }
				}; break;
		case EMBOSS: matrix = new float[][] 
				{
					{ -1, -1, 0 },
					{ -1, 0, 1 },
					{ 0, 1, 1 }
				}; break;
		default:
			matrix = new float[][] 
				{
					{ 1, 0, 0 },
					{ 0, 1, 0 },
					{ 0, 0, 1 }
				};
			break;
		}
		
		return matrix;
	}

	
	
	public static int clamp(int value, int min, int max)
	{
		if(value < min) return min;
		if(value > max) return max;
		return value;
	}
	
	public static int saturate(int value)
	{
		return clamp(value, 0, 255);
	}
	@Override
	public boolean handleWindowClose()
	{
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean check(int a, int b)
	{
		if(a - b == 0 || a - b == 1)
			return true;
		else return false;
	}
	
	@Override
	public String getName()
	{
		return "filterstate";
	}

	@Override
	public void resumeState()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suspendState()
	{
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void handleMouseDown(int x, int y, GFMouseButton button)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseUp(int x, int y, GFMouseButton button)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseMove(int x, int y)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleKeyDown(int keyCode)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleKeyUp(int keyCode)
	{
		if(keyCode == KeyEvent.VK_F4) {
			System.exit(0);
		}
		
		if(keyCode == KeyEvent.VK_ESCAPE){
			host.setState("maingamestate");
		}
	}

	public BufferedImage getImageLeft() {
		return imageLeft;
	}

	public void setImageLeft(BufferedImage imgLeft) {
		imageLeft = imgLeft;
	}

	public BufferedImage getImageRight() {
		return imageRight;
	}

	public void setImageRight(BufferedImage imageRight) {
		this.imageRight = imageRight;
	}

}
