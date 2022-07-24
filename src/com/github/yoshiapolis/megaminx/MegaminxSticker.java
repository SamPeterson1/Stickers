/*
    PrimePuzzle Twisty Puzzle Simulator
    Copyright (C) 2022 Sam Peterson
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.github.yoshiapolis.megaminx;

import com.github.yoshiapolis.cube.pieces.Mathf;
import com.github.yoshiapolis.puzzle.PuzzleSticker;
import com.github.yoshiapolis.puzzle.StickerPlacement;

import processing.core.PApplet;
import processing.core.PMatrix3D;

public class MegaminxSticker extends PuzzleSticker {

	private Line[] edges;
	private float[] points;
	
	public MegaminxSticker(StickerPlacement placement, int cubeSize, float size) {
		super(placement, cubeSize, size, new PMatrix3D());
	}
	
	public void copyFromPoints(float[] p) {
		this.points = new float[p.length];
		for(int i = 0; i < p.length; i ++) {
			this.points[i] = p[i];
		}
	}
	
	public void defineShape(Line[] lines) {
		float centerX = 0;
		float centerY = 0;
		this.edges = lines;
		this.points = new float[lines.length*2];
		for(int i = 0; i < lines.length; i ++) {
			float[] point = lines[i].intersect(lines[(i+1) % lines.length]);
			centerX += point[0];
			centerY += point[1];
			points[i*2] = point[0];
			points[i*2+1] = point[1];
		}
		
		centerX /= lines.length;
		centerY /= lines.length;
		
		for(int i = 0; i < lines.length; i ++) {
			points[i*2] -= centerX;
			points[i*2+1] -= centerY;
		}
		
		super.translationMat.translate(centerX, size*cubeSize*Mathf.MEGAMINX_UNIT_APOTHEM, centerY);
	}
	
	@Override
	public MegaminxSticker cloneTranslation() {
		MegaminxSticker sticker = new MegaminxSticker(placement, cubeSize, size);
		sticker.defineShape(edges);
		return sticker;
	}
	
	@Override
	public void drawStickerShape(PApplet app) {
		app.rotateX(Mathf.PI/2);
		app.beginShape();
		for(int i = 0; i < points.length; i += 2) {
			app.vertex(points[i], points[i+1]);
		}
		app.endShape();
		app.rotateX(-Mathf.PI/2);
	}
	
}