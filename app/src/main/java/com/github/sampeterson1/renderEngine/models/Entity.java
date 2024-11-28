package com.github.sampeterson1.renderEngine.models;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.rendering.Scene;

public class Entity {
	
	public Vector3f rotation;
	public Vector3f position;
	public Vector3f scale;
	
	private Mesh mesh;
	
	public Entity() {
		this.rotation = new Vector3f(0, 0, 0);
		this.position = new Vector3f(0, 0, 0);
		this.scale = new Vector3f(1, 1, 1);
	}
	
	public Entity(Mesh mesh) {
		this.mesh = mesh;
		this.rotation = new Vector3f(0, 0, 0);
		this.position = new Vector3f(0, 0, 0);
		this.scale = new Vector3f(0, 0, 0);
		
		Scene.addEntity(this);
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
		Scene.addEntity(this);
	}
	
	public Mesh getMesh() {
		return mesh;
	}

	public Matrix3D getTransform() {
		Matrix3D transform = new Matrix3D();
		transform.scale(scale.x, scale.y, scale.z);
		transform.rotateXYZ(rotation.x, rotation.y, rotation.z);
		transform.translate(position.x, position.y, position.z);
		
		return transform;
	}
}