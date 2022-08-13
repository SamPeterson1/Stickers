package com.github.sampeterson1.renderEngine.models;

import com.github.sampeterson1.renderEngine.rendering.MeshType;

public class Mesh {

	public static int GUI_MESH = 0;
	public static int COLORED_MESH = 2;
	public static int TEXT_MESH = 1;
	
	private MeshData data;
	private MeshType type;
	
	public Mesh(MeshData data, MeshType type) {
		this.data = data;
		this.type = type;
	}
	
	public MeshType getType() {
		return this.type;
	}
	
	public MeshData getData() {
		return this.data;
	}
}
