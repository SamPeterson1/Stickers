package com.github.yoshiapolis.cube.display;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.display.Sticker;
import com.github.yoshiapolis.puzzle.display.StickerPlacement;

import processing.core.PApplet;
import processing.core.PMatrix3D;

public class CubeSticker extends Sticker {

	public CubeSticker() {
		super();
	}

	public CubeSticker(StickerPlacement placement, int cubeSize, float size, PMatrix3D translationMat) {
		super(placement, cubeSize, size, translationMat);
	}

	@Override
	protected void drawStickerShape(PApplet app) {
		app.rotateX(Mathf.PI/2);
		app.rect(-size/2, -size/2, size, size);
		app.rotateX(-Mathf.PI/2);
	}
	
	@Override
	public Sticker cloneTranslation() {
		return new CubeSticker(placement, cubeSize, size, translationMat.get());
	}
	
}
