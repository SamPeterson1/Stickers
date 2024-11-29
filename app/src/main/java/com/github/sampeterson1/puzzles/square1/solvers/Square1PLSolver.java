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
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.square1.meta.Square1;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;
import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;

public class Square1PLSolver {
	
	private Square1 sq1;
	private List<Square1PLCase> cases;
	private Algorithm flipEquator;
	
	public Square1PLSolver(Square1 sq1) {
		this.sq1 = sq1;
		this.cases = new ArrayList<Square1PLCase>();
		this.flipEquator = Square1Util.parseAlgorithm("/(6,0)/(6,0)/(6,0)");
		loadCases("square1/PL_Algs.txt");
	}

	private void loadCases(String file) {
		this.cases = new ArrayList<Square1PLCase>();
		
		BufferedReader reader = ResourceLoader.openFile(file);
		String line;
		try {
			while((line = reader.readLine()) != null)
				cases.add(parseCase(line));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int[] parsePositions(String positionStr) {
		int[] positions = new int[16];
		String[] tokens = positionStr.split(",");
		System.out.println(positionStr);
		for(int i = 0; i < 16; i ++) {
			if(tokens[i].equals("-")) {
				positions[i] = -1;
			} else {
				positions[i] = Integer.parseInt(tokens[i]);
			}
		}
		
		return positions;
	}
	
	private Square1PLCase parseCase(String line) {
		String[] tokens = line.split(" ");
		System.out.println("parse");
		System.out.println(line);
		System.out.println(tokens[0]);
		int[] positions = parsePositions(tokens[0]);
		String solution = tokens[1];
		
		return new Square1PLCase(solution, positions);
	}
	
	public Algorithm solve() {
		sq1.clearMoveLog();
		sq1.setLogMoves(true);		

		boolean swapLayers = false;
		for(int i = 0; i < 3; i ++) {
			int caseIndex = 0;
			for(Square1PLCase plCase : cases) {
				caseIndex ++;
				if(plCase.solve(sq1, swapLayers)) {
					System.out.println("Case: " + caseIndex);
					break;
				}
			}
			if(i == 1) {
				swapLayers = true;
			}
		}
		
		while(sq1.getPiece(1).getColor(0) != Color.GREEN) {
			sq1.makeMove(new Move(Axis.SU, true).repeated(3));
		}
		
		while(sq1.getPiece(13).getColor(2) != Color.GREEN) {
			sq1.makeMove(new Move(Axis.SD, true).repeated(3));
		}

		if(sq1.getCenter(0).getColor(0) == Color.BLUE) {
			sq1.executeAlgorithm(flipEquator);
		}
		
		sq1.setLogMoves(false);		

		return Square1Util.simplify(sq1.getMoveLog());
	}
	
}
