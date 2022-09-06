package com.github.sampeterson1.puzzles.pyraminx.solvers;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzles.pyraminx.pieces.Pyraminx;

public class PyraminxRingSolver {

	private Pyraminx pyr;
	
	public PyraminxRingSolver(Pyraminx pyr) {
		this.pyr = pyr;
	}
	
	private List<Color> intersect(Iterable<Color> a, Iterable<Color> b) {
		List<Color> retVal = new ArrayList<Color>();
		for(Color aColor : a) {
			for(Color bColor : b) { 
				if(aColor == bColor)
					retVal.add(aColor);
			}
		}
		
		return retVal;
	}
	
	private List<Color> getInnerCornerColors(int position) {
		Piece innerCorner = getInnerCorner(position);
		List<Color> colors = new ArrayList<Color>();
		
		for(int i = 0; i < 3; i ++) 
			colors.add(innerCorner.getColor(i));
		
		return colors;
	}
	
	private Piece getInnerCorner(int position) {
		return pyr.getGroup(PieceType.CORNER, position).getPiece(0);
	}
	
	private Piece getOuterCorner(int position) {
		return pyr.getGroup(PieceType.CORNER, position).getPiece(1);
	}
	
	private Color getBottomCornerColor() {
		List<Color> colors = intersect(getInnerCornerColors(1), getInnerCornerColors(2));
		colors = intersect(colors, getInnerCornerColors(3));
		
		return colors.get(0);
	}
	
	private void solveBottomCorners() {
		Color color = getBottomCornerColor();
		System.out.println("Common bottom color: " + color);
		
		for(int i = 1; i <= 3; i ++) {
			Piece corner = getInnerCorner(i);
			
			while(corner.getColor(1) != color) {
				System.out.println("Rotating corner position: " + i);
				for(int j = 0; j < pyr.getSize() - 1; j ++)
					pyr.makeMove(new Move(Axis.PL, true));
			}
			
			pyr.makeRotation(Axis.PD, true);
		}
	}
	
	public Algorithm solve() {
		pyr.setLogMoves(true);
		pyr.clearMoveLog();
		pyr.pushRotations();
		
		solveBottomCorners();
		
		pyr.setLogMoves(false);
		pyr.popRotations();
		
		Algorithm alg = pyr.getMoveLog();
		
		return alg;
	}
	
}

