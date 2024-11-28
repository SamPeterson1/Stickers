package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.renderEngine.gui.GUIComponent;
import com.github.sampeterson1.renderEngine.gui.GUITextBox;

public class TextBoxShader extends GUIColorShader {

	private static final String VERTEX_FILE = "TextBoxVert.glsl";
	private static final String FRAGMENT_FILE = "TextBoxFrag.glsl";
	 
	private static final String[] UNIFORM_NAMES = {
			"transformationMatrix", "borderColor", "fillColor", "dimensions", "cursorPosition", "cursorHeight"
	};

	public TextBoxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected String[] getAllUniformNames() {
		return UNIFORM_NAMES;
	}
	
	@Override
	public void loadGUIComponent(GUIComponent component) {
		GUITextBox textBox = (GUITextBox) component;
		super.loadMatrix("transformationMatrix", textBox.getTransform());
		super.loadVector2f("dimensions", textBox.getDimensions());
		super.loadVector3f("borderColor", textBox.getBorderColor());
		super.loadVector3f("fillColor", textBox.getFillColor());
		super.loadFloat("cursorPosition", textBox.getCursorPosition());
		super.loadFloat("cursorHeight", textBox.getCursorHeight());
	}
	
}
