package com.github.yoshiapolis.puzzle.display;

import com.github.yoshiapolis.main.Main;
import com.github.yoshiapolis.puzzle.lib.Piece;

import processing.core.PMatrix3D;

public abstract class DisplayPiece {
	
	private PMatrix3D translationMat;
	private PMatrix3D rotationMat;
	
	protected abstract void drawShape();
	
	public abstract void setPosition(Piece position, float drawSize);
	
	public DisplayPiece(Piece position, float drawSize) {
		setPosition(position, drawSize);
	}
	
	public void show() {
		Main.app.pushMatrix();
		
		if(rotationMat != null)
			Main.app.applyMatrix(rotationMat);
		Main.app.applyMatrix(translationMat);
		
		drawShape();
		
		Main.app.popMatrix();
	}

	public void setRotationMat(PMatrix3D rotation) {
		this.rotationMat = rotation;
	}
	
	protected void setTranslationMat(PMatrix3D translation) {
		this.translationMat = translation;
	}
}
