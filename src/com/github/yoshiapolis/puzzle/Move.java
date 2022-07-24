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

package com.github.yoshiapolis.puzzle;

public class Move {
	
	protected boolean isCubeRotation;
	protected boolean cw;
	protected int startLayer;
	protected int endLayer;
	protected Face face;
	
	public Move(Face face, int startLayer, int endLayer, boolean cw, boolean isCubeRotation) {
		this.face = face;
		this.startLayer = startLayer;
		this.endLayer = endLayer;
		this.cw = cw;
		this.isCubeRotation = isCubeRotation;
	}
	
	public Move(Face face, int layer, boolean cw, boolean isCubeRotation) {
		this(face, layer, layer, cw, isCubeRotation);
	}
	
	public Move(Face face, boolean cw, boolean isCubeRotation) {
		this(face, 0, 0, cw, isCubeRotation);
	}
	
	public Move(Face face, int startLayer, int endLayer, boolean cw) {
		this(face, startLayer, endLayer, cw, false);
	}
	
	public Move(Face face, int layer, boolean cw) {
		this(face, layer, layer, cw, false);
	}
	
	public Move(Face face, boolean cw) {
		this(face, 0, 0, cw, false);
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
	
	public int getStartLayer() {
		return this.startLayer;
	}
	
	public int getEndLayer() {
		return this.endLayer;
	}
	
	public int getLayer() {
		return this.startLayer;
	}
	
	public Face getFace() {
		return this.face;
	}
	
	public Move getInverse() {
		return new Move(face, startLayer, endLayer, !cw, isCubeRotation);
	}
	
	public Move transpose(Puzzle puzzle) {
		Face newFace = puzzle.transposeFace(face);
		return new Move(newFace, startLayer, endLayer, cw, isCubeRotation);
	}
	
}
