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

package com.github.sampeterson1.ivyCube;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;

public class IvyCubeCornerBehavior extends SimplePieceBehavior {
	
	private static final PieceType type = PieceType.CORNER;
	
	private static final Color[][] colors = {
			{Color.WHITE, Color.GREEN, Color.ORANGE},
			{Color.WHITE, Color.BLUE, Color.RED},
			{Color.YELLOW, Color.GREEN, Color.RED},
			{Color.YELLOW, Color.BLUE, Color.ORANGE}
	};
	
	@Override
	public Piece createPiece(int position) {
		Piece corner = new Piece(PieceType.CORNER, position);
		for(int i = 0; i < 3; i ++) corner.setColor(i, colors[position][i]);
		
		return corner;
	}

	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		Axis axis = move.getFace();
		int affectedPosition = 0;
		if(axis == Axis.IR) {
			affectedPosition = 2;
		} else if(axis == Axis.IL) {
			affectedPosition = 0;
		} else if(axis == Axis.ID) {
			affectedPosition = 3;
		} else if(axis == Axis.IB) {
			affectedPosition = 1;
		}
		
		return (piece.getPosition() == affectedPosition);
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		Color[] copy = new Color[] {piece.getColor(0), piece.getColor(1), piece.getColor(2)};
		
		if(move.isCW()) {
			piece.setColor(0, copy[2]);
			piece.setColor(1, copy[0]);
			piece.setColor(2, copy[1]);
		} else {
			piece.setColor(0, copy[1]);
			piece.setColor(1, copy[2]);
			piece.setColor(2, copy[0]);	
		}
	}

	@Override
	public PieceType getType() {
		return type;
	}

}
