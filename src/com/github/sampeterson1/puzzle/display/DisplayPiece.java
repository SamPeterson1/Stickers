package com.github.sampeterson1.puzzle.display;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.renderEngine.models.ColoredModel;

public abstract class DisplayPiece {
	
	private Piece position;
	private ColoredModel model;
	private Matrix3D transformationMat;
	private Matrix3D rotationMat;
	
	public int firstPosition;
	
	protected abstract ColoredModel loadModel(Piece position);
	
	public abstract void setWorldPosition(Piece position);
	
	public DisplayPiece(Piece position) {
		this.transformationMat = new Matrix3D();
		this.rotationMat = new Matrix3D();
		this.model = loadModel(position);
		this.position = position;
		this.firstPosition = position.getPosition();
		setWorldPosition(position);
	}

	public Piece getPosition() {
		//System.out.println(this.position.getPosition() + " " + this.firstPosition);
		return this.position;
	}
	
	public ColoredModel getModel() {
		return this.model;
	}
	
	public Matrix3D getTransformationMat() {
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
