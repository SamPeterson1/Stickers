package com.yoshiapolis.puzzle;

import java.util.List;

import processing.core.PApplet;

public interface StickerPlacement {
	
	public float getRotationAmt();
	
	public float getMinY(int cubeSize, float drawSize);
	
	public float getYStep(int cubeSize, float drawSize);
	
	public List<PuzzleSticker> createStickerFace(int cubeSize, float drawSize);
	
	public Face[] getFaces();
	
	public void drawLayerBlocker(PApplet app, int layer, int cubeSize, float drawSize);

}
