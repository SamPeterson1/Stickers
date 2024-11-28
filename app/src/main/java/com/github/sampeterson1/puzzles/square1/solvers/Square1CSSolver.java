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

import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.square1.meta.Square1;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;
import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;

public class Square1CSSolver {
	
	private Square1 sq1;
	private Algorithm swapLayers;
	private List<Square1CSCase> cases;
	
	public Square1CSSolver(Square1 sq1) {
		this.sq1 = sq1;
		this.swapLayers = Square1Util.parseAlgorithm("/(6,6)/");
		loadCases("square1/CS_Algs.txt");
	}
	
	private void loadCases(String file) {
		this.cases = new ArrayList<Square1CSCase>();
		
		BufferedReader reader = ResourceLoader.openFile(file);
		String line;
		try {
			while((line = reader.readLine()) != null)
				parseCase(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean[] parseLayerConfiguration(String config) {
		boolean[] retVal = new boolean[config.length()];
		for(int i = 0; i < config.length(); i ++) {
			retVal[i] = (config.charAt(i) == 'c') ? Square1CSCase.CORNER : Square1CSCase.EDGE;
		}
		
		return retVal;
	}
	
	private void parseCase(String line) {
		String[] tokens = line.split(" ");
		
		boolean[] piecesTop = parseLayerConfiguration(tokens[0]);
		boolean[] piecesBottom = parseLayerConfiguration(tokens[1]);

		addCase(piecesTop, piecesBottom);
	}
	
	private void addCase(boolean[] cornersTop, boolean[] cornersBottom) {
		Square1CSCase base = new Square1CSCase(cornersTop, cornersBottom);
		
		cases.add(base);
		cases.add(base.getMirror());
	}

	public Algorithm solve() {	
		sq1.clearMoveLog();
		sq1.setLogMoves(true);
	
		//if the puzzle is already in cube shape, return
		if(tryAllignSquareLayers()) 
			return Square1Util.simplify(sq1.getMoveLog());
		
		//if we can't solve it the first time, swap the layers and try again
		if(!trySolveCS()) {
			sq1.executeAlgorithm(swapLayers);
			trySolveCS();
		}
		
		return Square1Util.simplify(sq1.getMoveLog());
	}
	
	//returns true if solving cubeshape was successful
	private boolean trySolveCS() {
		boolean caseRecognized = false;
		
		for(int i = 0; i < 7; i ++) {
			for(Square1CSCase cubeShapeCase : cases) {
				if(cubeShapeCase.solve(sq1)) {
					caseRecognized = true;
					break;
				}
			}
		}
		
		return caseRecognized;
	}
	
	//returns true if the puzzle is in a cube shape
	private boolean tryAllignSquareLayers() {
		int moves = 0;
		final int maxMoves = 12; //if we turn more than this, we can guarantee that the puzzle is not in a cube shape
		
		while(!Square1Util.topSquare(sq1) && moves < maxMoves) {
			sq1.makeMove(new Move(Axis.SU, true));
			moves ++;
		}
		
		while(!Square1Util.bottomSquare(sq1) && moves < maxMoves) {
			sq1.makeMove(new Move(Axis.SD, true));
			moves ++;
		}
		
		return (moves < maxMoves);
	}
	
}
