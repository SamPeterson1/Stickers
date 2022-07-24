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

package com.github.yoshiapolis.cube.solvers;

import com.github.yoshiapolis.cube.pieces.Cube;
import com.github.yoshiapolis.cube.pieces.CubeEdgeUtil;
import com.github.yoshiapolis.puzzle.Algorithm;
import com.github.yoshiapolis.puzzle.Color;
import com.github.yoshiapolis.puzzle.Face;
import com.github.yoshiapolis.puzzle.Move;
import com.github.yoshiapolis.puzzle.PuzzlePiece;

public class CrossSolver {
	
	private Cube cube;
	
	public CrossSolver(Cube cube) {
		this.cube = cube;
	}
	
	public Algorithm solve() {
		
		if(cube.getSize() < 3) return new Algorithm();
		
		cube.setLogMoves(true);
		cube.clearMoveLog();
		cube.pushRotations();
		
		Color c = cube.getColor(Face.D);
		
		for(int i = 0; i < 4; i ++) {
			solveEdge(c);
		}
		
		cube.popRotations();
		Algorithm alg = cube.getMoveLog();
		alg.simplify();
		
		return alg;
	}
	
	private void moveCorner(PuzzlePiece toSolve) {
		while(toSolve.getPosition() != 8) {
			cube.makeRotation(Face.U, true);
		}
		cube.makeMove(new Move(Face.F, 0, true));
	}
	
	private int getEdgeDistance(PuzzlePiece toSolve, Color c) {
		while(toSolve.getPosition() != 4) {
			cube.makeRotation(Face.U, true);
		}
		
		int turns = 0;
		Color color = ((toSolve.getColor(0)) == c) ? toSolve.getColor(1) : toSolve.getColor(0);
		
		cube.pushRotations();
		while(cube.getColor(Face.F) != color) {
			cube.makeRotation(Face.U, true);
			turns ++;
		}
		cube.popRotations();
		
		return turns;
	}
	
	private void insertSideEdge(PuzzlePiece toSolve, int turns, boolean flipped) {
		int iters = 0;
		Move insertion = null;
		
		if(flipped) {
			iters = turns + 1;
			insertion = new Move(Face.L, 0, true);
		} else {
			iters = turns;
			insertion = new Move(Face.F, 0, false);
		}
		
		for(int i = 0; i < iters; i ++) {
			cube.makeMove(new Move(Face.D, 0, false));
		}
		cube.makeMove(insertion);
		for(int i = 0; i < iters; i ++) {
			cube.makeMove(new Move(Face.D, 0, true));
		}
	}
	
	private void insertTopEdge(PuzzlePiece toSolve, boolean flipped) {
		Color target = (flipped ? toSolve.getColor(0) : toSolve.getColor(1));
		
		while(true) {
			cube.makeMove(new Move(Face.U, 0, true));
			Face face = CubeEdgeUtil.getFace(toSolve.getPosition(), 1);
			if(cube.getColor(face) == target) {
				break;
			}
		}
		
		if(flipped) cube.makeMove(new Move(Face.U, true));
		
		while(toSolve.getPosition() != 0) {
			cube.makeRotation(Face.U, true);
		}
		
		if(flipped) {
			cube.makeMove(new Move(Face.F, 0, true));
			cube.makeMove(new Move(Face.R, 0, false));
			cube.makeMove(new Move(Face.F, 0, false));
		} else {
			cube.makeMove(new Move(Face.F, 0, true));
			cube.makeMove(new Move(Face.F, 0, true));
		}
	}
	
	private PuzzlePiece findEdge(Color c) {
		for(int i = 0; i < 12; i ++) {
			PuzzlePiece piece = cube.getEdge(i).getPiece(0);
			if(piece.getColor(0) == c || piece.getColor(1) == c) {
				if(i >= 8 && i <= 11) {
					Face face = CubeEdgeUtil.getFace(i, 1);
					if(cube.getColor(face) != piece.getColor(1)) {
						return piece;
					}
				} else {
					return piece;
				}
			}
		}
		
		return null;
	}
	
	private void solveEdge(Color c) {
		
		PuzzlePiece toSolve = findEdge(c);
		System.out.println("0");
		cube.pushRotations();
		if(toSolve != null) {
			int pos = toSolve.getPosition();
			if(pos >= 0 && pos <= 3) {
				System.out.println("1");
				boolean flipped = (toSolve.getColor(1) == c);
				insertTopEdge(toSolve, flipped);
			} else if(pos >= 4 && pos <= 7) {	
				System.out.println("2");
				int turns = getEdgeDistance(toSolve, c);
				boolean flipped = (toSolve.getColor(1) == c);
				insertSideEdge(toSolve, turns, flipped);
			} else if(pos >= 8 && pos <= 11) {
				System.out.println("3");
				moveCorner(toSolve);
				solveEdge(c);
			}
		}
		cube.popRotations();
	}
	
}
