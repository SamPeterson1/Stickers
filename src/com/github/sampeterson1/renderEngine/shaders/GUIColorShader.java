package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.renderEngine.gui.GUIComponent;

public abstract class GUIColorShader extends Shader {

	public GUIColorShader(String vertexShader, String fragShader) {
		super(vertexShader, fragShader);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public abstract void loadGUIComponent(GUIComponent component);
	
}
