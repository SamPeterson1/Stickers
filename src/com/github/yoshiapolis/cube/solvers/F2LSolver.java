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
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;

public class F2LSolver {

	private Cube cube;

	public F2LSolver(Cube cube) {
		this.cube = cube;
	}

	public Algorithm solve() {
		if (cube.getSize() < 3)
			return new Algorithm();

		cube.setLogMoves(true);
		cube.clearMoveLog();
		cube.pushRotations();

		Color avoid = cube.getColor(Face.U);
		for (int i = 0; i < 4; i++) {
			solveEdge(avoid);
		}

		cube.popRotations();
		Algorithm alg = cube.getMoveLog();
		alg.simplify();

		return alg;
	}

	public void solveEdge(Color avoid) {
		Piece toInsert = findEdge_U(avoid);

		if (toInsert != null) {
			allignEdge(toInsert);

			Color rColor = cube.getColor(Face.R);
			Color lColor = cube.getColor(Face.L);
			Color c1 = toInsert.getColor(0);

			if (c1 == rColor) {
				insertRight();
			} else if (c1 == lColor) {
				insertLeft();
			}
		} else {
			toInsert = findEdge_D(avoid);
			if (toInsert != null) {
				moveEdge(toInsert);
				solveEdge(avoid);
			}
		}
	}

	private void allignEdge(Piece toInsert) {
		while (toInsert.getPosition() != 0) {
			cube.makeMove(new Move(Face.U, 0, true));
		}

		Color fColor = cube.getColor(Face.F);
		while (toInsert.getColor(1) != fColor) {
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeRotation(Face.U, false);
			fColor = cube.getColor(Face.F);
		}
	}

	private Piece findEdge_D(Color avoid) {
		for (int i = 4; i < 8; i++) {
			Piece piece = cube.getEdge(i).getPiece(0);
			cube.pushRotations();
			while (piece.getPosition() != 5) {
				cube.makeRotation(Face.U, true);
			}
			Color fColor = cube.getColor(Face.F);
			if (piece.getColor(0) != avoid && piece.getColor(1) != avoid && piece.getColor(0) != fColor) {
				return piece;
			}
			cube.popRotations();
		}

		return null;
	}

	private Piece findEdge_U(Color avoid) {
		for (int i = 0; i < 4; i++) {
			Piece piece = cube.getEdge(i).getPiece(0);
			if (piece.getColor(0) != avoid && piece.getColor(1) != avoid) {
				return piece;
			}
		}

		return null;
	}

	private void insertLeft() {
		cube.makeMove(new Move(Face.U, 0, false));
		cube.makeMove(new Move(Face.L, 0, false));
		cube.makeMove(new Move(Face.U, 0, true));
		cube.makeMove(new Move(Face.L, 0, true));
		cube.makeMove(new Move(Face.F, 0, false));
		cube.makeMove(new Move(Face.L, 0, true));
		cube.makeMove(new Move(Face.F, 0, true));
		cube.makeMove(new Move(Face.L, 0, false));
	}

	private void insertRight() {
		cube.makeMove(new Move(Face.U, 0, true));
		cube.makeMove(new Move(Face.R, 0, true));
		cube.makeMove(new Move(Face.U, 0, false));
		cube.makeMove(new Move(Face.R, 0, false));
		cube.makeMove(new Move(Face.F, 0, true));
		cube.makeMove(new Move(Face.R, 0, false));
		cube.makeMove(new Move(Face.F, 0, false));
		cube.makeMove(new Move(Face.R, 0, true));
	}

	private void moveEdge(Piece toInsert) {
		while (toInsert.getPosition() != 5) {
			cube.makeRotation(Face.U, true);
		}
		cube.makeMove(new Move(Face.R, 0, true));
		cube.makeMove(new Move(Face.U, 0, false));
		cube.makeMove(new Move(Face.R, 0, false));
		cube.makeMove(new Move(Face.F, 0, true));
		cube.makeMove(new Move(Face.R, 0, false));
		cube.makeMove(new Move(Face.F, 0, false));
		cube.makeMove(new Move(Face.R, 0, true));
	}

}
