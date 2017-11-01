package main;

import java.awt.Color;
import java.awt.Toolkit;
//import java.awt.image.WritableRaster;

import game_states.MainGameState;
import game_states.MainMenuState;
import rafgfxlib.GameHost;
//import rafgfxlib.ImageViewer;
//import rafgfxlib.Util;
//import rafgfxlib_e.ImageViewer_E;

public class Main {

	public static void main(String[] args) 
	{
		/*
		jedan ekran:
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		*/
		/*
		vise ekrana:
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		*/
		
		/*int screenWidth = 300;
		int screenHeight = 300;
		WritableRaster rasterLeft = Util.createRaster(screenWidth, screenHeight, false);
		WritableRaster rasterRight = Util.createRaster(screenWidth, screenHeight, false);
		
		int rgb[] = new int[3];
		int rgb2[] = new int[3];
		
		rgb[0] = 255;
		rgb[1] = 0;
		rgb[2] = 0;
				
		rgb2[0] = 0;
		rgb2[1] = 255;
		rgb2[2] = 0;
		
		
		for(int y = 0; y <screenHeight; y++)
		{
			for(int x = 0; x < screenWidth; x++)
			{
				rasterLeft.setPixel(x, y, rgb);
				rasterRight.setPixel(x, y, rgb2);
			}
		}
		
		
		ImageViewer.showImageWindow(Util.rasterToImage(ImageViewer_E.rasterCombine(rasterLeft, rasterRight, false)), "Krugher");
	
	*/
	
		
		GameHost host = new GameHost("Krugher and Brent RED", (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()*3/4,
																(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*3/4, false);
		
		host.setUpdateRate(59);
		host.setBackgroundClearColor(Color.BLACK);

		new MainMenuState(host);
		new MainGameState(host);
		
		host.setState("mainmenustate");
		
		
		
		
	}
	


}
