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

package com.yoshiapolis.puzzle;

import java.util.ArrayList;
import java.util.List;

public class PuzzlePieceGroup {
	
	private Puzzle puzzle;
	private PieceBehavior behavior;
	private List<PuzzlePiece> pieces;
	private List<PuzzlePiece> movedPieces;
	private int position;
	private int puzzleSize;
	
	public PuzzlePieceGroup(PieceBehavior behavior, Puzzle puzzle, int position) {
		this.position = position;
		this.pieces = new ArrayList<PuzzlePiece>();
		this.movedPieces = new ArrayList<PuzzlePiece>();
		this.puzzle = puzzle;
		this.puzzleSize = puzzle.getSize();
		this.behavior = behavior;
		
		int numPieces = behavior.getNumPieces(puzzleSize);
		for(int i = 0; i < numPieces; i ++) {
			pieces.add(behavior.createPiece(position, i));
		}
	}
	
	public boolean isSolved() {
		for(PuzzlePiece piece : pieces) {
			if(!piece.isSolved()) return false;
		}
		
		return true;
	}
	
	public void setSolved(boolean solved) {
		for(PuzzlePiece piece : pieces) {
			piece.setSolved(true);
		}
	}
	
	public void applyMoves() {
		for(PuzzlePiece piece : movedPieces) {
			pieces.set(piece.getIndex(), piece);
		}
		movedPieces = new ArrayList<PuzzlePiece>();
	}
	
	public void addMovedPiece(PuzzlePiece piece) {
		this.movedPieces.add(piece);
	}
	
	public void makeMove(Move move) {
		List<PuzzlePiece> toMove = behavior.getAffectedPieces(move, this);
		for(PuzzlePiece piece : toMove) {
			behavior.movePiece(move, piece, puzzleSize);
			PuzzlePieceGroup group = puzzle.getGroup(piece.getType(), piece.getPosition());
			group.addMovedPiece(piece);
		}
	}
	
	public int getPuzzleSize() {
		return this.puzzleSize;
	}
	
	public List<PuzzlePiece> getPieces() {
		return this.pieces;
	}
	
	public PuzzlePiece getPiece() {
		return getPiece(0);
	}
	
	public PuzzlePiece getPiece(int index) {
		return this.pieces.get(index);
	}
	
	public final int getPosition() {
		return this.position;
	}
	
}
