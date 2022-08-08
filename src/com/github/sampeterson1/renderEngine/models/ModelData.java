package com.github.sampeterson1.renderEngine.models;

public class ModelData {
	
	private int vaoID;
	private int[] vboIDs;
	private int numIndices;
	private int numVertices;
	
	public ModelData(int vaoID, int[] vboIDs, int numVertices, int numIndices) {
		this.vaoID = vaoID;
		this.vboIDs = vboIDs;
		this.numIndices = numIndices;
		this.numVertices = numVertices;
	}
	
	public int getVboID(int attributeID) {
		return this.vboIDs[attributeID];
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getNumIndices() {
		return numIndices;
	}

	public int getNumVertices() {
		return numVertices;
	}
	
}
