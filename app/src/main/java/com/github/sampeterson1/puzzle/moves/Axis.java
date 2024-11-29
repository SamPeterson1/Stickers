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

package com.github.sampeterson1.puzzle.moves;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.lib.PuzzleType;

//All of the rotation axes used in every puzzle
public enum Axis {
		
	R("R", new Vector3f(1, 0, 0), Mathf.PI/2, PuzzleType.CUBE), 
	U("U", new Vector3f(0, 1, 0), Mathf.PI/2, PuzzleType.CUBE), 
	F("F", new Vector3f(0, 0, 1), Mathf.PI/2, PuzzleType.CUBE), 
	L("L", new Vector3f(-1, 0, 0), Mathf.PI/2, PuzzleType.CUBE), 
	D("D", new Vector3f(0, -1, 0), Mathf.PI/2, PuzzleType.CUBE), 
	B("B", new Vector3f(0, 0, -1), Mathf.PI/2, PuzzleType.CUBE),
	
	PF("F", new Vector3f(0, 1.0f/3, Mathf.sqrt(8.0f/9)), 2*Mathf.PI/3, PuzzleType.PYRAMINX),
	PR("R", new Vector3f(Mathf.sqrt(2.0f/3), 1.0f/3, -Mathf.sqrt(2.0f/9)), 2*Mathf.PI/3, PuzzleType.PYRAMINX),
	PL("L", new Vector3f(-Mathf.sqrt(2.0f/3), 1.0f/3, -Mathf.sqrt(2.0f/9)), 2*Mathf.PI/3, PuzzleType.PYRAMINX),
	PD("D", new Vector3f(0, -1, 0), 2*Mathf.PI/3, PuzzleType.PYRAMINX),
	
	IR("R", new Vector3f(1, -1, 1), 2*Mathf.PI/3, PuzzleType.IVY_CUBE),
	IL("L", new Vector3f(-1, 1, 1), 2*Mathf.PI/3, PuzzleType.IVY_CUBE),
	IB("B", new Vector3f(1, 1, -1), 2*Mathf.PI/3, PuzzleType.IVY_CUBE),
	ID("D", new Vector3f(-1, -1, -1), 2*Mathf.PI/3, PuzzleType.IVY_CUBE),
	
	S1("/", new Vector3f(Mathf.sin(Mathf.DEG_TO_RAD * 75), 0, Mathf.cos(Mathf.DEG_TO_RAD * 75)), Mathf.PI, PuzzleType.SQUARE1),
	SU("U", new Vector3f(0, 1, 0), Mathf.PI / 6, PuzzleType.SQUARE1),
	SD("D", new Vector3f(0, -1, 0), Mathf.PI / 6, PuzzleType.SQUARE1),
	
	SKF("F", new Vector3f(1, 1, 1), 2 * Mathf.PI / 3, PuzzleType.SKEWB),
	SKB("B", new Vector3f(-1, 1, -1), 2 * Mathf.PI / 3, PuzzleType.SKEWB),
	SKR("R", new Vector3f(1, -1, -1), 2 * Mathf.PI / 3, PuzzleType.SKEWB),
	SKL("L", new Vector3f(-1, -1, 1), 2 * Mathf.PI / 3, PuzzleType.SKEWB);

	private PuzzleType puzzleType;
	private Vector3f rotationAxis;
	private float rotationAmount;
	private String name;

	Axis(String name, Vector3f rotationAxis, float rotationAmount, PuzzleType puzzleType) {
		this.name = name;
		this.rotationAxis = rotationAxis;
		this.rotationAmount = rotationAmount;
		this.puzzleType = puzzleType;
		UniversalAlgorithmParser.addAxis(this);
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

	public PuzzleType getPuzzleType() {
		return this.puzzleType;
	}

}
