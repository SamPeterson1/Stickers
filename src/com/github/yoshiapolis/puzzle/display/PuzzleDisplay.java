/*
    PrimePuzzle Twisty Puzzle Simulator and Solver
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

package com.github.yoshiapolis.puzzle.display;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PVector;

public class PuzzleDisplay {
	
	private PApplet app;
	private float currentRotation = 0;
	private Move animatingMove;
	private long lastTime;
	private float animationSpeed = Mathf.PI;
	private boolean animate = true;
	private int direction = 0;
	private int cubeSize;
	private float drawSize;
	private float turnRotation;

	private StickerPlacement placement;
	protected List<Sticker> stickers;

	public PuzzleDisplay(PApplet app, StickerPlacement placement, int cubeSize, float drawSize) {
		this.stickers = new ArrayList<Sticker>();
		this.app = app;
		this.cubeSize = cubeSize;
		this.drawSize = drawSize;
		this.turnRotation = placement.getRotationAmt();
		this.lastTime = System.currentTimeMillis();
		this.placement = placement;
		Face[] faces = placement.getFaces();
		
		List<Sticker> faceStickers = placement.createStickerFace(cubeSize, drawSize);
		for(Sticker sticker : faceStickers) {
			for(Face f : faces) {
				PMatrix3D rot = f.getRotationMat();
				PVector colorVec = f.getColor().getRGB();
				int color = app.color(colorVec.x, colorVec.y, colorVec.z);
				
				Sticker clone = sticker.cloneTranslation();
				clone.applyRotationMatrix(rot);
				clone.setColor(color);
				
				stickers.add(clone);
			}
		}
		
	}

	public final boolean isAnimating() {
		return (animatingMove != null);
	}

	public final void setAnimationSpeed(float speed) {
		this.animationSpeed = speed;
	}

	public final void setAnimate(boolean animate) {
		this.animate = animate;
	}

	public final void makeMove(Move move) {
		this.animatingMove = move;
		direction = animatingMove.isCW() ? 1 : -1;
		if(!animate) {
			finishAnimation();
		}
	}

	public final void show() {
		float deltaTime = (System.currentTimeMillis() - lastTime)/1000.0f;
		lastTime = System.currentTimeMillis();

		if(animate && animatingMove != null) {
			currentRotation += deltaTime * animationSpeed * direction;
			for(Sticker sticker : stickers) {
				sticker.calculatePosition(animatingMove, currentRotation);
			}

			drawLayerBlockers();
			
			if(Mathf.abs(currentRotation) >= turnRotation) {
				finishAnimation();  
			}
		}
		
		for(Sticker sticker : stickers) {
			sticker.show(app);
		}
	}
	
	private void drawLayerBlocker(Face face, int layer, float yOff) {
		app.pushMatrix();
		app.applyMatrix(face.getMoveMat(currentRotation));
		app.applyMatrix(face.getRotationMat());
		app.translate(0, yOff, 0);
		app.rotateX(Mathf.PI/2);
		placement.drawLayerBlocker(app, layer, cubeSize, drawSize);
		app.popMatrix();
		
		app.pushMatrix();
		app.applyMatrix(face.getRotationMat());
		app.translate(0, yOff, 0);
		app.rotateX(Mathf.PI/2);
		placement.drawLayerBlocker(app, layer, cubeSize, drawSize);
		app.popMatrix();
	}
	
	private void drawLayerBlockers() {
		Face face = animatingMove.getFace();
		int layer = animatingMove.getLayer();
		int nextLayer = layer + 1;
		
		app.pushMatrix();
		app.fill(0);
		
		if(!face.inverted()) {
			layer = cubeSize - layer - 1;
			nextLayer = cubeSize - nextLayer - 1;
		}
		
		float yStep = placement.getYStep(cubeSize, drawSize);
		float yOff = placement.getMinY(cubeSize, drawSize) + layer * yStep;
	
		drawLayerBlocker(face, nextLayer, yOff);
		drawLayerBlocker(face, layer, yOff + yStep);
		
		app.popMatrix();
	}

	public final void finishAnimation() {
		currentRotation = direction * turnRotation;
		for(Sticker sticker : stickers) {
			sticker.calculatePosition(animatingMove, currentRotation);
			sticker.applyRotation();
		}
		currentRotation = 0;
		animatingMove = null;
	}
}
