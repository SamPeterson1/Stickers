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

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.renderEngine.models.ColoredMesh;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.PieceBatch;
import com.github.sampeterson1.renderEngine.rendering.Scene;

//Controls the puzzle displayed in the puzzle viewport
public class PuzzleDisplay {
	
	private static final float DEFAULT_ANIMATION_SPEED = 20f;
	
	private Move animatingMove;
	private float animationSpeed = DEFAULT_ANIMATION_SPEED;
	private boolean animate = true;
	
	private float currentRotation;
	private float targetRotation;
	private int direction;
	
	private int puzzleSize;
	
	private long lastTime;

	private Puzzle puzzle;

	private Map<Mesh, PieceBatch> pieceBatches;
	private Map<Piece, DisplayPiece> pieceMap;
	private List<DisplayPiece> affectedPieces;
	private List<DisplayPiece> allDisplayPieces;
	
	private ColorPalette palette;
	
	public PuzzleDisplay(Puzzle puzzle) {
		this.puzzle = puzzle;
		this.palette = puzzle.getMetaFunctions().createDefaultColorPalette();
		
		createPieces();
		this.lastTime = System.currentTimeMillis();
	}
	
	//Delete all pieces
	public void delete() {
		for(PieceBatch pieceBatch : pieceBatches.values()) {
			pieceBatch.delete();
		}
		Scene.clearPieceBatches();
	}
	
	//populate all PieceBatches with DisplayPieces that are linked to our puzzle's pieces
	private void createPieces() {
		this.pieceBatches = new HashMap<Mesh, PieceBatch>();		
		this.pieceMap = new HashMap<Piece, DisplayPiece>();
		this.allDisplayPieces = new ArrayList<DisplayPiece>();
		
		for(Piece piece : puzzle.getAllPieces()) {
			DisplayPiece displayPiece = puzzle.getMetaFunctions().createDisplayPiece(piece);
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

	public float getCurrentRotation() {
		return currentRotation;
	}

	public Move getAnimatingMove() {
		return animatingMove;
	}

	public int getPuzzleSize() {
		return puzzleSize;
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
		this.targetRotation = move.getAxis().getRotationAmount();
		getAffectedPieces();
		direction = animatingMove.isCW() ? 1 : -1;
		if(!animate) {
			finishAnimation();
		}
	}

	//Return a list of DisplayPieces that are affected by the current animating move
	private void getAffectedPieces() {
		List<Piece> pieces = puzzle.getAffectedPieces(animatingMove);
		this.affectedPieces = new ArrayList<DisplayPiece>();
		
		for(Piece piece : pieces) {
			DisplayPiece displayPiece = pieceMap.get(piece);
			affectedPieces.add(displayPiece);
		}
	}
	
	//Update the animation
	public void update() {
		float deltaTime = (System.currentTimeMillis() - lastTime)/1000.0f;
		lastTime = System.currentTimeMillis();

		if(animate && animatingMove != null) {
			currentRotation += deltaTime * animationSpeed * direction;
			Matrix3D rotationMat = getMoveRotationMatrix(animatingMove, currentRotation);
			
			for(DisplayPiece piece : affectedPieces) {
				piece.setRotationMat(rotationMat);
			}
			
			if(Mathf.abs(currentRotation) >= targetRotation) {
				finishAnimation();  
			}
		}
	}
	
	//Return a rotation matrix that rotates pieces around the current move axis by a specified amount
	private Matrix3D getMoveRotationMatrix(Move move, float rotation) {
		Vector3f axis = move.getAxis().getRotationAxis();
		Matrix3D rotationMat = new Matrix3D();
		rotationMat.rotateAroundAxis(axis, rotation);
		
		return rotationMat;
	}
	
	//Finish the current animation by applying the current rotation to the piece's internal transformation matrix
	public final void finishAnimation() {
		currentRotation = direction * targetRotation;

		Matrix3D rotationMat = getMoveRotationMatrix(animatingMove, currentRotation);	
		for(DisplayPiece piece : affectedPieces) {
			piece.applyRotation(rotationMat);
		}
	
		puzzle.makeMove(animatingMove);		
		currentRotation = 0;	
		animatingMove = null;
	}
}
