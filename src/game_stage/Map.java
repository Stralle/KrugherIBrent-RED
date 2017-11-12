package game_stage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

import game_states.FilterState.FilterType;
import main.Model;
import rafgfxlib.GameHost;
import rafgfxlib.GameHost.GFMouseButton;
import rafgfxlib.GameState;
import rafgfxlib.ImageViewer;
import rafgfxlib.Util;

public class Map extends GameState {

	public static final int MAP_WIDTH = 32;
	public static final int MAP_HEIGHT = 32;

	public static final int PLYR_WIDTH = 64;
	public static final int PLYR_HEIGHT = 64;

	public static final int IMG_WIDTH = 64;
	public static final int IMG_HEIGHT = 64;

	public static final int TERAIN_FIELDS = 9;
	private static final int SPEC_FLD_NUM = 30;

	int moveCnt = 2;

	private BufferedImage mapImage = null;
	private BufferedImage arrowImage = null;
	private static final String stateName = "maingamestate";

	private Player player = null;

	private Field terainFields[] = new Field[9];

	private int xPoint = 0, yPoint = 0;
	private int shiftX = 0;
	private int shiftY = 0;

	private int plyrShiftX = 0;
	private int plyrShiftY = 0;
	private int pos = 0;
	private int off;
	private int cnt = 0;

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
		System.out.println(host.getWidth() + " " + host.getHeight());
		this.host = host;
		arrowImage = Util.loadImage("/photos/arrow.png");
		initVariables();
		initTerain();
		initSpecialFields();
		initPlayer();
		generateMap();
	}

	public void initVariables() {
		SHIFTX_MIN = 0;
		SHIFTX_MAX = IMG_WIDTH * MAP_WIDTH - host.getWidth();
		SHIFTY_MIN = 0;
		SHIFTY_MAX = IMG_WIDTH * MAP_HEIGHT - host.getHeight();
	}

	public void initPlayer() {
		BufferedImage img = null;
		try {
			img = Util.loadImage("/photos/charss.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		plySS = new SpriteSheet(img, 64);
		playerUp = plySS.getRow(0, 9);
		playerLeft = plySS.getRow(1, 9);
		playerDown = plySS.getRow(2, 9);
		playerRight = plySS.getRow(3, 9);
		player = new Player(0, 0, playerDown[0]);
	}

	public void initTerain() {
		BufferedImage img = null;
		try {
			img = Util.loadImage("/photos/grass64p.png");
		} catch (Exception e) {
			e.printStackTrace();
		}

		terainFields[0] = new Field(IMG_WIDTH, IMG_HEIGHT, img, false);
		for (int y = 0; y < MAP_HEIGHT; y++) {
			for (int x = 0; x < MAP_WIDTH; x++) {
				fields[y][x] = 0;
				isSpecial[y][x] = false;
			}
		}
	}

	public void initSpecialFields() {
		try {
			specialField = Util.loadImage("/photos/redb.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		off = specialField.getHeight() - IMG_HEIGHT;
		Random r = new Random();
		for (int i = 0; i < SPEC_FLD_NUM;) {
			int x = r.nextInt(MAP_WIDTH);
			int y = r.nextInt(MAP_HEIGHT);
			if (!isSpecial[y][x]) {
				isSpecial[y][x] = true;
				i++;
				fieldsType[y][x] = FilterType.values()[r.nextInt(FilterType.values().length)];
			}
		}
	}

	/*
	 * Generating map with Perlin Noise function
	 */
	public void generateMap() {
		int octaves = 11;

		int octaveSize = 2;

		float persistence = 0.65f;

		int width = (int) Math.pow(octaveSize, octaves);
		int height = width;

		WritableRaster target = Util.createRaster(width, height, false);

		float[][] tempMap = new float[width][height];

		float[][] finalMap = new float[width][height];

		float multiplier = 1.0f;

		for (int o = 0; o < octaves; ++o) {
			float[][] octaveMap = new float[octaveSize][octaveSize];
			for (int x = 0; x < octaveSize; ++x) {
				for (int y = 0; y < octaveSize; ++y) {
					octaveMap[x][y] = ((float) Math.random() - 0.5f) * 2.0f;
				}
			}

			Util.floatMapRescale(octaveMap, tempMap);

			Util.floatMapMAD(tempMap, finalMap, multiplier);

			octaveSize *= 2;

			multiplier *= persistence;
		}

		BufferedImage imgGradient = Util.loadImage("/photos/gradient3.jpg");

		if (imgGradient == null) {
			System.out.println("Nema gradijenta!");
			return;
		}

		Util.mapFloatMapViaGradient(finalMap, -1.0f, 1.0f, Util.imageToGradient(imgGradient), target);

		// ImageViewer.showImageWindow(Util.rasterToImage(target), "RAF
		// Racunarska Grafika");
		mapImage = Util.rasterToImage(target);

	}

	public void renderMap(Graphics2D g, int sw, int sh) {
		g.drawImage(mapImage.getSubimage(xPoint + shiftX, yPoint + shiftY, host.getWidth(), host.getHeight()), 0, 0,
				null);
	}

	public void renderPlayer(Graphics2D g, int sw, int sh) {
		g.drawImage(player.getImage(), plyrShiftX - shiftX, plyrShiftY - shiftY, null);
	}

	public void renderArrows(Graphics2D g, int sw, int sh) {
		if (plyrShiftX > shiftX + host.getWidth()) {
			g.drawImage(arrowImage, host.getWidth() - arrowImage.getWidth(),
					host.getHeight() / 2 - arrowImage.getHeight() / 2, null);
		} else if (plyrShiftX + PLYR_WIDTH < shiftX) {
			g.drawImage(rotateImage(rotateImage(arrowImage)), 0, host.getHeight() / 2 - arrowImage.getHeight() / 2,
					null);
		} else if (plyrShiftY > shiftY + host.getHeight()) {
			g.drawImage(rotateImage(arrowImage), host.getWidth() / 2 - arrowImage.getWidth() / 2,
					host.getHeight() - arrowImage.getHeight(), null);
		} else if (plyrShiftY + PLYR_HEIGHT < shiftY) {
			g.drawImage(rotateImage(rotateImage(rotateImage(arrowImage))),
					host.getWidth() / 2 - arrowImage.getWidth() / 2, 0, null);
		}
	}

	public void renderSpecialFields(Graphics2D g, int sw, int sh) {
		for (int y = 0; y < MAP_HEIGHT; y++) {
			for (int x = 0; x < MAP_WIDTH; x++) {
				if (isSpecial[y][x]) {
					if (doIntersect(plyrShiftX, plyrShiftY, PLYR_WIDTH, PLYR_HEIGHT, x * IMG_WIDTH, y * IMG_HEIGHT,
							IMG_WIDTH, IMG_HEIGHT)) {
						g.setColor(Color.RED);
						g.drawRect(x * IMG_WIDTH - shiftX, y * IMG_HEIGHT - shiftY, IMG_WIDTH, IMG_HEIGHT);
					}
					g.drawImage(specialField, x * IMG_WIDTH - shiftX, y * IMG_HEIGHT - 30 - shiftY, null);
				}
			}
		}
	}

	public BufferedImage fadeScreen() {
		// WritableRaster source = (mapImage.getSubimage(xPoint + shiftX, yPoint
		// + shiftY, host.getWidth(), host.getHeight())).getRaster();
		WritableRaster source = (mapImage.getSubimage(xPoint + shiftX, yPoint + shiftY, host.getWidth(),
				host.getHeight())).getRaster();

		WritableRaster target = Util.createRaster(source.getWidth(), source.getHeight(), true);

		int rgb[] = new int[4];
		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				source.getPixel(x, y, rgb);
				rgb[3] = 255;
				target.setPixel(x, y, rgb);
			}
		}

		WritableRaster img = specialField.getRaster();
		for (int y = 0; y < MAP_HEIGHT; y++) {
			for (int x = 0; x < MAP_WIDTH; x++) {
				if (isSpecial[y][x]) {
					// g.drawImage(specialField, x*IMG_WIDTH-shiftX,
					// y*IMG_HEIGHT-30-shiftY, null);
					for (int i = 0; i < img.getHeight(); i++) {
						for (int j = 0; j < img.getWidth(); j++) {
							if (x * IMG_WIDTH - shiftX + j >= 0 && x * IMG_WIDTH - shiftX + j < host.getWidth()
									&& y * IMG_HEIGHT - shiftY - 30 + i >= 0
									&& y * IMG_HEIGHT - 30 - shiftY + i < host.getHeight()) {
								img.getPixel(j, i, rgb);
								if (rgb[3] != 0)
									target.setPixel(x * IMG_WIDTH - shiftX + j, y * IMG_HEIGHT - shiftY - 30 + i, rgb);
							}
						}
					}
				}
			}
		}

		img = playerDown[0].getRaster();
		for (int y = 0; y < PLYR_HEIGHT; y++) {
			for (int x = 0; x < PLYR_WIDTH; x++) {
				img.getPixel(x, y, rgb);
				if (plyrShiftX - shiftX + x >= 0 && plyrShiftX - shiftX + x < host.getWidth()
						&& plyrShiftY - shiftY + y >= 0 && plyrShiftY - shiftY + y < host.getHeight() && (rgb[3] > 0)) {
					target.setPixel(plyrShiftX - shiftX + x, plyrShiftY - shiftY + y, rgb);
				}
			}
		}
		int step = 5;
		for (int y = 0; y < target.getHeight(); y++) {
			for (int x = 0; x < target.getWidth(); x++) {
				target.getPixel(x, y, rgb);
				rgb[0] = clamp(rgb[0], cnt*step);
				rgb[1] = clamp(rgb[1], step*cnt);
				rgb[2] = clamp(rgb[2], step*cnt);
				target.setPixel(x, y, rgb);
			}
		}
		cnt++;

		return Util.rasterToImage(target);
	}

	public int clamp(int val, int step) {
		return val - step < 0 ? 0 : val - step;
	}

	public boolean doIntersect(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
		Rectangle r1 = new Rectangle(x1, y1, w1, h1);
		Rectangle r2 = new Rectangle(x2, y2, w2, h2);
		return r1.getBounds().intersects(r2.getBounds());
	}

	public void findIntersections() {
		Model.selectedFilters.clear();
		Rectangle plyrRect = new Rectangle(plyrShiftX, plyrShiftY, PLYR_WIDTH, PLYR_HEIGHT);
		Rectangle fldRect = null;
		System.out.println("--------");
		for (int y = 0; y < MAP_HEIGHT; y++) {
			for (int x = 0; x < MAP_WIDTH; x++) {
				fldRect = new Rectangle(x * IMG_WIDTH, y * IMG_WIDTH, IMG_WIDTH, IMG_HEIGHT);
				if (isSpecial[y][x] && plyrRect.getBounds().intersects(fldRect.getBounds())) {
					Model.selectedFilters.add(fieldsType[y][x]);
				}
			}
		}
		for (FilterType ft : Model.selectedFilters) {
			System.out.println(ft);
		}
	}

	public BufferedImage rotateImage(BufferedImage img) {
		if (img == null)
			return null;

		WritableRaster source = img.getRaster();
		WritableRaster target = Util.createRaster(source.getWidth(), source.getHeight(), true);

		int rgb[] = new int[4];

		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				source.getPixel(x, y, rgb);

				target.setPixel(y, x, rgb);
			}
		}

		int temp[] = new int[4];
		int temp2[] = new int[4];
		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth() / 2; x++) {
				target.getPixel(source.getWidth() - 1 - x, y, temp);
				target.getPixel(x, y, temp2);
				target.setPixel(x, y, temp);
				target.setPixel(source.getWidth() - x - 1, y, temp2);
			}
		}
		// ImageViewer.showImageWindow(Util.rasterToImage(target), "RAF
		// Racunarska Grafika");

		return Util.rasterToImage(target);

	}

	public void updateMap() {

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
		if (!Model.isTransition) {
			renderMap(g, sw, sh);
			renderSpecialFields(g, sw, sh);
			renderArrows(g, sw, sh);
			renderPlayer(g, sw, sh);
		} else {
			g.drawImage(fadeScreen(), 0, 0, null);
			if(cnt>55){
				cnt = 0;
				Model.isTransition = false;
				host.setState("filterstate");
			}
		}
	}

	@Override
	public void update() {
		// player.updatePlayer(plyrVelX, plyrVelY);

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

		if (keyCode == KeyEvent.VK_A) {
			moveCnt = (moveCnt + 1) % 3;
			plyrShiftX -= 6;
			if (moveCnt == 0) {
				player.setImage(playerLeft[pos++]);
			}
		}

		if (keyCode == KeyEvent.VK_D) {
			moveCnt = (moveCnt + 1) % 3;
			plyrShiftX += 6;
			if (moveCnt == 0) {
				player.setImage(playerRight[pos++]);
			}
		}

		if (keyCode == KeyEvent.VK_W) {
			moveCnt = (moveCnt + 1) % 3;
			plyrShiftY -= 6;
			if (moveCnt == 0) {
				player.setImage(playerUp[pos++]);
			}
		}

		if (keyCode == KeyEvent.VK_S) {
			moveCnt = (moveCnt + 1) % 3;
			plyrShiftY += 6;
			if (moveCnt == 0) {
				player.setImage(playerDown[pos++]);
			}
		}
		if (keyCode == KeyEvent.VK_LEFT) {
			shiftX -= 10;
			if (shiftX < SHIFTX_MIN) {
				shiftX = SHIFTX_MIN;
			}
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			shiftX += 10;
			if (shiftX > SHIFTX_MAX) {
				shiftX = SHIFTX_MAX;
			}
		}
		if (keyCode == KeyEvent.VK_UP) {
			shiftY -= 10;
			if (shiftY < SHIFTY_MIN) {
				shiftY = SHIFTY_MIN;
			}
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			shiftY += 10;
			if (shiftY > SHIFTY_MAX) {
				shiftY = SHIFTY_MAX;
			}
		}

		pos %= 9;
	}

	@Override
	public void handleKeyUp(int keyCode) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.VK_ESCAPE) {
			host.setState("mainmenustate");
		}

		if (keyCode == KeyEvent.VK_SPACE) {
			findIntersections();
			if (Model.selectedFilters.size() > 0) {
				Model.isFiltered = false;
				Model.isTransition = true;
				// host.setState("filterstate");
			}
		}

		if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_A
				|| keyCode == KeyEvent.VK_D) {
			// pos = 0;
			moveCnt = 2;
			// player.setImage(playerDown[0]);
		}
	}

}