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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzles.square1.meta.Square1;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;
import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;

public class Square1OLSolver {
	
	private Square1 sq1;
	private List<Square1OLCase> cases;
	
	public Square1OLSolver(Square1 sq1) {
		this.sq1 = sq1;
		this.cases = new ArrayList<Square1OLCase>();
		
		loadCases("square1/OL_Algs.txt");
	}

	private void loadCases(String file) {
		this.cases = new ArrayList<Square1OLCase>();
		
		BufferedReader reader = ResourceLoader.openFile(file);
		String line;
		try {
			while((line = reader.readLine()) != null)
				cases.add(parseCase(line));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Color[] parseColors(String colorsStr) {
		Color[] colors = new Color[16];
		for(int i = 0; i < 16; i ++) {
			char c = colorsStr.charAt(i);
			if(c == 'w') colors[i] = Color.WHITE;
			else if(c == 'y') colors[i] = Color.YELLOW;
			else if(c == '-') colors[i] = Color.NONE;
			
			System.out.print(colors[i] + " ");
		}
		System.out.println();
		
		return colors;
	}
	
	private Square1OLCase parseCase(String line) {
		String[] tokens = line.split(" ");
		Color[] colors = parseColors(tokens[0]);
		String solution = tokens[1];
		
		return new Square1OLCase(solution, colors);
	}
	
	public Algorithm solve() {
		sq1.clearMoveLog();
		sq1.setLogMoves(true);
		
		for(int i = 0; i < 2; i ++) {
			int j = 0;
			for(Square1OLCase olCase : cases) {
				j ++;
				if(olCase.solve(sq1)) {
					System.out.println("ol solve line " + j);
					break;
				}
			}
		}
		System.out.println("solved");
		
		return Square1Util.simplify(sq1.getMoveLog());
	}
	
}
