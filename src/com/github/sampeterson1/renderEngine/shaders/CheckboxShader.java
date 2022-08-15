package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.renderEngine.gui.GUIComponent;

public class CheckboxShader extends GUIColorShader {

	private static final String VERTEX_FILE = "CheckboxVert.glsl";
	private static final String FRAGMENT_FILE = "CheckboxFrag.glsl";
	
	private static final String[] UNIFORM_NAMES = {
			"transformationMatrix"
	};

	public CheckboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected String[] getAllUniformNames() {
		return UNIFORM_NAMES;
	}
	
	@Override
	public void loadGUIComponent(GUIComponent component) {
		super.loadMatrix("transformationMatrix", component.getTransform());
	}
	
}
