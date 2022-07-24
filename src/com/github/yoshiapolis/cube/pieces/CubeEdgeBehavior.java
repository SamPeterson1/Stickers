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

package com.github.yoshiapolis.cube.pieces;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.puzzle.Color;
import com.github.yoshiapolis.puzzle.Face;
import com.github.yoshiapolis.puzzle.Move;
import com.github.yoshiapolis.puzzle.PieceBehavior;
import com.github.yoshiapolis.puzzle.PieceType;
import com.github.yoshiapolis.puzzle.PuzzlePiece;
import com.github.yoshiapolis.puzzle.PuzzlePieceGroup;

public class CubeEdgeBehavior implements PieceBehavior {

	@Override
	public PuzzlePiece createPiece(int position, int index) {
		PuzzlePiece edge = new PuzzlePiece(PieceType.EDGE, position, index, 2);
		Color[] colors = CubeEdgeUtil.getColors(position);
		edge.setColor(0, colors[0]);
		edge.setColor(1, colors[1]);
		
		return edge;
	}
	
	@Override
	public void movePiece(Move move, PuzzlePiece piece, int puzzleSize) {
		PuzzlePiece mapped = CubeEdgeUtil.mapEdge(move, piece, puzzleSize);
		
		piece.setColor(0, mapped.getColor(0));
		piece.setColor(1, mapped.getColor(1));
		piece.setPosition(mapped.getPosition());
		piece.setIndex(mapped.getIndex());
	}

	@Override
	public List<PuzzlePiece> getAffectedPieces(Move move, PuzzlePieceGroup group) {
		if(move.isCubeRotation()) return group.getPieces();
		
		int size = group.getPuzzleSize() - 2;
		int position = group.getPosition();
		
		move = CubeMoveUtil.normalize(move, size+2);
		Face moveFace = move.getFace();
		Face oppMoveFace = Cube.getOpposingFace(moveFace);
		Face edgeFace1 = CubeEdgeUtil.getFace(position, 0);
		Face edgeFace2 = CubeEdgeUtil.getFace(position, 1);
		
		List<PuzzlePiece> retVal = new ArrayList<PuzzlePiece>();
		
		if((move.getLayer() == 0 && (moveFace == edgeFace1 || moveFace == edgeFace2)) ||
				(move.getLayer() == size+1 && (oppMoveFace == edgeFace1 || oppMoveFace == edgeFace2))) {
			return group.getPieces();
		} else if(move.getLayer() != 0 && move.getLayer() != size+1 
				&& moveFace != edgeFace1 && moveFace != edgeFace2 && 
				oppMoveFace != edgeFace1 && oppMoveFace != edgeFace2) {
			PuzzlePiece calc = null;
			if(moveFace == Face.R) {
				calc = new PuzzlePiece(PieceType.EDGE, 0, size-move.getLayer(), 2);	
			} else if(moveFace == Face.U) {
				calc = new PuzzlePiece(PieceType.EDGE, 4, size-move.getLayer(), 2);	
			} else if(moveFace == Face.F) {
				calc = new PuzzlePiece(PieceType.EDGE, 1, move.getLayer()-1, 2);	
			}
			
			while(calc.getPosition() != position) {
				calc = CubeEdgeUtil.mapEdge(move, calc, size+2);
			}
			
			int index = calc.getIndex();
			retVal.add(group.getPiece(index));
		}
		
		return retVal;
	}

	@Override
	public int getNumPieces(int puzzleSize) {
		return puzzleSize - 2;
	}

}
