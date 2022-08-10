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

package com.github.sampeterson1.puzzle.lib;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Vector3f;

public enum Axis {

	R("R", new Vector3f(1, 0, 0), Mathf.PI/2), 
	U("U", new Vector3f(0, 1, 0), Mathf.PI/2), 
	F("F", new Vector3f(0, 0, 1), Mathf.PI/2), 
	L("L", new Vector3f(-1, 0, 0), Mathf.PI/2), 
	D("D", new Vector3f(0, -1, 0), Mathf.PI/2), 
	B("B", new Vector3f(0, 0, -1), Mathf.PI/2),
	
	PF("PF", new Vector3f(0, 1.0f/3, Mathf.sqrt(8.0f/9)), 2*Mathf.PI/3),
	PR("PR", new Vector3f(Mathf.sqrt(2.0f/3), 1.0f/3, -Mathf.sqrt(2.0f/9)), 2*Mathf.PI/3),
	PL("PL", new Vector3f(-Mathf.sqrt(2.0f/3), 1.0f/3, -Mathf.sqrt(2.0f/9)), 2*Mathf.PI/3),
	PD("PD", new Vector3f(0, -1, 0), 2*Mathf.PI/3),
	
	IR("IR", new Vector3f(1, -1, 1), 2*Mathf.PI/3),
	IL("IL", new Vector3f(-1, 1, 1), 2*Mathf.PI/3),
	IB("IB", new Vector3f(1, 1, -1), 2*Mathf.PI/3),
	ID("ID", new Vector3f(-1, -1, -1), 2*Mathf.PI/3);
	
	private Vector3f rotationAxis;
	private float rotationAmount;
	private String name;

	Axis(String name, Vector3f rotationAxis, float rotationAmount) {
		this.name = name;
		this.rotationAxis = rotationAxis;
		this.rotationAmount = rotationAmount;
		Algorithm.addAxis(this);
		rotationAxis.normalize();
	}

	public String getName() {
		return this.name;
	}
	
	public float getRotationAmount() {
		return this.rotationAmount;
	}
	
	public Vector3f getRotationAxis() {
		return this.rotationAxis;
	}
}
