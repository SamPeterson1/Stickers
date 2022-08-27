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

package com.github.sampeterson1.puzzle.lib;

import java.util.ArrayList;
import java.util.List;

public abstract class SimplePieceBehavior extends PieceBehavior {
	
	public SimplePieceBehavior(PieceType type, Puzzle puzzle) {
		super(type, puzzle);
	}

	protected abstract Piece createPiece(int position);
	
	protected abstract boolean affectedByMove(Move move, Piece piece);
	
	@Override
	public Piece createPiece(int position, int index) {
		return createPiece(position);
	}
	
	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		if(affectedByMove(move, group.getPiece())) {
			return group.getPieces();
		}
		
		return new ArrayList<Piece>();
	}
	
	@Override
	public int getNumPieces(int puzzleSize) {
		return 1;
	}
	
}
