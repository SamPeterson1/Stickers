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

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.window.Event;
import com.github.sampeterson1.renderEngine.window.Window;

public class GUISlider extends GUIComponent {
	
	public static final Vector2f SLIDER_HANDLE_DIMENSIONS = new Vector2f(0.007f, 0.02f);
	public static final float SLIDER_TRACK_PADDING = 0; 
	private static final float FULL_SLIDER_HEIGHT = 2 * SLIDER_HANDLE_DIMENSIONS.y;
	private static final float SLIDER_TRACK_HEIGHT = 0.005f;
	private static final float MESH_PADDING = 0.005f;
	
	private final Vector2f dimensions;
	
	private Vector3f sliderColor = new Vector3f(1);
	private Vector3f handleHighlightColor = new Vector3f(0.3f, 1f, 0.3f);
	private Vector3f handleBaseColor = new Vector3f(0, 1, 0);
	
	private boolean dragging;
	private boolean highlight;
	private float value;
	
	public GUISlider(String name, float x, float y, float width) {
		this(null, name, x, y, width);
	}
	
	public GUISlider(GUIComponent parent, String name, float x, float y, float width) {
		super(parent, name, x, y, width, FULL_SLIDER_HEIGHT);
		
		this.dimensions = new Vector2f(width/2, SLIDER_TRACK_HEIGHT/2);
		createMesh();
	}
	
	@Override
	public void handleEvent(Event e) {
		float mouseX = (float) e.getMouseX() / Window.getWidth();
		float mouseY = (float) e.getMouseY() / Window.getHeight();
		int eventType = e.getType();
		
		if(eventType == Event.EVENT_MOUSE_BUTTON_PRESS && e.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
			dragging = inBounds(mouseX, mouseY);
		} else if(eventType == Event.EVENT_MOUSE_DRAG && dragging) {
			float rawValue = (mouseX - super.getAbsoluteX()) / super.getAbsoluteWidth();
			value = Mathf.min(1, Mathf.max(0, rawValue));
			
			GUIMaster.createGUIEvent(new GUIEvent(GUIEventType.SLIDER_MOVED, this));
		} else if(eventType == Event.EVENT_MOUSE_BUTTON_RELEASE && dragging) {
			dragging = false;
		} else if(eventType == Event.EVENT_MOUSE_MOVE) {
			highlight = inBounds(mouseX, mouseY);
		}
	}
	
	private void createMesh() {
		float fullWidth = super.getAbsoluteWidth() + SLIDER_HANDLE_DIMENSIONS.x;
		float[] vertices = new float[] {
				-MESH_PADDING - SLIDER_HANDLE_DIMENSIONS.x, -MESH_PADDING,
				fullWidth + MESH_PADDING, -MESH_PADDING,
				fullWidth + MESH_PADDING, FULL_SLIDER_HEIGHT + MESH_PADDING,
				-MESH_PADDING - SLIDER_HANDLE_DIMENSIONS.x, FULL_SLIDER_HEIGHT + MESH_PADDING
		};
		
		MeshData meshData = Loader.load2DMesh(vertices, Loader.quadIndices);
		super.setMesh(new Mesh(meshData, MeshType.SLIDER));
	}
	
	private boolean inBounds(float mouseX, float mouseY) {
		float handleX = value * super.getAbsoluteWidth();
		float minX = super.getAbsoluteX() + handleX - SLIDER_HANDLE_DIMENSIONS.x;
		float maxX = minX + 2*SLIDER_HANDLE_DIMENSIONS.x;
		float minY = super.getAbsoluteY();
		float maxY = minY + 2*SLIDER_HANDLE_DIMENSIONS.y;
		
		return (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY);
	}
	
	public void setValue(float value) {
		this.value = value;
	}

	public void setSliderColor(Vector3f sliderColor) {
		this.sliderColor = sliderColor;
	}

	public void setHandleHighlightColor(Vector3f handleHighlightColor) {
		this.handleHighlightColor = handleHighlightColor;
	}

	public void setHandleBaseColor(Vector3f handleBaseColor) {
		this.handleBaseColor = handleBaseColor;
	}

	public Vector3f getSliderColor() {
		return this.sliderColor;
	}
	
	public Vector3f getHandleColor() {
		return highlight ? handleHighlightColor : handleBaseColor;
	}
	
	public Vector2f getDimensions() {
		return this.dimensions;
	}

	public float getValue() {
		return this.value;
	}

}
