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

package com.github.sampeterson1.puzzles.square1.meta;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzle.templates.SimplePuzzle;
import com.github.sampeterson1.puzzles.square1.pieces.Square1CenterBehavior;
import com.github.sampeterson1.puzzles.square1.pieces.Square1CornerBehavior;
import com.github.sampeterson1.puzzles.square1.pieces.Square1EdgeBehavior;

public class Square1 extends SimplePuzzle {

	private static final int NUM_CENTERS = 2;
	private static final int[] EDGE_POSITIONS = new int[] {0, 3, 6, 9, 12, 15, 18, 21};
	private static final int[] CORNER_POSITIONS = new int[] {1, 4, 7, 10, 13, 16, 19, 22};

	public Square1() {
		super(PuzzleType.SQUARE1, true);
		
		super.createPieces(new Square1CenterBehavior(this), NUM_CENTERS);
		super.createPieces(new Square1EdgeBehavior(this), EDGE_POSITIONS);
		super.createPieces(new Square1CornerBehavior(this), CORNER_POSITIONS);
	}
	
	public Piece getPiece(int position) {
		Piece edge = getEdge(position);
		
		if(edge == null) return getCorner(position);
		return edge;
	}
	
	public Piece getCenter(int position) {
		return super.getPiece(PieceType.CENTER, position);
	}
	
	public Piece getEdge(int position) {
		return super.getPiece(PieceType.EDGE, position);
	}
	
	public Piece getCorner(int position) {
		return super.getPiece(PieceType.CORNER, position);
	}

	@Override
	protected PuzzleMetaFunctions<? extends Puzzle> createMetaFunctions() {
		return new Square1MetaFunctions(this);
	}
	
}
