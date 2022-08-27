package com.github.sampeterson1.square1;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;

public class Square1OECase {
	
	private Algorithm solution;
	private Color[] cornerColors;
	
	public Square1OECase(String solutionStr, Color[] cornerColors) {
		this.solution = Square1Util.parseAlgorithm(solutionStr);
		this.cornerColors = cornerColors;
	}
	
	public Algorithm getSolution() {
		return this.solution;
	}
	
	private boolean topMatches(Square1 sq1) {
		for(int i = 0; i < 12; i += 3) {
			Color actualColor = sq1.getPiece(i).getColor(1);
			Color solutionColor = cornerColors[i / 3];
			if(actualColor != solutionColor) return false;
		}
		
		return true;
	}
	
	private boolean bottomMatches(Square1 sq1) {
		for(int i = 12; i < 24; i += 3) {
			Color actualColor = sq1.getPiece(i).getColor(1);
			Color solutionColor = cornerColors[i / 3];
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
