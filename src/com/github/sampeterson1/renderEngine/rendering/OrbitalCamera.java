package com.github.sampeterson1.renderEngine.rendering;

import com.github.sampeterson1.math.Matrix3D;

public class OrbitalCamera {

	private float rotX;
	private float rotY;
	
	private float distance;
	
	public OrbitalCamera(float distance) {
		this.distance = distance;
	}
	
	public Matrix3D getViewMatrix() {
		Matrix3D viewMatrix = new Matrix3D();
		
		viewMatrix.rotateY(rotY);
		viewMatrix.rotateX(rotX);
		viewMatrix.translate(0, 0, -distance);
		
		return viewMatrix;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
	
}
