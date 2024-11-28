/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
	
	@Override
	public void handleEvent(Event e) {
		float mouseX = (float) e.getMouseX() / Window.getWidth();
		float mouseY = (float) e.getMouseY() / Window.getHeight();
		
		int eventType = e.getType();
		if(eventType == Event.EVENT_MOUSE_BUTTON_PRESS && e.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
			if(inBounds(mouseX, mouseY)) 
				pressButton();
		} else if(eventType == Event.EVENT_MOUSE_BUTTON_RELEASE && pressed) {
			releaseButton();
		} else if(eventType == Event.EVENT_MOUSE_MOVE) {
			highlighted = inBounds(mouseX, mouseY);
		}
	}
	
	private void releaseButton() {
		float aspect = (float) Window.getWidth() / Window.getHeight();
		
		super.setAbsoluteX(super.getAbsoluteX() + BUTTON_SHADOW_OFFSET / aspect);
		super.setAbsoluteY(super.getAbsoluteY() - BUTTON_SHADOW_OFFSET);
		
		pressed = false;
		
		GUIMaster.createGUIEvent(new GUIEvent(GUIEventType.BUTTON_RELEASE, this));
	}
	
	private void pressButton() {
		float aspect = (float) Window.getWidth() / Window.getHeight();
		
		super.setAbsoluteX(super.getAbsoluteX() - BUTTON_SHADOW_OFFSET / aspect);
		super.setAbsoluteY(super.getAbsoluteY() + BUTTON_SHADOW_OFFSET);
		
		pressed = true;
		GUIMaster.createGUIEvent(new GUIEvent(GUIEventType.BUTTON_PRESS, this));
	}
	
	private boolean inBounds(float mouseX, float mouseY) {
		float minX = super.getAbsoluteX();
		float minY = super.getAbsoluteY();
		float maxX = minX + super.getAbsoluteWidth();
		float maxY = minY + super.getAbsoluteHeight();
		return (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY);
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
		
		MeshData meshData = Loader.load2DMesh(vertices, Loader.quadIndices);
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

}
