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

public class ModelData {
	
	private int vaoID;
	private int[] vboIDs;
	private int numIndices;
	private int numVertices;
	
	public ModelData(int vaoID, int[] vboIDs, int numVertices, int numIndices) {
		this.vaoID = vaoID;
		this.vboIDs = vboIDs;
		this.numIndices = numIndices;
		this.numVertices = numVertices;
	}
	
	public int getVboID(int attributeID) {
		return this.vboIDs[attributeID];
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getNumIndices() {
		return numIndices;
	}

	public int getNumVertices() {
		return numVertices;
	}
	
}
