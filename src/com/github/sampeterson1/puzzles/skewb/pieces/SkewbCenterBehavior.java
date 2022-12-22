package com.github.sampeterson1.puzzles.skewb.pieces;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.skewb.util.SkewbUtil;

public class SkewbCenterBehavior extends SimplePieceBehavior {

	public SkewbCenterBehavior(Puzzle puzzle) {
		super(PieceType.CENTER, puzzle);
	}

	@Override
	public Piece createPiece(int position) {
		Piece center = new Piece(super.getPuzzle(), PieceType.CENTER, position);
		center.setColor(SkewbUtil.CENTER_COLORS[position]);
		
		return center;
	}

	@Override
	public boolean affectedByMove(Move move, Piece piece) {
		int p = piece.getPosition();
		return (p == 0 || p == 1 || p == 2);
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		// TODO Auto-generated method stub
		
	}


}