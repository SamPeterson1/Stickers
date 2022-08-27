/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.puzzles.square1.util;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzles.square1.pieces.Square1;

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
		} else if(algStr.equals("")) {
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
	
	public static Algorithm simplify(Algorithm alg) {
		List<Move> newMoves = new ArrayList<Move>();
		
		int numUMoves = 0;
		int numDMoves = 0;
		
		for(Move move : alg.getMoves()) {
			Axis axis = move.getAxis();
			int increment = move.isCW() ? 1 : -1;
			if(axis == Axis.SU) {
				numUMoves += increment;
			} else if(axis == Axis.SD) {
				numDMoves += increment;
			} else if(axis == Axis.S1) {
				addLayerMoves(newMoves, numUMoves, numDMoves);
				newMoves.add(new Move(Axis.S1, true));
				numUMoves = 0;
				numDMoves = 0;
			}
		}
		
		addLayerMoves(newMoves, numUMoves, numDMoves);
		
		return new Algorithm(newMoves);
	}
	
	private static void addLayerMoves(List<Move> moves, int numUMoves, int numDMoves) {
		while(numUMoves > 6) numUMoves -= 12;
		while(numDMoves > 6) numDMoves -= 12;
		
		while(numUMoves < -6) numUMoves += 12;
		while(numDMoves < -6) numDMoves += 12;
		
		if(numUMoves < 0)
			for(int i = 0; i < -numUMoves; i ++) moves.add(new Move(Axis.SU, false));
		else
			for(int i = 0; i < numUMoves; i ++) moves.add(new Move(Axis.SU, true));
		
		if(numDMoves < 0)
			for(int i = 0; i < -numDMoves; i ++) moves.add(new Move(Axis.SD, false));
		else
			for(int i = 0; i < numDMoves; i ++) moves.add(new Move(Axis.SD, true));
	}
	
	public static void movePiece(Move move, Piece piece) {
		int position = piece.getPosition();
		boolean cw = move.isCW();
		Axis axis = move.getAxis();
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
		Axis axis = move.getAxis();
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
			if(move.getAxis() == Axis.S1) {
				mirror.addMove(move);
			} else {
				mirror.addMove(move.getInverse());
			}
		}
		
		return mirror;
	}
	
	public static boolean topSquare(Square1 sq1) {
		for(int i = 0; i < 12; i += 3) {
			if(sq1.getEdge(i) == null) return false;
		}
		for(int i = 1; i < 12; i += 3) {
			if(sq1.getCorner(i) == null) return false;
		}
		return true;
	}
	
	public static boolean bottomSquare(Square1 sq1) {
		for(int i = 12; i < 24; i += 3) {
			if(sq1.getEdge(i) == null) return false;
		}
		for(int i = 13; i < 24; i += 3) {
			if(sq1.getCorner(i) == null) return false;
		}
		return true;
	}
	
	public static Algorithm flip(Algorithm alg) {
		Algorithm mirror = new Algorithm();
		for(Move move : alg.getMoves()) {
			Axis axis = move.getAxis();
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
