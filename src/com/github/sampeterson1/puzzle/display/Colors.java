package com.github.sampeterson1.puzzle.display;

import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.lib.Color;

public class Colors {
	
	public static final int MAX_COLORS = 32;
	
	private static int colorPtr = 0;
	private static Vector3f[] colors = new Vector3f[MAX_COLORS];
	
	public static final Vector3f RED = initColor(new Vector3f(1f, 0, 0));
	public static final Vector3f GREEN = initColor(new Vector3f(0, 1f, 0));
	public static final Vector3f BLUE = initColor(new Vector3f(0, 0, 1f));
	public static final Vector3f YELLOW = initColor(new Vector3f(1f, 1f, 0));
	public static final Vector3f ORANGE = initColor(new Vector3f(1f, 0.5f, 0));
	public static final Vector3f WHITE = initColor(new Vector3f(1f, 1f, 1f));
	public static final Vector3f PURPLE = initColor(new Vector3f(0.5f, 0, 1f));
	public static final Vector3f LIME_GREEN = initColor(new Vector3f(0.5f, 1f, 0.4f));
	public static final Vector3f PINK = initColor(new Vector3f(1f, 0, 1f));
	public static final Vector3f PALE_YELLOW = initColor(new Vector3f(1f, 1f, 0.5f));
	public static final Vector3f LIGHT_BLUE = initColor(new Vector3f(0, 0.75f, 1f));
	public static final Vector3f GRAY = initColor(new Vector3f(0.6f, 0.6f, 0.6f));
	
	private static final Map<Color, Vector3f> colorConversionMap = initColorConversionMap();
	
	private static Map<Color, Vector3f> initColorConversionMap() {
		Map<Color, Vector3f> colorConversionMap = new EnumMap<Color, Vector3f>(Color.class);
		
		colorConversionMap.put(Color.RED, RED);
		colorConversionMap.put(Color.GREEN, GREEN);
		colorConversionMap.put(Color.BLUE, BLUE);
		colorConversionMap.put(Color.YELLOW, YELLOW);
		colorConversionMap.put(Color.ORANGE, ORANGE);
		colorConversionMap.put(Color.WHITE, WHITE);
		colorConversionMap.put(Color.PURPLE, PURPLE);
		colorConversionMap.put(Color.LIME_GREEN, LIME_GREEN);
		colorConversionMap.put(Color.PINK, PINK);
		colorConversionMap.put(Color.PALE_YELLOW, PALE_YELLOW);
		colorConversionMap.put(Color.LIGHT_BLUE, LIGHT_BLUE);
		colorConversionMap.put(Color.GRAY, GRAY);
		
		return colorConversionMap;
	}
	
	private static Vector3f initColor(Vector3f color) {
		colors[colorPtr] = color;
		colorPtr ++;
		
		return color;
	}
	
	public static int addColor(Vector3f color) {
		colors[colorPtr] = color;
		colorPtr ++;
		
		return colorPtr --;
	}
	
	public static Vector3f getColor(int colorID) {
		return colors[colorID];
	}
	
	public static Vector3f convertColor(Color color) {
		return colorConversionMap.get(color);
	}
}
