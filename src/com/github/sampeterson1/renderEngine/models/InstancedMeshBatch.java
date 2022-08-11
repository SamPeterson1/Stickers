package com.github.sampeterson1.renderEngine.models;

import java.nio.FloatBuffer;

import com.github.sampeterson1.renderEngine.loaders.Loader;

public class InstancedMeshBatch {
	
	private Mesh mesh;
	
	private int instanceDataLength;
	private int numInstances;
	private int instancedVboID;
	
	public InstancedMeshBatch(int instanceDataLength, int numInstances) {
		this(null, instanceDataLength, numInstances);
	}
	
	public InstancedMeshBatch(Mesh mesh, int instanceDataLength, int numInstances) {
		this.mesh = mesh;
		this.instanceDataLength = instanceDataLength;
		this.numInstances = numInstances;
		int numFloats = instanceDataLength * numInstances;
		this.instancedVboID = Loader.createInstancedVBO(numFloats);
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}
	
	public int getNumInstances() {
		return this.numInstances;
	}
	
	public void addInstancedAttribute(int attributeID, int coordinateSize, int offset) {
		int vaoID = mesh.getData().getVaoID();
		Loader.addInstancedAttribute(instancedVboID, vaoID, attributeID, coordinateSize, instanceDataLength, offset);
	}
	
	public void updateInstanceData(FloatBuffer data) {
		Loader.updateVBO(instancedVboID, data);
	}
	
}
