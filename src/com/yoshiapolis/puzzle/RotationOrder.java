/*
    PrimePuzzle Twisty Puzzle Simulator
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

package com.yoshiapolis.puzzle;

import processing.core.PMatrix3D;
import processing.core.PVector;

public enum RotationOrder {
	
	XYZ(0, 1, 2), YXZ(1, 0, 2);
	
	private int x, y, z;
	
	RotationOrder(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	void rotate(PMatrix3D mat, PVector vec, int sequence) {
		if(x == sequence) {
			mat.rotateX(vec.x);
		} else if(y == sequence) {
			mat.rotateY(vec.y);
		} else if(z == sequence) {
			mat.rotateZ(vec.z);
		}
	}
	
	PMatrix3D getMatrix(PVector vec) {
		PMatrix3D retVal = new PMatrix3D();
		for(int i = 0; i < 3; i ++) {
			rotate(retVal, vec, i);
		}
		
		return retVal;
	}
}
