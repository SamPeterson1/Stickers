/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.puzzle.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.cube.display.CubeDisplayPiece;
import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.renderEngine.models.ColoredMesh;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.PieceBatch;
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
	private float targetRotation;

	private Puzzle puzzle;

	private Map<Mesh, PieceBatch> pieceBatches;
	private Map<Piece, DisplayPiece> pieceMap;
	private List<Piece> movedPieces;
	private List<DisplayPiece> allDisplayPieces;
	
	private ColorPalette palette;
	
	public PuzzleDisplay(Puzzle puzzle, float drawSize) {
		this.puzzle = puzzle;
		this.drawSize = drawSize;
		this.palette = new ColorPalette();
		palette.addColor(Color.GREEN);
		palette.addColor(Color.RED);
		palette.addColor(Color.BLUE);
		palette.addColor(Color.LIME_GREEN);
		palette.addColor(Color.WHITE);
		palette.addColor(Color.YELLOW);
		createPieces();
		
		this.lastTime = System.currentTimeMillis();
	}
	
	private void createPieces() {
		this.pieceBatches = new HashMap<Mesh, PieceBatch>();		
		this.pieceMap = new HashMap<Piece, DisplayPiece>();
		this.allDisplayPieces = new ArrayList<DisplayPiece>();
		
		for(Piece piece : puzzle.getAllPieces()) {
			DisplayPiece displayPiece = new CubeDisplayPiece(piece);
			ColoredMesh mesh = displayPiece.getMesh();
			
			if(pieceBatches.containsKey(mesh)) {
				PieceBatch pieceBatch = pieceBatches.get(mesh);
				pieceBatch.addPiece(displayPiece);
			} else {
				PieceBatch pieceBatch = new PieceBatch(mesh, palette);
				pieceBatch.addPiece(displayPiece);
				
				pieceBatches.put(mesh, pieceBatch);
				Scene.addPieceBatch(pieceBatch);
			}
			
			pieceMap.put(piece, displayPiece);	
			allDisplayPieces.add(displayPiece);
		}
		
		for(PieceBatch batch : pieceBatches.values()) {
			batch.create();
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
		if(!move.isCubeRotation()) {
			this.animatingMove = move;
			this.movedPieces = getAffectedPieces();
			this.targetRotation = move.getFace().getRotationAmount();
			direction = animatingMove.isCW() ? 1 : -1;
			if(!animate) {
				finishAnimation();
			}
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
			
			if(Mathf.abs(currentRotation) >= targetRotation) {
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
		currentRotation = direction * targetRotation;

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
