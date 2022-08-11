package com.github.sampeterson1.renderEngine.gui;

import java.util.List;

import com.github.sampeterson1.renderEngine.loaders.ModelLoader;
import com.github.sampeterson1.renderEngine.models.Entity;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.window.Event;

public abstract class GUIComponent extends Entity {

	private static final float[] quadVertices = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f
	};
	
	private static final int[] quadIndices = {
			0, 1, 2, 
			2, 3, 0
	};
	
	private String name;
	
	private List<GUIComponent> children;
	private GUIComponent parent;
	
	public GUIComponent(String name, float x, float y, float width, float height) {
		this.name = name;
		//MeshData meshData = ModelLoader.loadData(quadVertices, quadVertices, quadIndices);
		//super.setMesh(new Mesh(meshData));
		GUIMaster.addComponent(this);
	}
	
	protected abstract void handleEvent(Event e);
	
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
