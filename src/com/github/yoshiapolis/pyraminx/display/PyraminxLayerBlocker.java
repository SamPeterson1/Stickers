package com.github.yoshiapolis.pyraminx.display;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.display.LayerBlocker;
import com.github.yoshiapolis.puzzle.display.PuzzleDisplay;

import processing.core.PApplet;

public class PyraminxLayerBlocker extends LayerBlocker {
	
	private float getYOff(int layer, int puzzleSize, float drawSize) {
		float minY = -drawSize * (Mathf.sqrt(6) / 4);
		float yStep = drawSize / puzzleSize * Mathf.sqrt(2.0f / 3);
		
		return minY + layer * yStep;
	}
	
	@Override
	public void drawAtLayer(PuzzleDisplay display, int layer) {
		int puzzleSize = display.getPuzzleSize();
		float drawSize = display.getDrawSize();
		PApplet app = display.getApp();
		
		if (layer != 0 && layer != puzzleSize) {
			float yOff = getYOff(layer, puzzleSize, drawSize);			
			float size = drawSize/puzzleSize * layer;
			
			float a = size/2;
			float b = size*Mathf.sqrt(3)/6;
			float c = 2*b;
			
			app.pushMatrix();
			
			app.translate(0, yOff, 0);
			app.rotateX(Mathf.PI/2);
			
			app.beginShape();
			app.vertex(a, b);
			app.vertex(-a, b);
			app.vertex(0, -c);
			app.endShape();
			
			app.popMatrix();
		}
	}
	
}
