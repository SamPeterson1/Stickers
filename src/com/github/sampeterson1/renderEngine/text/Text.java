package com.github.sampeterson1.renderEngine.text;

import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.gui.GUIComponent;

public class Text extends GUIComponent {
	
	public Vector3f color = new Vector3f(1);
	public Vector3f offsetColor = new Vector3f(0);
	
	public Vector2f offset = new Vector2f(0);
	
	public float thickness = 0.5f;
	public float blending = 0.1f;
	public float offsetThickness = 0;
	
	public Text(String name, String text, Font font, float x, float y) {
		this(name, text, font, x, y, 2f);
	}
	
	public Text(String name, String text, Font font, float x, float y, float lineWidth) {
		super(name, x, y);
		super.setMesh(TextMeshGenerator.generateMesh(text, font));
	}
	
}
