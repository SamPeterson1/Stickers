/*
    PrimePuzzle Twisty Puzzle Simulator
    Copyright (C) 2022 Sam Peterson
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.yoshiapolis.cube.solvers;

import java.util.ArrayList;

import com.yoshiapolis.cube.pieces.Cube;
import com.yoshiapolis.cube.pieces.CubeEdgeUtil;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PuzzlePiece;
import com.yoshiapolis.puzzle.PuzzlePieceGroup;

public class EdgeSolver {
	
	private Cube cube;
	private int edgeSize;
	
	public EdgeSolver(Cube cube) {
		this.cube = cube;
		this.edgeSize = cube.getSize() - 2;
	}

	public Algorithm solve() {
		if(cube.getSize() < 4) return new Algorithm();
		
		cube.setLogMoves(true);
		cube.clearMoveLog();
		cube.pushRotations();
		
		for(int i = 0; i < 8; i ++) {
			solveEdge();
			saveEdge();
		}
		
		restoreCenters();
		
		for(int i = 0; i < 3; i ++) {
			solveBodyEdge();
			cube.makeRotation(Face.U, true);
		}
		
		solveParity();
		
		cube.popRotations();
		Algorithm alg = cube.getMoveLog();
		alg.simplify();
		
		return alg;
	}
	
	private void solveParity() {
		PuzzlePieceGroup edge = cube.getEdge(4);
		PuzzlePiece center = edge.getPiece(edgeSize/2);
		ArrayList<Integer> layers = new ArrayList<Integer>();
		for(int i = 0; i < edgeSize/2; i ++) {
			PuzzlePiece piece = edge.getPiece(i);
			if(flipped(piece, center)) {
				layers.add(edgeSize-piece.getIndex());
			}
		}
		
		if(layers.size() != 0) {
			cube.makeRotation(Face.F, true);
			for(int layer : layers) {
				cube.makeMove(new Move(Face.R, layer, true));
				cube.makeMove(new Move(Face.R, layer, true));
			}
			
			cube.makeMove(new Move(Face.B, 0, true));
			cube.makeMove(new Move(Face.B, 0, true));
			
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeMove(new Move(Face.U, 0, true));
			
			for(int layer : layers) {
				cube.makeMove(new Move(Face.L, layer, true));
			}
			
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeMove(new Move(Face.U, 0, true));
			
			for(int layer : layers) {
				cube.makeMove(new Move(Face.R, layer, false));
			}
			
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeMove(new Move(Face.U, 0, true));
			
			for(int layer : layers) {
				cube.makeMove(new Move(Face.R, layer, true));
			}
			
			cube.makeMove(new Move(Face.U, 0, true));
			cube.makeMove(new Move(Face.U, 0, true));
			
			cube.makeMove(new Move(Face.F, 0, true));
			cube.makeMove(new Move(Face.F, 0, true));
			
			for(int layer : layers) {
				cube.makeMove(new Move(Face.R, layer, true));
			}
			
			cube.makeMove(new Move(Face.F, 0, true));
			cube.makeMove(new Move(Face.F, 0, true));
			
			for(int layer : layers) {
				cube.makeMove(new Move(Face.L, layer, false));
			}
			
			cube.makeMove(new Move(Face.B, 0, true));
			cube.makeMove(new Move(Face.B, 0, true));
			
			for(int layer : layers) {
				cube.makeMove(new Move(Face.R, layer, true));
				cube.makeMove(new Move(Face.R, layer, true));
			}
		}
	}
	
	private boolean sameColors(PuzzlePiece a, PuzzlePiece b) {
		Color a1 = a.getColor(0);
		Color a2 = a.getColor(1);
		Color b1 = b.getColor(0);
		Color b2 = b.getColor(1);
		
		return ((a1 == b1 && a2 == b2) || (a1 == b2 && a2 == b1));
	}
	
	private boolean flipped(PuzzlePiece a, PuzzlePiece b) {
		return (a.getColor(0) == b.getColor(1)) && b.getColor(0) == a.getColor(1);
	}
	
	private ArrayList<PuzzlePiece> findPuzzlePieces(PuzzlePiece root) {
		ArrayList<PuzzlePiece> pieces = new ArrayList<PuzzlePiece>();
		
		for(int pos = 0; pos < 12; pos ++) {
			PuzzlePieceGroup edge = cube.getEdge(pos);
			for(int index = 0; index < edgeSize; index ++) {
				PuzzlePiece piece = edge.getPiece(index);
				if(sameColors(piece, root)) {
					pieces.add(piece);
				}
			}
		}
		
		return pieces;
	}
	
	private void flipEdge(PuzzlePiece piece) {
		if(piece.getPosition() == 4) {
			cube.makeMove(new Move(Face.U, edgeSize-piece.getIndex(), true));
		}
		
		cube.pushRotations();
		while(piece.getPosition() != 5) {
			cube.makeRotation(Face.U, true);
		}
		
		cube.makeMove(new Move(Face.R, 0, true));
		cube.makeMove(new Move(Face.U, 0, true));
		cube.makeMove(new Move(Face.R, 0, false));
		cube.makeMove(new Move(Face.F, 0, true));
		cube.makeMove(new Move(Face.R, 0, false));
		cube.makeMove(new Move(Face.F, 0, false));
		cube.makeMove(new Move(Face.R, 0, true));
		cube.popRotations();
	}
	
	private void insertEdge_U(PuzzlePiece piece, boolean flipped) {
		int pos = piece.getPosition();
		
		if(pos == 3) {
			if(flipped) {
				cube.makeMove(new Move(Face.B, 0, false));
				cube.makeMove(new Move(Face.U, 0, true));
				cube.makeMove(new Move(Face.B, 0, true));
			} else {	
				cube.makeMove(new Move(Face.L, 0, false));
				cube.makeMove(new Move(Face.B, 0, true));
				cube.makeMove(new Move(Face.L, 0, true));
				cube.makeMove(new Move(Face.B, 0, false));
			}
		} else {
			while(piece.getPosition() != 0) {
				cube.makeRotation(Face.U, true);
			}
			
			if(flipped) {
				cube.makeMove(new Move(Face.F, 0, true));
				cube.makeMove(new Move(Face.R, 0, false));
				cube.makeMove(new Move(Face.F, 0, false));
				cube.makeMove(new Move(Face.R, 0, true));
			} else {
				cube.makeMove(new Move(Face.R, 0, true));
				cube.makeMove(new Move(Face.U, 0, false));
				cube.makeMove(new Move(Face.R, 0, false));
			}
		}
	}
	
	private void saveEdge() {
		
		cube.pushRotations();
		
		int numSolved = 0;
		for(int j = 0; j < 4; j ++) {
			if(cube.getEdge(j).isSolved()) numSolved ++;
		}
		
		if(numSolved == 4) {
			cube.makeRotation(Face.F, true);
			cube.makeRotation(Face.F, true);
			cube.makeRotation(Face.U, true);
			numSolved = 0;
			for(int j = 0; j < 4; j ++) {
				if(cube.getEdge(j).isSolved()) numSolved ++;
			}
		}
		
		if(numSolved == 3) {
			while(!cube.getEdge(0).isSolved()) {
				cube.makeMove(new Move(Face.U, 0, true));
			}
		}
		
		cube.makeMove(new Move(Face.F, 0, true));
		while(cube.getEdge(0).isSolved()) {
			cube.makeMove(new Move(Face.U, 0, true));
		}
		cube.makeMove(new Move(Face.F, 0, false));
		
		cube.popRotations();
	}
	
	private void restoreCenters() {
		PuzzlePieceGroup center = cube.getCenter(Face.F);
		Color solvingColor = center.getPiece(0).getColor();
		for(int i = edgeSize; i < edgeSize*edgeSize; i += edgeSize) {
			while(center.getPiece(i).getColor() != solvingColor) {
				cube.makeMove(new Move(Face.U, 1+(i/edgeSize), true));
			}
		}
	}
	
	private boolean containsEdge(PuzzlePiece root, PuzzlePieceGroup edge) {
		for(int i = 0; i < edgeSize; i ++) {
			PuzzlePiece piece = edge.getPiece(i);
			if(flipped(root, piece) || (root.getColor(0) == piece.getColor(0) && root.getColor(1) == piece.getColor(1))) {
				return true;
			}
		}
		
		return false;
	}
	
	private void insertEdges(PuzzlePiece root, PuzzlePieceGroup edge) {
		int turns = edge.getPosition() - 4;
		ArrayList<Integer> layers = new ArrayList<Integer>();
		
		for(int i = 0; i < edgeSize; i ++) {
			PuzzlePiece piece = edge.getPiece(i);
			if(flipped(piece, root)) {
				if(!layers.contains(edgeSize-i)) {
					layers.add(i + 1);
				}
			}
		}
		
		for(int layer : layers) {
			for(int i = 0; i < turns; i ++) {
				cube.makeMove(new Move(Face.U, layer, false));
			}
		}
		flipEdge(edge.getPiece(0));
		for(int layer : layers) {
			for(int i = 0; i < turns; i ++) {
				cube.makeMove(new Move(Face.U, layer, true));
			}
		}
	}
	
	private void solveBodyEdge() {
		PuzzlePiece root = cube.getEdge(4).getPiece(edgeSize/2);
		for(int i = 5; i <= 7; i ++) {
			PuzzlePieceGroup edge = cube.getEdge(i);
			while(containsEdge(root, edge)) {
				insertEdges(root, edge);
			}
		}
		
		PuzzlePieceGroup rootEdge = cube.getEdge(4);
		
		ArrayList<Integer> layers = new ArrayList<Integer>();
		
		for(int i = 0; i < edgeSize; i ++) {
			PuzzlePiece edge = rootEdge.getPiece(i);
			if(root.getColor(0) == edge.getColor(1) && root.getColor(1) == edge.getColor(0)) {
				layers.add(edgeSize-i);
			}
		}
		
		
		PuzzlePieceGroup edge = cube.getEdge(5);
		for(int layer : layers) {
			cube.makeMove(new Move(Face.U, layer, false));
		}
		flipEdge(edge.getPiece(0));
		for(int layer : layers) {
			cube.makeMove(new Move(Face.U, layer, true));
		}
		
	}
	
	private void solveEdge() {
		PuzzlePiece root = cube.getEdge(4).getPiece(0);
		ArrayList<PuzzlePiece> pieces = findPuzzlePieces(root);
		
		for(PuzzlePiece piece : pieces) {
			boolean flipped = flipped(root, piece);
			Face face = CubeEdgeUtil.getFace(piece.getPosition(), 0);
			
			cube.pushRotations();
			if(face == Face.U) { 
				insertEdge_U(piece, flipped);
			} else if(face == Face.D) {
				cube.makeRotation(Face.F, true);
				cube.makeRotation(Face.F, true);
				cube.makeRotation(Face.U, true);
				insertEdge_U(piece, !flipped);
			} else if(flipped) {
				flipEdge(piece);
			}
			cube.popRotations();
			
			while(piece.getPosition() != 4) {
				cube.makeMove(new Move(Face.U, edgeSize-piece.getIndex(), true));
			}
		}
		
		cube.getEdge(4).setSolved(true);
	}
	
}
