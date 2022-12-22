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

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.square1.meta.Square1;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;

public class Square1OLCase {
	
	private Algorithm solution;
	private Color[] colors;
	
	public Square1OLCase(String solutionStr, Color[] colors) {
		this.solution = Square1Util.parseAlgorithm(solutionStr);
		this.colors = colors;
	}
	
	public Algorithm getSolution() {
		return this.solution;
	}
	
	private boolean topMatches(Square1 sq1) {
		int colorIndex = 0;
		for(int i = 0; i < 12; i ++) {
			Piece piece = sq1.getPiece(i);
			
			if(piece != null) {
				Color actualColor = sq1.getPiece(i).getColor(1);
				Color solutionColor = colors[colorIndex++];
				if(solutionColor != Color.NONE && actualColor != solutionColor) return false;
			}
		}
		
		return true;
	}
	
	private boolean bottomMatches(Square1 sq1) {
		int colorIndex = 8;
		for(int i = 12; i < 24; i ++) {
			Piece piece = sq1.getPiece(i);
			
			if(piece != null) {
				Color actualColor = sq1.getPiece(i).getColor(1);
				Color solutionColor = colors[colorIndex++];
				if(solutionColor != Color.NONE && actualColor != solutionColor) return false;
			}
		}
		
		return true;
	}
	
	public boolean solve(Square1 sq1) {
		for(int i = 0; i < 4; i ++) {
			if(!topMatches(sq1)) {
				sq1.makeMove(new Move(Axis.SU, true).repeated(3));
				if(i == 3)
					return false;
			} else {
				break;
			}
		}
		
		for(int i = 0; i < 4; i ++) {
			if(!bottomMatches(sq1)) {
				sq1.makeMove(new Move(Axis.SD, true).repeated(3));
				if(i == 3)
					return false;
			} else {
				break;
			}
		}
		
		sq1.executeAlgorithm(solution);
		return true;
	}
	
}
