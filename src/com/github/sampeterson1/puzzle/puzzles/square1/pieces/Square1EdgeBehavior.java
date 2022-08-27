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

public class Square1EdgeBehavior extends SimplePieceBehavior {

	private static final Color[][] edgeColors = new Color[][] {
		{Color.GREEN, Color.WHITE},
		{Color.RED, Color.WHITE},
		{Color.BLUE, Color.WHITE},
		{Color.ORANGE, Color.WHITE},
		{Color.GREEN, Color.YELLOW},
		{Color.RED, Color.YELLOW},
		{Color.BLUE, Color.YELLOW},
		{Color.ORANGE, Color.YELLOW},
	};

	public Square1EdgeBehavior(Puzzle puzzle) {
		super(PieceType.EDGE, puzzle);
	}
	
	@Override
	protected Piece createPiece(int position) {
		Piece piece = new Piece(super.getPuzzle(), PieceType.EDGE, position);
		
		Color[] colors = edgeColors[position/3];
		for(int i = 0; i < 2; i ++)
			piece.setColor(i, colors[i]);
		
		return piece;
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		Square1Util.movePiece(move, piece);
	}

	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		return Square1Util.affectedByMove(move, piece);
	}

}
