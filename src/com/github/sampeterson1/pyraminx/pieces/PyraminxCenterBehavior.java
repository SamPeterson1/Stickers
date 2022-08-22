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

package com.github.sampeterson1.pyraminx.pieces;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.pyraminx.util.PyraminxCenterUtil;
import com.github.sampeterson1.pyraminx.util.PyraminxMoveUtil;

public class PyraminxCenterBehavior extends PieceBehavior {

	private static PieceType type = PieceType.CENTER;

	public PyraminxCenterBehavior(Puzzle puzzle) {
		super(puzzle);
	}
	
	@Override
	public Piece createPiece(int position, int index) {
		Piece center = new Piece(super.getPuzzle(), type, position, index);
		center.setColor(PyraminxCenterUtil.getColor(position));
		
		return center;
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {

		Axis face = Pyraminx.faces[group.getPosition()];
		Axis pivot = move.getFace();
		
		int layer = move.getLayer();
		int puzzleSize = group.getPuzzleSize();
		
		List<Piece> pieces = new ArrayList<Piece>();
		if(face == pivot) {
			if(move.getLayer() == 0)
				return group.getPieces();
		} else if(layer > 0 && layer < puzzleSize - 2) {
			int rotation = PyraminxCenterUtil.getRotationOffset(pivot, face);
			int centerSize = group.getPuzzleSize() - 3;
			int sqrSize = centerSize * centerSize;
			
			int invLayer = centerSize - move.getLayer() + 1;
			int lowerIndex = sqrSize - invLayer * invLayer;
			invLayer --;
			int upperIndex = sqrSize - (invLayer * invLayer);
			
			for(int i = lowerIndex; i < upperIndex; i ++) {
				int index = i;
				if(rotation < 0) {
					for(int j = 0; j < -rotation; j ++)
						index = PyraminxCenterUtil.rotateIndexCCW(index, puzzleSize - 3);
				} else if(rotation > 0) {
					for(int j = 0; j < rotation; j ++)
						index = PyraminxCenterUtil.rotateIndexCW(index, puzzleSize - 3);
				}
				
				pieces.add(group.getPiece(index));
			}
		}
		
		return pieces;
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		int puzzleSize = piece.getPuzzleSize();
		Axis currentFace = Pyraminx.faces[piece.getPosition()];
		
		int newIndex = PyraminxCenterUtil.mapIndex(move, currentFace, piece.getIndex(), puzzleSize);
		Axis newFace = PyraminxMoveUtil.mapFace(currentFace, move);
		
		piece.setIndex(newIndex);
		piece.setPosition(Pyraminx.getAxisIndex(newFace));
	}

	@Override
	public int getNumPieces(int puzzleSize) {
		int a = puzzleSize - 3;
		return a*a;
	}

	@Override
	public PieceType getType() {
		return type;
	}
}
