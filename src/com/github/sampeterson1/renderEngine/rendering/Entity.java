package com.github.sampeterson1.renderEngine.rendering;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.models.ColoredModel;

public class Entity {
	
	private ColoredModel model;
	private Matrix3D transformationMatrix;
	
	public Entity(ColoredModel model) {
		this.model = model;
		this.transformationMatrix = new Matrix3D();
	}
	
	public Entity(ColoredModel model, Matrix3D transformationMatrix) {
		this.model = model;
		this.transformationMatrix = transformationMatrix;
	}
	
	public ColoredModel getModel() {
		return this.model;
	}
	
	public Matrix3D getTransformationMatrix() {
		return this.transformationMatrix;
	}
	
	public void setTransformationMatrix(Matrix3D transformationMatrix) {
		this.transformationMatrix = transformationMatrix;
	}
}
