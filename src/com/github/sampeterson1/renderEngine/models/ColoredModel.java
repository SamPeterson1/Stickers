package com.github.sampeterson1.renderEngine.models;

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.loaders.ModelLoader;

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
		float[] vertexColors = new float[modelData.getNumVertices() * 3];
		
		for(ColoredVertexGroup group : colorGroups.values()) {
			Vector3f color = group.getColor();
			for(int index : group.getIndices()) {
				vertexColors[index * 3] = color.x;
				vertexColors[index * 3 + 1] = color.y;
				vertexColors[index * 3 + 2] = color.z;
			}
		}

		ModelLoader.updateAttributeData(modelData, 1, vertexColors);
	}
	
	public void setColor(Vector3f color) {
		for(ColoredVertexGroup colorGroup : colorGroups.values()) {
			colorGroup.setColor(color);
		}
	}

	public void setColor(String groupName, Vector3f color) {
		colorGroups.get(groupName).setColor(color);
	}
	
	public ModelData getData() {
		return this.modelData;
	}
}
