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

package com.github.sampeterson1.math;

public class Mathf {

	public static final float PI = (float) Math.PI;
	public static final float DEG_TO_RAD = PI / 180.0f;
	public static final float RAD_TO_DEG = 180.0f / PI;
	public static final float MEGAMINX_UNIT_APOTHEM = 0.5f * sqrt(2.5f + 1.1f * sqrt(5));
	public static final float MEGAMINX_DIHEDRAL_ANGLE = acos(-1 / sqrt(5));
	public static final float PHI = (1 + sqrt(5)) / 2.0f;
	
	public static float random(int a, int b) {
		return (float) Math.random() * (b - a) + a;
	}
	
	public static float random(float a, float b) {
		return (float) Math.random() * (b - a) + a;
	}
	
	public static float min(float a, float b) {
		return Math.min(a, b);
	}
	
	public static float max(float a, float b) {
		return Math.max(a, b);
	}
	
	public static float abs(float a) {
		return (float) Math.abs(a);
	}

	public static float acos(float a) {
		return (float) Math.acos(a);
	}

	public static float asin(float a) {
		return (float) Math.asin(a);
	}

	public static float atan(float a) {
		return (float) Math.atan(a);
	}

	public static float atan2(float a, float b) {
		return (float) Math.atan2(a, b);
	}

	public static float cos(float a) {
		return (float) Math.cos(a);
	}

	public static float sin(float a) {
		return (float) Math.sin(a);
	}

	public static float sqrt(float a) {
		return (float) Math.sqrt(a);
	}

	public static float tan(float a) {
		return (float) Math.tan(a);
	}
	
}
