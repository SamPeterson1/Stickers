package com.github.sampeterson1.square1;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;

public class Square1CubeShapeCase {
	
	public static final boolean EDGE = false;
	public static final boolean CORNER = true;
	
	private boolean[] cornersTop;
	private boolean[] cornersBottom;
	
	private Algorithm solution;
	
	private Square1CubeShapeCase(Algorithm solution, boolean[] cornersTop, boolean[] cornersBottom) {
		this.solution = solution;
		this.cornersTop = cornersTop;
		this.cornersBottom = cornersBottom;
	}
	
	public Square1CubeShapeCase(String solutionStr, boolean[] cornersTop, boolean[] cornersBottom) {
		this.solution = Square1Util.parseAlgorithm(solutionStr);
		this.cornersTop = cornersTop;
		this.cornersBottom = cornersBottom;
	}
	
	public Square1CubeShapeCase getFlip() {
		Algorithm flipSolution = Square1Util.flip(solution);
		return new Square1CubeShapeCase(flipSolution, cornersBottom, cornersTop);
	}

	public void print() {
		for(boolean b : cornersTop) System.out.println(b + " ");
		System.out.println();
		for(boolean b : cornersBottom) System.out.println(b + " ");
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
		System.out.println();
		for(int i = 12; i < 24; i ++) {
			Piece piece = sq1.getPiece(i);
			if(piece != null) {
				boolean isCorner = (piece.getType() == PieceType.CORNER);
				if(index == cornersBottom.length) return false;
				boolean shouldBeCorner = cornersBottom[index++];
				System.out.println(isCorner + " " + shouldBeCorner);
				if(isCorner != shouldBeCorner) return false;
			}
		}
		
		return true;
	}
	
	public Algorithm getSolution() {
		return this.solution;
	}
	
	public boolean solve(Square1 sq1) {
		int numUMoves = 0;
		for(int i = 0; i < 12; i ++) {
			if(!topMatches(sq1)) {
				numUMoves ++;
				sq1.makeMove(new Move(Axis.SU, true));
				if(i == 11) {
					sq1.clearMoveLog();
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
					for(int j = 0; j < numUMoves; j ++) sq1.makeMove(new Move(Axis.SU, false));
					sq1.clearMoveLog();
					return false;
				}
			} else {
				break;
			}
		}
		
		System.out.println(solution.length());
		sq1.executeAlgorithm(solution);
		return true;
	}
	
}
