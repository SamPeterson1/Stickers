package com.github.sampeterson1.renderEngine.gui;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.window.Event;

public class GUIComponent {

	private String name;
	
	private List<GUIComponent> children;
	private GUIComponent parent;
	
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
		this.children = new ArrayList<GUIComponent>();
		this.parent = GUIMaster.getRoot();
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
		if(parent == GUIMaster.getRoot()) {
			Matrix3D transform = new Matrix3D();
			float xTransform = 2 * x - 1;
			float yTransform = 2 * y - 1;
			transform.scale(2);
			transform.translate(xTransform, yTransform, 0);
			return transform;
		} else {
			Matrix3D transform = new Matrix3D();
			float parentX = 2 * parent.getX() - 1;
			float parentY = 2 * parent.getY() - 1;
			float xTransform = parentX + 2 * parent.getWidth() * x;
			float yTransform = parentY + 2 * parent.getHeight() * y;
			transform.scale(2);
			transform.translate(xTransform, yTransform, 0);
			return transform;
		}
	}
	
	public void addChild(GUIComponent child) {
		children.add(child);
	}

	public GUIComponent getParent() {
		return this.parent;
	}
	
	public void setParent(GUIComponent newParent) {
		parent.children.remove(this);
		newParent.addChild(this);
		this.parent = newParent;
	}
	
	public String getName() {
		return this.name;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}
