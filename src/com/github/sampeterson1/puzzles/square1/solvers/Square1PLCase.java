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
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.square1.meta.Square1;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;

public class Square1PLCase {
	
	private static Algorithm layerSwapAlg = Square1Util.parseAlgorithm("/(6,6)/(-1,1)");
	private static Square1 solvedReference = new Square1();
	private Algorithm solution;

	private int[] positions;
	
	public Square1PLCase(String solution, int[] positions) {
		this.solution = Square1Util.parseAlgorithm(solution);
		this.positions = positions;
	}
	
	public Algorithm getSolution() {
		return this.solution;
	}
	
	private boolean colorsMatch(Piece a, Piece b) {
		PieceType type = a.getType();
		if(type == PieceType.CORNER) {
			return (a.getColor(0) == b.getColor(0) && a.getColor(2) == b.getColor(2) ||
					a.getColor(2) == b.getColor(0) && a.getColor(0) == b.getColor(2));
		} else if(type == PieceType.EDGE) {
			return (a.getColor(0) == b.getColor(0));
		}
		
		return true;
	}
	
	private boolean topMatches(Square1 sq1) {
		int positionIndex = 0;
		for(int i = 0; i < 12; i ++) {
			Piece piece = sq1.getPiece(i);
			if(piece != null) {
				int position = positions[positionIndex++];
				if(position != -1) {
					Piece solvedPiece = solvedReference.getPiece(position);
					if(!colorsMatch(piece, solvedPiece)) return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean bottomMatches(Square1 sq1) {
		int positionIndex = 8;
		for(int i = 12; i < 24; i ++) {
			Piece piece = sq1.getPiece(i);
			if(piece != null) {
				int position = positions[positionIndex++];
				if(position != -1) {
					Piece solvedPiece = solvedReference.getPiece(position);
					if(!colorsMatch(piece, solvedPiece)) return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean solve(Square1 sq1, boolean swapLayers) {
		
		sq1.pushState();
		solvedReference.pushState();

		int uMoves = 0;
		int dMoves = 0;
		
		if(swapLayers) {
			sq1.executeAlgorithm(layerSwapAlg);
			solvedReference.executeAlgorithm(layerSwapAlg);
		}
		
		boolean solved = false;
		for(int j = 0; j < 4; j ++) {
			for(int i = 0; i < 4; i ++) {
				if(!topMatches(sq1)) {
					uMoves ++;
					sq1.makeMove(new Move(Axis.SU, true).repeated(3));
				} else {
					solved = true;
					break;
				}
			}
			if(solved) break;
			if(j == 3) {
				solvedReference.popState();
				sq1.popState();
				return false;
			}
			solvedReference.makeMove(new Move(Axis.SU, true).repeated(3));
		}		
		
		solved = false;
		for(int j = 0; j < 4; j ++) {		
			for(int i = 0; i < 4; i ++) {
				if(!bottomMatches(sq1)) {
					dMoves ++;
					sq1.makeMove(new Move(Axis.SD, true).repeated(3));
				} else {
					solved = true;
					break;
				}
			}
			if(solved) break;
			if(j == 3) {
				solvedReference.popState();
				sq1.popState();
				return false;
			}
			solvedReference.makeMove(new Move(Axis.SD, true).repeated(3));
		}
		
		sq1.popState();
		solvedReference.popState();
		
		if(swapLayers) {
			sq1.executeAlgorithm(layerSwapAlg);
		}
		
		sq1.makeMove(new Move(Axis.SU, true).repeated(uMoves * 3));
		sq1.makeMove(new Move(Axis.SD, true).repeated(dMoves * 3));
		
		sq1.executeAlgorithm(solution);
		
		if(swapLayers) {
			sq1.executeAlgorithm(layerSwapAlg);
		}
		
		return true;
	}
	
}
