package com.github.yoshiapolis.renderEngine.models;

import com.github.yoshiapolis.math.Vector3f;

public class ColoredVertexGroup {
	
	private String name;
	private Vector3f color;
	private int[] indices;
	
	public ColoredVertexGroup(String name, int[] indices) {
		this.name = name;
		this.indices = indices;
		this.color = Vector3f.ONE;
	}
	
	public int[] getIndices() {
		return this.indices;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Vector3f getColor() {
		return this.color;
	}

	public void setColor(Vector3f baseColor) {
		this.color = baseColor;
	}
}
