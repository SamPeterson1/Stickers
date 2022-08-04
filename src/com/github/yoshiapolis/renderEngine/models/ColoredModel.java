package com.github.yoshiapolis.renderEngine.models;

import java.util.HashMap;
import java.util.Map;

import com.github.yoshiapolis.renderEngine.loaders.ModelLoader;

public class ColoredModel {
	
	private ModelData modelData;
	private Map<String, ColoredVertexGroup> colorGroups;
	
	public ColoredModel(ModelData modelData) {
		this.colorGroups = new HashMap<String, ColoredVertexGroup>();
		this.modelData = modelData;
	}
	
	public void addColorGroup(ColoredVertexGroup group) {
		colorGroups.put(group.getName(), group);
	}
	
	public void prepareColors() {
		float[] texCoords = new float[modelData.getNumVertices() * 2];
		
		for(ColoredVertexGroup group : colorGroups.values()) {
			float[] baseColorPosition = ColorPalette.getTextureCoords(group.getBaseColor());
			for(int index : group.getBaseIndices()) {
				texCoords[2 * index] = baseColorPosition[0];
				texCoords[2 * index + 1] = baseColorPosition[1];
			}
			
			float[] accentColorPosition = ColorPalette.getTextureCoords(group.getAccentColor());
			for(int index : group.getAccentIndices()) {
				texCoords[2 * index] = accentColorPosition[0];
				texCoords[2 * index + 1] = accentColorPosition[1];
			}
		}

		ModelLoader.updateAttributeData(modelData, 1, texCoords);
	}
	
	public void setBaseColor(int colorID) {
		for(ColoredVertexGroup colorGroup : colorGroups.values()) {
			colorGroup.setBaseColor(colorID);
		}
	}
	
	public void setAccentColor(int colorID) {
		for(ColoredVertexGroup colorGroup : colorGroups.values()) {
			colorGroup.setAccentColor(colorID);
		}
	}
	
	public void setAccentColor(String groupName, int colorID) {
		colorGroups.get(groupName).setAccentColor(colorID);
	}

	public void setBaseColor(String groupName, int colorID) {
		colorGroups.get(groupName).setBaseColor(colorID);
	}
	
	public ModelData getData() {
		return this.modelData;
	}
}
