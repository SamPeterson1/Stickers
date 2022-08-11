package com.github.sampeterson1.renderEngine.models;

public class Mesh {
	
	private MeshData data;
	
	public Mesh(MeshData data) {
		this.data = data;
	}
	
	public MeshData getData() {
		return this.data;
	}
}
