package com.github.sampeterson1.renderEngine.text;

import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.gui.GUIComponent;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.models.Texture;
import com.github.sampeterson1.renderEngine.rendering.MeshType;

public class Text extends GUIComponent {
	
	public Vector3f color = new Vector3f(1);
	public Vector3f offsetColor = new Vector3f(0);
	
	public Vector2f offset = new Vector2f(0);
	public Vector2f minPosition = new Vector2f(0);
	public Vector2f maxPosition = new Vector2f(1);
	
	public float thickness = 0.5f;
	public float blending = 0.1f;
	public float offsetThickness = 0;
	
	private String text;
	private Font font;
	
	public Text(String name, String text, Font font, float x, float y) {
		this(name, text, font, x, y, 2f);
	}
	
	public Text(String name, String text, Font font, float x, float y, float lineWidth) {
		super(name, x, y);
		this.font = font;
		this.text = text;
		MeshData meshData = TextMeshGenerator.generateMesh(text, font);
		super.setMesh(new Mesh(meshData, MeshType.TEXT));
	}
	
	public String getText() {
		return this.text;
	}
	
	public void updateText(String text) {
		updateText(text, font);
	}
	
	public void updateText(String text, Font font) {
		super.getMesh().getData().delete();
		this.text = text;
		MeshData meshData = TextMeshGenerator.generateMesh(text, font);
		super.setMesh(new Mesh(meshData, MeshType.TEXT));
	}
	
	public Font getFont() {
		return this.font;
	}
	
	public Texture getAtlasTexture() {
		return this.font.getTexture();
	}
	
}
