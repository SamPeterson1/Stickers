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
