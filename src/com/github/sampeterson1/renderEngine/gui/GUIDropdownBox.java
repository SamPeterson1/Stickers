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

import java.io.IOException;

import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
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

public class GUIDropdownBox extends GUIComponent {
	
	private static final float LEFT_JUSTIFY_PADDING = 0.01f;
	
	private Vector3f selectionColor = new Vector3f(0, 1, 0);
	private Vector3f arrowBaseColor = new Vector3f(0, 1, 0);
	private Vector3f arrowHighlightColor = new Vector3f(0.5f, 1, 0.5f);
	
	private Texture dropdownArrow;
	
	private boolean expanded;
	private boolean arrowHighlighted;
	private int hoverSelectionID = -1;
	private int selectionID = -1;
	private GUIText[] options;
	private GUIText selected;
	
	public GUIDropdownBox(String name, float x, float y, float width, float height) {
		this(null, name, x, y, width, height);
	}
	
	public GUIDropdownBox(GUIComponent parent, String name, float x, float y, float width, float height) {
		super(parent, name, x, y, width, height);
		loadTexture();
	}
	
	@Override
	public void handleEvent(Event e) {
		float mouseX = (float) e.getMouseX() / Window.getWidth();
		float mouseY = (float) e.getMouseY() / Window.getHeight();
		int eventType = e.getType();
		
		if(eventType == Event.EVENT_MOUSE_MOVE) {
			if(expanded) hoverSelectionID = getSelectionID(mouseX, mouseY);
			arrowHighlighted = mouseOverArrow(mouseX, mouseY);
		} else if(eventType == Event.EVENT_MOUSE_BUTTON_PRESS) {
			if(arrowHighlighted) {
				if(expanded) contract();
				else expand();
			} else if(expanded && hoverSelectionID >= 0) {
				selectionID = hoverSelectionID;
				selected.updateText(options[selectionID].getText());
				hoverSelectionID = -1;
				contract();
				GUIMaster.createGUIEvent(new GUIEvent(GUIEventType.DROPDOWN_SELECTED, this));
			}
		}
	}
	
	public void createOptions(String[] optionNames, Font font) {
		options = new GUIText[optionNames.length];
		float x = super.getAbsoluteX() + LEFT_JUSTIFY_PADDING;

		for(int i = 0; i < optionNames.length; i ++) {
			options[i] = createOptionText(optionNames[i], font, i, x);
		}
		
		float y = super.getAbsoluteY() - super.getAbsoluteHeight()/2 + FontUtil.getScaledLineHeight(font)/2;
		selected = new GUIText(super.getName() + "_selection", optionNames[0], font, x, y);
		selected.color = new Vector3f(0);
		selected.offsetColor = new Vector3f(0.7f, 0.7f, 0.7f);
		selected.offset = new Vector2f(0.005f, 0.005f);
		
		MeshData meshData = DropdownMeshUtil.createMesh(options.length, super.getAbsoluteWidth(), super.getAbsoluteHeight());
		super.setMesh(new Mesh(meshData, MeshType.DROPDOWN_BOX));
	}
	
	private GUIText createOptionText(String name, Font font, int optionID, float x) {
		float y = -(optionID + 1.5f) + FontUtil.getScaledLineHeight(font) / 2 / super.getAbsoluteHeight();
		
		GUIText optionText = new GUIText(this, super.getName() + "_" + name, name, font, x, y);
		optionText.setVisible(false);
		optionText.color = new Vector3f(0);
		optionText.offsetColor = new Vector3f(0.7f, 0.7f, 0.7f);
		optionText.offset = new Vector2f(0.005f, 0.005f);
		
		return optionText;
	}
	
	private void loadTexture() {
		try {
			dropdownArrow = TextureLoader.loadTexture("dropdownArrow.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean mouseOverArrow(float mouseX, float mouseY) {
		float minX = super.getAbsoluteX() + super.getAbsoluteWidth() - super.getAbsoluteHeight();
		float minY = super.getAbsoluteY() - super.getAbsoluteHeight();
		float maxX = minX + super.getAbsoluteHeight();
		float maxY = super.getAbsoluteY();
		
		return (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY);
	}
	
	private int getSelectionID(float mouseX, float mouseY) {
		float cellWidth = super.getAbsoluteWidth();
		float cellHeight = super.getAbsoluteHeight();
		float y = super.getAbsoluteY() - cellHeight;
		float x = super.getAbsoluteX();
		
		if(mouseX >= x && mouseX <= x + cellWidth && mouseY <= y && mouseY >= y - options.length * cellHeight) {
			return (int) ((y - mouseY) / cellHeight);
		}
		
		return -1;
	}
	
	private void contract() {
		expanded = false;
		for(GUIText optionText : options) {
			optionText.setVisible(false);
		}
	}
	
	private void expand() {
		expanded = true;
		for(GUIText optionText : options) {
			optionText.setVisible(true);
		}
	}
	
	public String getSelection() {
		if(selectionID >= 0) {
			return options[selectionID].getText();
		} else {
			return null;
		}
	}
	
	public Texture getTexture() {
		return this.dropdownArrow;
	}
	
	public boolean isExpanded() {
		return this.expanded;
	}
	
	public int getSelectionID() {
		return this.selectionID;
	}
	
	public int getHoverSelectionID() {
		return this.hoverSelectionID;
	}
	
	public Vector3f getArrowColor() {
		return arrowHighlighted ? arrowHighlightColor : arrowBaseColor;
	}
	
	public Vector3f getSelectionColor() {
		return this.selectionColor;
	}
}
