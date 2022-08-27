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

package com.github.sampeterson1.puzzles.ivyCube.pieces;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;

//Defines how a corner piece moves on an Ivy Cube
public class IvyCubeCornerBehavior extends SimplePieceBehavior {

	private static final Color[][] colors = {
			{Color.WHITE, Color.GREEN, Color.ORANGE},
			{Color.WHITE, Color.BLUE, Color.RED},
			{Color.YELLOW, Color.GREEN, Color.RED},
			{Color.YELLOW, Color.BLUE, Color.ORANGE}
	};
	
	public IvyCubeCornerBehavior(Puzzle puzzle) {
		super(PieceType.CORNER, puzzle);
	}
	
	@Override
	public Piece createPiece(int position) {
		Piece corner = new Piece(super.getPuzzle(), PieceType.CORNER, position);
		for(int i = 0; i < 3; i ++) corner.setColor(i, colors[position][i]);
		
		return corner;
	}

	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		Axis axis = move.getAxis();
		int affectedPosition = 0;
		
		if(axis == Axis.IR) {
			affectedPosition = IvyCube.R_CORNER;
		} else if(axis == Axis.IL) {
			affectedPosition = IvyCube.L_CORNER;
		} else if(axis == Axis.ID) {
			affectedPosition = IvyCube.D_CORNER;
		} else if(axis == Axis.IB) {
			affectedPosition = IvyCube.B_CORNER;
		}
		
		return (piece.getPosition() == affectedPosition);
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		Color[] copy = new Color[] {piece.getColor(0), piece.getColor(1), piece.getColor(2)};
		
		if(move.isCW()) {
			//rotate the colors on the piece clockwise
			piece.setColor(0, copy[2]);
			piece.setColor(1, copy[0]);
			piece.setColor(2, copy[1]);
		} else {
			//rotate the colors on the piece counterclockwise
			piece.setColor(0, copy[1]);
			piece.setColor(1, copy[2]);
			piece.setColor(2, copy[0]);	
		}
	}

}
