package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.renderEngine.text.Text;

public class TextShader extends Shader {

	private static final String VERTEX_FILE = "TextVert.glsl";
	private static final String FRAGMENT_FILE = "TextFrag.glsl";
	
	private static final String[] UNIFORM_NAMES = {
			"transformationMatrix", "thickness", "blending",
			"offsetThickness", "offset", "color", "offsetColor",
			"minPosition", "maxPosition"
	};
	
	public TextShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
	}

	@Override
	protected String[] getAllUniformNames() {
		return UNIFORM_NAMES;
	}

	public void loadText(Text text) {
		super.loadMatrix("transformationMatrix", text.getTransform());
		super.loadFloat("thickness", text.thickness);
		super.loadFloat("blending", text.blending);
		super.loadFloat("offsetThickness", text.offsetThickness);
		super.loadVector2f("offset", text.offset);
		super.loadVector3f("color", text.color);
		super.loadVector3f("offsetColor", text.offsetColor);
		super.loadVector2f("minPosition", text.minPosition);
		super.loadVector2f("maxPosition", text.maxPosition);
	}
	
}
