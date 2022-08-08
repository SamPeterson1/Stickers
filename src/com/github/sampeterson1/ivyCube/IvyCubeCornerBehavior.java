package com.github.sampeterson1.ivyCube;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;

public class IvyCubeCornerBehavior extends SimplePieceBehavior {
	
	private static final PieceType type = PieceType.CORNER;
	
	private static final Color[][] colors = {
			{Color.WHITE, Color.GREEN, Color.ORANGE},
			{Color.WHITE, Color.BLUE, Color.RED},
			{Color.YELLOW, Color.GREEN, Color.RED},
			{Color.YELLOW, Color.BLUE, Color.ORANGE}
	};
	
	@Override
	public Piece createPiece(int position) {
		Piece corner = new Piece(PieceType.CORNER, position);
		for(int i = 0; i < 3; i ++) corner.setColor(i, colors[position][i]);
		
		return corner;
	}

	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		Axis axis = move.getFace();
		int affectedPosition = 0;
		if(axis == Axis.IR) {
			affectedPosition = 2;
		} else if(axis == Axis.IL) {
			affectedPosition = 0;
		} else if(axis == Axis.ID) {
			affectedPosition = 3;
		} else if(axis == Axis.IB) {
			affectedPosition = 1;
		}
		
		return (piece.getPosition() == affectedPosition);
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		Color[] copy = new Color[] {piece.getColor(0), piece.getColor(1), piece.getColor(2)};
		
		if(move.isCW()) {
			piece.setColor(0, copy[2]);
			piece.setColor(1, copy[0]);
			piece.setColor(2, copy[1]);
		} else {
			piece.setColor(0, copy[1]);
			piece.setColor(1, copy[2]);
			piece.setColor(2, copy[0]);	
		}
	}

	@Override
	public PieceType getType() {
		return type;
	}

}
