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

package com.yoshiapolis.pyraminx;

import java.util.ArrayList;
import java.util.List;

import com.yoshiapolis.cube.pieces.Mathf;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PuzzleSticker;
import com.yoshiapolis.puzzle.StickerPlacement;

import processing.core.PApplet;
import processing.core.PMatrix3D;

public class PyraminxStickerPlacement implements StickerPlacement {

	@Override
	public List<PuzzleSticker> createStickerFace(int cubeSize, float drawSize) {
		List<PuzzleSticker> stickers = new ArrayList<PuzzleSticker>();

		float size = drawSize/cubeSize;
		float xStep = size/2;
		float yOff = size*Mathf.sqrt(3)/12;
		float yStep = size*Mathf.sqrt(3)/2;
		int layer = 0;

		float layersTop = cubeSize-cubeSize/3-1;
		if(cubeSize % 3 == 0) {
			layersTop += 0.5;
		}
		float layersBottom = cubeSize-layersTop-1;
		float yStart = -yStep*layersTop;
		float yEnd = yStep*layersBottom + 0.01f;

		if(cubeSize % 3 == 1) {
			yStart -= yOff;
			yEnd -= yOff;
		} else if(cubeSize % 3 == 2) {
			yStart += yOff;
			yEnd += yOff;
		}
		
		float zOff = Mathf.sqrt(6)*drawSize/12;

		for(float y = yStart; y <= yEnd; y += yStep) {
			for(int i = -layer; i <= layer; i ++) {
				float xPos = xStep*i;
				float yPos = y + (((i + layer) % 2 == 0) ? yOff : -yOff);
				PMatrix3D trans = new PMatrix3D();
				trans.translate(xPos, zOff, yPos);
				boolean flipped = ((i+layer) % 2 == 0);
				stickers.add(new PyraminxSticker(this, flipped, cubeSize, drawSize/cubeSize, trans));
			}
			layer ++;
		}

		return stickers;
	}

	@Override
	public float getMinY(int cubeSize, float drawSize) {
		return -drawSize * (Mathf.sqrt(6)/4) + getYStep(cubeSize, drawSize)/2;
	}

	@Override
	public float getYStep(int cubeSize, float drawSize) {
		float size = drawSize/cubeSize;
		return size*Mathf.sqrt(2/3.0f);
	}

	@Override
	public float getRotationAmt() {
		return 2*Mathf.PI/3;
	}
	
	@Override
	public Face[] getFaces() {
		return Pyraminx.faces;
	}

	@Override
	public void drawLayerBlocker(PApplet app, int layer, int cubeSize, float drawSize) {
		if(layer != -1 && layer != cubeSize - 1) {
			float size = drawSize/cubeSize * (layer + 1);
			
			float a = size/2;
			float b = size*Mathf.sqrt(3)/6;
			float c = 2*b;
			
			app.beginShape();
			app.vertex(a, b);
			app.vertex(-a, b);
			app.vertex(0, -c);
			app.endShape();
		}
	}
}
