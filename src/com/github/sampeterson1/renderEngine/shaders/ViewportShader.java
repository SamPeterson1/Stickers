package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.math.Matrix3D;

public class ViewportShader extends Shader {

	private static final String VERTEX_FILE = "ViewportVert.glsl";
	private static final String FRAGMENT_FILE = "ViewportFrag.glsl";

	private static final String[] UNIFORM_NAMES = {
			"projectionMatrix"
	};
	
	public ViewportShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected String[] getAllUniformNames() {
		return UNIFORM_NAMES;
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
	}

	public void setProjectionMatrix(Matrix3D matrix) {
		super.loadMatrix("projectionMatrix", matrix);
	}
	
}
