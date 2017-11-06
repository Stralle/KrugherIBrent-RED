package game_states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Model;
import rafgfxlib.GameHost;
import rafgfxlib.GameHost.GFMouseButton;
import rafgfxlib.GameState;
import rafgfxlib.Util;


/**
 * Main menu for a video game Krugher and Brent RED.
 * This class extends GameState and it will provide all necessary things in main menu.
 * @author Strahinja Rodic (Stralle)
 *
 */
public class MainMenuState extends GameState {
		
		private static final int BEER_MAX = 6;
				
		private static class Beer {
			private int posX;
			private int posY;
			private int speed;
		}
		
		private static final int BUBBLE_MAX = 100;
		
		private static class Bubble {
			private int posX;
			private int posY;
			private int speed;
			private int life = 0;
			private int width;
			private int height;
		}
		
		private enum MenuButton {
			Start,
			Upload,
			Controls,
			About,
			Exit
		}
		
		private enum MenuType {
			Default,
			About,
			Controls,
		}
		
		public BufferedImage globalImage = Util.loadImage("/photos/doge.png");
				
		private Beer[] beers = new Beer[BEER_MAX];
		private Bubble[] bubbles = new Bubble[BUBBLE_MAX];
		
		private Image beerImage;
		private Random random = new Random();
		
		private Color stringColor = Color.CYAN;
		private Color defaultColor = Color.CYAN;
		
		private MenuButton currentMenuButton = null;
		private MenuType currentMenuType = MenuType.Default;
		
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
				beers[i].posX = random.nextInt(host.getWidth() - 150);
				beers[i].posY = random.nextInt(10) - 150;
				beers[i].speed = random.nextInt(10 + 1) + 10;
			}
			
//			System.out.println("BUBBLES INIT");
			for(int i = 0; i < BUBBLE_MAX; i++) {
				bubbles[i] = new Bubble();
				bubbles[i].posX = random.nextInt(host.getWidth());
				bubbles[i].posY = random.nextInt(100)*(-1) + host.getHeight();
				bubbles[i].speed = random.nextInt(10 + 1) + 5;
				bubbles[i].life = random.nextInt(15) + 10;
				bubbles[i].height = random.nextInt(10) + 20;
				bubbles[i].width = random.nextInt(10) + 20;
//				System.out.println(bubbles[i].posX + " " +bubbles[i].posY + " " +bubbles[i].speed + " " +bubbles[i].life + " " +bubbles[i].height + " " +bubbles[i].width);
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
			
			Font font = new Font("Serif", Font.BOLD, 24);
			g.setFont(font);
			g.setColor(defaultColor);
			
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Start) {
				g.setColor(stringColor);
				g.drawString("START GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("START GAME", offsetX, offsetY);
			}
			
			offsetY += 50;
			
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Upload) {
				g.setColor(stringColor);
				g.drawString("CHOOSE PHOTO", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("CHOOSE PHOTO", offsetX, offsetY);
			}
			
			offsetY += 50;
			
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Controls) {
				g.setColor(stringColor);
				g.drawString("CONTROLS", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("CONTROLS", offsetX, offsetY);
			}
			
			offsetY += 50;
			
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.About) {
				g.setColor(stringColor);
				g.drawString("ABOUT GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("ABOUT GAME", offsetX, offsetY);
			}
			
			offsetY += 50;
			
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Exit) {
				g.setColor(stringColor);
				g.drawString("EXIT GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.drawString("EXIT GAME", offsetX, offsetY);
			}

		}

		public void renderAbout(Graphics2D g, int sw, int sh) {
			int startX = host.getWidth() / 2 - 200;
			int startY = host.getHeight() / 2 - 250;
			int offsetX = startX + 25;
			int offsetY = startY + 50;
			
			Font font = new Font("Serif", Font.BOLD, 24);
			g.setFont(font);
			g.setColor(defaultColor);
			
			g.drawString("WELCOME TO KRUGHER & BRENT RED!", offsetX, offsetY);
			offsetY += 30;
			g.drawString("CHOOSE AN IMAGE BY CLICKING ON", offsetX, offsetY);
			offsetY += 30;
			g.drawString("UPLOAD IMAGE MENU ITEM.", offsetX, offsetY);
			offsetY += 30;
			g.drawString("MOVE THE DWARF TO THE BEER", offsetX, offsetY);
			offsetY += 30;
			g.drawString("HE WILL DRINK IT", offsetX, offsetY);
			offsetY += 30;
			g.drawString("IF YOU HIT SPACE THAT WILL", offsetX, offsetY);
			offsetY += 30;
			g.drawString("APPLY THE FILTER TO IMAGE.", offsetX, offsetY);
			offsetY += 60;
			g.drawString("Long time ago, in a brewery far far ago...", offsetX, offsetY);
			offsetY += 30;
			g.drawString("Krugher & Brent RED was invented", offsetX, offsetY);
			offsetY += 30;
			g.drawString("And a dwarf has drinked it all.", offsetX, offsetY);
			offsetY += 100;
			
			g.setColor(Color.GREEN);
			g.drawString("Developed by: nvkvkc, SeriousFresh and Stralle", offsetX, offsetY);
			offsetY += 50;
			
			offsetX += 85;
			g.setColor(Color.RED);
			g.drawString("Press ESC to go back", offsetX, offsetY);
			
		}
		
		public void renderControls(Graphics2D g, int sw, int sh) {
			int startX = host.getWidth() / 2 - 200;
			int startY = host.getHeight() / 2 - 150;
			int offsetX = startX + 25;
			int offsetY = startY + 50;
			
			Font font = new Font("Serif", Font.BOLD, 24);
			g.setFont(font);
			g.setColor(Color.GREEN);
			
			offsetX += 85;
			g.drawString("		  CONTROLS", offsetX, offsetY);
			offsetY += 50;
			offsetX -= 85;
			g.setColor(defaultColor);
			g.drawString("LEFT ARROW == MOVE LEFT", offsetX, offsetY);
			offsetY += 30;
			g.drawString("RIGHT ARROW == MOVE RIGHT", offsetX, offsetY);
			offsetY += 30;
			g.drawString("UP ARROW== MOVE UP", offsetX, offsetY);
			offsetY += 30;
			g.drawString("DOWN ARROW == MOVE DOWN", offsetX, offsetY);
			offsetY += 30;
			g.drawString("SPACE == APPLY FILTER", offsetX, offsetY);
			offsetY += 50;

			offsetX += 70;
			g.setColor(Color.RED);
			g.drawString("Press ESC to go back", offsetX, offsetY);
			
		}
		
		@Override
		public void render(Graphics2D g, int sw, int sh) {	
			
			for(Beer beer : beers) {	
				g.drawImage(beerImage, beer.posX, beer.posY, null);
			}
			
			for(Bubble bubble : bubbles) {
//				System.out.println("Draw bubble" + bubble.posX + " " + bubble.posY + " " + bubble.width + " " + bubble.height);
				g.setColor(Color.WHITE);
				g.drawOval(bubble.posX, bubble.posY, bubble.width, bubble.height);
				g.fillOval(bubble.posX, bubble.posY, bubble.width, bubble.height);
			}
			
			if(currentMenuType == MenuType.Default)
				renderGameMenu(g, sw, sh, currentMenuButton);
			if(currentMenuType == MenuType.About)
				renderAbout(g, sw, sh);
			if(currentMenuType == MenuType.Controls)
				renderControls(g, sw, sh);
			
//			g.drawImage(globalImage, null, 50, 50);
			
		}

		@Override
		public void update() {
			for(Beer beer: beers) {
				
				if(beer.posY >= host.getHeight()) {
					beer.posY = -300;
					beer.posX = random.nextInt(host.getWidth() - 100);
				}
				else {
					beer.posY += beer.speed;
				}
				
			}
			
			for(Bubble bubble: bubbles) {
				
				if(bubble.life <= 0) {
					bubble.posX = random.nextInt(host.getWidth());
					bubble.posY = random.nextInt(100)*(-1) + host.getHeight();
					bubble.speed = random.nextInt(10 + 1) + 5;
					bubble.life = random.nextInt(15) + 10;
					bubble.height = random.nextInt(10) + 20;
					bubble.width = random.nextInt(10) + 20;
				}
				else {
					bubble.posY -= bubble.speed;
					bubble.life--;
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
				if(currentMenuType == MenuType.Default) {
					if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //START
						host.setState("maingamestate");
						FilterState.setImageLeft(Model.getGlobalImage());
					}
					offsetY += 50;
					if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //UPLOAD
						//TODO: upload picture
						globalImage = Util.browseForImage("", host.getWindow());
						Model.setGlobalImage(globalImage);
					}
					offsetY += 50;
					if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //CONTROLS
						currentMenuType = MenuType.Controls;
					}
					offsetY += 50;
					if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //ABOUT
						currentMenuType = MenuType.About;
					}
					offsetY += 50;
					if(x >= offsetX - 5 && x <= offsetX - 5 + 200 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //EXIT
						System.exit(0);
					}
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
			if(keyCode == KeyEvent.VK_ESCAPE && currentMenuType != MenuType.Default) {
				currentMenuType = MenuType.Default;
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
