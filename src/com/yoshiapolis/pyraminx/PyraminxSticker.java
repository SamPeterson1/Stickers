package com.yoshiapolis.pyraminx;

import com.yoshiapolis.cube.pieces.Mathf;
import com.yoshiapolis.puzzle.PuzzleSticker;
import com.yoshiapolis.puzzle.StickerPlacement;

import processing.core.PApplet;
import processing.core.PMatrix3D;

public class PyraminxSticker extends PuzzleSticker {

	private boolean flipped;
	
	public PyraminxSticker() {
		super();
	}
	
	public PyraminxSticker(StickerPlacement placement, boolean flipped, int cubeSize, float size, PMatrix3D translationMat) {
		super(placement, cubeSize, size, translationMat);
		this.flipped = flipped;
	}
	
	public PyraminxSticker(StickerPlacement placement, int cubeSize, float size, PMatrix3D translationMat) {
		super(placement, cubeSize, size, translationMat);
	}

	@Override
	public PuzzleSticker cloneTranslation() {
		return new PyraminxSticker(placement, flipped, cubeSize, size, translationMat.get()); 
	}
	
	@Override
	protected void drawStickerShape(PApplet app) {
		app.rotateX(Mathf.PI/2);
		float a = size/2;
		float b = (flipped ? -1 : 1) * size*Mathf.sqrt(3)/6;
		float c = 2*b;
		app.beginShape();
		app.vertex(a, -b);
		app.vertex(-a, -b);
		app.vertex(0, c);
		app.endShape();
		app.rotateX(-Mathf.PI/2);
	}
	
}
