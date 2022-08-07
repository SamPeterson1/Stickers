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

package com.github.yoshiapolis.cube.solvers;

import com.github.yoshiapolis.cube.pieces.Cube;
import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;

public class CornerSolver {

	private Cube cube;

	public CornerSolver(Cube cube) {
		this.cube = cube;
	}

	public void solve() {
		cube.pushRotations();
		
		Color c = cube.getCenterColor(Axis.D);
		for (int i = 0; i < 4; i++) {
			solveCorner(c);
		}
		
		cube.popRotations();
	}

	private void allignCorner(Piece toSolve) {
		while (toSolve.getPosition() != 1) {
			cube.makeMove(new Move(Axis.U, 0, true));
		}

		Color fColor = cube.getCenterColor(Axis.F);
		Color rColor = cube.getCenterColor(Axis.R);
		while (toSolve.indexOfColor(fColor) == -1 || toSolve.indexOfColor(rColor) == -1) {
			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeRotation(Axis.U, false);
			
			fColor = cube.getCenterColor(Axis.F);
			rColor = cube.getCenterColor(Axis.R);
		}
	}

	private Piece findCorner_D(Color c) {
		for (int i = 4; i < 8; i++) {
			Piece piece = cube.getCorner(i).getPiece();
			int colorIndex = piece.indexOfColor(c);
			if (colorIndex == 2) {
				cube.pushRotations();
				while (piece.getPosition() != 5) {
					cube.makeRotation(Axis.U, true);
				}
				if (piece.getColor(0) != cube.getCenterColor(Axis.F)) {
					cube.popRotations();
					return piece;
				} else {
					cube.popRotations();
				}
			} else if (colorIndex == 0 || colorIndex == 1) {
				return piece;
			}
		}

		return null;
	}

	private Piece findCorner_U(Color c) {
		for (int i = 0; i < 4; i++) {
			Piece piece = cube.getCorner(i).getPiece();
			if (piece.indexOfColor(c) != -1) {
				return piece;
			}
		}

		return null;
	}

	private void insertCorner(Piece toSolve, Color c) {
		int colorIndex = toSolve.indexOfColor(c);
		if (colorIndex == 0) {
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.U, 0, false));
			cube.makeMove(new Move(Axis.F, 0, true));
		} else if (colorIndex == 1) {
			cube.makeMove(new Move(Axis.R, 0, true));
			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeMove(new Move(Axis.R, 0, false));
			cube.makeMove(new Move(Axis.U, 0, false));
			cube.makeMove(new Move(Axis.R, 0, true));
			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeMove(new Move(Axis.R, 0, false));
		} else if (colorIndex == 2) {
			cube.makeMove(new Move(Axis.R, 0, true));
			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeMove(new Move(Axis.R, 0, false));
		}
	}

	private void moveCorner(Piece toSolve) {
		while (toSolve.getPosition() != 5) {
			cube.makeRotation(Axis.U, true);
		}
		cube.makeMove(new Move(Axis.R, 0, true));
		cube.makeMove(new Move(Axis.U, 0, true));
		cube.makeMove(new Move(Axis.R, 0, false));
	}

	private void solveCorner(Color c) {
		Piece toSolve = findCorner_U(c);
		if (toSolve != null) {
			allignCorner(toSolve);
			insertCorner(toSolve, c);
		} else {
			toSolve = findCorner_D(c);
			if (toSolve != null) {
				moveCorner(toSolve);
				solveCorner(c);
			}
		}
	}
}
