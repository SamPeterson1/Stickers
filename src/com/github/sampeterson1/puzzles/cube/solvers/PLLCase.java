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

package com.github.sampeterson1.puzzles.cube.solvers;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzles.cube.meta.Cube;
import com.github.sampeterson1.puzzles.cube.util.CubeAlgorithmUtil;

//A representation of a full PLL case in the CFOP method
public class PLLCase {

	/*
	 * Where each edge should be in counterclockwise order starting from the edge at position 0.
	 * We use the color at index 1 to determine which face it belongs to.
	 */
	private Axis[] edgeLocations;
	
	/*
	 * Where each corner should be in counterclockwise order starting from the corner at position 0.
	 * We use the color at index 2 to determine which face it belongs to.
	 */
	private Axis[] cornerLocations;
	
	private Algorithm solution;
	
	/*
	 * The locations array is interleaved with edge and corner positions, starting with the corner at position 0.
	 * The array must be in the following order: 
	 * corner position 0, edge position 0, corner position 1, edge position 1, corner position 2, edge position 2... etc.
	 */
	public PLLCase(String solution, Axis[] locations) {
		this.solution = CubeAlgorithmUtil.parseAlgorithm(solution);

		cornerLocations = new Axis[4];
		for (int i = 0; i < 8; i += 2) {
			cornerLocations[i / 2] = locations[i];
		}
		edgeLocations = new Axis[4];
		for (int i = 1; i < 8; i += 2) {
			edgeLocations[i / 2] = locations[i];
		}
	}

	public Algorithm getSolution() {
		return this.solution;
	}

	//Return true if the cube matches this case
	public boolean recognize(Cube cube) {
		//Check the permutations of the corners
		for (int i = 0; i < 4; i++) {
			Color compareTo = cube.getSolveColor(cornerLocations[i]);
			Color actual = cube.getCorner(i).getPiece().getColor(2);

			if (compareTo != actual) return false;
		}

		//Check the permutations of the edges, but only if the cube has edges (size > 2)
		if (cube.getSize() > 2) {
			for (int i = 0; i < 4; i++) {
				Color compareTo = cube.getSolveColor(edgeLocations[i]);
				Color actual = cube.getEdge(i).getPiece(0).getColor(1);

				if (compareTo != actual) return false;
			}
		}

		return true;
	}

}
