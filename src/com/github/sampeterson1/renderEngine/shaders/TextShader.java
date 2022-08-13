package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.math.Matrix3D;

public class TextShader extends Shader {

	private static final String VERTEX_FILE = "TextVert.glsl";
	private static final String FRAGMENT_FILE = "TextFrag.glsl";
	
	private int location_transformationMatrix;
	
	public TextShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}
		
	public void setTransformationMatrix(Matrix3D matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
}
