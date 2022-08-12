package com.github.sampeterson1.puzzle.display;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.lib.Color;

public class ColorPalette {
	
	private Map<Color, Integer> colorIDs;
	private List<Vector3f> colors;
	
	public ColorPalette() {
		colorIDs = new EnumMap<Color, Integer>(Color.class);
		colors = new ArrayList<Vector3f>();
	}
	
	public void addColor(Color color) {
		Vector3f rgbColor = Colors.convertColor(color);
		colors.add(rgbColor);
		colorIDs.put(color, colors.size() - 1);
	}
	
	public List<Vector3f> getColors() {
		return this.colors;
	}
	
	public int getColorID(Color color) {
		return colorIDs.get(color);
	}
}
