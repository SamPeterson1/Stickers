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

import com.github.sampeterson1.renderEngine.loaders.Loader;

public class TexturedMesh extends Mesh {
	
	private Texture texture;
	
	public TexturedMesh(MeshData data, String texture) {
		super(data);
		this.texture = Loader.loadTexture(texture);
	}
	
	public int getTextureID() {
		return this.texture.getID();
	}
	
}
