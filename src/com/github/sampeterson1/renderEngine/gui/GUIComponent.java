package com.github.sampeterson1.renderEngine.gui;

import java.util.List;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Entity;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.window.Event;

public class GUIComponent extends Entity {

	private String name;
	
	private List<GUIComponent> children;
	private GUIComponent parent;
	private Matrix3D transformationMatrix;
	
	public GUIComponent(String name) {
		this.name = name;
		this.transformationMatrix = new Matrix3D();

		GUIMaster.addComponent(this);
	}
	
	protected void handleEvent(Event e) {}
	
	public Matrix3D getTransformationMatrix() {
		return this.transformationMatrix;
	}
	
	public void addChild(GUIComponent child) {
		children.add(child);
	}

	public void setParent(GUIComponent newParent) {
		parent.children.remove(this);
		newParent.addChild(this);
		this.parent = newParent;
	}
	
	public String getName() {
		return this.name;
	}
	
}
