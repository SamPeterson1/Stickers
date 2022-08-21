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

package com.github.sampeterson1.cube.util;

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;

public class CubeCornerUtil {
	
	private static final int[] positionArr_R = {3, 2, 6, 7, 0, 1, 5, 4};
	private static final int[] positionArr_U = {3, 0, 1, 2, 7, 4, 5, 6};
	private static final int[] positionArr_F = {1, 5, 6, 2, 0, 4, 7, 3};
	
	private static final int[] rotationArr_R = {2, 1, 0, 2, 0, 1, 1, 2};
	private static final int[] rotationArr_U = {0, 0, 0, 0, 0, 0, 0, 0};
	private static final int[] rotationArr_F = {1, 0, 2, 2, 1, 1, 2, 0};

	private static Map<Axis, Integer[]> positions = initPositions();

	private static final Color[][] colors = { 
			{ Color.ORANGE, Color.WHITE, Color.GREEN },
			{ Color.GREEN, Color.WHITE, Color.RED }, 
			{ Color.RED, Color.WHITE, Color.BLUE },
			{ Color.BLUE, Color.WHITE, Color.ORANGE }, 
			{ Color.ORANGE, Color.GREEN, Color.YELLOW },
			{ Color.GREEN, Color.RED, Color.YELLOW },
			{ Color.RED, Color.BLUE, Color.YELLOW },
			{ Color.BLUE, Color.ORANGE, Color.YELLOW }
	};

	public static Color[] getColors(int position) {
		return colors[position];
	}

	public static Integer[] getPositions(Axis face) {
		return positions.get(face);
	}
	
	private static Map<Axis, Integer[]> initPositions() {
		Map<Axis, Integer[]> positions = new HashMap<Axis, Integer[]>();
		
		positions.put(Axis.R, new Integer[] { 1, 2, 6, 5 });
		positions.put(Axis.U, new Integer[] { 0, 3, 2, 1 });
		positions.put(Axis.F, new Integer[] { 0, 1, 5, 4 });
		positions.put(Axis.L, new Integer[] { 0, 3, 7, 4 });
		positions.put(Axis.D, new Integer[] { 4, 7, 6, 5 });
		positions.put(Axis.B, new Integer[] { 2, 6, 7, 3 });
		
		return positions;
	}

	public static int[] mapCorner(Move move, Piece piece) {

		int cubeSize = piece.getPuzzleSize();
		int position = piece.getPosition();
		move = CubeMoveUtil.normalize(move, cubeSize);
		int layer = move.getLayer();
		Axis face = move.getFace();

		int[] positionArr = null;
		int[] rotationArr = null;

		if (layer == 0 || layer == cubeSize - 1) {
			if (face == Axis.R) {
				positionArr = positionArr_R;
				rotationArr = rotationArr_R;
			} else if (face == Axis.U) {
				positionArr = positionArr_U;
				rotationArr = rotationArr_U;
			}
			if (face == Axis.F) {
				positionArr = positionArr_F;
				rotationArr = rotationArr_F;
			}
		}

		int rotation = rotationArr[position];
		int newPos = positionArr[position];
		return new int[] { newPos, rotation };
	}

	public static void populate(int[][] arr, Map<Integer, Integer> map) {
		for (int i = 0; i < arr.length; i++) {
			int key = arr[i][0];
			int val = arr[i][1];
			map.put(key, val);
		}
	}

	public static void rotateCW(Piece corner) {
		Color[] cpy = new Color[3];
		for (int i = 0; i < 3; i++)
			cpy[i] = corner.getColor(i);
		for (int i = 0; i < 3; i++)
			corner.setColor((i + 1) % 3, cpy[i]);
	}
}
