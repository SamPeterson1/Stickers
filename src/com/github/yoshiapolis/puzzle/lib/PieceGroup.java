/*
    PrimePuzzle Twisty Puzzle Simulator and Solver
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

package com.github.yoshiapolis.puzzle.lib;

import java.util.ArrayList;
import java.util.List;

public class PieceGroup {
	
	private Puzzle puzzle;
	private PieceBehavior behavior;
	private List<Piece> pieces;
	private List<Piece> movedPieces;
	private int position;
	private int puzzleSize;
	
	public PieceGroup(PieceBehavior behavior, Puzzle puzzle, int position) {
		this.position = position;
		this.pieces = new ArrayList<Piece>();
		this.movedPieces = new ArrayList<Piece>();
		this.puzzle = puzzle;
		this.puzzleSize = puzzle.getSize();
		this.behavior = behavior;
		
		int numPieces = behavior.getNumPieces(puzzleSize);
		for(int i = 0; i < numPieces; i ++) {
			pieces.add(behavior.createPiece(position, i, puzzleSize));
		}
	}
	
	public boolean isSolved() {
		for(Piece piece : pieces) {
			if(!piece.isSolved()) return false;
		}
		
		return true;
	}
	
	public int getNumPieces() {
		return this.pieces.size();
	}
	
	public void setSolved(boolean solved) {
		for(Piece piece : pieces) {
			piece.setSolved(true);
		}
	}
	
	public void applyMoves() {
		for(Piece piece : movedPieces) {
			pieces.set(piece.getIndex(), piece);
		}
		movedPieces = new ArrayList<Piece>();
	}
	
	public void addMovedPiece(Piece piece) {
		this.movedPieces.add(piece);
	}
	
	public void makeMove(Move move) {
		List<Piece> toMove = null;
		if(move.isCubeRotation()) {
			toMove = pieces;
		} else {
			toMove = behavior.getAffectedPieces(move, this);
		}
		
		for(Piece piece : toMove) {
			behavior.movePiece(move, piece);
			PieceGroup group = puzzle.getGroup(piece.getType(), piece.getPosition());
			group.addMovedPiece(piece);
		}
	}
	
	public int getPuzzleSize() {
		return this.puzzleSize;
	}
	
	public List<Piece> getPieces() {
		return this.pieces;
	}
	
	public Piece getPiece() {
		return getPiece(0);
	}
	
	public Piece getPiece(int index) {
		return this.pieces.get(index);
	}
	
	public final int getPosition() {
		return this.position;
	}
	
}
