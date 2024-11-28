package com.github.sampeterson1.puzzles.skewb.pieces;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.skewb.util.SkewbUtil;

public class SkewbCornerBehavior extends SimplePieceBehavior {

	public SkewbCornerBehavior(Puzzle puzzle) {
		super(PieceType.CORNER, puzzle);
	}

	private void rotateCorner(Piece corner, int amount) {
		Color[] colors = new Color[3];
		for(int i = 0; i < 3; i ++) colors[i] = corner.getColor(i);
		
		for(int i = 0; i < 3; i ++) corner.setColor((i + amount) % 3, colors[i]);
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
		Axis axis = move.getAxis();
		int position = piece.getPosition();
		
		int newPosition = SkewbUtil.mapCornerPosition(axis, position);
		int rotation = SkewbUtil.mapCornerRotation(axis, position);
		
		return !(newPosition == position && rotation == 0);
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		Axis axis = move.getAxis();
		int position = piece.getPosition();
		int numCWMoves = move.isCCW() ? 2 : 1;
		int newPosition = position;
		
		for(int i = 0; i < numCWMoves; i ++) {
			rotateCorner(piece, SkewbUtil.mapCornerRotation(axis, newPosition));
			newPosition = SkewbUtil.mapCornerPosition(axis, newPosition);
		}
		
		piece.setPosition(newPosition);
	}

}
