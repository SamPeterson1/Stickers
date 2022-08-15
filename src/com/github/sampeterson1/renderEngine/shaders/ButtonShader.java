package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.renderEngine.gui.GUIButton;
import com.github.sampeterson1.renderEngine.gui.GUIComponent;

public class ButtonShader extends GUIColorShader {

	private static final String VERTEX_FILE = "ButtonVert.glsl";
	private static final String FRAGMENT_FILE = "ButtonFrag.glsl";
	
	private static final String[] UNIFORM_NAMES = {
			"transformationMatrix", "color", "shadowColor",
			"buttonDim", "shadowOffset", "pressed"
	};

	public ButtonShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected String[] getAllUniformNames() {
		return UNIFORM_NAMES;
	}
	
	@Override
	public void loadGUIComponent(GUIComponent component) {
		GUIButton button = (GUIButton) component;
		super.loadMatrix("transformationMatrix", button.getTransform());
		super.loadVector3f("color", button.getColor());
		super.loadVector3f("shadowColor", button.getShadowColor());
		super.loadVector2f("buttonDim", button.getDim());
		super.loadFloat("shadowOffset", button.getShadowOffset());
		super.loadBoolean("pressed", button.isPressed());
	}
	
}
