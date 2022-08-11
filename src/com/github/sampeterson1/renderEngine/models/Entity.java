package com.github.sampeterson1.renderEngine.models;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;

public class Entity {
	
	public Vector3f rotation;
	public Vector3f position;
	public Vector3f scale;
	
	private Mesh mesh;
	private Matrix3D transform;
	private boolean updated;
	
	public Entity() {
		this.rotation = new Vector3f(0, 0, 0);
		this.position = new Vector3f(0, 0, 0);
		this.scale = new Vector3f(0, 0, 0);
		this.transform = new Matrix3D();
	}
	
	public Entity(Mesh mesh) {
		this.mesh = mesh;
		this.rotation = new Vector3f(0, 0, 0);
		this.position = new Vector3f(0, 0, 0);
		this.scale = new Vector3f(0, 0, 0);
		this.transform = new Matrix3D();
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	private void updateTransform() {
		transform = new Matrix3D();
		transform.scale(scale.x, scale.y, scale.z);
		transform.rotateXYZ(rotation.x, rotation.y, rotation.z);
		transform.translate(position.x, position.y, position.z);
	}
	
	public Matrix3D getTransform() {
		if(updated) {
			updateTransform();
			updated = false;
		}
		
		return transform;
	}
}
