package com.github.sampeterson1.square1;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;

public class Square1Util {

	private static final int TOP_LAYER_SLICE_MAX = 5;
	private static final int BOTTOM_LAYER_SLICE_MAX = 17;

	private static final int TOP_LAYER_START = 0;
	private static final int TOP_LAYER_END = 11;
	
	private static final int BOTTOM_LAYER_START = 12;
	private static final int BOTTOM_LAYER_END = 23;
	
	public static Algorithm parseAlgorithm(String algStr) {
		Algorithm alg = new Algorithm();
		if(algStr.equals("/")) {
			alg.addMove(new Move(Axis.S1, true));
			return alg;
		}
		if(algStr.charAt(0) == '/') {
			alg.addMove(new Move(Axis.S1, true));
			algStr = algStr.substring(1);
		}
		String[] moves = algStr.split("/");
		for(int i = 0; i < moves.length; i ++) {
			String move = moves[i];
			String orderedPair[] = move.substring(1, move.length() - 1).split(",");
			int top = Integer.parseInt(orderedPair[0]);
			int bottom = Integer.parseInt(orderedPair[1]);
			
			boolean topCW = true;
			boolean bottomCW = true;
			
			if(top < 0) {
				topCW = false;
				top = -top;
			}
			if(bottom < 0) {
				bottomCW = false;
				bottom = -bottom;
			}
			
			for(int j = 0; j < top; j ++) alg.addMove(new Move(Axis.SU, topCW));
			for(int j = 0; j < bottom; j ++) alg.addMove(new Move(Axis.SD, bottomCW));
			if(i < moves.length - 1) alg.addMove(new Move(Axis.S1, true));
		}
		
		if(algStr.charAt(algStr.length() - 1) == '/') {
			alg.addMove(new Move(Axis.S1, true));
		}
		
		return alg;
	}
	
	public static void movePiece(Move move, Piece piece) {
		int position = piece.getPosition();
		boolean cw = move.isCW();
		Axis axis = move.getFace();
		int newPosition = position;
		PieceType type = piece.getType();
		
		if(axis == Axis.SU) {
			if(cw) newPosition = decrementPosition(position);
			else newPosition = incrementPosition(position);
		} else if(axis == Axis.SD) {
			if(cw) newPosition = incrementPosition(position);
			else newPosition = decrementPosition(position);
		} else if(axis == Axis.S1) {
			if(inTopLayer(position)) {
				newPosition = TOP_LAYER_SLICE_MAX - position + BOTTOM_LAYER_START;
				if(type == PieceType.CORNER) newPosition --;
			} else if(inBottomLayer(position)) {
				newPosition = BOTTOM_LAYER_SLICE_MAX - position;
				if(type == PieceType.CORNER) newPosition --;
			}
			
		}
			
		piece.setPosition(newPosition);
	}
	
	private static int incrementPosition(int position) {
		if(position >= BOTTOM_LAYER_START) {
			position ++; 
			if(position > BOTTOM_LAYER_END) position = BOTTOM_LAYER_START;	
		} else if(position >= TOP_LAYER_START) {
			position ++; 
			if(position > TOP_LAYER_END) position = TOP_LAYER_START;
		}
		
		return position;
	}
	
	private static int decrementPosition(int position) {
		if(position >= BOTTOM_LAYER_START) {
			position --; 
			if(position < BOTTOM_LAYER_START) position = BOTTOM_LAYER_END;	
		} else if(position >= TOP_LAYER_START) {
			position --; 
			if(position < TOP_LAYER_START) position = TOP_LAYER_END;
		}
		
		return position;
	}
	
	public static boolean affectedByMove(Move move, Piece piece) {
		Axis axis = move.getFace();
		int position = piece.getPosition();

		if(axis == Axis.S1) {
			return onSlice(position) && !isLocked((Square1) piece.getPuzzle());
		} else if(axis == Axis.SU) {
			return inTopLayer(position);
		} else if(axis == Axis.SD) {
			return inBottomLayer(position);
		}
		
		return false;
	}
	
	private static boolean inBottomLayer(int position) {
		return (position >= BOTTOM_LAYER_START && position <= BOTTOM_LAYER_END);
	}
	
	private static boolean inTopLayer(int position) {
		return (position >= TOP_LAYER_START && position <= TOP_LAYER_END);
	}
	
	private static boolean onSlice(int position) {
		boolean topSlice = (position >= TOP_LAYER_START && position <= TOP_LAYER_SLICE_MAX);
		boolean bottomSlice = (position >= BOTTOM_LAYER_START && position <= BOTTOM_LAYER_SLICE_MAX);
		
		return (topSlice || bottomSlice);
	}
	
	private static boolean checkPosition(Square1 sq1, int index) {
		Piece piece = sq1.getPiece(index);
		return (piece != null && piece.getType() == PieceType.CORNER);
	}
	
	public static boolean topLocked(Square1 sq1) {
		return (checkPosition(sq1, TOP_LAYER_END) || checkPosition(sq1, TOP_LAYER_SLICE_MAX));
	}
	
	public static boolean bottomLocked(Square1 sq1) {
		return (checkPosition(sq1, BOTTOM_LAYER_END) || checkPosition(sq1, BOTTOM_LAYER_SLICE_MAX));
	}
	
	public static boolean isLocked(Square1 sq1) {
		return (topLocked(sq1) || bottomLocked(sq1));
	}
	
	public static Algorithm mirror(Algorithm alg) {
		Algorithm mirror = new Algorithm();
		for(Move move : alg.getMoves()) {
			if(move.getFace() == Axis.S1) {
				mirror.addMove(move);
			} else {
				mirror.addMove(move.getInverse());
			}
		}
		
		return mirror;
	}
	
	public static Algorithm flip(Algorithm alg) {
		Algorithm mirror = new Algorithm();
		for(Move move : alg.getMoves()) {
			Axis axis = move.getFace();
			if(axis == Axis.S1) {
				mirror.addMove(move);
			} else if(axis == Axis.SD){
				mirror.addMove(new Move(Axis.SU, move.isCCW()));
			} else if(axis == Axis.SU) {
				mirror.addMove(new Move(Axis.SD, move.isCCW()));
			}
		}
		
		return mirror;
	}
}
