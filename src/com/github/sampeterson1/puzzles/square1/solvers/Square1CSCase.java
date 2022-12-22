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

package com.github.sampeterson1.puzzles.square1.solvers;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.square1.meta.Square1;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;

public class Square1CSCase {
	
	public static final boolean EDGE = false;
	public static final boolean CORNER = true;
	
	private boolean mirrored;
	private boolean[] cornersTop;
	private boolean[] cornersBottom;

	public Square1CSCase(boolean[] cornersTop, boolean[] cornersBottom) {
		this.cornersTop = cornersTop;
		this.cornersBottom = cornersBottom;
		this.mirrored = false;
	}
	
	public Square1CSCase getMirror() {
		Square1CSCase mirror = new Square1CSCase(mirror(cornersTop), mirror(cornersBottom));
		mirror.mirrored = true;
		
		return mirror;
	}
	
	private boolean[] mirror(boolean[] corners) {	
		boolean[] mirroredCorners = new boolean[corners.length];
		int cornerIndex = 0;
		
		for(int i = corners.length - 1; i >= 0; i --) {
			mirroredCorners[cornerIndex++] = corners[i]; 
		}
		
		return mirroredCorners;
	}
	
	private boolean topMatches(Square1 sq1) {
		if(Square1Util.topLocked(sq1)) return false;

		int index = 0;
		for(int i = 0; i < 12; i ++) {
			Piece piece = sq1.getPiece(i);
			if(piece != null) {
				boolean isCorner = (piece.getType() == PieceType.CORNER);
				if(index == cornersTop.length) return false;
				boolean shouldBeCorner = cornersTop[index++];	

				if(isCorner != shouldBeCorner) return false;
			}
		}
		
		return true;
	}
	
	private boolean bottomMatches(Square1 sq1) {
		if(Square1Util.bottomLocked(sq1)) return false;
		
		int index = 0;
		for(int i = 12; i < 24; i ++) {
			Piece piece = sq1.getPiece(i);
			if(piece != null) {
				boolean isCorner = (piece.getType() == PieceType.CORNER);
				if(index == cornersBottom.length) return false;
				boolean shouldBeCorner = cornersBottom[index++];

				if(isCorner != shouldBeCorner) return false;
			}
		}
		
		return true;
	}
	
	public boolean solve(Square1 sq1) {
		for(int i = 0; i < 12; i ++) {
			if(!topMatches(sq1)) {
				sq1.makeMove(new Move(Axis.SU, true));
				if(i == 11) {
					return false;
				}
				
			} else {
				break;
			}
		}
		
		for(int i = 0; i < 12; i ++) {
			if(!bottomMatches(sq1)) {
				sq1.makeMove(new Move(Axis.SD, true));
				if(i == 11) {
					return false;
				}
			} else {
				break;
			}
		}

		if(mirrored) {
			for(int i = 0; i < 6; i ++) sq1.makeMove(new Move(Axis.SU, true));
			for(int i = 0; i < 6; i ++) sq1.makeMove(new Move(Axis.SD, true));
		}
		
		sq1.makeMove(new Move(Axis.S1, true));
		return true;
	}
	
}
