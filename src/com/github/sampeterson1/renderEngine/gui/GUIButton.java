package com.github.sampeterson1.renderEngine.gui;

import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.text.Font;
import com.github.sampeterson1.renderEngine.text.FontUtil;
import com.github.sampeterson1.renderEngine.text.Text;
import com.github.sampeterson1.renderEngine.window.Event;
import com.github.sampeterson1.renderEngine.window.Window;

public class GUIButton extends GUIComponent {
	
	private static final int[] quadIndices = {
			0, 1, 2, 
			2, 3, 0
	};
	
	private static final float MESH_PADDING = 0.005f;
	private static final float BUTTON_SHADOW_OFFSET = 0.006f;
	private static final float TEXT_SHADOW_OFFSET = 0.0075f;

	private Vector3f highlightColor = new Vector3f(0.3f, 1.0f, 0.3f);
	private Vector3f shadowColor = new Vector3f(0);
	private Vector3f baseColor = new Vector3f(0, 1, 0);
	private Vector2f dim;
	
	private boolean pressed;
	private boolean highlighted;
	
	private Text labelText;
	
	public GUIButton(String name, float x, float y, float width, float height) {
		super(name, x, y, width, height);
		float[] vertices = createVertices(width, height);
		MeshData meshData = Loader.load2DMesh(vertices, quadIndices);
		super.setMesh(new Mesh(meshData, MeshType.BUTTON));
		this.dim = new Vector2f(width / 2, height / 2);
	}
	
	private float[] createVertices(float width, float height) {
		float offset = MESH_PADDING + BUTTON_SHADOW_OFFSET;
		return new float[] {
				-offset, -offset,
				width + offset, -offset,
				width + offset, height + offset,
				-offset, height + offset
		};
	}
	
	public void createLabel(String label, Font font) {
		if(labelText == null) {
			float xOff = FontUtil.getWidth(font, label) / 2.0f;
			float x = super.getX() + super.getWidth() * 0.5f - xOff;
			float y = super.getY() + super.getHeight() * 0.5f + FontUtil.getScaledLineHeight(font) / 2;
			labelText = new Text(super.getName() + "_label", label, font, x, y);
			labelText.offsetColor = shadowColor;
			labelText.offset = new Vector2f(TEXT_SHADOW_OFFSET);
		} else {
			System.err.println("Label already exists!");
		}
	}
	
	public void setLabelShadowColor(Vector3f shadowColor) {
		this.labelText.offsetColor = shadowColor;
	}
	
	public void setLabelColor(Vector3f textColor) {
		this.labelText.color = textColor;
	}
	
	public void setHighlightColor(Vector3f highlightColor) {
		this.highlightColor = highlightColor;
	}
	
	public void setBaseColor(Vector3f baseColor) {
		this.baseColor = baseColor;
	}
	
	public void setShadowColor(Vector3f shadowColor) {
		this.shadowColor = shadowColor;
		labelText.offsetColor = shadowColor;
	}
	
	public boolean isPressed() {
		return this.pressed;
	}
	
	private boolean inBounds(float mouseX, float mouseY) {
		float minX = super.getX();
		float minY = super.getY();
		float maxX = minX + 2 * dim.x;
		float maxY = minY + 2 * dim.y;
		return (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY);
	}
	
	public float getShadowOffset() {
		return BUTTON_SHADOW_OFFSET;
	}
	
	public Vector3f getHighlightColor() {
		return this.highlightColor;
	}
	
	public Vector3f getShadowColor() {
		return this.shadowColor;
	}
	
	public Vector3f getColor() {
		return highlighted ? highlightColor : baseColor;
	}
	
	public Vector2f getDim() {
		return this.dim;
	}

	@Override
	public void handleEvent(Event e) {
		float mouseX = (float) e.getMouseX() / Window.getWidth();
		float mouseY = (float) e.getMouseY() / Window.getHeight();
		float aspect =(float) Window.getWidth() / Window.getHeight();
		int eventType = e.getType();
		if(eventType == Event.EVENT_MOUSE_BUTTON_PRESS && e.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
			if(inBounds(mouseX, mouseY)) {
				labelText.setX(labelText.getX() - BUTTON_SHADOW_OFFSET / aspect);
				labelText.setY(labelText.getY() + BUTTON_SHADOW_OFFSET);
				super.setX(super.getX() - BUTTON_SHADOW_OFFSET / aspect);
				super.setY(super.getY() + BUTTON_SHADOW_OFFSET);
				
				pressed = true;
				GUIMaster.createEvent(new GUIEvent(GUIEventType.BUTTON_PRESS, this));
			}
		} else if(eventType == Event.EVENT_MOUSE_BUTTON_RELEASE && pressed) {
			labelText.setX(labelText.getX() + BUTTON_SHADOW_OFFSET / aspect);
			labelText.setY(labelText.getY() - BUTTON_SHADOW_OFFSET);
			super.setX(super.getX() + BUTTON_SHADOW_OFFSET / aspect);
			super.setY(super.getY() - BUTTON_SHADOW_OFFSET);
			
			highlighted = inBounds(mouseX, mouseY);
			pressed = false;
			GUIMaster.createEvent(new GUIEvent(GUIEventType.BUTTON_RELEASE, this));
		} else if(eventType == Event.EVENT_MOUSE_MOVE) {
			highlighted = inBounds(mouseX, mouseY);
		}
	}
}
