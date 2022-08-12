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

public class ColoredMesh extends Mesh {
	
	private int currentID = 0;
	private Map<String, Integer> colorGroupIDs;
	
	public ColoredMesh(MeshData meshData) {
		super(meshData);
		this.colorGroupIDs = new HashMap<String, Integer>();
	}
	
	public void addColorGroup(String name) {
		colorGroupIDs.put(name, currentID++);
	}

	public int getColorGroupID(String name) {
		return this.colorGroupIDs.get(name);
	}
	
	public int getNumColorGroups() {
		return this.currentID;
	}
}
