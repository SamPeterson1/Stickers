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

package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.display.ColorPalette;

public class PieceShader extends Shader {
	
	private static final String VERTEX_FILE = "PieceVert.glsl";
	private static final String FRAGMENT_FILE = "PieceFrag.glsl";
	
	private static final String[] UNIFORM_NAMES = {
			"projectionMatrix", "viewMatrix",
			"lightDirection", "colorPalette"
	};
	
	public PieceShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "normal");
		super.bindAttribute(2, "colorGroupID");
		super.bindAttribute(3, "transformationMatrix");
		super.bindAttribute(7, "colorIDs");
	}

	@Override
	protected String[] getAllUniformNames() {
		return UNIFORM_NAMES;
	}
	
	public void setColorPalette(ColorPalette palette) {
		super.loadVector3fList("colorPalette", palette.getColors());
	}
	
	public void setLightDirection(Vector3f direction) {
		super.loadVector3f("lightDirection", direction);
	}
	
	public void setProjectionMatrix(Matrix3D matrix) {
		super.loadMatrix("projectionMatrix", matrix);
	}
	
	public void setViewMatrix(Matrix3D matrix) {
		super.loadMatrix("viewMatrix", matrix);
	}
}
