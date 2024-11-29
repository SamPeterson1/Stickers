package com.github.sampeterson1.puzzle.lib;

import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.InvalidAlgorithmException;
import com.github.sampeterson1.puzzle.templates.Puzzle;

public abstract class PuzzleMetaFunctions<T extends Puzzle> {

	private T puzzle;
	
	public PuzzleMetaFunctions(T puzzle) {
		this.puzzle = puzzle;
	}
	
	protected T getPuzzle() {
		return this.puzzle;
	}
	
	//parses a string into an algorithm
	public abstract Algorithm parseAlgorithm(String alg) throws InvalidAlgorithmException;
	
	//simplifies an algorithm to have a less or equal length
	public abstract Algorithm simplify(Algorithm alg);
	
	//scrambles this puzzle and returns the scramble used
	public abstract Algorithm scramble(int length);
	
	//solves the puzzle and returns the solution used
	public abstract Algorithm solve();
	
	//creates the default sticker color values for this puzzle 
	public abstract ColorPalette createDefaultColorPalette();
	
	//creates a DisplayPiece that represents the given piece
	public abstract DisplayPiece createDisplayPiece(Piece piece);
	
}
