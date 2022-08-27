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

import java.util.List;

//Provides a template for defining how a piece/piece group moves
public abstract class PieceBehavior {

	private Puzzle puzzle;
	private PieceType type;
	
	public PieceBehavior(PieceType type, Puzzle puzzle) {
		this.puzzle = puzzle;
		this.type = type;
	}
	
	public final PieceType getType() {
		return this.type;
	}
	
	protected final Puzzle getPuzzle() {
		return this.puzzle;
	}
	
	public abstract Piece createPiece(int position, int index);
	
	public abstract List<Piece> getAffectedPieces(Move move, PieceGroup group);
	
	public abstract void movePiece(Move move, Piece piece);
	
	public abstract int getNumPieces(int puzzleSize);
	
}
