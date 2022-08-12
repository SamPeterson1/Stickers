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

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.renderEngine.models.ColoredMesh;
import com.github.sampeterson1.renderEngine.models.PieceBatch;

//A visible piece on a puzzle
public abstract class DisplayPiece {
	
	private PieceBatch pieceBatch;
	
	//Keeps track of where we are on the puzzle
	private Piece piece;
	
	//The base position of the piece
	private Matrix3D transformationMat;
	
	//Rotation of the piece; used for animating moves
	private Matrix3D rotationMat;
	
	//Indices into the current color palette that determine the colors of the piece
	private int[] colorIDs;
	 
	//Return the mesh to be used for this piece 
	protected abstract ColoredMesh getMesh();
	
	//Set the colors of this piece
	protected abstract void setColors();
	
	//Return a matrix representing the world position of this piece
	protected abstract Matrix3D getWorldPosition();
	
	public DisplayPiece(Piece piece) {
		this.transformationMat = new Matrix3D();
		this.rotationMat = new Matrix3D();
		this.piece = piece;		
		this.colorIDs = new int[PieceBatch.MAX_COLORS];
		this.transformationMat = getWorldPosition();
	}
	
	public void setPieceBatch(PieceBatch pieceBatch) {
		this.pieceBatch = pieceBatch;
		setColors();
	}
	
	public int[] getColorIDs() {
		return this.colorIDs;
	}

	public Piece getPiece() {
		return this.piece;
	}
	
	public Matrix3D getTransform() {
		Matrix3D retVal = new Matrix3D();
		retVal.multiply(transformationMat);
		retVal.multiply(rotationMat);
		
		return retVal;
	}

	public void setRotationMat(Matrix3D rotation) {
		this.rotationMat = rotation;
	}
	
	public void applyRotation(Matrix3D rotation) {
		transformationMat.multiply(rotation);
		rotationMat = new Matrix3D();
	}
	
	protected void setColor(String groupName, Color color) {
		int colorGroupID = pieceBatch.getMesh().getColorGroupID(groupName);
		int colorID = pieceBatch.getPalette().getColorID(color);
		colorIDs[colorGroupID] = colorID;
	}
}
