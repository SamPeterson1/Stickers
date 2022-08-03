package com.github.yoshiapolis.puzzle.lib;

import java.util.EnumMap;
import java.util.Map;

public class PuzzleData {
	
	private PieceType[] pieces;
	private Face[] faces;
	private MoveSimplificationRule simplificationRule;
	
	private Map<PieceType, PieceBehavior> pieceBehaviorMap;
	
	public PuzzleData() {
		this.pieceBehaviorMap = new EnumMap<PieceType, PieceBehavior>(PieceType.class);
	}

	public void putPieceBehavior(PieceType type, PieceBehavior behavior) {
		pieceBehaviorMap.put(type, behavior);
	}

}
