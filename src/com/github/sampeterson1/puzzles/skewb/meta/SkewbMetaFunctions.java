package com.github.sampeterson1.puzzles.skewb.meta;

import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.UniversalAlgorithmParser;
import com.github.sampeterson1.puzzles.skewb.display.SkewbDisplayPiece;

public class SkewbMetaFunctions extends PuzzleMetaFunctions<Skewb> {

	public SkewbMetaFunctions(Skewb puzzle) {
		super(puzzle);
	}

	@Override
	public Algorithm simplify(Algorithm alg) {
		return alg;
	}

	@Override
	public Algorithm scramble(int length) {
		return new Algorithm();
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
		return new SkewbDisplayPiece(piece);
	}

	@Override
	public Algorithm parseAlgorithm(String alg) {
		return UniversalAlgorithmParser.parseAlgorithm(alg);
	}

}
