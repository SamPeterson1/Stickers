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

package com.github.sampeterson1.puzzles.cube.util;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;

//Utilities relating to edges on a Rubik's Cube
public class CubeEdgeUtil {
	
	private static final int[] edgeMapArr_R = {-3, -7, -11, 8, -4, 2, -10, 12, -1, 6, -9, -5};
	private static final int[] edgeMapArr_U = {4, 1, 2, 3, 8, 5, 6, 7, 12, 9, 10, 11};
	private static final int[] edgeMapArr_F = {-6, -10, 7, -2, 1, -9, 11, -3, 5, -12, -8, -4};

	private static final Color[][] colors = { 
			{ Color.WHITE, Color.GREEN }, 
			{ Color.WHITE, Color.RED },
			{ Color.WHITE, Color.BLUE },
			{ Color.WHITE, Color.ORANGE }, 
			{ Color.ORANGE, Color.GREEN },
			{ Color.GREEN, Color.RED }, 
			{ Color.RED, Color.BLUE }, 
			{ Color.BLUE, Color.ORANGE },
			{ Color.YELLOW, Color.GREEN }, 
			{ Color.YELLOW, Color.RED }, 
			{ Color.YELLOW, Color.BLUE },
			{ Color.YELLOW, Color.ORANGE } 
	};

	public static Color[] getColors(int position) {
		return colors[position];
	}

	public static Axis getFace(int position, int side) {
		if (side == 0) {
			if (position >= 0 && position <= 3) {
				return Axis.U;
			} else if (position == 4) {
				return Axis.L;
			} else if (position == 5) {
				return Axis.F;
			} else if (position == 6) {
				return Axis.R;
			} else if (position == 7) {
				return Axis.B;
			} else if (position >= 8 && position <= 11) {
				return Axis.D;
			}
		} else if (side == 1) {
			if (position == 0 || position == 4 || position == 8) {
				return Axis.F;
			} else if (position == 1 || position == 5 || position == 9) {
				return Axis.R;
			} else if (position == 2 || position == 6 || position == 10) {
				return Axis.B;
			} else if (position == 3 || position == 7 || position == 11) {
				return Axis.L;
			}
		}

		return null;
	}

	public static Piece mapEdge(Move move, Piece piece) {
		move = CubeMoveUtil.faceNormalize(move);
		int position = piece.getPosition();
		int index = piece.getIndex();
		int puzzleSize = piece.getPuzzleSize();
		Color c1 = piece.getColor(0);
		Color c2 = piece.getColor(1);

		int[] mapArr = getEdgeMapArr(move.getAxis());
		int n = move.isCW() ? 1 : 3;

		for (int i = 0; i < n; i++) {
			int mapVal = mapArr[position];
			if (mapVal < 0) {
				mapVal = -mapVal;
				index = puzzleSize - index - 3;
				Color tmp = c1;
				c1 = c2;
				c2 = tmp;
			}
			position = mapVal - 1;
		}

		Piece retVal = new Piece(piece.getPuzzle(), PieceType.EDGE, position, index);
		retVal.setColor(0, c1);
		retVal.setColor(1, c2);

		return retVal;
	}

	private static int[] getEdgeMapArr(Axis face) {
		if (face == Axis.R) {
			return edgeMapArr_R;
		} else if (face == Axis.U) {
			return edgeMapArr_U;
		} else if (face == Axis.F) {
			return edgeMapArr_F;
		}

		return null;
	}

}
