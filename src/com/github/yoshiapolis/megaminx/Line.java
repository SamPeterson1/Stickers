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

package com.github.yoshiapolis.megaminx;

public class Line {
	public float x1, y1;
	public float x2, y2;

	public Line(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public float[] intersect(Line other) {
		float m1 = (y1 - y2) / (x1 - x2);
		float m2 = (other.y1 - other.y2) / (other.x1 - other.x2);
		float xSolution = (m1 * x1 - m2 * other.x1 + other.y1 - y1) / (m1 - m2);
		float ySolution = m1 * (xSolution - x1) + y1;

		return new float[] { xSolution, ySolution };
	}
}
