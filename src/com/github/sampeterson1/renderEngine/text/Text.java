package com.github.sampeterson1.renderEngine.text;

import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.gui.GUIComponent;

public class Text extends GUIComponent {
	
	public Text(String name, float x, float y, String text, Font font) {
		super(name);
		
		super.scale = new Vector3f(font.getFontSize());
		super.position = new Vector3f(x, y, 0);
		super.setMesh(TextMeshGenerator.generateMesh(text, font));
	}
	
}
