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

package com.github.yoshiapolis.megaminx;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.display.LayerBlocker;
import com.github.yoshiapolis.puzzle.display.PuzzleDisplay;

import processing.core.PApplet;

public class MegaminxLayerBlocker extends LayerBlocker {
	
	private float getYOff(int layer, int puzzleSize, float drawSize) {
		float minY = -drawSize * Mathf.MEGAMINX_UNIT_APOTHEM;
		float layerLen = drawSize / (puzzleSize * 2) * Mathf.cos(Mathf.PI / 10);
		float yStep = layerLen * Mathf.sin(Mathf.MEGAMINX_DIHEDRAL_ANGLE);
		
		return minY + layer * yStep;
	}
	
	@Override
	public void drawAtLayer(PuzzleDisplay display, int layer) {
		int puzzleSize = display.getPuzzleSize();
		float drawSize = display.getDrawSize();
		
		PApplet app = display.getApp();
		
		if (layer != 0) {
			float theta = Mathf.PI / 2;
			float angOff = 2 * Mathf.PI / 5;
			float lineLen = drawSize + layer * drawSize / puzzleSize * Mathf.cos(2 * Mathf.PI / 5);
			float r = lineLen / (2 * Mathf.sin(Mathf.PI / 5));
			float yOff = getYOff(layer, puzzleSize, drawSize);
			
			app.pushMatrix();
			
			app.translate(0, yOff, 0);
			app.rotateX(Mathf.PI/2);
			
			app.beginShape();
			for (int i = 0; i < 5; i++) {
				app.vertex(r * Mathf.cos(theta), r * Mathf.sin(theta));
				theta += angOff;
			}
			app.endShape();
			
			app.popMatrix();
		}
	}

}
