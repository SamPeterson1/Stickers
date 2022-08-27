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

import java.util.ArrayList;
import java.util.List;

public class Move {
	
	private boolean isCubeRotation;
	private boolean cw;
	
	private int repetitions;
	private int layer;
	
	private Axis axis;
	
	public Move(Axis axis, int layer, boolean cw, boolean isCubeRotation) {
		this.axis = axis;
		this.layer = layer;
		this.cw = cw;
		this.isCubeRotation = isCubeRotation;
		this.repetitions = 1;
	}
	
	public Move(Axis face, boolean cw, boolean isCubeRotation) {
		this(face, 0, cw, isCubeRotation);
	}

	public Move(Axis face, int layer, boolean cw) {
		this(face, layer, cw, false);
	}
	
	public Move(Axis face, boolean cw) {
		this(face, 0, cw, false);
	}
	
	public List<Move> expandRepetitions() {
		List<Move> moves = new ArrayList<Move>();
		
		for(int i = 0; i < repetitions; i ++) {
			moves.add(new Move(axis, layer, cw, isCubeRotation));
		}
		
		return moves;
	}
	
	public Move repeated(int repetitions) {
		this.repetitions = repetitions;
		return this;
	}
	
	public boolean isCubeRotation() {
		return this.isCubeRotation;
	}
	
	public boolean isCW() {
		return this.cw;
	}
	
	public boolean isCCW() {
		return !this.cw;
	}
	
	public int getLayer() {
		return this.layer;
	}
	
	public Axis getAxis() {
		return this.axis;
	}
	
	public Move getInverse() {
		return new Move(axis, layer, !cw, isCubeRotation);
	}
	
	public Move transpose(Puzzle puzzle) {
		Axis newFace = puzzle.transposeAxis(axis);
		return new Move(newFace, layer, cw, isCubeRotation);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Move other = (Move) obj;
		return cw == other.cw && axis == other.axis && isCubeRotation == other.isCubeRotation && layer == other.layer;
	}

	
}
