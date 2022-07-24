package com.yoshiapolis.cube.pieces;

import java.util.ArrayList;
import java.util.List;

import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PuzzleSticker;
import com.yoshiapolis.puzzle.StickerPlacement;

import processing.core.PApplet;
import processing.core.PMatrix3D;

public class CubeStickerPlacement implements StickerPlacement {

	public List<PuzzleSticker> createStickerFace(int cubeSize, float drawSize) {
		float stickerSize = drawSize/cubeSize;
		float start = -stickerSize * (cubeSize/2);
		if(cubeSize % 2 == 0) start += stickerSize/2;
		float end = -start + 0.01f;
		float y = stickerSize/2 - start;
		
		List<PuzzleSticker> stickers = new ArrayList<PuzzleSticker>();
		
		for(float x = start; x < end; x += stickerSize) {
			for(float z = start; z < end; z += stickerSize) {			
				PMatrix3D trans = new PMatrix3D();
				trans.translate(x, y, z);	
				stickers.add(new PuzzleSticker(this, cubeSize, drawSize/cubeSize, trans));
			}
		}
		
		return stickers;
	}

	@Override
	public float getMinY(int cubeSize, float drawSize) {
		float stickerSize = drawSize/cubeSize;
		return -stickerSize * (cubeSize/2.0f) + stickerSize/2;
	}

	@Override
	public float getYStep(int cubeSize, float drawSize) {
		return drawSize/cubeSize;
	}
	
	@Override
	public float getRotationAmt() {
		return Mathf.PI/2;
	}

	@Override
	public Face[] getFaces() {
		return Cube.faces;
	}

	@Override
	public void drawLayerBlocker(PApplet app, int layer, int cubeSize, float drawSize) {
		if(layer != -1 && layer != cubeSize - 1) {
			app.rect(-drawSize/2, -drawSize/2, drawSize, drawSize);
		}
	}

}
