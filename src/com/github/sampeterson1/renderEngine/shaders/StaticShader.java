package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;

public class StaticShader extends Shader {
	
	private static final String VERTEX_FILE = "vertex.glsl";
	private static final String FRAGMENT_FILE = "frag.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightDirection;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "vertColor");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightDirection = super.getUniformLocation("lightDirection");
	}
	
	public void setLightDirection(Vector3f direction) {
		super.loadVector3f(location_lightDirection, direction);
	}
	
	public void setTransformationMatrix(Matrix3D matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void setProjectionMatrix(Matrix3D matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void setViewMatrix(Matrix3D matrix) {
		super.loadMatrix(location_viewMatrix, matrix);
	}
}
