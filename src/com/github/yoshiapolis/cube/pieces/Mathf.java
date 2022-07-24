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

package com.github.yoshiapolis.cube.pieces;

import processing.core.PMatrix3D;
import processing.core.PVector;

public class Mathf {

	public static final float PI = (float) Math.PI;
	public static final float MEGAMINX_UNIT_APOTHEM = 0.5f * sqrt(2.5f + 1.1f*sqrt(5)); 
	public static final float MEGAMINX_DIHEDRAL_ANGLE = acos(-1/sqrt(5));
	public static final float PHI = (1 + sqrt(5))/2.0f;
	public static final PVector DOWN = new PVector(0, 1, 0);
	
	public static float abs(float a) {
		return (float)Math.abs(a);
	}
	
	public static float sqrt(float a) {
		return (float)Math.sqrt(a);
	}
	
	public static float sin(float a) {
		return (float)Math.sin(a);
	}
	
	public static float cos(float a) {
		return (float)Math.cos(a);
	}
	
	public static float tan(float a) {
		return (float)Math.tan(a);
	}
	
	public static float asin(float a) {
		return (float)Math.asin(a);
	}
	
	public static float acos(float a) {
		return (float)Math.acos(a);
	}
	
	public static float atan(float a) {
		return (float)Math.atan(a);
	}
	
	public static float atan2(float a, float b) {
		return (float)Math.atan2(a, b);
	}
	
	/*
	public static PVector toSpherical(PVector cartesian) {
		float x = cartesian.x;
		float y = cartesian.y;
		float z = cartesian.z;
		
		float r = sqrt(x*x + y*y + z*z);
		float theta = atan2(x, z);
		float phi = asin(y/r);
		
		return new PVector(r, theta, phi);
	}
	*/

	public static PVector toCartesian(PVector spherical) {
		
		float r = spherical.x;
		float theta = spherical.y - PI/2;
		float phi = spherical.z;
			
		float x = r*sin(phi)*sin(theta);
		float y = r*cos(phi);
		float z = r*sin(phi)*cos(theta);
		
		return new PVector(z, y, x);
	}
	
	public static PMatrix3D getRotationTo(PVector vec) {
		
		PVector norm = vec.copy();
		norm.normalize();
		PVector axis = norm.cross(DOWN);
		axis.normalize();
		float angle = acos(norm.dot(DOWN));
		return rotateAroundAxis(axis, angle);
	}
	
	public static PMatrix3D rotateAroundAxis(PVector axis, float theta) {
		axis.normalize();
		PMatrix3D mat = new PMatrix3D();
		float cos = cos(theta);
		float sin = sin(theta);
		float x = axis.x;
		float y = axis.y;
		float z = axis.z;
		
		mat.m00 = cos + x*x*(1 - cos);
		mat.m01 = x*y*(1-cos) - z*sin;
		mat.m02 = x*z*(1-cos) + y*sin;
		mat.m10 = y*x*(1-cos) + z*sin;
		mat.m11 = cos + y*y*(1-cos);
		mat.m12 = y*z*(1-cos) - x*sin;
		mat.m20 = z*x*(1-cos) - y*sin;
		mat.m21 = z*y*(1-cos) + x*sin;
		mat.m22 = cos + z*z*(1-cos);
		
		return mat;
	}
	
}
