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

import com.github.sampeterson1.math.Vector3f;

//Provides some default rgb colors
public enum Color {
	
	BORDER(1f, 1f, 1f),
	
	RED(1f, 0f, 0f), GREEN(0f, 1f, 0f), BLUE(0f, 0f, 1f),
	YELLOW(1f, 1f, 0f), ORANGE(1f, 0.5f, 0f), WHITE(1f, 1f, 1f),
	PURPLE(0.5f, 0f, 1f), LIME_GREEN(0.5f, 1f, 0.4f), PINK(1f, 0f, 1f),
	PALE_YELLOW(1, 1f, 0.5f), LIGHT_BLUE(0f, 0.75f, 1f), GRAY(0.6f, 0.6f, 0.6f);

	private Vector3f rgb;
	
	private Color(float r, float g, float b) {
		this.rgb = new Vector3f(r, g, b);
	}
	
	public Vector3f getRGB() {
		return this.rgb;
	}
	
}
