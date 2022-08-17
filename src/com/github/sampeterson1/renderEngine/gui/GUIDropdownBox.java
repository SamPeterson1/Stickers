package com.github.sampeterson1.renderEngine.gui;

import java.io.IOException;

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
import com.github.sampeterson1.renderEngine.text.Text;
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
	private Text[] options;
	private Text selected;
	
	public GUIDropdownBox(String name, float x, float y, float width, float height) {
		super(name, x, y, width, height);
		try {
			dropdownArrow = TextureLoader.loadTexture("dropdownArrow.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private boolean mouseOverArrow(float mouseX, float mouseY) {
		float minX = super.getX() + super.getWidth() - super.getHeight();
		float minY = super.getY() - super.getHeight();
		float maxX = minX + super.getHeight();
		float maxY = super.getY();
		
		return (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY);
	}
	
	private int getSelectionID(float mouseX, float mouseY) {
		float cellWidth = super.getWidth();
		float cellHeight = super.getHeight();
		float y = super.getY() - cellHeight;
		float x = super.getX();
		
		if(mouseX >= x && mouseX <= x + cellWidth && mouseY <= y && mouseY >= y - options.length * cellHeight) {
			return (int) ((y - mouseY) / cellHeight);
		}
		
		return -1;
	}
	
	public void createOptions(String[] optionNames, Font font) {
		
		options = new Text[optionNames.length];
		float x = super.getX() + LEFT_JUSTIFY_PADDING;

		for(int i = 0; i < optionNames.length; i ++) {
			String name = optionNames[i];
			float y = super.getY() - (i + 1.5f) * super.getHeight() + FontUtil.getScaledLineHeight(font)/2;
			Text optionText = new Text(super.getName() + "_" + name, name, font, x, y);
			optionText.setVisible(false);
			optionText.color = new Vector3f(0);
			optionText.offsetColor = new Vector3f(0.7f, 0.7f, 0.7f);
			optionText.offset = new Vector2f(0.005f, 0.005f);
			options[i] = optionText;
		}
		
		float y = super.getY() - super.getHeight()/2 + FontUtil.getScaledLineHeight(font)/2;
		selected = new Text(super.getName() + "_selection", optionNames[0], font, x, y);
		selected.color = new Vector3f(0);
		selected.offsetColor = new Vector3f(0.7f, 0.7f, 0.7f);
		selected.offset = new Vector2f(0.005f, 0.005f);
		
		MeshData meshData = createMesh(options.length);
		super.setMesh(new Mesh(meshData, MeshType.DROPDOWN_BOX));
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
				GUIMaster.createEvent(new GUIEvent(GUIEventType.DROPDOWN_SELECTED, this));
			}
		}
	}
	
	private void contract() {
		expanded = false;
		for(Text optionText : options) {
			optionText.setVisible(false);
		}
	}
	
	private void expand() {
		expanded = true;
		for(Text optionText : options) {
			optionText.setVisible(true);
		}
	}

	private MeshData createMesh(int numOptions) {
		float[] vertices = new float[(numOptions + 2) * 8];
		float[] texCoords = new float[vertices.length];
		int[] optionIDs = new int[vertices.length / 2];
		int[] indices = new int[(numOptions + 2) * 6];
		float width = super.getWidth();
		float height = super.getHeight();
		
		for(int i = 0; i < numOptions + 1; i ++) {
			float maxX = (i == 0) ? (width - height) : width;
			float yOff = -i * height;
			
			vertices[8*i] = 0;
			vertices[8*i + 1] = yOff;
			
			vertices[8*i + 2] = maxX;
			vertices[8*i + 3] = yOff;
			
			vertices[8*i + 4] = maxX;
			vertices[8*i + 5] = yOff - height;
			
			vertices[8*i + 6] = 0;
			vertices[8*i + 7] = yOff - height;
			
			indices[6*i] = 4*i;
			indices[6*i + 1] = 4*i + 2;
			indices[6*i + 2] = 4*i + 1;
			
			indices[6*i + 3] = 4*i + 0;
			indices[6*i + 4] = 4*i + 3;
			indices[6*i + 5] = 4*i + 2;
			
			for(int j = 0; j < 4; j ++) {
				optionIDs[4*i + j] = i - 1;
			}
		}
		
		float texPadding = 0.2f;
		int boxVertIndex = (numOptions + 1) * 8;
		vertices[boxVertIndex] = width - height;
		vertices[boxVertIndex + 1] = 0;
		texCoords[boxVertIndex] = -texPadding;
		texCoords[boxVertIndex + 1] = -texPadding;
		
		vertices[boxVertIndex + 2] = width;
		vertices[boxVertIndex + 3] = 0;
		texCoords[boxVertIndex + 2] = 1 + texPadding;
		texCoords[boxVertIndex + 3] = -texPadding;
		
		vertices[boxVertIndex + 4] = width;
		vertices[boxVertIndex + 5] = -height;
		texCoords[boxVertIndex + 4] = 1 + texPadding;
		texCoords[boxVertIndex + 5] = 1 + texPadding;
		
		vertices[boxVertIndex + 6] = width - height;
		vertices[boxVertIndex + 7] = -height;
		texCoords[boxVertIndex + 6] = -texPadding;
		texCoords[boxVertIndex + 7] = 1 + texPadding;
		
		for(int i = 0; i < 4; i ++) {
			optionIDs[boxVertIndex / 2 + i] = -1;
		}
		
		int indicesIndex = (numOptions + 1) * 6;
		indices[indicesIndex++] = boxVertIndex / 2;
		indices[indicesIndex++] = boxVertIndex / 2 + 2;
		indices[indicesIndex++] = boxVertIndex / 2 + 1;
		
		indices[indicesIndex++] = boxVertIndex / 2;
		indices[indicesIndex++] = boxVertIndex / 2 + 3;
		indices[indicesIndex++] = boxVertIndex / 2 + 2;
		
		for(int i = 0; i < indices.length; i ++) {
			System.out.print(indices[i] + ", ");
		}
		System.out.println();
		
		return Loader.loadDropdownMesh(vertices, texCoords, indices, optionIDs);
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
