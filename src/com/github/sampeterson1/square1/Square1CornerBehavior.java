package com.github.sampeterson1.square1;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;

public class Square1CornerBehavior extends SimplePieceBehavior {

	private static final Color[][] cornerColors = new Color[][] {
		{Color.GREEN, Color.WHITE, Color.RED},
		{Color.RED, Color.WHITE, Color.BLUE},
		{Color.BLUE, Color.WHITE, Color.ORANGE},
		{Color.ORANGE, Color.WHITE, Color.GREEN},
		{Color.RED, Color.YELLOW, Color.GREEN},
		{Color.BLUE, Color.YELLOW, Color.RED},
		{Color.ORANGE, Color.YELLOW, Color.BLUE},
		{Color.GREEN, Color.YELLOW, Color.ORANGE},
	};
	
	@Override
	protected Piece createPiece(int position) {
		Piece piece = new Piece(PieceType.CORNER, position);
		
		Color[] colors = cornerColors[(position - 1)/3];
		for(int i = 0; i < 3; i ++)
			piece.setColor(i, colors[i]);
		
		return piece;
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		Axis axis = move.getFace();
		int position = piece.getPosition();
		
		if(axis == Axis.SU) {
			position ++;
			piece.setPosition(position % 12);
		} else if(axis == Axis.SD) {
			position --;
			if(position < 12) position = 23;
			
			piece.setPosition(position);
		}
	}

	@Override
	public PieceType getType() {
		return PieceType.CORNER;
	}

	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		Axis axis = move.getFace();
		int position = piece.getPosition();
		
		if(axis == Axis.S1) {
			return (position <= 4 || (position >= 12 && position <= 16));  
		} else if(axis == Axis.SU) {
			return (position <= 4);
		} else if(axis == Axis.SD) {
			return (position >= 12);
		}
		
		return false;
	}

}
