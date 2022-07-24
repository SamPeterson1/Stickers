/*
    PrimePuzzle Twisty Puzzle Simulator
    Copyright (C) 2022 Sam Peterson
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.yoshiapolis.cube.pieces;

import java.util.HashMap;

import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PieceType;
import com.yoshiapolis.puzzle.PuzzlePiece;

public class CubeEdgeUtil {

	private static HashMap<Integer, Integer> edgeMap_R;
	private static HashMap<Integer, Integer> edgeMap_U;
	private static HashMap<Integer, Integer> edgeMap_F;
	
	private static int[][] edgeMapArr_R = {
		{4, 8}, {8, 12}, {12, -5}, {5, -4},
		{1, -3}, {3, -11}, {11, -9}, {9, -1},
		{2, -7}, {7, -10}, {10, 6}, {6, 2}
	};
	
	private static int[][] edgeMapArr_U = {
		{1, 4}, {4, 3}, {3, 2}, {2, 1},
		{5, 8}, {8, 7}, {7, 6}, {6, 5},
		{9, 12}, {12, 11}, {11, 10}, {10, 9}
	};
	
	private static int[][] edgeMapArr_F = {
		{3, 7}, {7, 11}, {11, -8}, {8, -3},
		{2, -10}, {10, -12}, {12, -4}, {4, -2},
		{1, -6}, {6, -9}, {9, 5}, {5, 1}
	};
	
	private static final Color[][] colors = {
			{Color.WHITE, Color.GREEN},
			{Color.WHITE, Color.RED},
			{Color.WHITE, Color.BLUE},
			{Color.WHITE, Color.ORANGE},
			{Color.ORANGE, Color.GREEN},
			{Color.GREEN, Color.RED},
			{Color.RED, Color.BLUE},
			{Color.BLUE, Color.ORANGE},
			{Color.YELLOW, Color.GREEN},
			{Color.YELLOW, Color.RED},
			{Color.YELLOW, Color.BLUE},
			{Color.YELLOW, Color.ORANGE}
	};
	
	public static void init() {
		edgeMap_R = new HashMap<Integer, Integer>();
		edgeMap_U = new HashMap<Integer, Integer>();
		edgeMap_F = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < 12; i ++) {
			edgeMap_R.put(edgeMapArr_R[i][0], edgeMapArr_R[i][1]);
			edgeMap_U.put(edgeMapArr_U[i][0], edgeMapArr_U[i][1]);
			edgeMap_F.put(edgeMapArr_F[i][0], edgeMapArr_F[i][1]);
		}
	}
	
	public static Color[] getColors(int position) {
		return colors[position];
	}
	
	public static Face getFace(int position, int side) {
		if(side == 0) {
			if(position >= 0 && position <= 3) {
				return Face.U;
			} else if(position == 4) {
				return Face.L;
			} else if(position == 5) {
				return Face.F;
			} else if(position == 6) {
				return Face.R;
			} else if(position == 7) {
				return Face.B;
			} else if(position >= 8 && position <= 11) {
				return Face.D;
			}
		} else if(side == 1) {
			if(position == 0 || position == 4 || position == 8) {
				return Face.F;
			} else if(position == 1 || position == 5 || position == 9) {
				return Face.R;
			} else if(position == 2 || position == 6 || position == 10) {
				return Face.B;
			} else if(position == 3 || position == 7 || position == 11) {
				return Face.L;
			}
		}
		
		return null;
	}
	
	public static PuzzlePiece mapEdge(Move move, PuzzlePiece piece, int puzzleSize) {
		move = CubeMoveUtil.faceNormalize(move);
		int position = piece.getPosition();
		int index = piece.getIndex();
		Color c1 = piece.getColor(0);
		Color c2 = piece.getColor(1);
		
		
		
		HashMap<Integer, Integer> map = CubeEdgeUtil.getEdgeMap(move.getFace());
		int n = move.isCW() ? 1 : 3;
		
		for(int i = 0; i < n; i ++) {
			int mapVal = map.get(position+1);
			if(mapVal < 0) {
				mapVal = -mapVal;
				index = puzzleSize-index-3;
				Color tmp = c1;
				c1 = c2;
				c2 = tmp;
			}
			position = mapVal-1;
		}
		
		PuzzlePiece retVal = new PuzzlePiece(PieceType.EDGE, position, index, 2);
		retVal.setColor(0, c1);
		retVal.setColor(1, c2);
		
		return retVal;
	}
	
	private static HashMap<Integer, Integer> getEdgeMap(Face face) {
		if(face == Face.R) {
			return edgeMap_R;
		} else if(face == Face.U) {
			return edgeMap_U;
		} else if(face == Face.F) {
			return edgeMap_F;
		}
		
		return null;
	}
	
}
