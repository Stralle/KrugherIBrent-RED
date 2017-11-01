package game_states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
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
		
		private Beer[] beers = new Beer[BEER_MAX];
		
		private Image beerImage;
		Random random = new Random();
		
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

		@Override
		public void render(Graphics2D g, int sw, int sh) {	
			
			for(Beer beer : beers) {	
				g.drawImage(beerImage, beer.posX, beer.posY, null);
			}
			
			g.setColor(Color.GREEN);
			g.drawRect(host.getWidth()/2-100, host.getHeight()/2-150, 200, 300);
			g.setColor(Color.CYAN);
			g.drawString("START GAME", host.getWidth()/2-70, host.getHeight()/2-120);

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
			if(button == GFMouseButton.Left)
			{
				if(x >= host.getWidth()/2-100 && x <= host.getWidth()/2+100 && y >= host.getHeight()/2-150 && y <= host.getHeight()/2+150) {
					host.setState("maingamestate");
				}
				// Na lijevi klik radimo trenutno prebacivanje na drugi GameState
//				host.setState("second");
			}
			else
			{
				// Na desni (ili bilo koji drugi) klik, nasumicno biramo jednu od
				// implementiranih tranzicija iz TransitionType enuma
//				TransitionType transType = TransitionType.values()[(int)(Math.random() * TransitionType.values().length)];
//				
				// Pozivamo staticki metod transitionTo, koji ce prvo da nas prebaci
				// u stanje tranzicije, koje ce da traje 0.5 sekundi, a zatim
				// u "second" stanje igre
//				Transition.transitionTo("second", transType, 0.5f);
			}
		}

		@Override
		public void handleMouseMove(int x, int y) { }

		@Override
		public void handleKeyDown(int keyCode) { }

		@Override
		public void handleKeyUp(int keyCode) { }

		@Override
		public String getName() {
			return "mainmenustate";
		}
}
