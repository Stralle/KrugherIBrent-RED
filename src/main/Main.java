package main;

import java.awt.Color;
import java.awt.Toolkit;
//import java.awt.image.WritableRaster;

import game_stage.Map;
import game_states.FilterState;
import game_states.MainMenuState;
import rafgfxlib.GameHost;
//import rafgfxlib.ImageViewer;
//import rafgfxlib.Util;
//import rafgfxlib_e.ImageViewer_E;
import rafgfxlib.Util;

public class Main {

	public static FilterState filter;
	
	public static void main(String[] args) 
	{
	
		
		GameHost host = new GameHost("Krugher and Brent RED", (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()*3/4,
																(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*3/4, false);
		
		host.setUpdateRate(59);
		host.setBackgroundClearColor(Color.BLACK);
		
		host.setIcon(Util.loadImage("/photos/icon.png"));

		new MainMenuState(host);
		new Map(host);
		filter = new FilterState(host);
		
		host.setState("mainmenustate");
		
		// GAME STATE je promenjen u maingamestate
		
		
	}
	
}