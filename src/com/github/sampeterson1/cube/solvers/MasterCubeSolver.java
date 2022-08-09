package com.github.sampeterson1.cube.solvers;

import com.github.sampeterson1.cube.pieces.Cube;
import com.github.sampeterson1.puzzle.lib.Algorithm;

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
		solution = cube.simplify(solution);
		
		float seconds = (System.currentTimeMillis() - startTime) / 1000.0f;
		System.out.println("Solved in " + seconds + " seconds!");
		
		return solution;
	}
}
