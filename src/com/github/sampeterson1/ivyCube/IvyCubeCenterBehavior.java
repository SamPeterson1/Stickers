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

package com.github.sampeterson1.ivyCube;

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;

public class IvyCubeCenterBehavior extends SimplePieceBehavior {
	
	private static final PieceType type = PieceType.CENTER;
	
	private static final int RED = 0;
	private static final int WHITE = 1;
	private static final int GREEN = 2;
	private static final int ORANGE = 3;
	private static final int YELLOW = 4;
	private static final int BLUE = 5;
	
	private static final int[][] rPositionArr = { {GREEN, RED}, {RED, YELLOW}, {YELLOW, GREEN} };
	private static final int[][] lPositionArr = { {WHITE, GREEN}, {GREEN, ORANGE}, {ORANGE, WHITE} };
	private static final int[][] dPositionArr = { {BLUE, ORANGE}, {ORANGE, YELLOW}, {YELLOW, BLUE} };
	private static final int[][] bPositionArr = { {WHITE, BLUE}, {BLUE, RED}, {RED, WHITE} };
	
	private static final Map<Integer, Integer> rPositionMap = initPositionMap(rPositionArr);
	private static final Map<Integer, Integer> lPositionMap = initPositionMap(lPositionArr);
	private static final Map<Integer, Integer> dPositionMap = initPositionMap(dPositionArr);
	private static final Map<Integer, Integer> bPositionMap = initPositionMap(bPositionArr);
	
	private static Color[] colors = {
			Color.RED, Color.WHITE, Color.GREEN,
			Color.ORANGE, Color.YELLOW, Color.BLUE
	};
	
	private static Map<Integer, Integer> initPositionMap(int[][] positionArr) {
		Map<Integer, Integer> positionMap = new HashMap<Integer, Integer>();
		for(int i = 0; i < 3; i ++) {
			positionMap.put(positionArr[i][0], positionArr[i][1]);
		}
		
		return positionMap;
	}
	
	private static Map<Integer, Integer> getPositionMap(Axis axis) {
		if(axis == Axis.IR) {
			return rPositionMap;
		} else if(axis == Axis.IL) {
			return lPositionMap;
		} else if(axis == Axis.ID) {
			return dPositionMap;
		} else if(axis == Axis.IB) {
			return bPositionMap;
		}
		
		return null;
	}
	
	@Override
	public Piece createPiece(int position) {
		Piece center = new Piece(PieceType.CENTER, position);
		center.setColor(colors[position]);
		
		return center;
	}
	
	@Override
	protected boolean affectedByMove(Move move, Piece piece) {
		Axis axis = move.getFace();
		int position = piece.getPosition();
		Map<Integer, Integer> positionMap = getPositionMap(axis);
		
		return positionMap.containsKey(position);
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		Axis axis = move.getFace();
		int position = piece.getPosition();
		Map<Integer, Integer> positionMap = getPositionMap(axis);
		
		int newPosition = positionMap.get(position);
		if(move.isCCW()) {
			newPosition = positionMap.get(newPosition);
		}
		
		piece.setPosition(newPosition);
	}

	@Override
	public PieceType getType() {
		return type;
	}

}
