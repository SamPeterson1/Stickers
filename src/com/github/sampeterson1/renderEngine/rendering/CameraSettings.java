package com.github.sampeterson1.renderEngine.rendering;

public class CameraSettings {

	private float fov = 70;
	private float zFar = 1000;
	private float zNear = 0.1f;

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}

	public float getZFar() {
		return zFar;
	}

	public void setZFar(float zFar) {
		this.zFar = zFar;
	}

	public float getZNear() {
		return zNear;
	}

	public void setZNear(float zNear) {
		this.zNear = zNear;
	}
	
}
