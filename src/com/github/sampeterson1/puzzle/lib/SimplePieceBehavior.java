package com.github.sampeterson1.puzzle.lib;

import java.util.ArrayList;
import java.util.List;

public abstract class SimplePieceBehavior implements PieceBehavior {
	
	protected abstract Piece createPiece(int position);
	protected abstract boolean affectedByMove(Move move, Piece piece);
	
	@Override
	public Piece createPiece(int position, int index, int puzzleSize) {
		return createPiece(position);
	}
	
	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		if(affectedByMove(move, group.getPiece())) {
			return group.getPieces();
		}
		
		return new ArrayList<Piece>();
	}
	
	@Override
	public int getNumPieces(int puzzleSize, int position) {
		return 1;
	}
	
}
