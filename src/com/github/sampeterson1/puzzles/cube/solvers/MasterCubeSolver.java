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

import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzles.cube.meta.Cube;

//Encapsulates all of the solvers needed to produce a full solution of a Rubik's Cube
public class MasterCubeSolver {
	
	private CenterSolver centerSolver;
	private EdgeSolver edgeSolver;
	private CrossSolver crossSolver;
	private CornerSolver cornerSolver;
	private F2LSolver f2lSolver;
	private OLLSolver ollSolver;
	private PLLSolver pllSolver;
	
	private Cube cube;
	
	public MasterCubeSolver(Cube cube) {
		centerSolver = new CenterSolver(cube);
		edgeSolver = new EdgeSolver(cube);
		crossSolver = new CrossSolver(cube);
		cornerSolver = new CornerSolver(cube);
		f2lSolver = new F2LSolver(cube);
		ollSolver = new OLLSolver(cube);
		pllSolver = new PLLSolver(cube);
		
		this.cube = cube;
	}
	
	public Algorithm solve() {
		long startTime = System.currentTimeMillis();
		
		cube.setLogMoves(true);
		cube.clearMoveLog();
		
		centerSolver.solve();
		edgeSolver.solve();
		crossSolver.solve();
		cornerSolver.solve();
		f2lSolver.solve();
		ollSolver.solve();
		pllSolver.solve();
		
		System.out.println("Simplifying solution...");
		Algorithm solution = cube.getMoveLog();
		solution = cube.getMetaFunctions().simplify(solution);
		
		float seconds = (System.currentTimeMillis() - startTime) / 1000.0f;
		System.out.println("Solved in " + seconds + " seconds and " + solution.length() + " moves");
		
		return solution;
	}
}
