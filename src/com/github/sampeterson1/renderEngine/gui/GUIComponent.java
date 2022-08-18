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
	private boolean visible;
	
	private GUIComponent parent;
	
	public GUIComponent(String name, float x, float y) {
		this(name, x, y, 1f, 1f);
	}
	
	public GUIComponent(String name, float x, float y, float width, float height) {
		this(null, name, x, y, width, height);
	}
	
	public GUIComponent(GUIComponent parent, String name, float x, float y) {
		this(parent, name, x, y, 1f, 1f);
	}
	
	public GUIComponent(GUIComponent parent, String name, float x, float y, float width, float height) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		this.visible = true;
	}
	
	protected void handleEvent(Event e) {}
	
	public GUIComponent getParent() {
		return this.parent;
	}
	
	public void setParent(GUIComponent parent) {
		float absX = getAbsoluteX();
		float absY = getAbsoluteY();
		float absWidth = getAbsoluteWidth();
		float absHeight = getAbsoluteHeight();
		
		this.parent = parent;
		
		setAbsoluteX(absX);
		setAbsoluteY(absY);
		setAbsoluteWidth(absWidth);
		setAbsoluteHeight(absHeight);
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	protected void setMesh(Mesh mesh) {
		this.mesh = mesh;
		GUIMaster.addComponent(this);
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}
	
	public Matrix3D getTransform() {
		Matrix3D transform = new Matrix3D();
		float xTransform = 2 * getAbsoluteX() - 1;
		float yTransform = 2 * getAbsoluteY() - 1;
		transform.scale(2);
		transform.translate(xTransform, yTransform, 0);
		return transform;
	}
	
	public String getName() {
		return this.name;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getRelativeX() {
		return x;
	}

	protected void setRelativeX(float x) {
		this.x = x;
	}

	public float getRelativeY() {
		return y;
	}

	protected void setRelativeY(float y) {
		this.y = y;
	}

	public float getRelativeWidth() {
		return width;
	}

	protected void setRelativeWidth(float width) {
		this.width = width;
	}

	public float getRelativeHeight() {
		return height;
	}

	protected void setRelativeHeight(float height) {
		this.height = height;
	}
	
	public float getAbsoluteX() {
		if(parent == null) return x;
		return x * parent.getAbsoluteWidth() + parent.getAbsoluteX();
	}

	protected void setAbsoluteX(float absX) {
		if(parent == null) this.x = absX;
		else this.x = (absX - parent.getAbsoluteX()) / parent.getAbsoluteWidth();
	}

	public float getAbsoluteY() {
		if(parent == null) return this.y;
		return y * parent.getAbsoluteHeight() + parent.getAbsoluteY();
	}

	protected void setAbsoluteY(float absY) {
		if(parent == null) this.y = absY;
		else this.y = (absY - parent.getAbsoluteY()) / parent.getAbsoluteHeight();
	}

	public float getAbsoluteWidth() {
		if(parent == null) return this.width;
		return width * parent.getAbsoluteWidth();
	}

	protected void setAbsoluteWidth(float absWidth) {
		if(parent == null) this.width = absWidth;
		else this.width = absWidth / parent.getAbsoluteWidth();
	}

	public float getAbsoluteHeight() {
		if(parent == null) return this.height;
		return height * parent.getAbsoluteHeight();
	}

	protected void setAbsoluteHeight(float absHeight) {
		if(parent == null) this.height = absHeight;
		else this.height = absHeight / parent.getAbsoluteHeight();
	}
	
}
