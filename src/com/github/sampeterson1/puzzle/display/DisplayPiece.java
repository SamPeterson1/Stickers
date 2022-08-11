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
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.renderEngine.models.ColoredMesh;
import com.github.sampeterson1.renderEngine.models.Entity;

public abstract class DisplayPiece extends Entity {
	
	private Piece position;
	private Matrix3D transformationMat;
	private Matrix3D rotationMat;
	
	public int firstPosition;
	
	protected abstract ColoredMesh loadMesh(Piece position);
	
	public abstract void setWorldPosition(Piece position);
	
	public DisplayPiece(Piece position) {
		super();
		this.transformationMat = new Matrix3D();
		this.rotationMat = new Matrix3D();
		this.position = position;
		this.firstPosition = position.getPosition();
		
		super.setMesh(loadMesh(position));
		setWorldPosition(position);
	}

	public Piece getPosition() {
		//System.out.println(this.position.getPosition() + " " + this.firstPosition);
		return this.position;
	}
	
	@Override
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
	
	protected void setTransformationMat(Matrix3D transformationMat) {
		this.transformationMat = transformationMat;
	}
}
