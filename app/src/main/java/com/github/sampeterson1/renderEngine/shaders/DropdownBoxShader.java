package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.renderEngine.gui.GUIDropdownBox;

public class DropdownBoxShader extends Shader {
	
	private static final String VERTEX_FILE = "DropdownBoxVert.glsl";
	private static final String FRAGMENT_FILE = "DropdownBoxFrag.glsl";
	
	private static final String[] UNIFORM_NAMES = {
			"transformationMatrix", "selectionID", "selectionColor", "arrowColor", "expanded"
	};

	public DropdownBoxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
		super.bindAttribute(2, "optionID");
	}
	
	@Override
	protected String[] getAllUniformNames() {
		return UNIFORM_NAMES;
	}
	
	public void loadDropdown(GUIDropdownBox dropdownBox) {
		super.loadMatrix("transformationMatrix", dropdownBox.getTransform());
		super.loadInt("selectionID", dropdownBox.getHoverSelectionID());
		super.loadVector3f("arrowColor", dropdownBox.getArrowColor());
		super.loadVector3f("selectionColor", dropdownBox.getSelectionColor());
		super.loadBoolean("expanded", dropdownBox.isExpanded());
	}
	
}
