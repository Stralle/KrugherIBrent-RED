package rafgfxlib_e;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.JFrame;
import rafgfxlib.ImageViewer;
import rafgfxlib.Util;

public class ImageViewer_E extends ImageViewer

{

	public ImageViewer_E(BufferedImage img)
	{
		super(img);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2854718298324222609L;
	
	public static WritableRaster rasterCombine(WritableRaster raster1, WritableRaster raster2, boolean alpha)
	{
		int width = raster1.getWidth() + raster2.getWidth();
		int height = raster1.getHeight();
		WritableRaster rasterOut = Util.createRaster(width, height, alpha);
		int rgb[] = new int[3];
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(x < raster1.getWidth())
					rasterOut.setPixel(x, y, raster1.getPixel(x, y, rgb));
				else
					rasterOut.setPixel(x, y, raster2.getPixel(x - raster1.getWidth(), y, rgb));
				
			}
		}
		
		return rasterOut;
	}
	
	public static void showImageWindow(BufferedImage image, String title)
	{
		JFrame win = constructViewer(image, null, title);
		win.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	//	win.setUndecorated(true);
		win.setVisible(true);
	}

}
