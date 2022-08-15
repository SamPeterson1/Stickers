package com.github.sampeterson1.renderEngine.gui;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.window.Event;

public class GUIComponent {

	private String name;
	
	private float x;
	private float y;
	private float width;
	private float height;
	
	private Mesh mesh;
	
	public GUIComponent(String name, float x, float y) {
		this(name, x, y, 1f, 1f);
	}
	
	public GUIComponent(String name, float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
	}
	
	protected void handleEvent(Event e) {}
	
	protected void setMesh(Mesh mesh) {
		this.mesh = mesh;
		GUIMaster.addComponent(this);
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}
	
	public Matrix3D getTransform() {
		Matrix3D transform = new Matrix3D();
		float xTransform = 2 * x - 1;
		float yTransform = 2 * y - 1;
		transform.scale(2);
		transform.translate(xTransform, yTransform, 0);
		return transform;
	}
	
	public String getName() {
		return this.name;
	}

	public float getX() {
		return x;
	}

	protected void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	protected void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	protected void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	protected void setHeight(float height) {
		this.height = height;
	}
	
}
