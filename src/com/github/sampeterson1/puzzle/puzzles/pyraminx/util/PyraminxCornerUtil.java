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

package com.github.sampeterson1.puzzle.puzzles.pyraminx.util;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;

public class PyraminxCornerUtil {

	private static Color[][] colors = {
			{Color.GREEN, Color.BLUE, Color.RED},
			{Color.GREEN, Color.YELLOW, Color.BLUE},
			{Color.BLUE, Color.YELLOW, Color.RED},
			{Color.RED, Color.YELLOW, Color.GREEN}
	};
	
	private static int[] dRotations = {1, 0, 0, 0};
	private static int[] fRotations = {0, -1, 1, 1};
	private static int[] rRotations = {-1, -1, -1, 1};
	private static int[] lRotations = {1, 1, 0, -1};
	
	private static int[] dPositions = {0, 2, 3, 1};
	private static int[] fPositions = {1, 3, 2, 0};
	private static int[] rPositions = {2, 0, 1, 3};
	private static int[] lPositions = {3, 1, 0, 2};
	
	public static Color[] getColors(int position) {
		return colors[position];
	}
	
	public static void rotateCorner(Piece piece, int rotation) {
		Color[] colors = new Color[3];
		for(int i = 0; i < 3; i ++) 
			colors[i] = piece.getColor(i);
		for(int i = 0; i < 3; i ++)
			piece.setColor((i + 3 + rotation) % 3, colors[i]);
	}
	
	public static void mapCorner(Move move, Piece piece) {
		int position = piece.getPosition();
		int[] rotationMap = null;
		int[] positionMap = null;
		
		switch(move.getAxis()) {
		case PD:
			rotationMap = dRotations;
			positionMap = dPositions;
			break;
		case PF:
			rotationMap = fRotations;
			positionMap = fPositions;
			break;
		case PR:
			rotationMap = rRotations;
			positionMap = rPositions;
			break;
		case PL:
			rotationMap = lRotations;
			positionMap = lPositions;
			break;
		default:
			return;
		}
		
		int iters = move.isCW() ? 1 : 2;
		int rotation = 0;
		for(int i = 0; i < iters; i ++) {
			rotation += rotationMap[position];
			position = positionMap[position];
		}
		
		rotateCorner(piece, rotation);
		piece.setPosition(position);
	}
}
