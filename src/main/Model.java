package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game_states.FilterState.FilterType;
import rafgfxlib.Util;

public class Model {

	public static String imagePath = "/photos/drunk.jpg" ;
	private static BufferedImage globalImage = Util.loadImage(imagePath);
	public static FilterType filterType = FilterType.NEGATIVE;
	public static ArrayList<FilterType> selectedFilters = new ArrayList<FilterType>();
	public static boolean isFiltered = false;
	public static boolean isTransition = false;
	
	public static BufferedImage getGlobalImage() {
		return globalImage;
	}
	
	public static void setGlobalImage(BufferedImage globalImage) {
		Model.globalImage = globalImage;
	}
}
