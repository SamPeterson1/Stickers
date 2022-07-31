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

package com.github.yoshiapolis.cube.pieces;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.cube.util.CubeCenterUtil;
import com.github.yoshiapolis.cube.util.CubeMoveUtil;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceBehavior;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;

public class CubeCenter implements PieceBehavior {

	@Override
	public Piece createPiece(int position, int index) {
		Piece piece = new Piece(PieceType.CENTER, position, index);
		piece.setColor(CubeCenterUtil.getColor(Cube.getFace(position)));
		return piece;
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		int size = group.getPuzzleSize() - 2;
		move = CubeMoveUtil.normalize(move, group.getPuzzleSize());
		Face moveFace = move.getFace();
		Face face = Cube.getFace(group.getPosition());
		int layer = move.getLayer() - 1;

		List<Piece> retVal = new ArrayList<Piece>();

		if (layer == -1 && moveFace == face) {
			return group.getPieces();
		} else if (layer == size && face == Cube.getOpposingFace(moveFace)) {
			return group.getPieces();
		} else if (face != Cube.getOpposingFace(moveFace) && face != moveFace && layer >= 0 && layer < size) {
			if (moveFace == Face.U) {
				for (int i = layer * size; i < (layer + 1) * size; i++) {
					int j = CubeCenterUtil.mapIndex(Face.U, Face.F, face, i, size);
					retVal.add(group.getPiece(j));
				}
			} else if (moveFace == Face.R) {
				layer = size - layer - 1;
				for (int i = layer; i < size * size; i += size) {
					int j = CubeCenterUtil.mapIndex(Face.R, Face.F, face, i, size);
					retVal.add(group.getPiece(j));
				}
			} else if (moveFace == Face.F) {
				for (int i = layer; i < size * size; i += size) {
					int j = CubeCenterUtil.mapIndex(Face.F, Face.R, face, i, size);
					retVal.add(group.getPiece(j));
				}
			}
		}

		return retVal;
	}

	@Override
	public int getNumPieces(int puzzleSize) {
		int size = puzzleSize - 2;
		return size * size;
	}

	@Override
	public void movePiece(Move move, Piece piece, int puzzleSize) {
		Face face = Cube.getFace(piece.getPosition());
		int index = piece.getIndex();
		int newIndex = index;
		int size = puzzleSize - 2;

		if (move.getFace() == face) {
			if (move.isCW()) {
				newIndex = CubeCenterUtil.rotateCW(index, size);
			} else {
				newIndex = CubeCenterUtil.rotateCCW(index, size);
			}
		} else if (move.getFace() == Cube.getOpposingFace(face)) {
			if (move.isCW()) {
				newIndex = CubeCenterUtil.rotateCCW(index, size);
			} else {
				newIndex = CubeCenterUtil.rotateCW(index, size);
			}
		} else {
			Face newFace = CubeMoveUtil.mapFace(face, move);
			newIndex = CubeCenterUtil.mapIndex(move.getFace(), face, newFace, index, size);
			piece.setPosition(newFace.getIndex());
		}

		piece.setIndex(newIndex);
	}
}
