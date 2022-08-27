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

package com.github.sampeterson1.puzzle.puzzles.square1.pieces;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;
import com.github.sampeterson1.puzzle.puzzles.square1.util.Square1Util;

public class Square1CornerBehavior extends SimplePieceBehavior {

	private static final Color[][] cornerColors = new Color[][] {
		{Color.GREEN, Color.WHITE, Color.RED},
		{Color.RED, Color.WHITE, Color.BLUE},
		{Color.BLUE, Color.WHITE, Color.ORANGE},
		{Color.ORANGE, Color.WHITE, Color.GREEN},
		{Color.RED, Color.YELLOW, Color.GREEN},
		{Color.BLUE, Color.YELLOW, Color.RED},
		{Color.ORANGE, Color.YELLOW, Color.BLUE},
		{Color.GREEN, Color.YELLOW, Color.ORANGE},
	};
	
	public Square1CornerBehavior(Puzzle puzzle) {
		super(PieceType.CORNER, puzzle);
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		Square1Util.movePiece(move, piece);
	}

	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		return Square1Util.affectedByMove(move, piece);
	}
	
	@Override
	protected Piece createPiece(int position) {
		Piece piece = new Piece(super.getPuzzle(), PieceType.CORNER, position);
		
		Color[] colors = cornerColors[(position - 1)/3];
		for(int i = 0; i < 3; i ++)
			piece.setColor(i, colors[i]);
		
		return piece;
	}

}
