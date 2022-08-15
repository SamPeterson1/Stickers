package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.text.Text;

public class TextShader extends Shader {

	private static final String VERTEX_FILE = "TextVert.glsl";
	private static final String FRAGMENT_FILE = "TextFrag.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_thickness;
	private int location_blending;
	private int location_offsetThickness;
	private int location_offset;
	private int location_color;
	private int location_offsetColor;
	
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
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_thickness = super.getUniformLocation("thickness");
		location_blending = super.getUniformLocation("blending");
		location_offsetThickness = super.getUniformLocation("offsetThickness");
		location_offset = super.getUniformLocation("offset");
		location_color = super.getUniformLocation("color");
		location_offsetColor = super.getUniformLocation("offsetColor");
	}
	
	public void loadText(Text text) {
		super.loadMatrix(location_projectionMatrix, GUIMaster.orthoProjection);
		super.loadMatrix(location_transformationMatrix, text.getTransform());
		super.loadFloat(location_thickness, text.thickness);
		super.loadFloat(location_blending, text.blending);
		super.loadFloat(location_offsetThickness, text.offsetThickness);
		super.loadVector2f(location_offset, text.offset);
		super.loadVector3f(location_color, text.color);
		super.loadVector3f(location_offsetColor, text.offsetColor);
	}
	
}
