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

package com.github.sampeterson1.puzzle.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.pyraminx.display.PyraminxDisplayPiece;
import com.github.sampeterson1.renderEngine.rendering.Scene;

public class PuzzleDisplay {
	
	private float currentRotation = 0;
	private Move animatingMove;
	private long lastTime;
	private float animationSpeed = Mathf.PI;
	private boolean animate = true;
	private int direction = 0;
	private int puzzleSize;
	private float drawSize;
	private float turnRotation;

	private Puzzle puzzle;

	private Map<Piece, DisplayPiece> pieceMap;
	private List<Piece> movedPieces;
	private List<DisplayPiece> allDisplayPieces;
	
	public PuzzleDisplay(Puzzle puzzle, float drawSize) {
		this.puzzle = puzzle;
		this.drawSize = drawSize;
		
		createPieces();
		
		this.lastTime = System.currentTimeMillis();
		this.turnRotation = 2 * Mathf.PI / 3;
	}
	
	private void createPieces() {
		this.pieceMap = new HashMap<Piece, DisplayPiece>();
		this.allDisplayPieces = new ArrayList<DisplayPiece>();
		
		for(Piece piece : puzzle.getAllPieces()) {
			DisplayPiece displayPiece = new PyraminxDisplayPiece(piece);
			Scene.addPiece(displayPiece);
			pieceMap.put(piece, displayPiece);	
			allDisplayPieces.add(displayPiece);
		}
	}
	
	public void print() {
		puzzle.print();
	}
	
	public float getCurrentRotation() {
		return currentRotation;
	}

	public Move getAnimatingMove() {
		return animatingMove;
	}

	public int getPuzzleSize() {
		return puzzleSize;
	}

	public float getDrawSize() {
		return drawSize;
	}

	public boolean isAnimating() {
		return (animatingMove != null);
	}

	public void setAnimationSpeed(float speed) {
		this.animationSpeed = speed;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}

	public void makeMove(Move move) {
		this.animatingMove = move;
		this.movedPieces = getAffectedPieces();
		direction = animatingMove.isCW() ? 1 : -1;
		if(!animate) {
			finishAnimation();
		}
	}

	private List<Piece> getAffectedPieces() {
		Map<PieceType, List<PieceGroup>> allGroups = puzzle.getAllGroups();
		List<Piece> allAffectedPieces = new ArrayList<Piece>();
		
		for(List<PieceGroup> groups : allGroups.values()) {
			for(PieceGroup group : groups) {
				List<Piece> groupAffectedPieces = group.getAffectedPieces(animatingMove);
				for(Piece piece : groupAffectedPieces) {
					allAffectedPieces.add(piece);
				}
			}
		}
		
		return allAffectedPieces;
	}
	
	public void update() {
		float deltaTime = (System.currentTimeMillis() - lastTime)/1000.0f;
		lastTime = System.currentTimeMillis();

		if(animate && animatingMove != null) {
			currentRotation += deltaTime * animationSpeed * direction;

			Matrix3D rotationMat = getMoveRotationMatrix();
			
			for(Piece position : movedPieces) {
				DisplayPiece displayPiece = pieceMap.get(position);
				displayPiece.setRotationMat(rotationMat);
			}
			
			if(Mathf.abs(currentRotation) >= turnRotation) {
				finishAnimation();  
			}
		}
	}
	
	private Matrix3D getMoveRotationMatrix() {
		Vector3f axis = animatingMove.getFace().getRotationAxis();
		Matrix3D rotationMat = new Matrix3D();
		rotationMat.rotateAroundAxis(axis, currentRotation);
		
		return rotationMat;
	}
	
	public final void finishAnimation() {
		currentRotation = direction * turnRotation;

		Matrix3D rotationMat = getMoveRotationMatrix();	
		for(Piece position : movedPieces) {
			DisplayPiece piece = pieceMap.get(position);
			piece.applyRotation(rotationMat);
		}
	
		puzzle.makeMove(animatingMove);		
		currentRotation = 0;	
		animatingMove = null;
	}
}
