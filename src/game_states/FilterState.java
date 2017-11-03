package game_states;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

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
		CONSTRAST,
		EDGEDETECT
	}

	private BufferedImage imageLeft;
	private BufferedImage imageRight;
	
	public FilterState(GameHost host)
	{
		super(host);
		this.imageLeft = Util.loadImage(Model.imagePath);
		this.imageRight = makeImage(imageLeft, Model.filterType);		
	}

	private BufferedImage makeImage(BufferedImage image, FilterType filterType)
	{
		int[][] matrix = getMatrix(filterType);
		
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
						source.getPixel(x + X - 1, y + Y - 1, rgb);
						
						pixel[0] += rgb[0] * matrix[X][Y];
						pixel[1] += rgb[1] * matrix[X][Y];
						pixel[2] += rgb[2] * matrix[X][Y];
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
	
	private int[][] getMatrix(FilterType filterType) 
	{
		int[][] matrix;
		switch(filterType)
		{
		case NEGATIVE: matrix = new int[][] 
				{
					{ -1, 0, 0 },
					{ 0, -1, 0 },
					{ 0, 0, -1 }
				}; break;
		default:
			matrix = new int[][] 
				{
					{ 1, 1, 1 },
					{ 1, 1, 1 },
					{ 1, 1, 1 }
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

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
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
	public void render(Graphics2D g, int sw, int sh)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update()
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
		// TODO Auto-generated method stub
		
	}

	public BufferedImage getImageLeft() {
		return imageLeft;
	}

	public void setImageLeft(BufferedImage imageLeft) {
		this.imageLeft = imageLeft;
	}

	public BufferedImage getImageRight() {
		return imageRight;
	}

	public void setImageRight(BufferedImage imageRight) {
		this.imageRight = imageRight;
	}

}
