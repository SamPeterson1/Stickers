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

package com.github.sampeterson1.renderEngine.rendering;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.models.ColoredModel;

public class Entity {
	
	private ColoredModel model;
	private Matrix3D transformationMatrix;
	
	public Entity(ColoredModel model) {
		this.model = model;
		this.transformationMatrix = new Matrix3D();
	}
	
	public Entity(ColoredModel model, Matrix3D transformationMatrix) {
		this.model = model;
		this.transformationMatrix = transformationMatrix;
	}
	
	public ColoredModel getModel() {
		return this.model;
	}
	
	public Matrix3D getTransformationMatrix() {
		return this.transformationMatrix;
	}
	
	public void setTransformationMatrix(Matrix3D transformationMatrix) {
		this.transformationMatrix = transformationMatrix;
	}
}
