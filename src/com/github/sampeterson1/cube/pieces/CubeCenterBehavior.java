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

import com.github.sampeterson1.cube.util.CubeCenterUtil;
import com.github.sampeterson1.cube.util.CubeMoveUtil;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;

public class CubeCenterBehavior implements PieceBehavior {
	
	private static final PieceType type = PieceType.CENTER;
	
	@Override
	public Piece createPiece(int position, int index, int puzzleSize) {
		Piece piece = new Piece(PieceType.CENTER, position, index, puzzleSize);
		piece.setColor(Cube.getFaceColor(Cube.getFace(position)));
		return piece;
	}

	//returns a list of center pieces affected by a slice move
	private List<Piece> getSlice(Move move, PieceGroup center) {
		List<Piece> slice = new ArrayList<Piece>();
		Axis centerFace = Cube.getFace(center.getPosition());
		Axis moveFace = move.getFace();
		int relativeLayer = move.getLayer() - 1;
		int centerSize = center.getPuzzleSize() - 2;
		
		if (moveFace == Axis.U) {
			//iterate over a horizontal line
			for (int i = relativeLayer * centerSize; i < (relativeLayer + 1) * centerSize; i++) {
				//map the index from the F face to the center's face by rotating about the U face
				int j = CubeCenterUtil.mapIndex(Axis.U, Axis.F, centerFace, i, centerSize);
				slice.add(center.getPiece(j));
			}
		} else if (moveFace == Axis.R) {
			relativeLayer = centerSize - relativeLayer - 1; //invert the layer
			//iterate over a vertical line
			for (int i = relativeLayer; i < centerSize * centerSize; i += centerSize) {
				//map the index from the F face to the center's face by rotating about the R face
				int j = CubeCenterUtil.mapIndex(Axis.R, Axis.F, centerFace, i, centerSize);
				slice.add(center.getPiece(j));
			}
		} else if (moveFace == Axis.F) {
			//iterate over a vertical line
			for (int i = relativeLayer; i < centerSize * centerSize; i += centerSize) {
				//map the index from the R face to the center's face by rotating about the R face
				int j = CubeCenterUtil.mapIndex(Axis.F, Axis.R, centerFace, i, centerSize);
				slice.add(center.getPiece(j));
			}
		}
		
		return slice;
	}
	
	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup center) {
		int centerSize = center.getPuzzleSize() - 2;
		move = CubeMoveUtil.normalize(move, center.getPuzzleSize());
		Axis moveFace = move.getFace();
		Axis centerFace = Cube.getFace(center.getPosition());
		int relativeLayer = move.getLayer() - 1;

		List<Piece> retVal = new ArrayList<Piece>();
		
		boolean isFirstLayerTurn = (relativeLayer == -1);
		boolean onFirstLayer = (moveFace == centerFace);
		boolean isLastLayerTurn = (relativeLayer == centerSize);
		boolean onLastLayer = (moveFace == Cube.getOpposingFace(centerFace));
		
		boolean isInnerSliceTurn = !isFirstLayerTurn && !isLastLayerTurn;
		boolean onInnerSlice = !onFirstLayer && !onLastLayer;
		
		if ((isFirstLayerTurn && onFirstLayer) || (isLastLayerTurn && onLastLayer)) {
			//first and last layer turns affect all of the center pieces in the group
			return center.getPieces();
		} else if (isInnerSliceTurn && onInnerSlice) {
			return getSlice(move, center);
		}

		return retVal;
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		Axis face = Cube.getFace(piece.getPosition());
		Axis moveFace = move.getFace();
		int index = piece.getIndex();
		int centerSize = piece.getPuzzleSize() - 2;

		int newIndex = index;
		if (face == moveFace || face == Cube.getOpposingFace(moveFace)) {
			//if we are rotating from the last layer, we invert the rotation direction
			boolean rotateCW = move.isCW() ^ (face != moveFace);
			if (rotateCW) {
				newIndex = CubeCenterUtil.rotateCW(index, centerSize);
			} else {
				newIndex = CubeCenterUtil.rotateCCW(index, centerSize);
			}
		} else {
			Axis newFace = CubeMoveUtil.mapFace(face, move);
			newIndex = CubeCenterUtil.mapIndex(moveFace, face, newFace, index, centerSize);
			piece.setPosition(Cube.getFacePosition(newFace));
		}

		piece.setIndex(newIndex);
	}

	@Override
	public int getNumPieces(int puzzleSize, int position) {
		int size = puzzleSize - 2;
		return size * size;
	}
	
	@Override
	public PieceType getType() {
		return type;
	}

}
