package com.github.sampeterson1.renderEngine.gui;

import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.window.Event;
import com.github.sampeterson1.renderEngine.window.Window;

public class GUICheckbox extends GUIComponent {

	private static final int[] quadIndices = {
			0, 1, 2, 
			2, 3, 0
	};
	
	private static final float MESH_PADDING = 0.005f;
	
	private Vector3f highlightedColor = new Vector3f(0.5f, 0.5f, 0.5f);
	private Vector3f checkedHighlightColor = new Vector3f(0.3f, 1, 0.3f);
	private Vector3f checkedColor = new Vector3f(0, 1, 0);
	private Vector3f borderColor = new Vector3f(1);
	
	private boolean highlighted;
	private boolean checked;
	
	public GUICheckbox(String name, float x, float y, float size) {
		this(null, name, x, y, size);
	}
	
	public GUICheckbox(GUIComponent parent, String name, float x, float y, float size) {
		super(parent, name, x, y, size, size);
		createMesh();
	}
	
	private void createMesh() {
		float size = super.getAbsoluteWidth();
		float[] vertices = new float[] {
				-MESH_PADDING, -MESH_PADDING,
				size + MESH_PADDING, -MESH_PADDING,
				size + MESH_PADDING, size + MESH_PADDING,
				-MESH_PADDING, size + MESH_PADDING
		};
		
		MeshData meshData = Loader.load2DMesh(vertices, quadIndices);
		super.setMesh(new Mesh(meshData, MeshType.CHECKBOX));
	}
	
	private boolean inBounds(float mouseX, float mouseY) {
		float minX = super.getAbsoluteX();
		float minY = super.getAbsoluteY();
		float maxX = minX + super.getAbsoluteWidth();
		float maxY = minY + super.getAbsoluteWidth();
		
		return mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY;
	}
	
	@Override
	public void handleEvent(Event e) {
		float mouseX = (float) e.getMouseX() / Window.getWidth();
		float mouseY = (float) e.getMouseY() / Window.getHeight();
		int eventType = e.getType();
		
		if(eventType == Event.EVENT_MOUSE_MOVE) {
			highlighted = inBounds(mouseX, mouseY);
		} else if(eventType == Event.EVENT_MOUSE_BUTTON_PRESS && e.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
			if(inBounds(mouseX, mouseY)) {
				checked = !checked;
				GUIMaster.createEvent(new GUIEvent(GUIEventType.CHECKBOX_TOGGLE, this));
			}
		}
	}
	
	public boolean hasFill() {
		return highlighted || checked;
	}
	
	public boolean isChecked() {
		return this.checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public Vector3f getFillColor() {
		if(checked) {
			return highlighted ? checkedHighlightColor : checkedColor;
		}
		return highlightedColor;
	}

	public Vector3f getBorderColor() {
		return this.borderColor;
	}

	public void setHighlightedColor(Vector3f highlightedColor) {
		this.highlightedColor = highlightedColor;
	}

	public void setCheckedHighlightColor(Vector3f checkedHighlightColor) {
		this.checkedHighlightColor = checkedHighlightColor;
	}

	public void setCheckedColor(Vector3f checkedColor) {
		this.checkedColor = checkedColor;
	}

}
