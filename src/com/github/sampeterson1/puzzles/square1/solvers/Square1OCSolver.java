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

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzles.square1.pieces.Square1;

public class Square1OCSolver {
	
	private static final Color W = Color.WHITE;
	private static final Color Y = Color.YELLOW;
	
	private Square1 sq1;
	private List<Square1OCCase> cases;
	
	public Square1OCSolver(Square1 sq1) {
		this.sq1 = sq1;
		this.cases = new ArrayList<Square1OCCase>();
		
		addCase("(1,0)/(-1,0)", Y, Y, W, W, W, W, Y, Y);
		addCase("(1,0)/(-3,0)/(-1,0)", W, W, W, Y, W, Y, Y, Y);
		addCase("(1,0)/(-3,-3)/(-1,0)", W, Y, W, Y, W, Y, W, Y);
		addCase("(1,0)/(0,-3)/(0,-3)/(-1,0)", Y, Y, W, W, Y, W, Y, W);
		addCase("(1,0)/(-3,0)/(-3,0)/(-1,0)", Y, W, Y, W, W, W, Y, Y);
		addCase("(1,0)/(-3,0)/(-3,0)/(-1,0)", Y, W, Y, W, W, W, Y, Y);
		addCase("/(6,6)/(-1,1)", Y, Y, Y, Y, W, W, W, W);
		addCase("", W, W, W, W, Y, Y, Y, Y);
	}
	
	private void addCase(String solution, Color... cornerColors) {
		cases.add(new Square1OCCase(solution, cornerColors));
	}
	
	public Algorithm solve() {
		sq1.clearMoveLog();
		sq1.setLogMoves(true);
		
		for(Square1OCCase ocCase : cases) {
			if(ocCase.solve(sq1)) {
				break;
			}
		}
		
		return sq1.getMoveLog();
	}
	
}
