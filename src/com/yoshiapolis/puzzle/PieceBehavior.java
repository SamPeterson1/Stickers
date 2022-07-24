package com.yoshiapolis.puzzle;

import java.util.List;

public interface PieceBehavior {

	public PuzzlePiece createPiece(int position, int index);
	
	public List<PuzzlePiece> getAffectedPieces(Move move, PuzzlePieceGroup group);
	
	public void movePiece(Move move, PuzzlePiece piece, int puzzleSize);
	
	public int getNumPieces(int puzzleSize);
	
}
