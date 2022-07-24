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

import java.util.Objects;

import com.github.yoshiapolis.puzzle.Face;

public class OrderedFacePair {
	
	Face a;
	Face b;
	
	public OrderedFacePair(Face a, Face b) {
		this.a = a;
		this.b = b;
	}
	
	public OrderedFacePair getInverse() {
		return new OrderedFacePair(b, a);
	}
	
	public Face getFaceA() {
		return this.a;
	}
	
	public Face getFaceB() {
		return this.b;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderedFacePair other = (OrderedFacePair) obj;
		return a == other.a && b == other.b;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}
	
}
