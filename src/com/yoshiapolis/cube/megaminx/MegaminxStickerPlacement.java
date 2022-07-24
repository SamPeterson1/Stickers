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

package com.yoshiapolis.cube.megaminx;

import java.util.ArrayList;
import java.util.List;

import com.yoshiapolis.cube.pieces.Mathf;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PuzzleSticker;
import com.yoshiapolis.puzzle.StickerPlacement;

import processing.core.PApplet;

public class MegaminxStickerPlacement implements StickerPlacement {

	@Override
	public float getMinY(int cubeSize, float drawSize) {
		float yStep = getYStep(cubeSize, drawSize);
		return -drawSize*Mathf.MEGAMINX_UNIT_APOTHEM + yStep/2;
	}

	@Override
	public float getYStep(int cubeSize, float drawSize) {
		float layerLen = drawSize/(cubeSize*2) * Mathf.cos(Mathf.PI/10);
		return layerLen * Mathf.sin(Mathf.MEGAMINX_DIHEDRAL_ANGLE);
	}
	
	float[][] partitionPoints(int cubeSize, float drawSize) {
		float ang = 2 * Mathf.PI / 5;
		float r = drawSize/(2*Mathf.sin(Mathf.PI/5));

		float[][] points = new float[5][4 * (cubeSize + 1)];
		for (int i = 0; i < 5; i++) {
			float theta = ang * i - Mathf.PI / 2;
			float startX = r * Mathf.cos(theta);
			float endX = r * Mathf.cos(theta + ang);
			float startY = r * Mathf.sin(theta);
			float endY = r * Mathf.sin(theta + ang);

			float divSizeX = (endX - startX) / (2 * cubeSize);
			float divSizeY = (endY - startY) / (2 * cubeSize);

			for (int j = 0; j <= 2 * cubeSize; j++) {
				points[i][2 * j] = startX + divSizeX * j;
				points[i][2 * j + 1] = startY + divSizeY * j;
			}
		}
		
		return points;
	}
	
	Line[][] createLines(int cubeSize, float drawSize) {
		float[][] points = partitionPoints(cubeSize, drawSize);
		Line[][] lines = new Line[5][(cubeSize + 1)];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < cubeSize + 1; j++) {
				int k = j;
				float x1 = points[(i + 2) % 5][2 * k];
				float y1 = points[(i + 2) % 5][2 * k + 1];
				k = 2 * cubeSize - k;
				float x2 = points[i][2 * k];
				float y2 = points[i][2 * k + 1];
				lines[i][j] = new Line(x1, y1, x2, y2);
			}
		}

		return lines;
	}
	
	@Override
	public List<PuzzleSticker> createStickerFace(int cubeSize, float drawSize) {
		Line[][] lines = createLines(cubeSize, drawSize);
		List<PuzzleSticker> stickers = new ArrayList<PuzzleSticker>();

		Line[] pentagonEdges = new Line[5];
		for (int side = 0; side < 5; side++) {
			pentagonEdges[side] = lines[side][cubeSize];
			for (int x = 0; x < cubeSize; x++) {
				Line[] trapezoidEdges = new Line[4];
				trapezoidEdges[0] = lines[side][x];
				trapezoidEdges[1] = lines[(side + 4) % 5][cubeSize];
				trapezoidEdges[2] = lines[side][x+1]; 
				trapezoidEdges[3] = lines[(side + 1) % 5][cubeSize];
				
				MegaminxSticker trapezoid = new MegaminxSticker(this, cubeSize, drawSize/cubeSize);
				trapezoid.defineShape(trapezoidEdges);
				stickers.add(trapezoid);
				
				for (int y = 0; y < cubeSize; y++) {
					Line[] diamondEdges = new Line[4];
					diamondEdges[0] = lines[side][x];
					diamondEdges[1] = lines[(side + 1) % 5][y];
					diamondEdges[2] = lines[side][x + 1];
					diamondEdges[3] = lines[(side + 1) % 5][y + 1];
					
					MegaminxSticker diamond = new MegaminxSticker(this, cubeSize, drawSize/cubeSize);
					diamond.defineShape(diamondEdges);
					stickers.add(diamond);
				}
			}
		}
		
		MegaminxSticker pentagon = new MegaminxSticker(this, cubeSize, drawSize/cubeSize);
		pentagon.defineShape(pentagonEdges);
		stickers.add(pentagon);
		
		return stickers;
	}

	@Override
	public float getRotationAmt() {
		return 2*Mathf.PI/5;
	}

	@Override
	public Face[] getFaces() {
		return Megaminx.faces;
	}

	@Override
	public void drawLayerBlocker(PApplet app, int layer, int cubeSize, float drawSize) {	
		if(layer != -1) {
			float theta = Mathf.PI/2;
			float angOff = getRotationAmt();
			float lineLen = drawSize + (layer+1) * drawSize/cubeSize * Mathf.cos(2*Mathf.PI/5);
			float r = lineLen/(2*Mathf.sin(Mathf.PI/5));
			
			app.beginShape();
			for(int i = 0; i < 5; i ++) {
				app.vertex(r*Mathf.cos(theta), r*Mathf.sin(theta));
				theta += angOff;
			}
			app.endShape();
		}
	}

}
