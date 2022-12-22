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


package com.github.sampeterson1.puzzles.pyraminx.solvers;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.pyraminx.meta.Pyraminx;
import com.github.sampeterson1.puzzles.pyraminx.util.PyraminxEdgeUtil;

public class PyraminxEdgeSolver {

	private Pyraminx pyr;
	private int edgeSize;
	
	public PyraminxEdgeSolver(Pyraminx pyr) {
		this.pyr = pyr;
		this.edgeSize = pyr.getGroup(PieceType.EDGE, 0).getNumPieces();
	}
	
	private Piece findPiece(Piece root, int targetIndex) {
		for(int i = 0; i < 6; i ++) {
			PieceGroup edge = pyr.getGroup(PieceType.EDGE, i);
			
			Piece originalEdge = edge.getPiece(targetIndex);
			if(matchesRoot(root, originalEdge, targetIndex)) return originalEdge;
			
			Piece reflectedEdge = edge.getPiece(edgeSize - targetIndex - 1);
			if(matchesRoot(root, reflectedEdge, targetIndex)) return reflectedEdge;
		}
		
		return null;
	}
	
	private Piece findUnsolvedSecondaryPiece(Piece root) {
		for(int i = 0; i < 3; i ++) {
			PieceGroup edge = pyr.getGroup(PieceType.EDGE, i);
			for(Piece edgePiece : edge.getPieces()) {
				if((edgePiece.getIndex() % 2 == 0) && root.hasEquivalentColors(edgePiece) && 
						(!root.hasExactColors(edgePiece) || edgePiece.getPosition() != 0)) {
					return edgePiece;
				}
			}
		}
		
		return null;
	}
	
	private Piece findUnsolvedPrimaryPiece(Piece root) {
		for(int i = 0; i < 3; i ++) {
			PieceGroup edge = pyr.getGroup(PieceType.EDGE, i);
			for(Piece edgePiece : edge.getPieces()) {
				if((edgePiece.getIndex() % 2 == 1) && root.hasEquivalentColors(edgePiece) && 
						(!root.hasExactColors(edgePiece) || edgePiece.getPosition() != 0)) {
					return edgePiece;
				}
			}
		}
		
		return null;
	}
	
	private boolean isBottomEdge(Piece edge) {
		return (PyraminxEdgeUtil.getFace(edge.getPosition(), 1) == Axis.PD);
	}
	
	private boolean matchesRoot(Piece root, Piece edge, int targetIndex) {
		if(!root.hasEquivalentColors(edge)) return false;

		int edgeMidpoint = edgeSize / 2;
		
		boolean isCenterEdge = (edgeMidpoint == edge.getIndex());
		boolean sameIndex = (targetIndex == edge.getIndex());
		boolean exactColors = root.hasExactColors(edge);
		
		return (sameIndex == exactColors) || isCenterEdge;
	}

	private void moveSecondaryEdgeToBottom(Piece edge) {
		pyr.pushRotations();
		
		int initialPosition = edge.getPosition();
		
		while(edge.getPosition() != 0)
			pyr.makeRotation(Axis.PD, true);
		
		while(pyr.getGroup(PieceType.EDGE, 3).isSolved())
			pyr.makeMove(new Move(Axis.PD, true));
		
		if(initialPosition == 0) {
			int lLayer = PyraminxEdgeUtil.getLayer(Axis.PL, edge);	
			pyr.makeMove(new Move(Axis.PL, lLayer, true));
			
			int rLayer = PyraminxEdgeUtil.getLayer(Axis.PR, edge);
			pyr.makeMove(new Move(Axis.PR, rLayer, false));
			pyr.makeMove(new Move(Axis.PL, lLayer, false));
			pyr.makeMove(new Move(Axis.PR, rLayer, true));
		} else {
			pyr.makeMove(new Move(Axis.PR, false));
			pyr.makeMove(new Move(Axis.PD, true));
			pyr.makeMove(new Move(Axis.PR, true));
		}
		
		pyr.popRotations();
	}
	
	private void movePrimaryEdgeToBottom(Piece edge) {
		pyr.pushRotations();
		
		int initialPosition = edge.getPosition();
		
		while(edge.getPosition() != 0)
			pyr.makeRotation(Axis.PD, true);	
		
		while(pyr.getGroup(PieceType.EDGE, 3).isSolved())
			pyr.makeMove(new Move(Axis.PD, true));
		
		if(initialPosition == 0) {
			int layer = PyraminxEdgeUtil.getLayer(Axis.PL, edge);
			
			pyr.makeMove(new Move(Axis.PL, layer, true));
			pyr.makeMove(new Move(Axis.PD, false));
			pyr.makeMove(new Move(Axis.PL, true));
			pyr.makeMove(new Move(Axis.PD, true));
			pyr.makeMove(new Move(Axis.PL, layer, false));
			pyr.makeMove(new Move(Axis.PD, false));
			pyr.makeMove(new Move(Axis.PL, false));
			pyr.makeMove(new Move(Axis.PD, true));
		} else {
			pyr.makeMove(new Move(Axis.PR, false));
			pyr.makeMove(new Move(Axis.PD, true));
			pyr.makeMove(new Move(Axis.PR, true));
		}
		
		pyr.popRotations();
	}
	
	private void insertPrimaryEdgeFromBottom(Piece edge, Piece root) {
		if(!edge.hasExactColors(root)) {
			while(edge.getPosition() != 3) pyr.makeMove(new Move(Axis.PD, true));
			int layer = PyraminxEdgeUtil.getLayer(Axis.PL, edge);

			pyr.makeMove(new Move(Axis.PD, false));
			pyr.makeMove(new Move(Axis.PL, true));
			pyr.makeMove(new Move(Axis.PD, true));
			pyr.makeMove(new Move(Axis.PL, layer, true));
			pyr.makeMove(new Move(Axis.PD, false));
			pyr.makeMove(new Move(Axis.PL, false));
			pyr.makeMove(new Move(Axis.PD, true));	
			pyr.makeMove(new Move(Axis.PL, layer, false));
		} else {
			while(edge.getPosition() != 4) pyr.makeMove(new Move(Axis.PD, true));	
			int layer = PyraminxEdgeUtil.getLayer(Axis.PF, edge);

			pyr.makeMove(new Move(Axis.PF, layer, true));
			pyr.makeMove(new Move(Axis.PD, false));
			pyr.makeMove(new Move(Axis.PF, true));
			pyr.makeMove(new Move(Axis.PD, true));
			pyr.makeMove(new Move(Axis.PF, layer, false));
			pyr.makeMove(new Move(Axis.PD, false));
			pyr.makeMove(new Move(Axis.PF, false));
			pyr.makeMove(new Move(Axis.PD, true));
		}
	}

	private void flipEdge(int position) {
		pyr.pushRotations();
		
		if(position == 1) pyr.makeRotation(Axis.PD, false);
		else if(position == 2) pyr.makeRotation(Axis.PD, true);
		
		pyr.makeMove(new Move(Axis.PR, false));
		pyr.makeMove(new Move(Axis.PD, false));
		pyr.makeMove(new Move(Axis.PR, true));
		
		pyr.makeMove(new Move(Axis.PF, false));
		pyr.makeMove(new Move(Axis.PR, true));
		pyr.makeMove(new Move(Axis.PF, true));
		pyr.makeMove(new Move(Axis.PR, false));
		
		pyr.popRotations();
	}
	
	private void insertSecondaryEdgeFromBottom(Piece edge, Piece root) {
		if(!edge.hasExactColors(root)) {
			while(edge.getPosition() != 3) pyr.makeMove(new Move(Axis.PD, true));
			
			int lLayer = PyraminxEdgeUtil.getLayer(Axis.PL, edge);
			pyr.makeMove(new Move(Axis.PL, lLayer, false));
			int dLayer = PyraminxEdgeUtil.getLayer(Axis.PD, edge);
			pyr.makeMove(new Move(Axis.PD, dLayer, false));
			pyr.makeMove(new Move(Axis.PL, lLayer, true));
			pyr.makeMove(new Move(Axis.PD, dLayer, true));
		} else {
			while(edge.getPosition() != 4) pyr.makeMove(new Move(Axis.PD, true));
			
			int lLayer = PyraminxEdgeUtil.getLayer(Axis.PL, edge);
			pyr.makeMove(new Move(Axis.PL, lLayer, true));
			int dLayer = PyraminxEdgeUtil.getLayer(Axis.PD, edge);
			pyr.makeMove(new Move(Axis.PD, dLayer, false));
			pyr.makeMove(new Move(Axis.PL, lLayer, false));
			pyr.makeMove(new Move(Axis.PD, dLayer, true));
		}
	}

	private void solveEdgePrimaries() {
		PieceGroup originEdge = pyr.getGroup(PieceType.EDGE, 0);
		Piece root = originEdge.getPiece(1);
		
		for(int targetIndex = 1; targetIndex < edgeSize; targetIndex += 2) {
			Piece edgeInPlace = originEdge.getPiece(targetIndex);
			if(!edgeInPlace.hasExactColors(root)) {
				Piece targetEdge = findPiece(root, targetIndex);
				if(targetEdge != null) {
					if(!isBottomEdge(targetEdge))
						movePrimaryEdgeToBottom(targetEdge);
					
					insertPrimaryEdgeFromBottom(targetEdge, root);
				}
			}
		}
	}
	
	private void solveEdgeSecondaries() {
		PieceGroup originEdge = pyr.getGroup(PieceType.EDGE, 0);
		Piece root = originEdge.getPiece(1);
		
		for(int targetIndex = 0; targetIndex < edgeSize; targetIndex +=2) {
			Piece edgeInPlace = originEdge.getPiece(targetIndex);
			if(!edgeInPlace.hasExactColors(root)) {
				Piece targetEdge = findPiece(root, targetIndex);
				if(targetEdge != null) {
					if(!isBottomEdge(targetEdge))
						moveSecondaryEdgeToBottom(targetEdge);
					
					insertSecondaryEdgeFromBottom(targetEdge, root);
				}
			}
		}
		
		originEdge.setSolved(true);
	}
	
	private void firstThreeEdges() {
		for(int i = 0; i < 3; i ++) {
			solveEdgePrimaries();
			solveEdgeSecondaries();
			
			while(pyr.getGroup(PieceType.EDGE, 3).isSolved())
				pyr.makeMove(new Move(Axis.PD, true));
			
			pyr.makeMove(new Move(Axis.PR, false));
			pyr.makeMove(new Move(Axis.PD, true));
			pyr.makeMove(new Move(Axis.PR, true));
		}
	}
	
	private void lastEdgeSecondaries() {
		PieceGroup originEdge = pyr.getGroup(PieceType.EDGE, 0);
		Piece root = originEdge.getPiece(1);
		
		Piece targetEdge;
		while((targetEdge = findUnsolvedSecondaryPiece(root)) != null) {
			int position = targetEdge.getPosition();
			if(position != 0 || !targetEdge.hasExactColors(root)) { 
				int layer = PyraminxEdgeUtil.getLayer(Axis.PD, targetEdge);
				int invLayer = pyr.getSize() - layer - 1;
				
				if(position == 0) {
					lastEdgeSecondaryInsertPos2(layer);
					position = targetEdge.getPosition();
					layer = PyraminxEdgeUtil.getLayer(Axis.PD, targetEdge);
				}
				
				boolean flipped = !root.hasExactColors(targetEdge);
				if(!flipped) flipEdge(position);
				
				invLayer = pyr.getSize() - PyraminxEdgeUtil.getLayer(Axis.PD, targetEdge) - 1;
				
				if(position == 1) {
					lastEdgeSecondaryInsertPos1(invLayer);
				} else if(position == 2) {
					lastEdgeSecondaryInsertPos2(invLayer);
				}
			}
		}
	}
	
	private void edgeCenterCycle(boolean cw) {
		int centerLayer = pyr.getSize() / 2;
		
		for(int i = 0; i < 3; i ++) {
			pyr.makeMove(new Move(Axis.PL, centerLayer, (i % 2 == 1)));
			pyr.makeMove(new Move(Axis.PD, centerLayer, cw));
		}	
	
		pyr.makeMove(new Move(Axis.PL, centerLayer, true));
	}
	
	private boolean centerMatches(int position) {
		PieceGroup edge = pyr.getGroup(PieceType.EDGE, position);
		return edge.getPiece(1).hasExactColors(edge.getPiece(edgeSize / 2));
	}
	
	private void solveLastEdgeCenters() {
		PieceGroup originEdge = pyr.getGroup(PieceType.EDGE, 0);

		Piece rootPos1 = pyr.getGroup(PieceType.EDGE, 1).getPiece(1);
		Piece rootPos2 = pyr.getGroup(PieceType.EDGE, 2).getPiece(1);
		Piece centerEdge = originEdge.getPiece(edgeSize/2);
		
		if(centerEdge.hasEquivalentColors(rootPos1)) {
			edgeCenterCycle(false);
		} else if(centerEdge.hasEquivalentColors(rootPos2)) {
			edgeCenterCycle(true);
		}
		
		
		int iters = 0;
		while((iters++) < 3) {
			pyr.makeRotation(Axis.PD, true);
			if(!centerMatches(0) && !centerMatches(2)) {
				lastEdgeSecondaryInsertPos2(pyr.getSize() / 2);
				break;
			}
		}
	}
	
	private void lastEdgeSecondaryInsertPos1(int layer) {
		int invLayer = pyr.getSize() - layer - 1;
		
		pyr.makeMove(new Move(Axis.PL, invLayer, true));
		pyr.makeMove(new Move(Axis.PD, layer, false));
		pyr.makeMove(new Move(Axis.PL, invLayer, false));
		pyr.makeMove(new Move(Axis.PD, layer, true));
		
		pyr.makeMove(new Move(Axis.PL, invLayer, false));
		pyr.makeMove(new Move(Axis.PF, layer, true));
		pyr.makeMove(new Move(Axis.PL, invLayer, true));
		pyr.makeMove(new Move(Axis.PF, layer, false));	
	}
	
	private void lastEdgeSecondaryInsertPos2(int layer) {
		int invLayer = pyr.getSize() - layer - 1;
		
		pyr.makeMove(new Move(Axis.PL, invLayer, false));
		pyr.makeMove(new Move(Axis.PD, layer, true));
		pyr.makeMove(new Move(Axis.PL, invLayer, true));
		pyr.makeMove(new Move(Axis.PD, layer, false));
		
		pyr.makeMove(new Move(Axis.PL, invLayer, true));
		pyr.makeMove(new Move(Axis.PR, layer, false));
		pyr.makeMove(new Move(Axis.PL, invLayer, false));
		pyr.makeMove(new Move(Axis.PR, layer, true));
	}
	
	private void lastEdgePrimaries() {
		PieceGroup originEdge = pyr.getGroup(PieceType.EDGE, 0);
		int rootIndex = edgeSize/2;

		Piece root = originEdge.getPiece(rootIndex).clone();
		
		Piece targetEdge;
		while((targetEdge = findUnsolvedPrimaryPiece(root)) != null) {
			int position = targetEdge.getPosition();
			if(position != 0 || !targetEdge.hasExactColors(root)) { 
				int layer = PyraminxEdgeUtil.getLayer(Axis.PD, targetEdge);
				
				if(position == 0) {
					pyr.makeMove(new Move(Axis.PD, layer, false));
					position = targetEdge.getPosition();
					flipEdge(position);
					pyr.makeMove(new Move(Axis.PD, layer, true));
				}
								
				boolean flipped = !root.hasExactColors(targetEdge);
				if(!flipped) flipEdge(position);
				
				int flippedLayer = pyr.getSize() - PyraminxEdgeUtil.getLayer(Axis.PD, targetEdge) - 2;
				
				if(position == 1) {
					pyr.makeMove(new Move(Axis.PD, flippedLayer, true));
					flipEdge(position);
					pyr.makeMove(new Move(Axis.PD, flippedLayer, false));
				} else if(position == 2) {
					pyr.makeMove(new Move(Axis.PD, flippedLayer, false));
					flipEdge(position);
					pyr.makeMove(new Move(Axis.PD, flippedLayer, true));
				}	
			}
		}
	}
	
	private void lastThreeEdges() {
		for(int i = 0; i < 2; i ++) {
			lastEdgePrimaries();
			pyr.makeRotation(Axis.PD, true);
		}
		
		if(pyr.getSize() % 2 == 1)
			solveLastEdgeCenters();

		for(int i = 0; i < 2; i ++) {
			lastEdgeSecondaries();
			pyr.makeRotation(Axis.PD, true);
		}
	}
	
	public Algorithm solve() {
		pyr.setLogMoves(true);
		pyr.clearMoveLog();
		pyr.pushRotations();

		firstThreeEdges();
		lastThreeEdges();
		
		pyr.setLogMoves(false);
		pyr.popRotations();
		
		Algorithm alg = pyr.getMoveLog();
		
		return alg;
	}
	
}
