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

import java.util.ArrayList;

import com.github.sampeterson1.cube.pieces.Cube;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;

public class OLLSolver {

	private Cube cube;
	private ArrayList<OLLCase> cases;

	public OLLSolver(Cube cube) {
		this.cube = cube;
		cases = new ArrayList<OLLCase>();

		addCase("R U2 R2 F R F' U2 R' F R F'", new int[] { 0, 1, 2, 1, 0, 1, 2, 1 });
		addCase("r U r' U2 r U2 R' U2 R U' r'", new int[] { 0, 1, 2, 1, 2, 1, 0, 1 });
		addCase("r' R2 U R' U r U2 r' U M'", new int[] { 1, 1, 0, 1, 0, 1, 0, 1 });
		addCase("M U' r U2 r' U' R U' R' M'", new int[] { 2, 1, 1, 1, 2, 1, 2, 1 });
		addCase("l' U2 L U L' U l", new int[] { 0, 1, 0, 1, 0, 0, 1, 0 });
		addCase("r U2 R' U' R U' r'", new int[] { 2, 1, 2, 0, 1, 0, 2, 1 });
		addCase("r U R' U R U2 r'", new int[] { 1, 1, 0, 1, 0, 0, 0, 0 });
		addCase("l' U' L U' L' U2 l", new int[] { 2, 1, 1, 0, 2, 0, 2, 1 });
		addCase("R U R' U' R' F R2 U R' U' F'", new int[] { 2, 1, 1, 1, 2, 0, 2, 0 });
		addCase("R U R' U R' F R F' R U2 R'", new int[] { 0, 0, 0, 1, 1, 1, 0, 0 });
		addCase("r U R' U R' F R F' R U2 r'", new int[] { 0, 1, 0, 1, 1, 0, 0, 0 });
		addCase("M' R' U' R U' R' U2 R U' R r'", new int[] { 2, 1, 2, 0, 2, 0, 1, 1 });
		addCase("F U R U' R2 F' R U R U' R'", new int[] { 1, 1, 0, 0, 0, 1, 0, 0 });
		addCase("R' F R U R' F' R F U' F'", new int[] { 2, 1, 1, 0, 2, 1, 2, 0 });
		addCase("l' U' l L' U' L U l' U l", new int[] { 0, 1, 0, 0, 0, 1, 1, 0 });
		addCase("r U r' R U R' U' r U' r'", new int[] { 2, 1, 2, 0, 1, 1, 2, 0 });
		addCase("F R' F' R2 r' U R U' R' U' M'", new int[] { 2, 1, 1, 1, 0, 1, 1, 1 });
		addCase("r U R' U R U2 r2 U' R U' R' U2 r", new int[] { 2, 1, 0, 1, 1, 1, 1, 1 });
		addCase("r' R U R U R' U' M' R' F R F'", new int[] { 0, 1, 2, 1, 1, 1, 1, 1 });
		addCase("r U R' U' M2 U R U' R' U' M'", new int[] { 1, 1, 1, 1, 1, 1, 1, 1 });
		addCase("R U2 R' U' R U R' U' R U' R'", new int[] { 2, 0, 0, 0, 2, 0, 0, 0 });
		addCase("R U2 R2 U' R2 U' R2 U2 R", new int[] { 0, 0, 0, 0, 2, 0, 2, 0 });
		addCase("R2 D' R U2 R' D R U2 R", new int[] { 1, 0, 1, 0, 2, 0, 0, 0 });
		addCase("r U R' U' r' F R F'", new int[] { 2, 0, 1, 0, 1, 0, 0, 0 });
		addCase("F' r U R' U' r' F R", new int[] { 1, 0, 0, 0, 1, 0, 2, 0 });
		addCase("R U2 R' U' R U' R'", new int[] { 2, 0, 2, 0, 1, 0, 2, 0 });
		addCase("R U R' U R U2 R'", new int[] { 1, 0, 0, 0, 0, 0, 0, 0 });
		addCase("r U R' U' r' R U R U' R'", new int[] { 1, 1, 1, 1, 1, 0, 1, 0 });
		addCase("R U R' U' R U' R' F' U' F R U R'", new int[] { 2, 1, 1, 1, 1, 0, 0, 0 });
		addCase("F R' F R2 U' R' U' R U R' F2", new int[] { 1, 1, 1, 1, 0, 0, 2, 0 });
		addCase("R' U' F U R U' R' F' R", new int[] { 2, 1, 1, 0, 1, 0, 0, 1 });
		addCase("L U F' U' L' U L F L'", new int[] { 1, 1, 0, 1, 2, 0, 1, 0 });
		addCase("R U R' U' R' F R F'", new int[] { 2, 1, 1, 0, 1, 1, 0, 0 });
		addCase("R U R2 U' R' F R U R U' F'", new int[] { 1, 1, 1, 0, 0, 1, 2, 0 });
		addCase("R U2 R2 F R F' R U2 R'", new int[] { 2, 0, 1, 0, 0, 1, 1, 1 });
		addCase("L' U' L U' L' U L U L F' L' F", new int[] { 0, 1, 1, 0, 2, 0, 1, 1 });
		addCase("F R' F' R U R U' R'", new int[] { 2, 1, 1, 1, 0, 0, 1, 0 });
		addCase("R U R' U R U' R' U' R' F R F'", new int[] { 1, 1, 2, 1, 1, 0, 0, 0 });
		addCase("L F' L' U' L U F U' L'", new int[] { 1, 1, 2, 0, 1, 1, 0, 0 });
		addCase("R' F R U R' U' F' U R", new int[] { 0, 1, 1, 0, 2, 1, 1, 0 });
		addCase("R U R' U R U2 R' F R U R' U' F'", new int[] { 1, 1, 1, 1, 2, 0, 0, 0 });
		addCase("R' U' R U' R' U2 R F R U R' U' F'", new int[] { 2, 0, 0, 1, 1, 1, 1, 0 });
		addCase("F' U' L' U L F", new int[] { 0, 1, 1, 0, 1, 0, 2, 1 });
		addCase("F U R U' R' F'", new int[] { 1, 1, 2, 1, 0, 0, 1, 0 });
		addCase("F R U R' U' F'", new int[] { 0, 1, 1, 0, 1, 1, 2, 0 });
		addCase("R' U' R' F R F' U R", new int[] { 1, 0, 2, 1, 0, 0, 1, 1 });
		addCase("R' U' R' F R F' R' F R F' U R", new int[] { 2, 1, 2, 0, 0, 0, 0, 1 });
		addCase("F R U R' U' R U R' U' F'", new int[] { 0, 1, 0, 1, 2, 0, 2, 0 });
		addCase("r U' r2 U r2 U r2 U' r", new int[] { 0, 1, 0, 0, 2, 0, 2, 1 });
		addCase("r' U r2 U' r2 U' r2 U r'", new int[] { 0, 0, 0, 0, 2, 1, 2, 1 });
		addCase("F U R U' R' U R U' R' F'", new int[] { 2, 1, 2, 0, 0, 1, 0, 0 });
		addCase("R U R' U R U' B U' B' R'", new int[] { 2, 0, 2, 1, 0, 0, 0, 1 });
		addCase("l' U2 L U L' U' L U L' U l", new int[] { 2, 1, 0, 0, 2, 0, 0, 1 });
		addCase("r U2 R' U' R U R' U' R U' r'", new int[] { 2, 1, 0, 1, 2, 0, 0, 0 });
		addCase("R' F R U R U' R2 F' R2 U' R' U R U R'", new int[] { 2, 1, 0, 0, 2, 1, 0, 0 });
		addCase("r' U' r U' R' U R U' R' U R r' U r", new int[] { 0, 1, 2, 0, 0, 1, 2, 0 });
		addCase("R U R' U' M' U R U' r'", new int[] { 1, 1, 1, 0, 1, 1, 1, 0 });
		addCase("", new int[] { 1, 0, 1, 0, 1, 0, 1, 0 });
	}

	public void solve() {
		System.out.println("Solving OLL...");
		cube.pushRotations();

		Algorithm solution = getOLLSolution();
		if (solution != null) {
			cube.executeAlgorithm(solution);
		} else {
			OLLParity();
			solve();
		}

		cube.popRotations();
	}

	private void addCase(String alg, int[] position) {
		cases.add(new OLLCase(alg, position));
	}

	private Algorithm getOLLSolution() {
		Algorithm solution = null;
		for (int i = 0; i < 4; i++) {
			for (OLLCase c : cases) {
				if (c.recognize(cube)) {
					return c.getSolution();
				}
			}
			cube.makeRotation(Axis.U, true);
		}

		return solution;
	}

	private void OLLParity() {
		int cubeSize = cube.getSize();
		for (int layer = 1; layer < cubeSize / 2; layer++) {
			cube.makeMove(new Move(Axis.R, layer, true));
			cube.makeMove(new Move(Axis.R, layer, true));
		}

		cube.makeMove(new Move(Axis.B, 0, true));
		cube.makeMove(new Move(Axis.B, 0, true));

		cube.makeMove(new Move(Axis.U, 0, true));
		cube.makeMove(new Move(Axis.U, 0, true));

		for (int layer = 1; layer < cubeSize / 2; layer++) {
			cube.makeMove(new Move(Axis.L, layer, true));
		}

		cube.makeMove(new Move(Axis.U, 0, true));
		cube.makeMove(new Move(Axis.U, 0, true));

		for (int layer = 1; layer < cubeSize / 2; layer++) {
			cube.makeMove(new Move(Axis.R, layer, false));
		}

		cube.makeMove(new Move(Axis.U, 0, true));
		cube.makeMove(new Move(Axis.U, 0, true));

		for (int layer = 1; layer < cubeSize / 2; layer++) {
			cube.makeMove(new Move(Axis.R, layer, true));
		}

		cube.makeMove(new Move(Axis.U, 0, true));
		cube.makeMove(new Move(Axis.U, 0, true));

		cube.makeMove(new Move(Axis.F, 0, true));
		cube.makeMove(new Move(Axis.F, 0, true));

		for (int layer = 1; layer < cubeSize / 2; layer++) {
			cube.makeMove(new Move(Axis.R, layer, true));
		}

		cube.makeMove(new Move(Axis.F, 0, true));
		cube.makeMove(new Move(Axis.F, 0, true));

		for (int layer = 1; layer < cubeSize / 2; layer++) {
			cube.makeMove(new Move(Axis.L, layer, false));
		}

		cube.makeMove(new Move(Axis.B, 0, true));
		cube.makeMove(new Move(Axis.B, 0, true));

		for (int layer = 1; layer < cubeSize / 2; layer++) {
			cube.makeMove(new Move(Axis.R, layer, true));
			cube.makeMove(new Move(Axis.R, layer, true));
		}
	}

}
