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

public class StaticShader extends Shader {
	
	private static final String VERTEX_FILE = "vertex.glsl";
	private static final String FRAGMENT_FILE = "frag.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightDirection;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "vertColor");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightDirection = super.getUniformLocation("lightDirection");
	}
	
	public void setLightDirection(Vector3f direction) {
		super.loadVector3f(location_lightDirection, direction);
	}
	
	public void setTransformationMatrix(Matrix3D matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void setProjectionMatrix(Matrix3D matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void setViewMatrix(Matrix3D matrix) {
		super.loadMatrix(location_viewMatrix, matrix);
	}
}
