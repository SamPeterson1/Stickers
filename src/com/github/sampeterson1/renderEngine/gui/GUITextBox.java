package com.github.sampeterson1.renderEngine.gui;

import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.loaders.TextureLoader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.models.Texture;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.text.Font;
import com.github.sampeterson1.renderEngine.text.FontUtil;
import com.github.sampeterson1.renderEngine.text.GUIText;
import com.github.sampeterson1.renderEngine.window.Event;
import com.github.sampeterson1.renderEngine.window.Window;

public class GUITextBox extends GUIComponent {

	private static final int[] quadIndices = {
			0, 1, 2, 
			2, 3, 0
	};

	private static final float MESH_PADDING = 0.005f;
	private static final float TEXT_PADDING = 0.007f;
	private static final float TEXTBOX_HEIGHT = 0.05f;
	private static final float CURSOR_OFFSET = 0.003f;
	private static final int CURSOR_BLINK_INTERVAL = 500;
	
	private static final Vector3f textBackgroundColor = new Vector3f(0.5f);
	private static final Vector2f textBackgroundOffset = new Vector2f(0.006f, 0.006f);
	private static final Vector3f textColor = new Vector3f(0.5f);
	private static final Vector3f fillColor = new Vector3f(0.8f);
	private static final Vector3f highlightedFillColor = new Vector3f(1);
	
	private static Texture cursorTexture;
	
	private boolean highlighted = true;
	private boolean selected = false;
	
	private Vector2f dimensions;
	private Vector3f borderColor = new Vector3f(0, 1, 0);
	private GUIText text;

	private int cursorOffset;
	private float cursorPositionBeforeDelete = Float.MIN_VALUE;
	
	private long lastTypeTime;
	private long startTime;
	
	public GUITextBox(String name, Font font, float x, float y, float width) {
		this(null, name, font, x, y, width);
	}
	
	public GUITextBox(GUIComponent parent, String name, Font font, float x, float y, float width) {
		super(parent, name, x, y, width, 0);
		super.setAbsoluteHeight(TEXTBOX_HEIGHT);
		this.dimensions = new Vector2f(super.getAbsoluteWidth() / 2, TEXTBOX_HEIGHT / 2);
		float textY = (TEXTBOX_HEIGHT + FontUtil.getScaledLineHeight(font)) / 2 / super.getAbsoluteHeight();
		float textX = TEXT_PADDING / super.getAbsoluteWidth();
		this.text = new GUIText(this, name + "_text", "", font, textX, textY);
		this.text.color = textColor;
		this.text.blending = 0.3f;
		this.text.thickness = 0.3f;
		this.text.minPosition = new Vector2f(super.getAbsoluteX() + TEXT_PADDING, 0);
		this.text.maxPosition = new Vector2f(super.getAbsoluteX() + super.getAbsoluteWidth() - TEXT_PADDING, 1);
		this.lastTypeTime = System.currentTimeMillis();
		this.startTime = System.currentTimeMillis();

		if(cursorTexture == null) {
			try {
				cursorTexture = TextureLoader.loadTexture("textCursor.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		MeshData meshData = Loader.load2DMesh(createVertices(width), quadIndices);
		super.setMesh(new Mesh(meshData, MeshType.TEXT_BOX));
	}
	
	private boolean inBounds(float mouseX, float mouseY) {
		float minX = super.getAbsoluteX();
		float minY = super.getAbsoluteY();
		float maxX = minX + super.getAbsoluteWidth();
		float maxY = minY + super.getAbsoluteHeight();
		
		return mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY;
	}

	@Override
	public void handleEvent(Event e) {
		float mouseX = (float) e.getMouseX() / Window.getWidth();
		float mouseY = (float) e.getMouseY() / Window.getHeight();
		int eventType = e.getType();
		String textString = text.getText();
		
		if(eventType == Event.EVENT_MOUSE_MOVE) {
			highlighted = inBounds(mouseX, mouseY);
		} else if(eventType == Event.EVENT_MOUSE_BUTTON_PRESS && e.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
			if(inBounds(mouseX, mouseY)) {
				selected = true;
			}
		} else if((eventType == Event.EVENT_KEY_PRESS || eventType == Event.EVENT_KEY_REPEAT) && selected) {
			char keyChar = e.getKeyChar();
			int keyCode = e.getKeyCode();
			if(keyChar != 0) {
				lastTypeTime = System.currentTimeMillis();
				textString = insertCharacter(textString, cursorOffset, keyChar);
				text.updateText(textString);
				cursorOffset ++;
			} else if(keyCode == GLFW.GLFW_KEY_BACKSPACE) {
				lastTypeTime = System.currentTimeMillis();
				if(textString.length() > 0 && cursorOffset > 0) {
					cursorPositionBeforeDelete = getRawCursorPosition();
					textString = deleteCharacter(textString, cursorOffset - 1);
					text.updateText(textString);
					cursorOffset --;
				}
			} else if(keyCode == GLFW.GLFW_KEY_ENTER) {
				selected = false;
				GUIMaster.createEvent(new GUIEvent(GUIEventType.TEXT_BOX_UPDATE, this));
			} else if(keyCode == GLFW.GLFW_KEY_LEFT) {
				lastTypeTime = System.currentTimeMillis();
				if(cursorOffset > 0) {
					cursorOffset --;
				}
			} else if(keyCode == GLFW.GLFW_KEY_RIGHT) {
				lastTypeTime = System.currentTimeMillis();
				if(cursorOffset < textString.length()) {
					cursorOffset ++;
				}
			}
		}
	}
	
	private String insertCharacter(String string, int index, char c) {
		String left = string.substring(0, index);
		String right = string.substring(index);
		
		return left + c + right;
	}
	
	private String deleteCharacter(String string, int index) {
		return string.substring(0, index) + string.substring(index + 1);
	}
	
	private float[] createVertices(float width) {
		return new float[] {
				-MESH_PADDING, -MESH_PADDING,
				width + MESH_PADDING, -MESH_PADDING,
				width + MESH_PADDING, TEXTBOX_HEIGHT + MESH_PADDING,
				-MESH_PADDING, TEXTBOX_HEIGHT + MESH_PADDING
		};
	}
	
	private void clampCursor() {
		float rawPosition = getRawCursorPosition();
		float maxPosition = super.getAbsoluteWidth() - TEXT_PADDING;
		float minPosition = TEXT_PADDING;
		
		if(cursorPositionBeforeDelete > Float.MIN_VALUE) {
			float width = FontUtil.getWidth(text.getFont(), text.getText());
			if(text.getAbsoluteX() - super.getAbsoluteX() < minPosition && text.getAbsoluteX() + width - super.getAbsoluteX() < maxPosition) {
				text.setAbsoluteX(text.getAbsoluteX() - rawPosition + cursorPositionBeforeDelete);
			}
			cursorPositionBeforeDelete = Float.MIN_VALUE;
		} else {
			if(rawPosition > maxPosition) {
				text.setAbsoluteX(text.getAbsoluteX() - rawPosition + maxPosition);
			} else if(rawPosition < minPosition) {
				text.setAbsoluteX(text.getAbsoluteX() - rawPosition + minPosition);
			}
		}
	}
	
	private float getRawCursorPosition() {
		String behindCursorText = text.getText().substring(0, cursorOffset);
		float behindCursorWidth = FontUtil.getWidth(text.getFont(), behindCursorText);
		return (behindCursorWidth + text.getAbsoluteX() - super.getAbsoluteX() + CURSOR_OFFSET);
	}
	
	private boolean cursorBlink() {
		long millis = System.currentTimeMillis();
		
		int sinceLastKeystroke = (int) (millis - lastTypeTime);
		if(sinceLastKeystroke < CURSOR_BLINK_INTERVAL) return true;
		
		int intervals = (int) ((millis - startTime) / CURSOR_BLINK_INTERVAL);
		return intervals % 2 == 0;
	}
	
	public String getString() {
		return this.text.getText();
	}
	
	public float getCursorHeight() {
		return FontUtil.getScaledLineHeight(text.getFont());
	}
	
	public float getCursorPosition() {
		if(selected && cursorBlink()) {
			clampCursor();
			return getRawCursorPosition() / (super.getAbsoluteWidth() + 2*MESH_PADDING);
		}
		
		return -1;
	}
	
	public Vector3f getFillColor() {
		return (highlighted || selected) ? highlightedFillColor : fillColor;
	}
	
	public Vector2f getDimensions() {
		return this.dimensions;
	}
	
	public Vector3f getBorderColor() {
		return this.borderColor;
	}
	
	public void setBorderColor(Vector3f borderColor) {
		this.borderColor = borderColor;
	}
	
	public void updateText(String text) {
		this.text.updateText(text);
	}
	
	public static Texture getTexture() {
		return cursorTexture;
	}
	
}
