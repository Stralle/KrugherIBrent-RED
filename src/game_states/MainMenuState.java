package game_states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Random;

import rafgfxlib.GameHost;
import rafgfxlib.GameHost.GFMouseButton;
import rafgfxlib.GameState;
import rafgfxlib.Util;

public class MainMenuState extends GameState {
		
		private static final int BEER_MAX = 6;
				
		private static class Beer {
			private int posX;
			private int posY;
			private int speed;
		}
		
		private enum MenuButton {
			Start,
			Upload,
			Controls,
			About,
			Exit
		}
		
		private Beer[] beers = new Beer[BEER_MAX];
		
		private Image beerImage;
		private Random random = new Random();
		
		private Color stringColor = Color.CYAN;
		private Color defaultColor = Color.CYAN;
		
		private MenuButton currentMenuButton = null;
		
		public MainMenuState(GameHost host) {
			super(host);
			
			try {
				beerImage = Util.loadImage("/photos/red.png");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for(int i = 0; i < BEER_MAX; ++i)
			{
				beers[i] = new Beer();
				beers[i].posX = random.nextInt(host.getWidth());
				beers[i].posY = 0;
				beers[i].speed = random.nextInt(20 + 1) + 10;
			}
		}

		@Override
		public boolean handleWindowClose() {
			return true;
		}

		@Override
		public void resumeState() { }

		@Override
		public void suspendState() { }

		public void renderGameMenu(Graphics2D g, int sw, int sh, MenuButton menuButton) {
			
			int startX = host.getWidth() / 2 - 100;
			int startY = host.getHeight() / 2 - 150;
			int offsetX = startX + 25;
			int offsetY = startY + 50;
			
//			int picCoordX = offsetX - 30;
//			int picCoordY = offsetY - 35;

			Font font = new Font("Serif", Font.BOLD, 24);
			g.setFont(font);
			g.setColor(defaultColor);
			
//			g.drawImage(Util.loadImage("/photos/red-small.png"), picCoordX, picCoordY, null);
			
//			g.drawRect(offsetX - 5, offsetY - 24, 200, 40);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Start) {
				g.setColor(stringColor);
				g.drawString("START GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("START GAME", offsetX, offsetY);
			}
			
			offsetY += 50;
			
//			g.drawRect(offsetX - 5, offsetY - 24, 200, 40);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Upload) {
				g.setColor(stringColor);
				g.drawString("CHOOSE PHOTO", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("CHOOSE PHOTO", offsetX, offsetY);
			}
			
			offsetY += 50;
			
//			g.drawRect(offsetX - 5, offsetY - 24, 200, 40);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Controls) {
				g.setColor(stringColor);
				g.drawString("CONTROLS", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("CONTROLS", offsetX, offsetY);
			}
			
			offsetY += 50;
			
//			g.drawRect(offsetX - 5, offsetY - 24, 200, 40);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.About) {
				g.setColor(stringColor);
				g.drawString("ABOUT GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("ABOUT GAME", offsetX, offsetY);
			}
			
			offsetY += 50;
			
//			g.drawRect(offsetX - 5, offsetY - 24, 200, 40);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Exit) {
				g.setColor(stringColor);
				g.drawString("EXIT GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("EXIT GAME", offsetX, offsetY);
			}

		}
		
		@Override
		public void render(Graphics2D g, int sw, int sh) {	
			
			for(Beer beer : beers) {	
				g.drawImage(beerImage, beer.posX, beer.posY, null);
			}
			
			renderGameMenu(g, sw, sh, currentMenuButton);

		}

		@Override
		public void update() {
			for(Beer beer: beers) {
				
				if(beer.posY >= host.getHeight()) {
					beer.posY = -200;
					beer.posX = random.nextInt(host.getWidth());
				}
				else {
					beer.posY += beer.speed;
				}
				
			}
			
		}

		@Override
		public void handleMouseDown(int x, int y, GFMouseButton button) { }

		@Override
		public void handleMouseUp(int x, int y, GFMouseButton button) { 
			int startX = host.getWidth() / 2 - 100;
			int startY = host.getHeight() / 2 - 150;
			int offsetX = startX + 25;
			int offsetY = startY + 50;
			
			if(button == GFMouseButton.Left)
			{
				if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //START
					host.setState("maingamestate");
				}
				offsetY += 50;
				if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //UPLOAD
					//TODO: upload picture
				}
				offsetY += 50;
				if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //CONTROLS
					//TODO: controls for the game
				}
				offsetY += 50;
				if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //ABOUT
					//TODO: About the game
				}
				offsetY += 50;
				if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //EXIT
					System.exit(0);
				}
			}
		}

		@Override
		public void handleMouseMove(int x, int y) { 
			int startX = host.getWidth() / 2 - 100;
			int startY = host.getHeight() / 2 - 150;
			int offsetX = startX + 25;
			int offsetY = startY + 50;
			int bool = 0;
			
			Color stringColor = Color.RED;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.Start;
				bool = 1;
			}
			offsetY += 50;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.Upload;
				bool = 1;
			}
			offsetY += 50;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.Controls;
				bool = 1;
			}
			offsetY += 50;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.About;
				bool = 1;
			}
			offsetY += 50;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.Exit;
				bool = 1;
			}
			if(bool == 0) {
				this.stringColor = defaultColor;
				currentMenuButton = null;
			}
		}

		@Override
		public void handleKeyDown(int keyCode) { }

		@Override
		public void handleKeyUp(int keyCode) {
			if(keyCode == KeyEvent.VK_F4) {
				System.exit(0);
			}
			//TODO: HOW TO EDIT HIS CLASS?
//			if(keyCode == KeyEvent.VK_F5) {
//				host.setFullscreenMode(true);
//			}
		}

		@Override
		public String getName() {
			return "mainmenustate";
		}
}
