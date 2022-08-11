package com.github.sampeterson1.puzzle.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.math.Vector3f;

public class ColorPalette {
	
	private Map<String, Integer> colorIndices = new HashMap<String, Integer>();
	private List<Vector3f> colors = new ArrayList<Vector3f>();
	
	public void addColor(String name, Vector3f value) {
		colors.add(value);
		colorIndices.put(name, colors.size() - 1);
	}
	
	public List<Vector3f> getColors() {
		return this.colors;
	}
	
	public int getColorIndex(String name) {
		return colorIndices.get(name);
	}
}
