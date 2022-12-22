package com.github.sampeterson1.puzzles.skewb.meta;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzle.templates.SimplePuzzle;
import com.github.sampeterson1.puzzles.skewb.pieces.SkewbCenterBehavior;
import com.github.sampeterson1.puzzles.skewb.pieces.SkewbCornerBehavior;

public class Skewb extends SimplePuzzle {

	private static final int NUM_CORNERS = 8;
	private static final int NUM_CENTERS = 6;

	public Skewb() {
		super(PuzzleType.SKEWB, true);
		
		super.createPieces(new SkewbCornerBehavior(this), NUM_CORNERS);
		super.createPieces(new SkewbCenterBehavior(this), NUM_CENTERS);	
	}

	public Piece getCenter(int position) {
		return super.getPiece(PieceType.CENTER, position);
	}
	
	public Piece getCorner(int position) {
		return super.getPiece(PieceType.CORNER, position);
	}

	@Override
	protected PuzzleMetaFunctions<? extends Puzzle> createMetaFunctions() {
		return new SkewbMetaFunctions(this);
	}

	
}
