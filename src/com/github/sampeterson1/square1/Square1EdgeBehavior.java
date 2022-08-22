package com.github.sampeterson1.square1;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;

public class Square1EdgeBehavior extends SimplePieceBehavior {

	private static final Color[][] edgeColors = new Color[][] {
		{Color.GREEN, Color.WHITE},
		{Color.RED, Color.WHITE},
		{Color.BLUE, Color.WHITE},
		{Color.ORANGE, Color.WHITE},
		{Color.GREEN, Color.YELLOW},
		{Color.RED, Color.YELLOW},
		{Color.BLUE, Color.YELLOW},
		{Color.ORANGE, Color.YELLOW},
	};

	public Square1EdgeBehavior(Puzzle puzzle) {
		super(puzzle);
	}
	
	@Override
	protected Piece createPiece(int position) {
		Piece piece = new Piece(super.getPuzzle(), PieceType.EDGE, position);
		
		Color[] colors = edgeColors[position/3];
		for(int i = 0; i < 2; i ++)
			piece.setColor(i, colors[i]);
		
		return piece;
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
	public PieceType getType() {
		return PieceType.EDGE;
	}

}
