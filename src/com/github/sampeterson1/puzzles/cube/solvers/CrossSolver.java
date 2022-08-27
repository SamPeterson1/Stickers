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

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzles.cube.pieces.Cube;
import com.github.sampeterson1.puzzles.cube.util.CubeEdgeUtil;

/*
 * This class solves the bottom cross of a cube using the CFOP method.
 * 
 * This implementation is not meant to be readable.
 * See https://solvethecube.com/#step1 for more information.
 */
public class CrossSolver {

	private Cube cube;

	public CrossSolver(Cube cube) {
		this.cube = cube;
	}

	public void solve() {
		System.out.println("Solving Cross...");
		if (cube.getSize() > 2) {
			cube.pushRotations();
	
			Color c = cube.getSolveColor(Axis.D);
			for (int i = 0; i < 4; i++) {
				solveEdge(c);
			}
	
			cube.popRotations();
		}
	}

	private Piece findEdge(Color c) {
		for (int i = 0; i < 12; i++) {
			Piece piece = cube.getEdge(i).getPiece(0);
			if (piece.getColor(0) == c || piece.getColor(1) == c) {
				if (i >= 8 && i <= 11) {
					Axis face = CubeEdgeUtil.getFace(i, 1);
					if (cube.getCenter(face).getPiece().getColor() != piece.getColor(1)) {
						return piece;
					}
				} else {
					return piece;
				}
			}
		}

		return null;
	}

	private int getEdgeDistance(Piece toSolve, Color c) {
		while (toSolve.getPosition() != 4) {
			cube.makeRotation(Axis.U, true);
		}

		int turns = 0;
		Color color = ((toSolve.getColor(0)) == c) ? toSolve.getColor(1) : toSolve.getColor(0);

		cube.pushRotations();
		while (cube.getCenter(Axis.F).getPiece().getColor() != color) {
			cube.makeRotation(Axis.U, true);
			turns++;
		}
		cube.popRotations();

		return turns;
	}

	private void insertSideEdge(Piece toSolve, int turns, boolean flipped) {
		int iters = 0;
		Move insertion = null;

		if (flipped) {
			iters = turns + 1;
			insertion = new Move(Axis.L, 0, true);
		} else {
			iters = turns;
			insertion = new Move(Axis.F, 0, false);
		}

		for (int i = 0; i < iters; i++) {
			cube.makeMove(new Move(Axis.D, 0, false));
		}
		cube.makeMove(insertion);
		for (int i = 0; i < iters; i++) {
			cube.makeMove(new Move(Axis.D, 0, true));
		}
	}

	private void insertTopEdge(Piece toSolve, boolean flipped) {
		Color target = (flipped ? toSolve.getColor(0) : toSolve.getColor(1));

		while (true) {
			cube.makeMove(new Move(Axis.U, 0, true));
			Axis face = CubeEdgeUtil.getFace(toSolve.getPosition(), 1);
			if (cube.getCenter(face).getPiece().getColor() == target) {
				break;
			}
		}

		if (flipped)
			cube.makeMove(new Move(Axis.U, true));

		while (toSolve.getPosition() != 0) {
			cube.makeRotation(Axis.U, true);
		}

		if (flipped) {
			cube.makeMove(new Move(Axis.F, 0, true));
			cube.makeMove(new Move(Axis.R, 0, false));
			cube.makeMove(new Move(Axis.F, 0, false));
		} else {
			cube.makeMove(new Move(Axis.F, 0, true));
			cube.makeMove(new Move(Axis.F, 0, true));
		}
	}

	private void moveCorner(Piece toSolve) {
		while (toSolve.getPosition() != 8) {
			cube.makeRotation(Axis.U, true);
		}
		cube.makeMove(new Move(Axis.F, 0, true));
	}

	private void solveEdge(Color c) {
		Piece toSolve = findEdge(c);
		cube.pushRotations();
		
		if (toSolve != null) {
			int pos = toSolve.getPosition();
			if (pos >= 0 && pos <= 3) {
				boolean flipped = (toSolve.getColor(1) == c);
				insertTopEdge(toSolve, flipped);
			} else if (pos >= 4 && pos <= 7) {
				int turns = getEdgeDistance(toSolve, c);
				boolean flipped = (toSolve.getColor(1) == c);
				insertSideEdge(toSolve, turns, flipped);
			} else if (pos >= 8 && pos <= 11) {
				moveCorner(toSolve);
				solveEdge(c);
			}
		}
		
		cube.popRotations();
	}

}
