/*
    PrimePuzzle Twisty Puzzle Simulator and Solver
    Copyright (C) 2022 Sam Peterson
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.github.sampeterson1.puzzle.lib;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;

public enum Axis {

	R(new Vector3f(1, 0, 0)), 
	U(new Vector3f(0, 1, 0)), 
	F(new Vector3f(0, 0, 1)), 
	L(new Vector3f(-1, 0, 0)), 
	D(new Vector3f(0, -1, 0)), 
	B(new Vector3f(0, 0, -1)),
	
	PF(new Vector3f(0, 1.0f/3, Mathf.sqrt(8.0f/9))),
	PR(new Vector3f(Mathf.sqrt(2.0f/3), 1.0f/3, -Mathf.sqrt(2.0f/9))),
	PL(new Vector3f(-Mathf.sqrt(2.0f/3), 1.0f/3, -Mathf.sqrt(2.0f/9))),
	PD(new Vector3f(0, -1, 0)),
	
	IR(new Vector3f(1, -1, 1)),
	IL(new Vector3f(-1, 1, 1)),
	IB(new Vector3f(1, 1, -1)),
	ID(new Vector3f(-1, -1, -1));
	
	private Vector3f rotationAxis;

	Axis(Vector3f rotationAxis) {
		this.rotationAxis = rotationAxis;
		rotationAxis.normalize();
	}

	public Vector3f getRotationAxis() {
		return this.rotationAxis;
	}
}
