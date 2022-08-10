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
import com.github.sampeterson1.cube.util.CubeUtil;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;

//An implementation of PieceBehavior that defines the behavior of edge pieces on a Rubik's Cube
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
	
	/*
	 * Returns the index of the piece in an edge that is affected by a slice (inner layer) move
	 * 
	 * To do this, we use a similar approach as implemented in CubeCenterBehavior. That is,
	 * we find the index of an affected edge piece at a predetermined and arbitrary position,
	 * and rotate it as defined by the given move until we arrive at the edge's real position.
	 */
	private int getSlicePieceIndex(Move move, PieceGroup edge) {
		Piece piece = null;
		int puzzleSize = edge.getPuzzleSize();
		int edgeSize = puzzleSize - 2;
		int position = edge.getPosition();
		Axis moveFace = move.getFace();
		
		//Create an edge piece that would be affected by the move
		if (moveFace == Axis.R) {
			piece = new Piece(PieceType.EDGE, 0, edgeSize - move.getLayer(), puzzleSize);
		} else if (moveFace == Axis.U) {
			piece = new Piece(PieceType.EDGE, 4, edgeSize - move.getLayer(), puzzleSize);
		} else if (moveFace == Axis.F) {
			piece = new Piece(PieceType.EDGE, 1, move.getLayer() - 1, puzzleSize);
		}
		
		//Move it until its position matches with what we want
		while (piece.getPosition() != position) {
			piece = CubeEdgeUtil.mapEdge(move, piece);
		}

		return piece.getIndex();
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		int puzzleSize = group.getPuzzleSize();
		int edgeSize = puzzleSize - 2;
		int position = group.getPosition();

		move = CubeMoveUtil.normalize(move, puzzleSize);
		Axis moveFace = move.getFace();
		Axis oppMoveFace = CubeUtil.getOpposingFace(moveFace);
		Axis edgeFace1 = CubeEdgeUtil.getFace(position, 0);
		Axis edgeFace2 = CubeEdgeUtil.getFace(position, 1);
		
		int layer = move.getLayer();
		List<Piece> retVal = new ArrayList<Piece>();

		boolean firstLayerTurn = (layer == 0);
		boolean lastLayerTurn = (layer == edgeSize + 1);
		boolean innerLayerTurn = (!firstLayerTurn && !lastLayerTurn);
		boolean edgeOnMoveFace = (moveFace == edgeFace1 || moveFace == edgeFace2);
		boolean edgeOnOppMoveFace = (oppMoveFace == edgeFace1 || oppMoveFace == edgeFace2);
		
		if ((firstLayerTurn && edgeOnMoveFace) || (lastLayerTurn && edgeOnOppMoveFace)) {
			//rotate an entire face
			return group.getPieces();
		} else if (innerLayerTurn && !edgeOnMoveFace && !edgeOnOppMoveFace) {
			int index = getSlicePieceIndex(move, group);
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
