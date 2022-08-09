/*
    PrimePuzzle Twisty Puzzle Simulator and Solver
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

package com.github.sampeterson1.cube.util;

import java.util.HashMap;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;

public class CubeCornerUtil {

	private static final int[][] positionArr_R = { { 1, 2 }, { 2, 6 }, { 6, 5 }, { 5, 1 }, { 0, 3 }, { 3, 7 }, { 7, 4 },
			{ 4, 0 } };

	private static final int[][] positionArr_U = { { 0, 3 }, { 3, 2 }, { 2, 1 }, { 1, 0 }, { 4, 7 }, { 7, 6 }, { 6, 5 },
			{ 5, 4 } };
	private static final int[][] positionArr_F = { { 0, 1 }, { 1, 5 }, { 5, 4 }, { 4, 0 }, { 2, 6 }, { 6, 7 }, { 7, 3 },
			{ 3, 2 } };

	private static HashMap<Integer, Integer> positionMap_R;
	private static HashMap<Integer, Integer> positionMap_U;
	private static HashMap<Integer, Integer> positionMap_F;

	private static final int[][] rotationArr_R = { { 1, 1 }, { 2, 0 }, { 6, 1 }, { 5, 1 }, { 0, 2 }, { 3, 2 }, { 7, 2 },
			{ 4, 0 }, };
	private static final int[][] rotationArr_U = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 7, 0 }, { 6, 0 },
			{ 5, 0 }, };
	private static final int[][] rotationArr_F = { { 0, 1 }, { 1, 0 }, { 5, 1 }, { 4, 1 }, { 2, 2 }, { 6, 2 }, { 7, 0 },
			{ 3, 2 } };

	private static HashMap<Integer, Integer> rotationMap_R;
	private static HashMap<Integer, Integer> rotationMap_U;
	private static HashMap<Integer, Integer> rotationMap_F;

	private static HashMap<Axis, Integer[]> positions;

	private static final Color[][] colors = { { Color.ORANGE, Color.WHITE, Color.GREEN },
			{ Color.GREEN, Color.WHITE, Color.RED }, { Color.RED, Color.WHITE, Color.BLUE },
			{ Color.BLUE, Color.WHITE, Color.ORANGE }, { Color.ORANGE, Color.GREEN, Color.YELLOW },
			{ Color.GREEN, Color.RED, Color.YELLOW }, { Color.RED, Color.BLUE, Color.YELLOW },
			{ Color.BLUE, Color.ORANGE, Color.YELLOW }, };

	public static Color[] getColors(int position) {
		return colors[position];
	}

	public static Integer[] getPositions(Axis face) {
		return positions.get(face);
	}

	public static void init() {
		positionMap_R = new HashMap<Integer, Integer>();
		positionMap_U = new HashMap<Integer, Integer>();
		positionMap_F = new HashMap<Integer, Integer>();

		populate(positionArr_R, positionMap_R);
		populate(positionArr_U, positionMap_U);
		populate(positionArr_F, positionMap_F);

		rotationMap_R = new HashMap<Integer, Integer>();
		rotationMap_U = new HashMap<Integer, Integer>();
		rotationMap_F = new HashMap<Integer, Integer>();

		populate(rotationArr_R, rotationMap_R);
		populate(rotationArr_U, rotationMap_U);
		populate(rotationArr_F, rotationMap_F);

		positions = new HashMap<Axis, Integer[]>();
		positions.put(Axis.R, new Integer[] { 1, 2, 6, 5 });
		positions.put(Axis.U, new Integer[] { 0, 3, 2, 1 });
		positions.put(Axis.F, new Integer[] { 0, 1, 5, 4 });
		positions.put(Axis.L, new Integer[] { 0, 3, 7, 4 });
		positions.put(Axis.D, new Integer[] { 4, 7, 6, 5 });
		positions.put(Axis.B, new Integer[] { 2, 6, 7, 3 });
	}

	public static int[] mapCorner(Move move, Piece piece) {

		int cubeSize = piece.getPuzzleSize();
		int position = piece.getPosition();
		move = CubeMoveUtil.normalize(move, cubeSize);
		int layer = move.getLayer();
		Axis face = move.getFace();

		HashMap<Integer, Integer> positionMap = null;
		HashMap<Integer, Integer> rotationMap = null;

		if (layer == 0 || layer == cubeSize - 1) {
			if (face == Axis.R) {
				positionMap = positionMap_R;
				rotationMap = rotationMap_R;
			} else if (face == Axis.U) {
				positionMap = positionMap_U;
				rotationMap = rotationMap_U;
			}
			if (face == Axis.F) {
				positionMap = positionMap_F;
				rotationMap = rotationMap_F;
			}
		}

		if (positionMap != null && positionMap.containsKey(position)) {
			int rotation = rotationMap.get(position);
			int newPos = positionMap.get(position);
			return new int[] { newPos, rotation };
		} else {
			return new int[] { position, 0 };
		}
	}

	public static void populate(int[][] arr, HashMap<Integer, Integer> map) {
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
