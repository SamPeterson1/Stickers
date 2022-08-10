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

import com.github.sampeterson1.cube.pieces.Cube;
import com.github.sampeterson1.cube.util.CubeAlgorithmUtil;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;

//This class represents a case in the OLL step of the CFOP method
public class OLLCase {

	private Algorithm solution;
	
	//The index of the top face's color on each edge and corner in counterclockwise order starting at position 0
	int[] edgeLocations;
	int[] cornerLocations;

	//The locations array must be interleaved in the same way as in PLLCase
	public OLLCase(String solution, int[] locations) {
		this.solution = CubeAlgorithmUtil.parseAlgorithm(solution);
		cornerLocations = new int[4];
		for (int i = 0; i < 8; i += 2)
			cornerLocations[i / 2] = locations[i];
		edgeLocations = new int[4];
		for (int i = 1; i < 8; i += 2)
			edgeLocations[i / 2] = locations[i];
	}

	public Algorithm getSolution() {
		return this.solution;
	}

	//Returns true if the cube matches this OLL case
	public boolean recognize(Cube cube) {
		Color top = cube.getSolveColor(Axis.U);
		
		//Check the orientation of the edges, but only if the cube has edges (size > 2)
		if (cube.getSize() > 2) {
			for (int i = 0; i < 4; i++) {
				Piece piece = cube.getEdge(i).getPiece(0);
				if (piece.indexOfColor(top) != edgeLocations[i]) {
					return false;
				}
			}
		}

		//Check the orientation of the corners
		for (int i = 0; i < 4; i++) {
			Piece piece = cube.getCorner(i).getPiece();
			if (piece.indexOfColor(top) != cornerLocations[i]) {
				return false;
			}
		}

		return true;
	}
}
