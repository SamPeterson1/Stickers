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

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;

//Utilities relating to center pieces on a Rubik's Cube
public class CubeCenterUtil {

	private static Map<OrderedFacePair, Integer> faceTranspositions = initFaceTranspositions();
	
	private static Map<OrderedFacePair, Integer> initFaceTranspositions() {
		Map<OrderedFacePair, Integer> transpositions = new HashMap<OrderedFacePair, Integer>();

		putTransposition(transpositions, Axis.R, Axis.R, 0);
		putTransposition(transpositions, Axis.U, Axis.U, 0);
		putTransposition(transpositions, Axis.F, Axis.F, 0);
		putTransposition(transpositions, Axis.L, Axis.L, 0);
		putTransposition(transpositions, Axis.D, Axis.D, 0);
		putTransposition(transpositions, Axis.B, Axis.B, 0);

		putTransposition(transpositions, Axis.R, Axis.U, -1);
		putTransposition(transpositions, Axis.R, Axis.F, 0);
		putTransposition(transpositions, Axis.R, Axis.D, 1);
		putTransposition(transpositions, Axis.R, Axis.B, 0);

		putTransposition(transpositions, Axis.U, Axis.F, 0);
		putTransposition(transpositions, Axis.U, Axis.L, -1);
		putTransposition(transpositions, Axis.U, Axis.B, 2);

		putTransposition(transpositions, Axis.F, Axis.L, 0);
		putTransposition(transpositions, Axis.F, Axis.D, 0);

		putTransposition(transpositions, Axis.L, Axis.D, -1);
		putTransposition(transpositions, Axis.L, Axis.B, 0);

		putTransposition(transpositions, Axis.D, Axis.B, 2);
		
		return transpositions;
	}
	
	private static void putTransposition(Map<OrderedFacePair, Integer> map, Axis a, Axis b, int rotation) {
		map.put(new OrderedFacePair(a, b), rotation);
		if (a != b)
			map.put(new OrderedFacePair(b, a), -rotation);
	}

	public static int getLayer(Piece piece, Axis moveFace, int size) {
		Axis face = CubeUtil.getFace(piece.getPosition());
		int index = piece.getIndex();

		if (face == moveFace)
			return 0;
		if (face == CubeUtil.getOpposingFace(moveFace))
			return size + 1;

		boolean invert = (moveFace == Axis.D || moveFace == Axis.B || moveFace == Axis.L);
		if (invert)
			moveFace = CubeUtil.getOpposingFace(moveFace);

		int layer = 0;

		if (moveFace == Axis.U) {
			int fIndex = CubeCenterUtil.mapIndex(Axis.U, face, Axis.F, index, size);
			layer = fIndex / size + 1;
		} else if (moveFace == Axis.R) {
			int fIndex = CubeCenterUtil.mapIndex(Axis.R, face, Axis.F, index, size);
			layer = size - fIndex % size;
		} else if (moveFace == Axis.F) {
			int uIndex = CubeCenterUtil.mapIndex(Axis.F, face, Axis.U, index, size);
			layer = size - uIndex / size;
		}

		if (invert)
			return size - layer + 1;
		return layer;
	}

	public static int mapIndex(Axis moveFace, Axis origin, Axis dest, int index, int size) {
		Move fakeMove = new Move(moveFace, 0, true);
		Axis next = CubeMoveUtil.mapFace(origin, fakeMove);
		while (origin != dest) {
			OrderedFacePair key = new OrderedFacePair(origin, next);
			int rotation = faceTranspositions.get(key);

			if (rotation < 0) {
				for (int i = 0; i < -rotation; i++)
					index = rotateCCW(index, size);
			} else {
				for (int i = 0; i < rotation; i++)
					index = rotateCW(index, size);
			}

			origin = next;
			next = CubeMoveUtil.mapFace(origin, fakeMove);
		}

		return index;
	}

	public static int rotateCCW(int index, int size) {
		float off = ((size % 2 == 0) ? 0.5f : 0) - size / 2;
		float x = index / size + off;
		float y = index % size + off;

		float tmp = x;
		x = -y;
		y = tmp;

		x -= off;
		y -= off;

		return (int) (x * size + y);
	}

	public static int rotateCW(int index, int size) {
		float off = ((size % 2 == 0) ? 0.5f : 0) - size / 2;
		float x = index / size + off;
		float y = index % size + off;

		float tmp = y;
		y = -x;
		x = tmp;

		x -= off;
		y -= off;

		return (int) (x * size + y);
	}
	
}
