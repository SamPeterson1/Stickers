package com.github.sampeterson1.square1;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;

public class Square1CenterBehavior extends SimplePieceBehavior {

	@Override
	protected Piece createPiece(int position) {
		Piece piece = new Piece(PieceType.SQUARE1_CENTER, position);
		
		if(position == 0) {
			piece.setColor(0, Color.GREEN);
			piece.setColor(1, Color.RED);
			piece.setColor(2, Color.BLUE);
		} else if(position == 1) {
			piece.setColor(0, Color.BLUE);
			piece.setColor(1, Color.ORANGE);
			piece.setColor(2, Color.GREEN);
		}
		
		return piece;
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		if(move.getFace() == Axis.S1) {
			Color front = piece.getColor(0);
			Color back = piece.getColor(2);
			piece.setColor(0, back);
			piece.setColor(1, front);
		}
	}

	@Override
	public PieceType getType() {
		return PieceType.SQUARE1_CENTER;
	}

	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		return (move.getFace() == Axis.S1 && piece.getPosition() == 0);
	}

}
