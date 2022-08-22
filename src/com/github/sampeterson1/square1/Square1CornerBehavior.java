package com.github.sampeterson1.square1;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;

public class Square1CornerBehavior extends SimplePieceBehavior {

	private static final Color[][] cornerColors = new Color[][] {
		{Color.GREEN, Color.WHITE, Color.RED},
		{Color.RED, Color.WHITE, Color.BLUE},
		{Color.BLUE, Color.WHITE, Color.ORANGE},
		{Color.ORANGE, Color.WHITE, Color.GREEN},
		{Color.RED, Color.YELLOW, Color.GREEN},
		{Color.BLUE, Color.YELLOW, Color.RED},
		{Color.ORANGE, Color.YELLOW, Color.BLUE},
		{Color.GREEN, Color.YELLOW, Color.ORANGE},
	};
	
	public Square1CornerBehavior(Puzzle puzzle) {
		super(puzzle);
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		Square1Util.movePiece(move, piece);
	}

	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		return Square1Util.affectedByMove(move, piece);
	}
	
	@Override
	protected Piece createPiece(int position) {
		Piece piece = new Piece(super.getPuzzle(), PieceType.CORNER, position);
		
		Color[] colors = cornerColors[(position - 1)/3];
		for(int i = 0; i < 3; i ++)
			piece.setColor(i, colors[i]);
		
		return piece;
	}

	@Override
	public PieceType getType() {
		return PieceType.CORNER;
	}

}
