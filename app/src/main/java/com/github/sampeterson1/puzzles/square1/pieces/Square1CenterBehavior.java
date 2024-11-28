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

package com.github.sampeterson1.puzzles.square1.pieces;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.square1.meta.Square1;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;

public class Square1CenterBehavior extends SimplePieceBehavior {
	
	public Square1CenterBehavior(Puzzle puzzle) {
		super(PieceType.CENTER, puzzle);
	}

	@Override
	public Piece createPiece(int position) {
		Piece piece = new Piece(super.getPuzzle(), PieceType.SQUARE1_CENTER, position);
		
		if(position == 0) {
			piece.setColor(0, Color.GREEN);
			piece.setColor(1, Color.RED);
			piece.setColor(2, Color.BLUE);
		} else if(position == 1) {
			piece.setColor(0, Color.BLUE);
			piece.setColor(1, Color.ORANGE);
			piece.setColor(2, Color.GREEN);
		}
		
		return piece;
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		if(move.getAxis() == Axis.S1) {
			Color front = piece.getColor(0);
			Color back = piece.getColor(2);
			piece.setColor(0, back);
			piece.setColor(2, front);
		}
	}

	@Override
	public boolean affectedByMove(Move move, Piece piece) {
		return (!Square1Util.isLocked((Square1) super.getPuzzle()) && move.getAxis() == Axis.S1 && piece.getPosition() == 0);
	}

}
