package com.github.sampeterson1.puzzles.skewb.pieces;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.skewb.util.SkewbUtil;

public class SkewbCornerBehavior extends SimplePieceBehavior {

	public SkewbCornerBehavior(Puzzle puzzle) {
		super(PieceType.CORNER, puzzle);
	}

	@Override
	public Piece createPiece(int position) {
		Piece corner = new Piece(super.getPuzzle(), PieceType.CORNER, position);
		for(int i = 0; i < 3; i ++) 
			corner.setColor(i, SkewbUtil.CORNER_COLORS[position][i]);
		
		return corner;
	}

	@Override
	public boolean affectedByMove(Move move, Piece piece) {
		int p = piece.getPosition();
		return (p == 0 || p == 1 || p == 2 || p == 5);
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		// TODO Auto-generated method stub
		
	}

}
