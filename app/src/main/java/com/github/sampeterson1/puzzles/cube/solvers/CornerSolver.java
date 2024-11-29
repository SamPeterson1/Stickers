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
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.cube.meta.Cube;

/*
 * This class solves the four bottom corners of the cube using the beginner's method.
 * 
 * This implementation is not meant to be readable.
 * See https://solvethecube.com/#step2 for more information.
 */
public class CornerSolver {

	private Cube cube;

	public CornerSolver(Cube cube) {
		this.cube = cube;
	}

	public void solve() {
		cube.pushRotations();
		
		Color c = cube.getSolveColor(Axis.D);
		for (int i = 0; i < 4; i++) {
			solveCorner(c);
		}
		
		cube.popRotations();
	}

	private void allignCorner(Piece toSolve) {
		while (toSolve.getPosition() != 1) {
			cube.makeMove(new Move(Axis.U, 0, true));
		}

		Color fColor = cube.getSolveColor(Axis.F);
		Color rColor = cube.getSolveColor(Axis.R);
		while (toSolve.indexOfColor(fColor) == -1 || toSolve.indexOfColor(rColor) == -1) {
			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeRotation(Axis.U, false);
			
			fColor = cube.getSolveColor(Axis.F);
			rColor = cube.getSolveColor(Axis.R);
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
				if (piece.getColor(0) != cube.getSolveColor(Axis.F)) {
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
