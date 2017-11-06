package game_stage;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import game_states.FilterState.FilterType;
import main.Model;
import rafgfxlib.GameHost;
import rafgfxlib.GameHost.GFMouseButton;
import rafgfxlib.GameState;
import rafgfxlib.Util;

public class Map extends GameState{
	
	public static final int MAP_WIDTH = 25;
	public static final int MAP_HEIGHT =  25;
	
	public static final int PLYR_WIDTH = 64;
	public static final int PLYR_HEIGHT = 64;
	
	public static final int IMG_WIDTH = 64;
	public static final int IMG_HEIGHT = 64;
	
	public static final int TERAIN_FIELDS = 9;
	private static final int SPEC_FLD_NUM = 30;
	
	private static int PLYR_UP = 0;
	private static int PLYR_DOWN = 0;
	private static int PLYR_LEFT = 0;
	private static int PLYR_RIGHT = 0;
	
	int moveCnt = 2;
	
	private static final String stateName = "maingamestate";
	
	private Player player = null;
	
	private Field terainFields[] = new Field[9];
	
	private int shiftX = 0;
	private int shiftY = 0;
	private int x0, x1, y0, y1;
	private int mdlX, mdlY;
	private int plyrVelX = 0;
	private int plyrVelY = 0;
	private int pos = 0;
	private int off;
	
	private GameHost host = null;
	
	private int fields[][] = new int[MAP_HEIGHT][MAP_WIDTH];
	private boolean isSpecial[][] = new boolean[MAP_HEIGHT][MAP_WIDTH];
	private FilterType fieldsType[][] = new FilterType[MAP_HEIGHT][MAP_WIDTH];
	
	private static int SHIFTX_MIN;
	private static int SHIFTX_MAX;
	private static int SHIFTY_MIN;
	private static int SHIFTY_MAX;
	
	private BufferedImage playerUp[] = new BufferedImage[9];
	private BufferedImage playerLeft[] = new BufferedImage[9];
	private BufferedImage playerRight[] = new BufferedImage[9];
	private BufferedImage playerDown[] = new BufferedImage[9];
	private BufferedImage specialField = null;
	
	SpriteSheet plySS = null;
	
	public Map(GameHost host) {
		super(host);
		System.out.println(host.getWidth()+" "+host.getHeight());
		this.host = host;
		initVariables();
		initTerain();
		initSpecialFields();
		initPlayer();
	}
	
	public void initVariables(){
		SHIFTX_MIN = -host.getWidth()/2+PLYR_WIDTH/2;
		SHIFTX_MAX = IMG_WIDTH*MAP_WIDTH+SHIFTX_MIN-PLYR_WIDTH;
		SHIFTY_MIN = -host.getHeight()/2-PLYR_HEIGHT/2+10;
		SHIFTY_MAX = IMG_WIDTH*MAP_HEIGHT+SHIFTY_MIN-10;
		mdlX = host.getWidth()/2;
		mdlY = host.getHeight()/2;
	}
	public void initPlayer(){
		BufferedImage img = null;
		try{
			img = Util.loadImage("/photos/charss.png");
		}catch(Exception e){
			e.printStackTrace();
		}
		plySS = new SpriteSheet(img, 64);
		playerUp = plySS.getRow(0, 9);
		playerLeft = plySS.getRow(1, 9);
		playerDown = plySS.getRow(2, 9);
		playerRight = plySS.getRow(3, 9);
		player = new Player(mdlX-PLYR_WIDTH/2, 
							mdlY-PLYR_HEIGHT/2, playerDown[0]);
	}
	
	public void initTerain(){
		BufferedImage img = null;
		try{
			img = Util.loadImage("/photos/grass64p.png");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		terainFields[0] = new Field(IMG_WIDTH, IMG_HEIGHT, img, false);
		for(int y = 0; y<MAP_HEIGHT; y++){
			for(int x = 0; x<MAP_WIDTH; x++){
				fields[y][x] = 0;
				isSpecial[y][x] = false;
			}
		}
	}
	
	public void initSpecialFields(){
		try{
			specialField = Util.loadImage("/photos/redb.png");
		}catch(Exception e){
			e.printStackTrace();
		}
		off = specialField.getHeight()-IMG_HEIGHT;
		Random r = new Random();
		for(int i = 0; i < SPEC_FLD_NUM; ){
			int x = r.nextInt(MAP_WIDTH);
			int y = r.nextInt(MAP_HEIGHT);
			if(!isSpecial[y][x]){
				isSpecial[y][x] = true;
				i++;
				fieldsType[y][x] = FilterType.values()[r.nextInt(FilterType.values().length)];
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
				g.drawImage(terainFields[fields[y][x]].getImage(), x*IMG_WIDTH-shiftX, y*IMG_HEIGHT-shiftY, null);
				if(isSpecial[y][x]){
					g.drawImage(specialField, x*IMG_WIDTH-shiftX, 
								y*IMG_HEIGHT-shiftY-off, null);
				}
			}
		}
	}
	
	public void findIntersections(){
		Model.selectedFilters.clear();
		Rectangle plyrRect = new Rectangle(mdlX+shiftX-PLYR_WIDTH/2, mdlY+shiftY-PLYR_HEIGHT/2, PLYR_WIDTH, 
				PLYR_HEIGHT);
		Rectangle fldRect = null;
		System.out.println("--------");
		for(int y = 0; y<MAP_HEIGHT; y++){
			for(int x = 0; x<MAP_WIDTH; x++){
				fldRect = new Rectangle(x*IMG_WIDTH, y*IMG_WIDTH, IMG_WIDTH, IMG_HEIGHT);
				if(isSpecial[y][x] && plyrRect.getBounds().intersects(fldRect.getBounds())){
					Model.selectedFilters.add(fieldsType[y][x]);
				}
			}
		}
		for(FilterType ft:Model.selectedFilters){
			System.out.println(ft);
		}
	}
	
	public void updateMap(){
		
	}

	@Override
	public boolean handleWindowClose() {
		return false;
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
		//player.updatePlayer(plyrVelX, plyrVelY);
		
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
		moveCnt = (moveCnt+1)%3;
		if(keyCode == KeyEvent.VK_LEFT) {
			shiftX -= 10;
			if(shiftX<SHIFTX_MIN){
				shiftX = SHIFTX_MIN;
			}
			if(moveCnt==0){
				player.setImage(playerLeft[pos++]); 
			}
		}
		if(keyCode == KeyEvent.VK_RIGHT) {
			shiftX += 10;
			if(shiftX>SHIFTX_MAX){
				shiftX = SHIFTX_MAX;
			}
			
			if(moveCnt==0){
				player.setImage(playerRight[pos++]); 
			}
		}
		if(keyCode == KeyEvent.VK_UP) {
			shiftY -= 10;
			if(shiftY<SHIFTY_MIN){
				shiftY = SHIFTY_MIN;
			}
			
			if(moveCnt==0){
				player.setImage(playerUp[pos++]); 
			}
		}
		if(keyCode == KeyEvent.VK_DOWN) {
			shiftY += 10;
			if(shiftY>SHIFTY_MAX){
				shiftY = SHIFTY_MAX;
			}
			
			if(moveCnt==0){
				player.setImage(playerDown[pos++]); 
			}
		}
		
		pos %= 9;
	}

	@Override
	public void handleKeyUp(int keyCode) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.VK_ESCAPE) {
			host.setState("mainmenustate");
		}
		
		if(keyCode == KeyEvent.VK_SPACE){
			findIntersections();
			if(Model.selectedFilters.size()>0)
				Model.isFiltered = false;
				host.setState("filterstate");
		}
		
		if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT
			|| keyCode == KeyEvent.VK_RIGHT){
			pos = 0;
			moveCnt = 2;
			player.setImage(playerDown[0]);
		}
	}
	
}