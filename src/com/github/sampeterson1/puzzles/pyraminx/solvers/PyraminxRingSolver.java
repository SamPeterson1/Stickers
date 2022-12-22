package com.github.sampeterson1.puzzles.pyraminx.solvers;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.pyraminx.meta.Pyraminx;

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
		
		for(int i = 0; i < 3; i ++) {
			Piece corner = getInnerCorner(1);
			
			while(corner.getColor(1) != color) {
				makeWideMove(Axis.PL, true);
			}
			
			pyr.makeRotation(Axis.PD, true);
		}
	}
	
	private Piece findEdgePiece(Color a, Color b) {
		for(int i = 0; i < 6; i ++) {
			Piece edge = pyr.getGroup(PieceType.EDGE, i).getPiece();
			if(edge.hasColors(a, b))
				return edge;
		}
		
		return null;
	}
	
	private void makeWideMove(Axis axis, boolean cw) {
		for(int i = 1; i < pyr.getSize(); i ++) {
			pyr.makeMove(new Move(axis, i, cw));
		}
	}
	
	private void solveBottomEdge() {
		Piece referenceCorner = getInnerCorner(1);
		Color frontColor = referenceCorner.getColor(0);
		Color bottomColor = referenceCorner.getColor(1);
		
		Piece edge = findEdgePiece(frontColor, bottomColor);
		System.out.println(edge);
		int position = edge.getPosition();
		
		if(position == 3 && edge.matchesColors(frontColor, bottomColor)) return;
		
		if(position >= 3) {
			pyr.pushRotations();
			
			while(edge.getPosition() != 3)
				pyr.makeRotation(Axis.PD, true);
			
			makeWideMove(Axis.PL, false);
			makeWideMove(Axis.PD, false);
			makeWideMove(Axis.PL, true);
			
			pyr.popRotations();
		}

		System.out.println(edge.getPosition());
		
		if(edge.getColor(0) == bottomColor) {
			while(edge.getPosition() != 0)
				makeWideMove(Axis.PD, false);

			makeWideMove(Axis.PL, true);
			makeWideMove(Axis.PR, false);
			makeWideMove(Axis.PL, false);
			makeWideMove(Axis.PR, true);
		} else {
			while(edge.getPosition() != 2)
				makeWideMove(Axis.PD, false);
			
			makeWideMove(Axis.PR, false);
			makeWideMove(Axis.PL, true);
			makeWideMove(Axis.PR, true);
			makeWideMove(Axis.PL, false);
		}
	}
	
	private boolean edgeAligned(int position) {
		pyr.pushRotations();

		Piece edge = pyr.getGroup(PieceType.EDGE, position).getPiece();
		
		while(edge.getPosition() != 0)
			pyr.makeRotation(Axis.PD, true);
		
		Piece referenceCorner = getInnerCorner(1);
		Color leftColor = referenceCorner.getColor(0);
		Color rightColor = referenceCorner.getColor(2);
		
		System.out.println(leftColor + " " + rightColor + " " + edge);
		
		pyr.popRotations();
		
		return edge.matchesColors(leftColor, rightColor);
	}
	
	private void alignOneEdge() {
		while(true) {
			makeWideMove(Axis.PD, true);

			for(int i = 0; i < 3; i ++) {
				if(edgeAligned(i)) return;
			}
		}
	}
	
	private boolean hasSolvedCenter(Axis face) {
		return (getEdgeColor(face) == getCenterColor(face));
	}
	
	private Color getCenterColor(Axis face) {
		return pyr.getGroup(PieceType.CENTER, face).getPiece().getColor();
	}
	
	private Color getEdgeColor(Axis face) {
		if(face == Axis.PF) {
			return pyr.getGroup(PieceType.EDGE, 0).getPiece().getColor(0);
		} else if(face == Axis.PD) {
			return pyr.getGroup(PieceType.EDGE, 3).getPiece().getColor(1);
		} else if(face == Axis.PR) {
			return pyr.getGroup(PieceType.EDGE, 0).getPiece().getColor(1);
		} else if(face == Axis.PL) {
			return pyr.getGroup(PieceType.EDGE, 2).getPiece().getColor(0);
		}
		
		return null;
	}
	
	private int getNumSolvedCenters() {
		int num = 0;
		for(Axis face : Pyraminx.faces) {
			if(hasSolvedCenter(face))
				num ++;
		}
		
		return num;
	}
	
	private void twoCenterSwap() {
		while(getEdgeColor(Axis.PF) != getCenterColor(Axis.PD))
			pyr.makeRotation(Axis.PF, true);
		
		for(int i = 0; i < 3; i ++) {
			makeWideMove(Axis.PL, true);
			makeWideMove(Axis.PR, false);
			makeWideMove(Axis.PL, false);
			makeWideMove(Axis.PR, true);
		}
	}
	
	private void solveCenters() {
		int numSolved = getNumSolvedCenters();
		
		if(numSolved == 1) {
			System.out.println("((:");
			if(hasSolvedCenter(Axis.PF)) {
				pyr.makeRotation(Axis.PL, true);
			} else if(hasSolvedCenter(Axis.PR)) {
				pyr.makeRotation(Axis.PF, true);
			} else if(hasSolvedCenter(Axis.PL)) {
				pyr.makeRotation(Axis.PF, false);
			}
			
			boolean cw = (getCenterColor(Axis.PL) == getEdgeColor(Axis.PF));
			
			for(int i = 0; i < 2; i ++) {
				makeWideMove(Axis.PL, false);
				makeWideMove(Axis.PD, cw);
				makeWideMove(Axis.PL, true);
				makeWideMove(Axis.PD, cw);
			}
			
			twoCenterSwap();
		} if(numSolved == 0) {
			twoCenterSwap();
		}
	}
	
	private void orientCorner() {
		Color frontColor = getCenterColor(Axis.PF);
		Piece topCorner = getInnerCorner(0);
		
		if(topCorner.getColor(0) != frontColor) {
			boolean cw = (topCorner.getColor(2) == frontColor);
			int layer = pyr.getSize() - 2;
			
			for(int i = 0; i < 2; i ++) {
				makeWideMove(Axis.PL, false);
				pyr.makeMove(new Move(Axis.PD, layer, cw));
				makeWideMove(Axis.PL, true);
				pyr.makeMove(new Move(Axis.PD, layer, cw));
			}
		}
		
		Piece tip = getOuterCorner(0);
		while(tip.getColor(0) != topCorner.getColor(0))
			pyr.makeMove(new Move(Axis.PD, pyr.getSize() - 1, true));
	}
	
	private void solveTopEdges() {
		alignOneEdge();
		
		int iters = 0;
		while((iters++) < 3) {
			pyr.makeRotation(Axis.PD, true);
			
			if(!edgeAligned(0) && !edgeAligned(2)) {
				makeWideMove(Axis.PL, false);
				makeWideMove(Axis.PD, true);
				makeWideMove(Axis.PL, true);
				makeWideMove(Axis.PD, false);
				
				makeWideMove(Axis.PL, true);
				makeWideMove(Axis.PR, false);
				makeWideMove(Axis.PL, false);
				makeWideMove(Axis.PR, true);
				
				break;
			}
		}
		
	}
	
	public Algorithm solve() {
		pyr.setLogMoves(true);
		pyr.clearMoveLog();
		pyr.pushRotations();
		
		solveBottomCorners();
		
		for(int i = 0; i < 3; i ++) {
			solveBottomEdge();
			pyr.makeRotation(Axis.PD, true);
		}
		
		solveTopEdges();
		System.out.println("solving centers");
		solveCenters();
		
		orientCorner();
		pyr.makeRotation(Axis.PF, true);
		
		for(int i = 0; i < 3; i ++) {
			orientCorner();
			pyr.makeRotation(Axis.PL, true);
		}

		pyr.setLogMoves(false);
		pyr.popRotations();
		
		Algorithm alg = pyr.getMoveLog();
		
		return alg;
	}
	
}

