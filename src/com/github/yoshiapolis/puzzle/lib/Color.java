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

import processing.core.PVector;

public enum Color {
	
	RED(255, 0, 0), GREEN(0, 255, 0), BLUE(0, 0, 255), 
	YELLOW(255, 255, 0), ORANGE(255, 125, 0), WHITE(255, 255, 255),
	PURPLE(153, 0, 255), LIME_GREEN(150, 255, 97), PINK(255, 0, 255),
	PALE_YELLOW(255, 255, 133), LIGHT_BLUE(0, 179, 255), GRAY(150, 150, 150);
	
	PVector rgb;
	
	Color(int r, int g, int b) {
		this.rgb = new PVector(r, g, b);
	}
	
	public PVector getRGB() {
		return this.rgb;
	}
	
}
