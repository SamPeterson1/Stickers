package com.github.sampeterson1.puzzle.display;

public abstract class LayerBlocker {
	/*
	public abstract void drawAtLayer(PuzzleDisplay display, int layer);
	
	private void drawLayerBlocker(PuzzleDisplay display, int layer) {
		PApplet app = display.getApp();
		Face face = display.getAnimatingMove().getFace();
		float currentRotation = display.getCurrentRotation();
		
		app.pushMatrix();
		app.applyMatrix(face.getMoveMat(currentRotation));
		app.applyMatrix(face.getRotationMat());
		drawAtLayer(display, layer);
		app.popMatrix();
		
		app.pushMatrix();
		app.applyMatrix(face.getRotationMat());
		drawAtLayer(display, layer);
		app.popMatrix();
	}
	
	public void show(PuzzleDisplay display) {
		Move animatingMove = display.getAnimatingMove();
		PApplet app = display.getApp();
		int puzzleSize = display.getPuzzleSize();
		
		Face face = animatingMove.getFace();
		int layer = animatingMove.getLayer();
		int nextLayer = layer - 1;
		
		app.fill(0);
		
		if(!face.inverted()) {
			layer = puzzleSize - layer - 1;
			nextLayer = puzzleSize - nextLayer - 1;
		}

		drawLayerBlocker(display, layer);
		drawLayerBlocker(display, nextLayer);
		
	}
	*/
}
