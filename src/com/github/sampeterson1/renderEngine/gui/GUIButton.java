package com.github.sampeterson1.renderEngine.gui;

import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.text.Text;
import com.github.sampeterson1.renderEngine.window.Event;
import com.github.sampeterson1.renderEngine.window.Window;

public class GUIButton extends GUIComponent {

	private static final float[] quadVertices = {
			-1.0f, -1.0f,
			1.0f, -1.0f,
			1.0f, 1.0f,
			-1.0f, 1.0f
	};
	
	private static final int[] quadIndices = {
			0, 1, 2, 
			2, 3, 0
	};

	public float shadowOffset = 0.006f;
	public Vector3f shadowColor = new Vector3f(0);
	public Vector3f baseColor;
	public Vector3f highlightColor;
	private Vector2f centerPos;
	private Vector2f dim;
	
	private boolean pressed;
	private boolean highlighted;
	
	private Text labelText;
	
	public GUIButton(String name, float x, float y, float width, float height) {
		super(name, x, y, width, height);
		MeshData meshData = Loader.load2DMesh(quadVertices, quadIndices);
		super.setMesh(new Mesh(meshData, MeshType.BUTTON));
		this.dim = new Vector2f(width / 2, height / 2);
		this.centerPos = new Vector2f(x + dim.x, y + dim.y);
	}
	
	public void createLabel(String label) {
		if(labelText == null) {
			
		} else {
			System.err.println("Label already exists!");
		}
	}
	
	public boolean isPressed() {
		return this.pressed;
	}
	
	private boolean inBounds(float mouseX, float mouseY) {
		float minX = centerPos.x - dim.x;
		float minY = centerPos.y - dim.y;
		float maxX = centerPos.x + dim.x;
		float maxY = centerPos.y + dim.y;
		return (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY);
	}
	
	public Vector3f getColor() {
		return highlighted ? highlightColor : baseColor;
	}
	
	public Vector2f getDim() {
		return this.dim;
	}
	
	public Vector2f getCenterPos() {
		return this.centerPos;
	}
	
	@Override
	public void handleEvent(Event e) {
		float mouseX = (float) e.getMouseX() / Window.getWidth();
		float mouseY = (float) e.getMouseY() / Window.getHeight();
		float aspect =(float) Window.getWidth() / Window.getHeight();
		int eventType = e.getType();
		if(eventType == Event.EVENT_MOUSE_BUTTON_PRESS && e.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
			if(inBounds(mouseX, mouseY)) {
				float x = super.getX() - shadowOffset / aspect;
				float y = super.getY() + shadowOffset;
				super.setX(x);
				super.setY(y);
				pressed = true;
				GUIMaster.createEvent(new GUIEvent(GUIEventType.BUTTON_PRESS, this));
			}
		} else if(eventType == Event.EVENT_MOUSE_BUTTON_RELEASE && pressed) {
			float x = super.getX() + shadowOffset / aspect;
			float y = super.getY() - shadowOffset;
			super.setX(x);
			super.setY(y);
			highlighted = inBounds(mouseX, mouseY);
			pressed = false;
			GUIMaster.createEvent(new GUIEvent(GUIEventType.BUTTON_RELEASE, this));
		} else if(eventType == Event.EVENT_MOUSE_MOVE) {
			highlighted = inBounds(mouseX, mouseY);
		}
	}
}
