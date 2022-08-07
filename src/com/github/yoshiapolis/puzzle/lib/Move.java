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

package com.github.yoshiapolis.puzzle.lib;

import java.util.Objects;

public class Move {
	
	protected boolean isCubeRotation;
	protected boolean cw;
	protected int layer;
	protected Axis face;
	
	public Move(Axis face, int layer, boolean cw, boolean isCubeRotation) {
		this.face = face;
		this.layer = layer;
		this.cw = cw;
		this.isCubeRotation = isCubeRotation;
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
	
	public Axis getFace() {
		return this.face;
	}
	
	public Move getInverse() {
		return new Move(face, layer, !cw, isCubeRotation);
	}
	
	public Move transpose(Puzzle puzzle) {
		Axis newFace = puzzle.transposeAxis(face);
		return new Move(newFace, layer, cw, isCubeRotation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cw, face, isCubeRotation, layer);
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
		return cw == other.cw && face == other.face && isCubeRotation == other.isCubeRotation && layer == other.layer;
	}

	
}
