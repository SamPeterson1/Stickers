package com.yoshiapolis.cube.solvers;

import com.yoshiapolis.cube.pieces.Cube;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PuzzlePiece;

public class CornerSolver {
	
	private Cube cube;

	public CornerSolver(Cube cube) {
		this.cube = cube;
	}
	
	public Algorithm solve() {
		cube.setLogMoves(true);
		cube.clearMoveLog();
		cube.pushRotations();
		
		Color c = cube.getColor(Face.D);
		for(int i = 0; i < 4; i ++) {
			solveCorner(c);
		}
		
		cube.popRotations();
		Algorithm alg = cube.getMoveLog();
		alg.simplify();
		return alg;
	}
	
	private void insertCorner(PuzzlePiece toSolve, Color c) {
		int colorIndex = toSolve.indexOfColor(c);
		if(colorIndex == 0) {
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.U, 0, false));
			cube.makeMove(new Move(Face.F, 0, true));
		} else if(colorIndex == 1) {
			cube.makeMove(new Move(Face.R, 0, true));
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeMove(new Move(Face.R, 0, false));
			cube.makeMove(new Move(Face.U, 0, false));
			cube.makeMove(new Move(Face.R, 0, true));
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeMove(new Move(Face.R, 0, false));
		} else if(colorIndex == 2) {
			cube.makeMove(new Move(Face.R, 0, true));
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeMove(new Move(Face.R, 0, false));
		}
	}
	
	private void allignCorner(PuzzlePiece toSolve) {
		while(toSolve.getPosition() != 1) {
			cube.makeMove(new Move(Face.U, 0, true));
		}
		
		Color fColor = cube.getColor(Face.F);
		Color rColor = cube.getColor(Face.R);
		while(toSolve.indexOfColor(fColor) == -1 || toSolve.indexOfColor(rColor) == -1) {
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeRotation(Face.U, false);
			fColor = cube.getColor(Face.F);
			rColor = cube.getColor(Face.R);
		}
	}
	
	private PuzzlePiece findCorner_U(Color c) {
		for(int i = 0; i < 4; i ++) {
			PuzzlePiece piece = cube.getCorner(i).getPiece();
			if(piece.indexOfColor(c) != -1) {
				return piece;
			}
		}
		
		return null;
	}
	
	private PuzzlePiece findCorner_D(Color c) {
		for(int i = 4; i < 8; i ++) {
			PuzzlePiece piece = cube.getCorner(i).getPiece();
			int colorIndex = piece.indexOfColor(c);
			if(colorIndex == 2) {
				cube.pushRotations();
				while(piece.getPosition() != 5) {
					cube.makeRotation(Face.U, true);
				}
				if(piece.getColor(0) != cube.getColor(Face.F)) {
					cube.popRotations();
					return piece;
				} else {
					cube.popRotations();
				}
			} else if(colorIndex == 0 || colorIndex == 1) {
				return piece;
			}
		}
		
		return null;
	}
	
	private void moveCorner(PuzzlePiece toSolve) {
		while(toSolve.getPosition() != 5) {
			cube.makeRotation(Face.U, true);
		}
		cube.makeMove(new Move(Face.R, 0, true));
		cube.makeMove(new Move(Face.U, 0, true));
		cube.makeMove(new Move(Face.R, 0, false));
	}
	
	private void solveCorner(Color c) {
		PuzzlePiece toSolve = findCorner_U(c);
		if(toSolve != null) {
			allignCorner(toSolve);
			insertCorner(toSolve, c);
		} else {
			toSolve = findCorner_D(c);
			if(toSolve != null) {
				moveCorner(toSolve);
				solveCorner(c);
			}
		}
	}
}
