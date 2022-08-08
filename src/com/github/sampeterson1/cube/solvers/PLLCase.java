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

package com.github.sampeterson1.cube.solvers;

import java.util.HashMap;

import com.github.sampeterson1.cube.pieces.Cube;
import com.github.sampeterson1.cube.util.CubeAlgorithmUtil;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;

public class PLLCase {

	private int[] edgeColors;
	private int[] cornerColors;
	private Algorithm solution;

	public PLLCase(String solution, int[] faces) {
		this.solution = CubeAlgorithmUtil.parseAlgorithm(solution);

		cornerColors = new int[4];
		for (int i = 0; i < 8; i += 2) {
			cornerColors[i / 2] = faces[i];
		}
		edgeColors = new int[4];
		for (int i = 1; i < 8; i += 2) {
			edgeColors[i / 2] = faces[i];
		}
	}

	public Algorithm getSolution() {
		return this.solution;
	}

	public boolean recognize(Cube cube, int off) {

		HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>();
		for (int i = 0; i < 4; i++) {
			colorMap.put(i, getColor(cube, (i + off) % 4));
		}

		for (int i = 0; i < 4; i++) {
			Color compareTo = colorMap.get(cornerColors[i]);
			Color actual = cube.getCorner(i).getPiece().getColor(2);

			if (compareTo != actual) {
				return false;
			}
		}

		if (cube.getSize() > 2) {
			for (int i = 0; i < 4; i++) {
				Color compareTo = colorMap.get(edgeColors[i]);
				Color actual = cube.getEdge(i).getPiece(0).getColor(1);

				if (compareTo != actual) {
					return false;
				}
			}
		}

		return true;
	}

	private Color getColor(Cube cube, int index) {
		Axis f = null;
		if (index == 0)
			f = Axis.F;
		else if (index == 1)
			f = Axis.R;
		else if (index == 2)
			f = Axis.B;
		else if (index == 3)
			f = Axis.L;

		return cube.getCenterColor(f);
	}
}
