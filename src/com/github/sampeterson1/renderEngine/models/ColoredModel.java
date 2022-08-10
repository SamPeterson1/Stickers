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
