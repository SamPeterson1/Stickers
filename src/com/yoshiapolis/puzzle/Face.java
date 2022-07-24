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

import com.yoshiapolis.cube.pieces.Mathf;

import processing.core.PMatrix3D;
import processing.core.PVector;

public enum Face {
	
	/*
	rMat.rotateY(Mathf.PI/2);
	uMat.rotateX(Mathf.PI/2);
	lMat.rotateY(-Mathf.PI/2);	
	dMat.rotateX(-Mathf.PI/2);		
	bMat.rotateY(Mathf.PI);
	*/
	
	R(0, Color.RED, new PVector(-1, 0, 0)), 
	U(1, Color.WHITE, new PVector(0, 1, 0), true), 
	F(2, Color.GREEN, new PVector(0, 0, -1)), 
	L(3, Color.ORANGE, new PVector(1, 0, 0)), 
	D(4, Color.YELLOW, new PVector(0, -1, 0), true), 
	B(5, Color.BLUE, new PVector(0, 0, 1)),
	
	PF(0, Color.GREEN, new PVector(0, -1.0f/3, -Mathf.sqrt(8.0f/9))),
	PR(2, Color.RED, new PVector(-Mathf.sqrt(2.0f/3),  -1.0f/3, Mathf.sqrt(2.0f/9))),
	PL(1, Color.BLUE, new PVector(Mathf.sqrt(2.0f/3), -1.0f/3, Mathf.sqrt(2.0f/9))),
	PD(3, Color.YELLOW, new PVector(0, 1, 0), false, 0),
	
	M1(0, Color.WHITE, new PVector(0, 1, 0), false, 0),
	M2(0, Color.YELLOW, Mathf.toCartesian(new PVector(1, 0, Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M3(0, Color.PURPLE, Mathf.toCartesian(new PVector(1, 2*Mathf.PI/5, Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M4(0, Color.GREEN, Mathf.toCartesian(new PVector(1, 4*Mathf.PI/5,Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M5(0, Color.RED, Mathf.toCartesian(new PVector(1, 6*Mathf.PI/5, Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M6(0, Color.BLUE, Mathf.toCartesian(new PVector(1, 8*Mathf.PI/5, Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M7(0, Color.PALE_YELLOW, Mathf.toCartesian(new PVector(-1, 0, Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M8(0, Color.PINK, Mathf.toCartesian(new PVector(-1, 2*Mathf.PI/5,Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M9(0, Color.LIME_GREEN, Mathf.toCartesian(new PVector(-1, 4*Mathf.PI/5, Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M10(0, Color.ORANGE, Mathf.toCartesian(new PVector(-1, 6*Mathf.PI/5, Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M11(0, Color.LIGHT_BLUE, Mathf.toCartesian(new PVector(-1, 8*Mathf.PI/5, Mathf.PI - Mathf.MEGAMINX_DIHEDRAL_ANGLE))),
	M12(0, Color.GRAY, new PVector(0, -1, 0), false, 0);
	
	private int index;
	private Color color;
	private PVector faceNormal;
	private PMatrix3D rotationMat;
	private boolean inverted;
	private float yRot;
	
	Face(int index, Color color, PVector faceNormal) {
		this(index, color, faceNormal, false, Mathf.PI);
	}
	
	Face(int index, Color color, PVector faceNormal, boolean inverted) {
		this(index, color, faceNormal, inverted, Mathf.PI);
	}
	
	Face(int index, Color color, PVector faceNormal, boolean inverted, float yRot) {
		this.index = index;
		this.color = color;
		this.inverted = inverted;
		this.faceNormal = faceNormal;
		this.yRot = yRot;
		faceNormal.normalize();
		this.rotationMat = new PMatrix3D();
		this.rotationMat.apply(Mathf.getRotationTo(faceNormal));
		this.rotationMat.rotateY(yRot);
	}
	
	public boolean inverted() {
		return this.inverted;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public PVector getFaceNormal() {
		return this.faceNormal;
	}
	
	public PMatrix3D getMoveMat(float theta) {
		PMatrix3D moveMat = new PMatrix3D();
		moveMat.rotateY(yRot);
		moveMat.apply(Mathf.rotateAroundAxis(faceNormal, theta));
		moveMat.rotateY(-yRot);
		
		return moveMat;
	}
	
	public PMatrix3D getRotationMat() {
		return this.rotationMat;
	}
	
	public int getIndex() {
		return this.index;
	}
	
}
