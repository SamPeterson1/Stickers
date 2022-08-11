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

package com.github.sampeterson1.cube.pieces;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.cube.util.CubeCornerUtil;
import com.github.sampeterson1.cube.util.CubeMoveUtil;
import com.github.sampeterson1.cube.util.CubeUtil;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;

//An implementation of PieceBehavior that defines the behavior of corner pieces on a Rubik's Cube
public class CubeCornerBehavior implements PieceBehavior {
	
	private static final PieceType type = PieceType.CORNER;
	
	@Override
	public Piece createPiece(int position, int index, int puzzleSize) {
		Piece piece = new Piece(PieceType.CORNER, position, index, puzzleSize);
		Color[] colors = CubeCornerUtil.getColors(position);
		for(int i = 0; i < 3; i ++) piece.setColor(i, colors[i]);
		return piece;
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		Axis face = move.getFace();
		int layer = move.getLayer();
		
		//we can treat a move on the last layer as a move on the first layer if we invert its face
		if(layer == group.getPuzzleSize() - 1) {
			layer = 0;
			face = CubeUtil.getOpposingFace(face);
		}
		
		List<Piece> retVal = new ArrayList<Piece>();
		
		//Only move corners when either the first or last layer is turned
		if(layer == 0) {
			//Move all corners that belong to the move's face
			Integer[] positions = CubeCornerUtil.getPositions(face);
			for(int i = 0; i < 4; i ++) {
				if(positions[i] == group.getPosition()) {
					retVal.add(group.getPiece());
				}
			}
		}
		
		return retVal;
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		//one clockwise move = three counterclockwise moves
		move = CubeMoveUtil.normalize(move, piece.getPuzzleSize());
		int iters = (move.isCW() ? 1 : 3);
		
		for(int i = 0; i < iters; i ++) {
			int[] mapping = CubeCornerUtil.mapCorner(move, piece);
			int newPosition = mapping[0];
			int rotation = mapping[1];
			piece.setPosition(newPosition);
			//rotate the indices of the colors on the corner such that they match up with their new position
			for(int j = 0; j < rotation; j ++) {
				CubeCornerUtil.rotateCW(piece);
			}
		}
	}
	
	@Override
	public int getNumPieces(int puzzleSize) {
		return 1;
	}

	@Override
	public PieceType getType() {
		return type;
	}
}
