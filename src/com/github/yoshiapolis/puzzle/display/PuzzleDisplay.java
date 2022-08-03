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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.yoshiapolis.cube.display.CubeDisplayPiece;
import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.puzzle.lib.Puzzle;

import processing.core.PApplet;
import processing.core.PMatrix3D;

public class PuzzleDisplay {
	
	private PApplet app;
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
		this.turnRotation = Mathf.PI / 2;
	}
	
	private void createPieces() {
		this.pieceMap = new HashMap<Piece, DisplayPiece>();
		this.allDisplayPieces = new ArrayList<DisplayPiece>();
		
		for(Piece piece : puzzle.getAllPieces()) {
			DisplayPiece displayPiece = new CubeDisplayPiece(piece, drawSize);
			
			pieceMap.put(piece, displayPiece);	
			allDisplayPieces.add(displayPiece);
		}
	}

	public PApplet getApp() {
		return app;
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
		System.out.println(this.animate);
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
				allAffectedPieces.addAll(groupAffectedPieces);
			}
		}
		
		return allAffectedPieces;
	}
	
	public void show() {
		float deltaTime = (System.currentTimeMillis() - lastTime)/1000.0f;
		lastTime = System.currentTimeMillis();

		if(animate && animatingMove != null) {
			currentRotation += deltaTime * animationSpeed * direction;
			System.out.println((currentRotation / (Mathf.PI / 2)) + " " + animationSpeed);
			PMatrix3D rotationMat = animatingMove.getFace().getMoveMat(currentRotation);
			
			for(Piece piece : movedPieces) {
				DisplayPiece displayPiece = pieceMap.get(piece);
				displayPiece.setRotationMat(rotationMat);
			}
			
			if(Mathf.abs(currentRotation) >= turnRotation) {
				System.out.println("finishing");
				finishAnimation();  
			}
		}
		
		for(DisplayPiece piece : allDisplayPieces) {
			piece.show();
		}
	}
	
	public final void finishAnimation() {
		currentRotation = direction * turnRotation;
		System.out.println("done");
		for(Piece piece : movedPieces) {
			DisplayPiece displayPiece = pieceMap.get(piece);
			displayPiece.setPosition(piece, drawSize);
			displayPiece.setRotationMat(null);
		}
		
		currentRotation = 0;
		animatingMove = null;
	}
}
