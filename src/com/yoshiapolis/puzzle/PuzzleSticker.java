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

package com.yoshiapolis.puzzle;

import com.jogamp.opengl.GL;
import com.yoshiapolis.cube.pieces.Mathf;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;

public class PuzzleSticker {

	protected float size;
	protected int cubeSize;
	protected int color;
	
	protected PMatrix3D lastRotationMat;
	protected PMatrix3D rotationMat;
	protected PMatrix3D translationMat;
	protected StickerPlacement placement;
	
	public PuzzleSticker() {}
	
	public PuzzleSticker(StickerPlacement placement, int cubeSize, float size, PMatrix3D translationMat) {
		this.size = size;
		this.cubeSize = cubeSize;
		this.translationMat = translationMat;
		this.placement = placement;
		this.lastRotationMat = new PMatrix3D();
		this.rotationMat = new PMatrix3D();
	}
	
	protected void drawStickerShape(PApplet app) {
		app.rotateX(Mathf.PI/2);
		app.rect(-size/2, -size/2, size, size);
		app.rotateX(-Mathf.PI/2);
	}
	
	public PuzzleSticker cloneTranslation() {
		return new PuzzleSticker(placement, cubeSize, size, translationMat.get());
	}
	
	public void setColor(int c) {
		this.color = c;
	}
	
	public void applyRotationMatrix(PMatrix3D rotation) {
		//rotation.apply(this.lastRotationMat);
		this.lastRotationMat.apply(rotation);
	}
	
	public void show(PApplet app) {
		app.pushMatrix();
		app.fill(color);
	    app.applyMatrix(rotationMat);
	    app.applyMatrix(lastRotationMat);
	    app.applyMatrix(translationMat);
	    drawStickerShape(app);
	    app.popMatrix();
	}
	
	private PVector getLayerVec(Face pivot) {
		PVector layerVec = new PVector();
		PMatrix3D invRotation = pivot.getRotationMat();
		invRotation.invert();
		invRotation.mult(getPos(), layerVec);
		
		return layerVec;
	}
	
	private PVector getPos() {
		PVector pos = new PVector();	
		PMatrix3D transformations = new PMatrix3D();
		transformations.apply(lastRotationMat);
		transformations.apply(translationMat);
		transformations.mult(pos, pos);
		
		return pos;
	}
	
	private boolean affectedByRotation(Face pivot, int layer) {
		if(!pivot.inverted()) layer = cubeSize - layer - 1;
		float start = placement.getMinY(cubeSize, size*cubeSize);
		float layerSize = placement.getYStep(cubeSize, size*cubeSize);
		float layerPos = start + layer*layerSize;
		PVector layerVec = getLayerVec(pivot);
		return (Mathf.abs(layerVec.y - layerPos) <= layerSize/2 + 0.001f);
	}
	
	public void calculatePosition(Move move, float rotation) {	
		for(int layer = move.getStartLayer(); layer <= move.getEndLayer(); layer ++) {
			Face pivot = move.getFace();
			if(affectedByRotation(pivot, layer)) {
				rotationMat = pivot.getMoveMat(rotation);
			}
		}
	}

	public void applyRotation() {
		PMatrix3D newMat = rotationMat.get();
		newMat.apply(lastRotationMat);
		lastRotationMat = newMat;
		rotationMat = new PMatrix3D();
	}
}
