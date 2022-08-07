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

package com.github.yoshiapolis.cube.util;

import java.util.Objects;

import com.github.yoshiapolis.puzzle.lib.Axis;

public class OrderedFacePair {

	Axis a;
	Axis b;

	public OrderedFacePair(Axis a, Axis b) {
		this.a = a;
		this.b = b;
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

	public Axis getFaceA() {
		return this.a;
	}

	public Axis getFaceB() {
		return this.b;
	}

	public OrderedFacePair getInverse() {
		return new OrderedFacePair(b, a);
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}

}
