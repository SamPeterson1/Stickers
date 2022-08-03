package com.github.yoshiapolis.puzzle.display;

import java.util.List;
import java.util.Map;

import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.Puzzle;

public interface PiecePlacement {

	public float getRotationAmt();
	
	public Map<Piece, List<DisplayPiece>> createPuzzlePieces(Puzzle puzzle, float drawSize);
	
}
