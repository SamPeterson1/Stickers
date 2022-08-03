/*
    PrimePuzzle Twisty Puzzle Simulator and Solver
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

package com.github.yoshiapolis.pyraminx.display;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.display.Sticker;
import com.github.yoshiapolis.puzzle.display.FaceStickerPlacement;

import processing.core.PApplet;
import processing.core.PMatrix3D;

public class PyraminxSticker extends Sticker {

	private boolean flipped;
	
	public PyraminxSticker() {
		super();
	}
	
	public PyraminxSticker(FaceStickerPlacement placement, boolean flipped, int cubeSize, float size, PMatrix3D translationMat) {
		super(placement, cubeSize, size, translationMat);
		this.flipped = flipped;
	}
	
	public PyraminxSticker(FaceStickerPlacement placement, int cubeSize, float size, PMatrix3D translationMat) {
		super(placement, cubeSize, size, translationMat);
	}

	@Override
	public Sticker cloneTranslation() {
		return new PyraminxSticker(placement, flipped, cubeSize, size, translationMat.get()); 
	}
	
	@Override
	protected void drawStickerShape(PApplet app) {
		float a = size/2;
		float b = (flipped ? -1 : 1) * size*Mathf.sqrt(3)/6;
		float c = 2*b;
		
		app.beginShape();
		app.vertex(a, -b);
		app.vertex(-a, -b);
		app.vertex(0, c);
		app.endShape();
	}
	
}
