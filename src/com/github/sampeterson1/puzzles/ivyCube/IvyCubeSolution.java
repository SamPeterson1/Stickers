package com.github.sampeterson1.puzzles.ivyCube;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzles.ivyCube.pieces.IvyCube;

public class IvyCubeSolution {
	
	private IvyCube cubeState;
	private Algorithm solution;
	
	public IvyCubeSolution(Algorithm initialScramble) {
		this(initialScramble, true);
	}
	
	public IvyCubeSolution(Algorithm initialScramble, boolean computeSolution) {
		this.cubeState = new IvyCube();
		this.cubeState.executeAlgorithm(initialScramble, false);
		
		if(computeSolution) {
			int depth = 5;
			while(solution == null) {
				recursiveSolve(0, depth);
				depth ++;
			}
		} else {
			this.solution = initialScramble.getInverse();
		}
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		int stateHash = IvyCubeUtil.hash(cubeState);
		str.append(stateHash + ";");
		
		for(Move move : solution.getMoves()) {
			str.append(move.getAxis().getName());
			if(move.isCCW()) str.append("'");
			str.append(" ");
		}
		
		return str.toString();
	}
	
	public IvyCube getState() {
		return this.cubeState;
	}
	
	public Algorithm getSolution() {
		return this.solution;
	}
	
	private void recursiveSolve(int depth, int maxDepth) {
		if(depth == maxDepth) return;
		if(solution != null && depth + 1 >= solution.length()) return;

		
		for(Axis axis : IvyCubeUtil.moveAxes) {
			for(int i = 0; i < 2; i ++) {		
				cubeState.pushState();
				boolean cw = (i == 0);
				Move move = new Move(axis, cw);
				cubeState.makeMove(move);
				
				if(IvyCubeUtil.isSolved(cubeState)) {
					solution = cubeState.getMoveLog().copy();
					cubeState.popState();
					return;
				} else {
					recursiveSolve(depth + 1, maxDepth);
				}
				
				cubeState.popState();
			}
		}
	}
	
}