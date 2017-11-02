package game_stage;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import rafgfxlib.GameHost;
import rafgfxlib.GameHost.GFMouseButton;
import rafgfxlib.GameState;
import rafgfxlib.Util;

public class Map extends GameState{
	
	private static final int MAP_WIDTH = 25;
	private static final int MAP_HEIGHT =  25;
	
	private static final int PLYR_WIDTH = 128;
	private static final int PLYR_HEIGHT = 128;
	
	private static final int IMG_WIDTH = 64;
	private static final int IMG_HEIGHT = 64;
	
	private static final int SPEC_FLD_NUM = 30;
	
	private static final String stateName = "maingamestate";
	
	private Field classicField = null;
	private Field specialField = null;
	private Player player = null;
	
	private int shiftX = 0;
	private int shiftY = 0;
	int x0, x1, y0, y1;
	int mdlX, mdlY;
	
	private GameHost host = null;
	
	private boolean map[][] = new boolean[MAP_HEIGHT][MAP_WIDTH];
	
	
	public Map(GameHost host) {
		super(host);
		System.out.println(host.getWidth()+" "+host.getHeight());
		this.host = host;
		initTerain();
		initPlayer();
	}
	
	public void initPlayer(){
		BufferedImage img = null;
		try{
			img = Util.loadImage("/photos/dwarf.png");
		}catch(Exception e){
			e.printStackTrace();
		}
		mdlX = host.getWidth()/2;
		mdlY = host.getHeight()/2;
		player = new Player(mdlX-PLYR_WIDTH/2, 
							mdlY-PLYR_HEIGHT/2, img);
	}
	
	public void initTerain(){
		BufferedImage img = null;
		try{
			img = Util.loadImage("/photos/grass.png");
		}catch(Exception e){
			e.printStackTrace();
		}
		classicField = new Field(IMG_WIDTH, IMG_HEIGHT, img, false);
		try{
			img = Util.loadImage("/photos/beer.png");
		}catch(Exception e){
			e.printStackTrace();
		}
		specialField = new Field(IMG_WIDTH, IMG_HEIGHT, img, true);
		Random r = new Random();
		for(int i = 0; i < SPEC_FLD_NUM; ){
			int x = r.nextInt(MAP_WIDTH);
			int y = r.nextInt(MAP_HEIGHT);
			if(!map[y][x]){
				map[y][x] = true;
				i++;
			}
		}
	}
	
	public void renderMap(Graphics2D g, int sw, int sh){
		 x0 = shiftX/IMG_WIDTH;
		 x1 = x0 + host.getWidth()/IMG_WIDTH+1;
		 y0 = shiftY/IMG_HEIGHT;
		 y1 = y0+host.getHeight()/IMG_HEIGHT+1;
		
		if(x0 < 0) x0 = 0;
		if(y0 < 0) y0 = 0;
		if(x1 < 0) x1 = 0;
		if(y1 < 0) y1 = 0;
		if(x0 >= MAP_WIDTH) x0 = MAP_WIDTH - 1;
		if(y0 >= MAP_HEIGHT) y0 = MAP_HEIGHT - 1;
		if(x1 >= MAP_WIDTH) x1 = MAP_WIDTH - 1;
		if(y1 >= MAP_HEIGHT) y1 = MAP_HEIGHT - 1;
		for(int y = y0; y<=y1; y++){
			
			for(int x = x0; x<=x1; x++){
				
				if(map[y][x]){
					g.drawImage(specialField.getImage(), x*IMG_WIDTH-shiftX, y*IMG_HEIGHT-shiftY, null);
				}else{
					g.drawImage(classicField.getImage(), x*IMG_WIDTH-shiftX, y*IMG_HEIGHT-shiftY, null);
				}
			}
		}
	}
	
	public void updateMap(){
		
	}

	@Override
	public boolean handleWindowClose() {
		return true;
	}

	@Override
	public String getName() {
		return stateName;
	}

	@Override
	public void resumeState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suspendState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g, int sw, int sh) {
		renderMap(g, sw, sh);
		player.renderPlayer(g, sw, sh);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseDown(int x, int y, GFMouseButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseUp(int x, int y, GFMouseButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseMove(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleKeyDown(int keyCode) {
		if(keyCode == KeyEvent.VK_LEFT) {
			shiftX -= 10;
			mdlX-=10;
			player.setInitialX(mdlX-PLYR_WIDTH/2);
		}
		if(keyCode == KeyEvent.VK_RIGHT) {
			shiftX += 10;
			mdlX+=10;
			player.setInitialX(mdlX-PLYR_WIDTH/2);
		}
		if(keyCode == KeyEvent.VK_UP) {
			shiftY -= 10;
			mdlY-=10;
			player.setInitialY(mdlY-PLYR_HEIGHT/2);
		}
		if(keyCode == KeyEvent.VK_DOWN) {
			shiftY += 10;
			mdlY+=10;
			player.setInitialY(mdlY-PLYR_HEIGHT/2);
		}
		
	}

	@Override
	public void handleKeyUp(int keyCode) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.VK_ESCAPE) {
			host.setState("mainmenustate");
		}
	}
	
}