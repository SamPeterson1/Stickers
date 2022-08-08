package com.github.sampeterson1.puzzle.display;

import java.util.List;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.Puzzle;

public interface PiecePlacement {

	public float getRotationAmt();
	
	public Map<Piece, List<DisplayPiece>> createPuzzlePieces(Puzzle puzzle, float drawSize);
	
}
