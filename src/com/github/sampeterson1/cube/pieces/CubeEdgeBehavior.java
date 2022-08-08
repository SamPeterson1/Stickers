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

package com.github.sampeterson1.cube.pieces;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.cube.util.CubeEdgeUtil;
import com.github.sampeterson1.cube.util.CubeMoveUtil;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;

public class CubeEdgeBehavior implements PieceBehavior {

	private static final PieceType type = PieceType.EDGE;
	
	@Override
	public Piece createPiece(int position, int index, int puzzleSize) {
		Piece edge = new Piece(PieceType.EDGE, position, index, puzzleSize);
		Color[] colors = CubeEdgeUtil.getColors(position);
		edge.setColor(0, colors[0]);
		edge.setColor(1, colors[1]);

		return edge;
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		Piece mapped = CubeEdgeUtil.mapEdge(move, piece);

		piece.setColor(0, mapped.getColor(0));
		piece.setColor(1, mapped.getColor(1));
		piece.setPosition(mapped.getPosition());
		piece.setIndex(mapped.getIndex());
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		int puzzleSize = group.getPuzzleSize();
		int size = puzzleSize - 2;
		int position = group.getPosition();

		move = CubeMoveUtil.normalize(move, size + 2);
		Axis moveFace = move.getFace();
		Axis oppMoveFace = Cube.getOpposingFace(moveFace);
		Axis edgeFace1 = CubeEdgeUtil.getFace(position, 0);
		Axis edgeFace2 = CubeEdgeUtil.getFace(position, 1);

		List<Piece> retVal = new ArrayList<Piece>();

		if ((move.getLayer() == 0 && (moveFace == edgeFace1 || moveFace == edgeFace2))
				|| (move.getLayer() == size + 1 && (oppMoveFace == edgeFace1 || oppMoveFace == edgeFace2))) {
			return group.getPieces();
		} else if (move.getLayer() != 0 && move.getLayer() != size + 1 && moveFace != edgeFace1 && moveFace != edgeFace2
				&& oppMoveFace != edgeFace1 && oppMoveFace != edgeFace2) {
			Piece calc = null;
			if (moveFace == Axis.R) {
				calc = new Piece(PieceType.EDGE, 0, size - move.getLayer(), puzzleSize);
			} else if (moveFace == Axis.U) {
				calc = new Piece(PieceType.EDGE, 4, size - move.getLayer(), puzzleSize);
			} else if (moveFace == Axis.F) {
				calc = new Piece(PieceType.EDGE, 1, move.getLayer() - 1, puzzleSize);
			}

			while (calc.getPosition() != position) {
				calc = CubeEdgeUtil.mapEdge(move, calc);
			}

			int index = calc.getIndex();
			retVal.add(group.getPiece(index));
		}

		return retVal;
	}

	@Override
	public int getNumPieces(int puzzleSize, int position) {
		return puzzleSize - 2;
	}

	@Override
	public PieceType getType() {
		return type;
	}

}
