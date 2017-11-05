package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import game_states.FilterState.FilterType;

public class Model {

	public static String imagePath = "/photos/drunk.jpg" ;
	public static FilterType filterType = FilterType.NEGATIVE;
	public static ArrayList<FilterType> selectedFilters = new ArrayList();
	public static boolean isFiltered = false;
}
