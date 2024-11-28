/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.puzzle.display;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.lib.Color;

//An rgb color palette
public class ColorPalette {
	
	private Map<Color, Integer> colorIDs;
	private List<Vector3f> colors;
	
	public ColorPalette() {
		colorIDs = new EnumMap<Color, Integer>(Color.class);
		colors = new ArrayList<Vector3f>();
	}
	
	//add/overwrite a color to the palette with its default rgb value
	public void putColor(Color color) {
		putColor(color, color.getRGB());
	}
	
	//add/overwrite a color to the palette with a custom rgb value
	public void putColor(Color color, Vector3f rgb) {
		if(!colorIDs.containsKey(color)) {
			colors.add(rgb);
		} else {
			colors.remove((int) colorIDs.get(color));
		}
		colorIDs.put(color, colors.size() - 1);
	}
	
	public List<Vector3f> getColors() {
		return this.colors;
	}
	
	//returns the index of "color" in the "colors" list
	public int getColorID(Color color) {
		return colorIDs.get(color);
	}
}
