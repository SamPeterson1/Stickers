package com.github.sampeterson1.renderEngine.gui;

import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.text.Font;
import com.github.sampeterson1.renderEngine.text.FontUtil;
import com.github.sampeterson1.renderEngine.text.GUIText;
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
	
	private boolean pressed;
	private boolean highlighted;
	
	private GUIText labelText;
	
	public GUIButton(String name, float x, float y, float width, float aspect) {
		this(null, name, x, y, width, aspect);
	}
	
	public GUIButton(GUIComponent parent, String name, float x, float y, float width, float aspect) {
		super(parent, name, x, y, width, width / aspect * parent.getAbsoluteWidth() / parent.getAbsoluteHeight());
		createMesh();
	}
	
	private void createMesh() {
		float offset = MESH_PADDING + BUTTON_SHADOW_OFFSET;
		float width = super.getAbsoluteWidth();
		float height = super.getAbsoluteHeight();
		
		float[] vertices = new float[] {
				-offset, -offset,
				width + offset, -offset,
				width + offset, height + offset,
				-offset, height + offset
		};
		
		MeshData meshData = Loader.load2DMesh(vertices, quadIndices);
		super.setMesh(new Mesh(meshData, MeshType.BUTTON));
	}
	
	public void createLabel(String label, Font font) {
		if(labelText == null) {
			float xOff = FontUtil.getWidth(font, label) / 2.0f;
			float yOff = FontUtil.getScaledLineHeight(font) / 2;
			float x = 0.5f - xOff / super.getAbsoluteWidth();
			float y = 0.5f + yOff / super.getAbsoluteHeight();
			
			labelText = new GUIText(this, super.getName() + "_label", label, font, x, y);
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
		float minX = super.getAbsoluteX();
		float minY = super.getAbsoluteY();
		float maxX = minX + super.getAbsoluteWidth();
		float maxY = minY + super.getAbsoluteHeight();
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

	@Override
	public void handleEvent(Event e) {
		float mouseX = (float) e.getMouseX() / Window.getWidth();
		float mouseY = (float) e.getMouseY() / Window.getHeight();
		float aspect =(float) Window.getWidth() / Window.getHeight();
		
		int eventType = e.getType();
		if(eventType == Event.EVENT_MOUSE_BUTTON_PRESS && e.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
			if(inBounds(mouseX, mouseY)) {
				super.setAbsoluteX(super.getAbsoluteX() - BUTTON_SHADOW_OFFSET / aspect);
				super.setAbsoluteY(super.getAbsoluteY() + BUTTON_SHADOW_OFFSET);
				
				pressed = true;
				GUIMaster.createEvent(new GUIEvent(GUIEventType.BUTTON_PRESS, this));
			}
		} else if(eventType == Event.EVENT_MOUSE_BUTTON_RELEASE && pressed) {
			super.setAbsoluteX(super.getAbsoluteX() + BUTTON_SHADOW_OFFSET / aspect);
			super.setAbsoluteY(super.getAbsoluteY() - BUTTON_SHADOW_OFFSET);
			
			highlighted = inBounds(mouseX, mouseY);
			pressed = false;
			
			GUIMaster.createEvent(new GUIEvent(GUIEventType.BUTTON_RELEASE, this));
		} else if(eventType == Event.EVENT_MOUSE_MOVE) {
			highlighted = inBounds(mouseX, mouseY);
		}
	}
}
