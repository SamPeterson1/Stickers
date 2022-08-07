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

import java.util.ArrayList;

import com.github.yoshiapolis.cube.pieces.Cube;
import com.github.yoshiapolis.cube.util.CubeEdgeUtil;
import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;

public class EdgeSolver {

	private Cube cube;
	private int edgeSize;

	public EdgeSolver(Cube cube) {
		this.cube = cube;
		this.edgeSize = cube.getSize() - 2;
	}

	public void solve() {
		if(cube.getSize() > 3) {
			cube.pushRotations();
	
			for (int i = 0; i < 8; i++) {
				solveEdge();
				saveEdge();
			}
	
			restoreCenters();
	
			for (int i = 0; i < 3; i++) {
				solveBodyEdge();
				cube.makeRotation(Axis.U, true);
			}
	
			solveParity();
			cube.popRotations();
		}
	}

	private boolean containsEdge(Piece root, PieceGroup edge) {
		for (int i = 0; i < edgeSize; i++) {
			Piece piece = edge.getPiece(i);
			if (flipped(root, piece)
					|| (root.getColor(0) == piece.getColor(0) && root.getColor(1) == piece.getColor(1))) {
				return true;
			}
		}

		return false;
	}

	private ArrayList<Piece> findPuzzlePieces(Piece root) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();

		for (int pos = 0; pos < 12; pos++) {
			PieceGroup edge = cube.getEdge(pos);
			for (int index = 0; index < edgeSize; index++) {
				Piece piece = edge.getPiece(index);
				if (sameColors(piece, root)) {
					pieces.add(piece);
				}
			}
		}

		return pieces;
	}

	private void flipEdge(Piece piece) {
		if (piece.getPosition() == 4) {
			cube.makeMove(new Move(Axis.U, edgeSize - piece.getIndex(), true));
		}

		cube.pushRotations();
		while (piece.getPosition() != 5) {
			cube.makeRotation(Axis.U, true);
		}

		cube.makeMove(new Move(Axis.R, 0, true));
		cube.makeMove(new Move(Axis.U, 0, true));
		cube.makeMove(new Move(Axis.R, 0, false));
		cube.makeMove(new Move(Axis.F, 0, true));
		cube.makeMove(new Move(Axis.R, 0, false));
		cube.makeMove(new Move(Axis.F, 0, false));
		cube.makeMove(new Move(Axis.R, 0, true));
		cube.popRotations();
	}

	private boolean flipped(Piece a, Piece b) {
		return (a.getColor(0) == b.getColor(1)) && b.getColor(0) == a.getColor(1);
	}

	private void insertEdge_U(Piece piece, boolean flipped) {
		int pos = piece.getPosition();

		if (pos == 3) {
			if (flipped) {
				cube.makeMove(new Move(Axis.B, 0, false));
				cube.makeMove(new Move(Axis.U, 0, true));
				cube.makeMove(new Move(Axis.B, 0, true));
			} else {
				cube.makeMove(new Move(Axis.L, 0, false));
				cube.makeMove(new Move(Axis.B, 0, true));
				cube.makeMove(new Move(Axis.L, 0, true));
				cube.makeMove(new Move(Axis.B, 0, false));
			}
		} else {
			while (piece.getPosition() != 0) {
				cube.makeRotation(Axis.U, true);
			}

			if (flipped) {
				cube.makeMove(new Move(Axis.F, 0, true));
				cube.makeMove(new Move(Axis.R, 0, false));
				cube.makeMove(new Move(Axis.F, 0, false));
				cube.makeMove(new Move(Axis.R, 0, true));
			} else {
				cube.makeMove(new Move(Axis.R, 0, true));
				cube.makeMove(new Move(Axis.U, 0, false));
				cube.makeMove(new Move(Axis.R, 0, false));
			}
		}
	}

	private void insertEdges(Piece root, PieceGroup edge) {
		int turns = edge.getPosition() - 4;
		ArrayList<Integer> layers = new ArrayList<Integer>();

		for (int i = 0; i < edgeSize; i++) {
			Piece piece = edge.getPiece(i);
			if (flipped(piece, root)) {
				if (!layers.contains(edgeSize - i)) {
					layers.add(i + 1);
				}
			}
		}

		for (int layer : layers) {
			for (int i = 0; i < turns; i++) {
				cube.makeMove(new Move(Axis.U, layer, false));
			}
		}
		flipEdge(edge.getPiece(0));
		for (int layer : layers) {
			for (int i = 0; i < turns; i++) {
				cube.makeMove(new Move(Axis.U, layer, true));
			}
		}
	}

	private void restoreCenters() {
		PieceGroup center = cube.getCenter(Axis.F);
		Color solvingColor = center.getPiece(0).getColor();
		for (int i = edgeSize; i < edgeSize * edgeSize; i += edgeSize) {
			while (center.getPiece(i).getColor() != solvingColor) {
				cube.makeMove(new Move(Axis.U, 1 + (i / edgeSize), true));
			}
		}
	}

	private boolean sameColors(Piece a, Piece b) {
		Color a1 = a.getColor(0);
		Color a2 = a.getColor(1);
		Color b1 = b.getColor(0);
		Color b2 = b.getColor(1);

		return ((a1 == b1 && a2 == b2) || (a1 == b2 && a2 == b1));
	}

	private void saveEdge() {

		cube.pushRotations();

		int numSolved = 0;
		for (int j = 0; j < 4; j++) {
			if (cube.getEdge(j).isSolved())
				numSolved++;
		}

		if (numSolved == 4) {
			cube.makeRotation(Axis.F, true);
			cube.makeRotation(Axis.F, true);
			cube.makeRotation(Axis.U, true);
			numSolved = 0;
			for (int j = 0; j < 4; j++) {
				if (cube.getEdge(j).isSolved())
					numSolved++;
			}
		}

		if (numSolved == 3) {
			while (!cube.getEdge(0).isSolved()) {
				cube.makeMove(new Move(Axis.U, 0, true));
			}
		}

		cube.makeMove(new Move(Axis.F, 0, true));
		while (cube.getEdge(0).isSolved()) {
			cube.makeMove(new Move(Axis.U, 0, true));
		}
		cube.makeMove(new Move(Axis.F, 0, false));

		cube.popRotations();
	}

	private void solveBodyEdge() {
		Piece root = cube.getEdge(4).getPiece(edgeSize / 2);
		for (int i = 5; i <= 7; i++) {
			PieceGroup edge = cube.getEdge(i);
			while (containsEdge(root, edge)) {
				insertEdges(root, edge);
			}
		}

		PieceGroup rootEdge = cube.getEdge(4);

		ArrayList<Integer> layers = new ArrayList<Integer>();

		for (int i = 0; i < edgeSize; i++) {
			Piece edge = rootEdge.getPiece(i);
			if (root.getColor(0) == edge.getColor(1) && root.getColor(1) == edge.getColor(0)) {
				layers.add(edgeSize - i);
			}
		}

		PieceGroup edge = cube.getEdge(5);
		for (int layer : layers) {
			cube.makeMove(new Move(Axis.U, layer, false));
		}
		flipEdge(edge.getPiece(0));
		for (int layer : layers) {
			cube.makeMove(new Move(Axis.U, layer, true));
		}

	}

	private void solveEdge() {
		Piece root = cube.getEdge(4).getPiece(0);
		ArrayList<Piece> pieces = findPuzzlePieces(root);

		for (Piece piece : pieces) {
			boolean flipped = flipped(root, piece);
			Axis face = CubeEdgeUtil.getFace(piece.getPosition(), 0);

			cube.pushRotations();
			if (face == Axis.U) {
				insertEdge_U(piece, flipped);
			} else if (face == Axis.D) {
				cube.makeRotation(Axis.F, true);
				cube.makeRotation(Axis.F, true);
				cube.makeRotation(Axis.U, true);
				insertEdge_U(piece, !flipped);
			} else if (flipped) {
				flipEdge(piece);
			}
			cube.popRotations();

			while (piece.getPosition() != 4) {
				cube.makeMove(new Move(Axis.U, edgeSize - piece.getIndex(), true));
			}
		}

		cube.getEdge(4).setSolved(true);
	}

	private void solveParity() {
		PieceGroup edge = cube.getEdge(4);
		Piece center = edge.getPiece(edgeSize / 2);
		ArrayList<Integer> layers = new ArrayList<Integer>();
		for (int i = 0; i < edgeSize / 2; i++) {
			Piece piece = edge.getPiece(i);
			if (flipped(piece, center)) {
				layers.add(edgeSize - piece.getIndex());
			}
		}

		if (layers.size() != 0) {
			cube.makeRotation(Axis.F, true);
			for (int layer : layers) {
				cube.makeMove(new Move(Axis.R, layer, true));
				cube.makeMove(new Move(Axis.R, layer, true));
			}

			cube.makeMove(new Move(Axis.B, 0, true));
			cube.makeMove(new Move(Axis.B, 0, true));

			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeMove(new Move(Axis.U, 0, true));

			for (int layer : layers) {
				cube.makeMove(new Move(Axis.L, layer, true));
			}

			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeMove(new Move(Axis.U, 0, true));

			for (int layer : layers) {
				cube.makeMove(new Move(Axis.R, layer, false));
			}

			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeMove(new Move(Axis.U, 0, true));

			for (int layer : layers) {
				cube.makeMove(new Move(Axis.R, layer, true));
			}

			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeMove(new Move(Axis.U, 0, true));

			cube.makeMove(new Move(Axis.F, 0, true));
			cube.makeMove(new Move(Axis.F, 0, true));

			for (int layer : layers) {
				cube.makeMove(new Move(Axis.R, layer, true));
			}

			cube.makeMove(new Move(Axis.F, 0, true));
			cube.makeMove(new Move(Axis.F, 0, true));

			for (int layer : layers) {
				cube.makeMove(new Move(Axis.L, layer, false));
			}

			cube.makeMove(new Move(Axis.B, 0, true));
			cube.makeMove(new Move(Axis.B, 0, true));

			for (int layer : layers) {
				cube.makeMove(new Move(Axis.R, layer, true));
				cube.makeMove(new Move(Axis.R, layer, true));
			}
		}
	}

}
