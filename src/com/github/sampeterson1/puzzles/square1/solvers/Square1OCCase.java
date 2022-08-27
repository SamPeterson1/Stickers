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

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzles.square1.pieces.Square1;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;

public class Square1OCCase {
	
	private Algorithm solution;
	private Color[] cornerColors;
	
	public Square1OCCase(String solutionStr, Color[] cornerColors) {
		this.solution = Square1Util.parseAlgorithm(solutionStr);
		this.cornerColors = cornerColors;
	}
	
	public Algorithm getSolution() {
		return this.solution;
	}
	
	private boolean topMatches(Square1 sq1) {
		for(int i = 1; i < 12; i += 3) {
			Color actualColor = sq1.getPiece(i).getColor(1);
			Color solutionColor = cornerColors[(i - 1) / 3];
			if(actualColor != solutionColor) return false;
		}
		
		return true;
	}
	
	private boolean bottomMatches(Square1 sq1) {
		for(int i = 13; i < 24; i += 3) {
			Color actualColor = sq1.getPiece(i).getColor(1);
			Color solutionColor = cornerColors[(i - 1) / 3];
			if(actualColor != solutionColor) return false;
		}
		
		return true;
	}
	
	public boolean solve(Square1 sq1) {
		int numU = 0;
		for(int i = 0; i < 4; i ++) {
			if(!topMatches(sq1)) {
				sq1.makeMove(new Move(Axis.SU, true));
				sq1.makeMove(new Move(Axis.SU, true));
				sq1.makeMove(new Move(Axis.SU, true));
				numU += 3;
				if(i == 3) {
					sq1.clearMoveLog();
					return false;
				}
			} else {
				break;
			}
		}
		
		for(int i = 0; i < 4; i ++) {
			if(!bottomMatches(sq1)) {
				sq1.makeMove(new Move(Axis.SD, true));
				sq1.makeMove(new Move(Axis.SD, true));
				sq1.makeMove(new Move(Axis.SD, true));
				if(i == 3) {
					for(int j = 0; j < numU; j ++) {
						sq1.makeMove(new Move(Axis.SU, false));
					}
					sq1.clearMoveLog();
					return false;
				}
			} else {
				break;
			}
		}
		
		sq1.executeAlgorithm(solution);
		return true;
	}
	
}
