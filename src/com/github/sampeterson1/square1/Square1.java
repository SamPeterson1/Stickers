package com.github.sampeterson1.square1;

import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.puzzle.lib.PuzzleType;

public class Square1 extends Puzzle {

	private static final int NUM_CENTERS = 2;
	
	public Square1() {
		super(1);
		
		super.createPieces(new Square1CenterBehavior(), NUM_CENTERS);
		
		PieceBehavior edgeBehavior = new Square1EdgeBehavior();
		PieceBehavior cornerBehavior = new Square1CornerBehavior();
		
		for(int i = 0; i < 8; i ++) {
			super.createPieceGroup(edgeBehavior, i * 3);
			super.createPieceGroup(cornerBehavior, i * 3 + 1);
		}
	}

	@Override
	public Axis transposeAxis(Axis face) {
		return face;
	}

	@Override
	public Algorithm parseAlgorithm(String alg) {
		return new Algorithm();
	}

	@Override
	public Algorithm simplify(Algorithm alg) {
		return alg;
	}

	@Override
	public Algorithm scramble(int length) {
		Algorithm alg = new Algorithm();
		alg.addMove(new Move(Axis.S1, true));
		
		return alg;
	}

	@Override
	public Algorithm solve() {
		return new Algorithm();
	}

	@Override
	public ColorPalette createDefaultColorPalette() {
		ColorPalette palette = new ColorPalette();
		
		palette.putColor(Color.BORDER);
		palette.putColor(Color.WHITE);
		palette.putColor(Color.YELLOW);
		palette.putColor(Color.ORANGE);
		palette.putColor(Color.BLUE);
		palette.putColor(Color.GREEN);
		palette.putColor(Color.RED);
		
		return palette;
	}

	@Override
	public DisplayPiece createDisplayPiece(Piece piece) {
		return new Square1DisplayPiece(piece);
	}

	@Override
	public PuzzleType getType() {
		return PuzzleType.SQUARE1;
	}

}
