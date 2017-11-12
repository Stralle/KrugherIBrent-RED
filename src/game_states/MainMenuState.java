package game_states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

import main.Main;
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
		
		public BufferedImage globalImage = Util.loadImage("/photos/drunk.jpg");
		private BufferedImage backgroundImage = null;
				
		private Beer[] beers = new Beer[BEER_MAX];
		private Bubble[] bubbles = new Bubble[BUBBLE_MAX];
		
		Graphics2D grafika = null;
		
		private Image beerImage;
		private Image corkImage;
//		private Image controlsImage;
//		private Image aboutImage;
		private Random random = new Random();
		
		private float imageAngle = (float)(Math.random() * Math.PI * 2.0);
//		private float imageRot = (float)(Math.random() - 0.5) * 0.1f;
		
		private Color stringColor = Color.CYAN;
		private Color defaultColor = Color.CYAN;
		
		private MenuButton currentMenuButton = null;
		private MenuType currentMenuType = MenuType.Default;
		
		private int start = 1;

//		private int clampx(int x) {
//			if(x > host.getWidth() - 200) {
//				return host.getWidth() - 200;
//			}
//			if(x < 200) {
//				return 200;
//			}
//			return x;
//		}
//
//		private int clampy(int x) {
//			if(x > host.getHeight() - 50) {
//				return host.getHeight() - 50;
//			}
//			return x;
//		}
		
		public void loadCork() {
			
		}
		
		public MainMenuState(GameHost host) {
			super(host);
			
			try {
				beerImage = Util.loadImage("/photos/redbeer.png");
				corkImage = Util.loadImage("/photos/cork.png");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for(int i = 0; i < BEER_MAX; ++i) {
				beers[i] = new Beer();
				beers[i].posX = random.nextInt(host.getWidth() - 150);
				beers[i].posY = random.nextInt(10) - 150;
				beers[i].speed = random.nextInt(10 + 1) + 10;
			}
			
			for(int i = 0; i < BUBBLE_MAX; i++) {
				bubbles[i] = new Bubble();
				bubbles[i].posX = random.nextInt(host.getWidth()) - 30;
				bubbles[i].posY = random.nextInt(100)*(-1) + host.getHeight();
				bubbles[i].speed = random.nextInt(10 + 1) + 5;
				bubbles[i].life = random.nextInt(15) + 10;
				bubbles[i].height = random.nextInt(10) + 20;
				bubbles[i].width = random.nextInt(10) + 20;
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
			
			int startX = host.getWidth() / 4;
			int startY = host.getHeight() / 2 - 120;
			int offsetX = startX + 25;
			int offsetY = startY + 50;
			
			Font font = new Font("Serif", Font.ITALIC, 36);
			g.setFont(font);
			g.setColor(defaultColor);
			
			Color bgColor = Color.BLUE;
			int shadowX = -3;
			int shadowY = 3;
			
			g.setColor(bgColor);
			g.drawString("START GAME", offsetX + shadowX, offsetY - shadowY);
			
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Start) {			
				g.setColor(stringColor);
				g.drawString("START GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.setColor(defaultColor);
				g.drawString("START GAME", offsetX, offsetY);
			}
			
			offsetY += 50;

			g.setColor(bgColor);
			g.drawString("CHOOSE PHOTO", offsetX + shadowX, offsetY - shadowY);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Upload) {
				g.setColor(stringColor);
				g.drawString("CHOOSE PHOTO", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.setColor(defaultColor);
				g.drawString("CHOOSE PHOTO", offsetX, offsetY);
			}
			
			offsetY += 50;

			g.setColor(bgColor);
			g.drawString("CONTROLS", offsetX + shadowX, offsetY - shadowY);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Controls) {
				g.setColor(stringColor);
				g.drawString("CONTROLS", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.setColor(defaultColor);
				g.drawString("CONTROLS", offsetX, offsetY);
			}
			
			offsetY += 50;
			
			g.setColor(bgColor);
			g.drawString("ABOUT GAME", offsetX + shadowX, offsetY - shadowY);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.About) {
				g.setColor(stringColor);
				g.drawString("ABOUT GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.setColor(defaultColor);
				g.drawString("ABOUT GAME", offsetX, offsetY);
			}
			
			offsetY += 50;
			
			g.setColor(bgColor);
			g.drawString("EXIT GAME", offsetX + shadowX, offsetY - shadowY);
			if(!stringColor.equals(defaultColor) && menuButton == MenuButton.Exit) {
				g.setColor(stringColor);
				g.drawString("EXIT GAME", offsetX, offsetY);
				g.setColor(defaultColor);
			} else {
				g.setColor(defaultColor);
				g.drawString("EXIT GAME", offsetX, offsetY);
			}

		}
		
		public static int lerp(int a, int b, double x) {
			return (int)(a + (b - a) * x);
		}
		
		public static void lerpRGB(int[] output, int[] a, int[] b, double x) {
			output[0] = lerp(a[0], b[0], x);
			output[1] = lerp(a[1], b[1], x);
			output[2] = lerp(a[2], b[2], x);
		}

		public void aboutBackground() {
			
			WritableRaster raster = Util.createRaster(500, 250, false);

			int rgb[] = new int[3];
			
			int bojaGL[] = new int[3];
			bojaGL[0] = 255;
			bojaGL[1] = 0;
			bojaGL[2] = 0;
			
			int bojaGD[] = new int[3];
			bojaGD[0] = 0;
			bojaGD[1] = 255;
			bojaGD[2] = 0;
					
			int bojaDL[] = new int[3];
			bojaDL[0] = 0;
			bojaDL[1] = 0;
			bojaDL[2] = 255;
					
			int bojaDD[] = new int[3];
			bojaDD[0] = 255;
			bojaDD[1] = 5;
			bojaDD[2] = 255;
			
			int tmpG[] = new int[3];
			int tmpD[] = new int[3];
			
			for(int y = 0; y < raster.getHeight(); y++)
			{
				for(int x = 0; x < raster.getWidth(); x++)
				{
					double fX = x / (double)raster.getWidth();
					double fY = y / (double)raster.getHeight();
					
					lerpRGB(tmpG, bojaGL, bojaGD, fX);
					
					lerpRGB(tmpD, bojaDL, bojaDD, fX);
					
					lerpRGB(rgb, tmpG, tmpD, fY);
					
					if((x+y)%3 != 0 && (x+y)%2 != 0  && (x+y)%5 != 0 && (x+y)%7 != 0 ||
						(x-y)%3 != 0 && (x-y)%2 != 0  && (x-y)%5 != 0 && (x-y)%7 != 0) {

						raster.setPixel(x, y, rgb);
					}
				}
			}
			
//			aboutImage = Util.rasterToImage(raster);
		}
		
		public void renderAbout(Graphics2D g, int sw, int sh) {

			int startX = host.getWidth() / 2 - 200;
			int startY = host.getHeight() / 2 - 250;
			int offsetX = startX + 25;
			int offsetY = startY + 150;

			Color bgColor = Color.GRAY;
			int shadowX = 2;
			int shadowY = 2;
			
//			g.drawImage(aboutImage, offsetX-5, offsetY-30, null);
			
			Font font = new Font("Serif", Font.BOLD, 24);
			g.setFont(font);
			
			g.setColor(bgColor);
			g.drawString("Long time ago, in a brewery far far ago...", offsetX + shadowX, offsetY - shadowY);
			
			g.setColor(defaultColor);
			g.drawString("Long time ago, in a brewery far far ago...", offsetX, offsetY);
			offsetY += 30;
			
			g.setColor(bgColor);
			g.drawString("Krugher & Brent RED was invented", offsetX + shadowX, offsetY - shadowY);
			
			g.setColor(defaultColor);
			g.drawString("Krugher & Brent RED was invented", offsetX, offsetY);
			offsetY += 30;
			
			g.setColor(bgColor);
			g.drawString("And a dwarf has drinked it all.", offsetX + shadowX, offsetY - shadowY);
			
			g.setColor(defaultColor);
			g.drawString("And a dwarf has drinked it all.", offsetX, offsetY);
			offsetY += 70;
			
			g.setColor(bgColor);
			g.drawString("Developed by: nvkvkc, SeriousFresh and Stralle", offsetX + shadowX, offsetY - shadowY);
			
			g.setColor(defaultColor);
			g.drawString("Developed by: nvkvkc, SeriousFresh and Stralle", offsetX, offsetY);
			offsetY += 50;
			
			offsetX += 35;
			
			g.setColor(bgColor);
			g.drawString("Press ESC to go back", offsetX + shadowX, offsetY - shadowY);
			g.setColor(Color.RED);
			g.drawString("Press ESC to go back", offsetX, offsetY);
			
		}

		public void controlsBackground() {
			WritableRaster raster = Util.createRaster(400, 300, false);

			int rgb[] = new int[3];
			
			int bojaGL[] = new int[3];
			bojaGL[0] = 255;
			bojaGL[1] = 0;
			bojaGL[2] = 0;
			
			int bojaGD[] = new int[3];
			bojaGD[0] = 0;
			bojaGD[1] = 255;
			bojaGD[2] = 0;
					
			int bojaDL[] = new int[3];
			bojaDL[0] = 0;
			bojaDL[1] = 0;
			bojaDL[2] = 255;
					
			int bojaDD[] = new int[3];
			bojaDD[0] = 255;
			bojaDD[1] = 255;
			bojaDD[2] = 255;
			
			int tmpG[] = new int[3];
			int tmpD[] = new int[3];
			
			for(int y = 0; y < raster.getHeight(); y++)
			{
				for(int x = 0; x < raster.getWidth(); x++)
				{
					double fX = x / (double)raster.getWidth();
					double fY = y / (double)raster.getHeight();
					
					lerpRGB(tmpG, bojaGL, bojaGD, fX);
					
					lerpRGB(tmpD, bojaDL, bojaDD, fX);
					
					lerpRGB(rgb, tmpG, tmpD, fY);
					
					if((x+y)%3 != 0 && (x+y)%5 != 0 && (x+y)%7 != 0 &&
						(x-y)%3 != 0 && (x-y)%5 != 0 && (x-y)%7 != 0) {
						
						rgb[0] = 0;
						rgb[1] = random.nextInt(256);
						rgb[2] = 0;
					}

					raster.setPixel(x, y, rgb);
					
				}
			}
			
//			controlsImage = Util.rasterToImage(raster);
		}
		
		public void renderControls(Graphics2D g, int sw, int sh) {
			int startX = host.getWidth() / 2 - 200;
			int startY = host.getHeight() / 2 - 150;
			int offsetX = startX + 25;
			int offsetY = startY + 50;

			Color bgColor = Color.RED;
			int shadowX = 2;
			int shadowY = 2;
			
			Font font = new Font("Serif", Font.BOLD, 24);
			g.setFont(font);
			
			offsetX += 85;
			
			g.setColor(bgColor);
			g.drawString("CONTROLS", offsetX + shadowX, offsetY - shadowY);
			
			g.setColor(Color.GREEN);
			g.drawString("CONTROLS", offsetX, offsetY);
			
			offsetY += 50;
			offsetX -= 105;
			
			g.setColor(bgColor);
			g.drawString("PLAYER MOVEMENTS: W, A, S, D", offsetX + shadowX, offsetY - shadowY);
			
			g.setColor(defaultColor);
			g.drawString("PLAYER MOVEMENTS: W, A, S, D", offsetX, offsetY);
			
			offsetY += 30;
			
			g.setColor(bgColor);
			g.drawString("MAP MOVEMENTS: KEYBOARD ARROWS", offsetX + shadowX, offsetY - shadowY);
			
			g.setColor(defaultColor);
			g.drawString("MAP MOVEMENTS: KEYBOARD ARROWS", offsetX, offsetY);
			
			offsetY += 30;
			
			g.setColor(bgColor);
			g.drawString("SPACE == APPLY FILTER", offsetX + shadowX, offsetY - shadowY);
			
			g.setColor(defaultColor);
			g.drawString("SPACE == APPLY FILTER", offsetX, offsetY);
			
			offsetY += 50;

			offsetX += 70;
			g.setColor(Color.DARK_GRAY);
			g.drawString("Press ESC to go back", offsetX + 2, offsetY - 2);
			g.setColor(Color.RED);
			g.drawString("Press ESC to go back", offsetX, offsetY);
			
		}
		
		public void renderBackground(Graphics2D g, int sw, int sh) {
			BufferedImage image = Util.loadImage("/photos/beerland.jpg");
//			grafika = g;
			
			if(image == null) { System.out.println("Nema slike!"); return; }
			
			WritableRaster source = image.getRaster();
			WritableRaster target = Util.createRaster(sw, sh, false);
			
			int rgb[] = new int[3];
			
			// Waves efekat
			// Snaga distorzije
			float power = 6.0f;
			// Velicina talasa
			float size = 0.15f;
			
			// Vignette efekat
			// Jacina vignette evekta
			double vignette = 2.0;
			// Radius efekta
			final double radius = Math.sqrt(2) / 2.35;
			
			for(int y = 0; y < target.getHeight(); y++)
			{			
				for(int x = 0; x < target.getWidth(); x++)
				{
					// Funkcijama sin() i cos() deformisemo koordinate
					float srcX = (float)(x + Math.sin(y * size) * power);
					float srcY = (float)(y + Math.cos(x * size) * power);
					
					// Koristimo deformisane koordinate za citanje bilinearnog uzorka
					Util.bilSample(source, srcX, srcY, rgb);
					target.setPixel(x, y, rgb);
					
					target.getPixel(x, y, rgb);
					
					// Pronalazimo normalizovane (X,Y) koordinate u 0-1 opsegu
					double fX = x / (double)source.getWidth();
					double fY = y / (double)source.getHeight();
					
					// Daljina normalizovanih koordinata od centra kruga (0.5, 0.5)
					double dist = Math.sqrt((fX - 0.5) * (fX - 0.5) + (fY - 0.5) * (fY - 0.5)) / radius;
					
					// Daljinu stepenujemo da bismo dobili nelinearnu progresiju,
					// iz estetskih razloga (zapamtimo da smo u 0-1 opsegu, 0 ce
					// i dalje biti 0, 1 ce biti 1, samo se mijenja oblik krive
					// izmedju tih krajeva).
					dist = Math.pow(dist, 1.8);
					
					// Trenutnu boju potamnjujemo zavisno od daljine i zeljene
					// jacine vignette efekta.
					rgb[0] = (int)(rgb[0] * (1.0 - dist * vignette))*2/3;
					rgb[1] = (int)(rgb[1] * (1.0 - dist * vignette))*2/3;
					rgb[2] = (int)(rgb[2] * (1.0 - dist * vignette))/2;
					
					target.setPixel(x, y, rgb);
					
				}
			}
			
			backgroundImage = Util.rasterToImage(target);
//			g.drawImage(Util.rasterToImage(target), 0, 0, null);
			
		}
		
		@Override
		public void render(Graphics2D g, int sw, int sh) {	
			if(start == 1) {
				renderBackground(g, sw, sh);
				controlsBackground();
				aboutBackground();
				start = 0;
//				g.drawImage(backgroundImage, 0, 0, null);
			}
			g.drawImage(backgroundImage, 0, 0, null);

			if(currentMenuType == MenuType.Controls || currentMenuType == MenuType.About) {
				AffineTransform transform = new AffineTransform();
				transform.setToIdentity();
				transform.translate(220, 220);
				transform.rotate(imageAngle);
				transform.translate(-6.0, -6.0);

				g.drawImage(corkImage, 40, 40, null);
				
				AffineTransform transform2 = new AffineTransform();
				transform2.setToIdentity();
				transform2.translate(host.getWidth() - 40, 40);
				transform2.rotate(imageAngle);
				transform2.translate(-16.0, -16.0);

				g.drawImage(corkImage, host.getWidth() - 240, 40, null);
			}

			
			for(Beer beer : beers) {	
				g.drawImage(beerImage, beer.posX, beer.posY, null);
				
			}
			
			for(Bubble bubble : bubbles) {
				g.setColor(Color.WHITE);
				g.drawOval(bubble.posX, bubble.posY, bubble.width, bubble.height);
				g.drawOval(bubble.posX + 5, bubble.posY + 5, bubble.width - 20, bubble.height - 20);
				g.fillOval(bubble.posX + 5, bubble.posY + 5, bubble.width - 20, bubble.height - 20);
			}
			
			if(currentMenuType == MenuType.Default)
				renderGameMenu(g, sw, sh, currentMenuButton);
			if(currentMenuType == MenuType.About)
				renderAbout(g, sw, sh);
			if(currentMenuType == MenuType.Controls)
				renderControls(g, sw, sh);
			
		}

		@Override
		public void update() {
			for(Beer beer: beers) {
				
				if(beer.posY >= host.getHeight()) {
					beer.posY = -300;
					beer.posX = random.nextInt(host.getWidth() - 150);
				}
				else {
					beer.posY += beer.speed;
				}
				
			}
			
			for(Bubble bubble: bubbles) {
				
				if(bubble.life <= 0) {
					bubble.posX = random.nextInt(host.getWidth()) - 30;
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

//			imageAngle += imageRot;
			
		}

		@Override
		public void handleMouseDown(int x, int y, GFMouseButton button) { }

		@Override
		public void handleMouseUp(int x, int y, GFMouseButton button) { 
			int startX = host.getWidth() / 4;
			int startY = host.getHeight() / 2 - 120;
			int offsetX = startX + 25;
			int offsetY = startY + 50;
			
			if(button == GFMouseButton.Left)
			{
				if(currentMenuType == MenuType.Default) {
					if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //START
						Main.filter.loadImage();
						host.setState("maingamestate");
						//Main.filter.setImageLeft(Model.getGlobalImage());
					}
					offsetY += 50;
					if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //UPLOAD
						globalImage = Util.browseForImage("", host.getWindow());
						Model.setGlobalImage(globalImage);
					}
					offsetY += 50;
					if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //CONTROLS
						currentMenuType = MenuType.Controls;
					}
					offsetY += 50;
					if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //ABOUT
						currentMenuType = MenuType.About;
					}
					offsetY += 50;
					if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) { //EXIT
						System.exit(0);
					}
				}
			}
		}

		@Override
		public void handleMouseMove(int x, int y) { 
			
			int startX = host.getWidth() / 4;
			int startY = host.getHeight() / 2 - 120;
			int offsetX = startX + 25;
			int offsetY = startY + 50;
			int bool = 0;
			
			Color stringColor = Color.RED;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.Start;
				bool = 1;
			}
			offsetY += 50;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.Upload;
				bool = 1;
			}
			offsetY += 50;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.Controls;
				bool = 1;
			}
			offsetY += 50;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
				this.stringColor = stringColor;
				currentMenuButton = MenuButton.About;
				bool = 1;
			}
			offsetY += 50;
			if(x >= offsetX - 5 && x <= offsetX - 5 + 250 && y >= offsetY - 24 && y <= offsetY - 24 + 40) {
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
		}

		@Override
		public String getName() {
			return "mainmenustate";
		}
}
