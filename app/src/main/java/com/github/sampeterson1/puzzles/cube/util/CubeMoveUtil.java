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

import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;

//Utilities relating to moves on a Rubik's Cube
public class CubeMoveUtil {

	public static Map<Axis, Axis> R_CW_Map;
	public static Map<Axis, Axis> U_CW_Map;
	public static Map<Axis, Axis> F_CW_Map;

	public static Map<Axis, Axis> R_CCW_Map;
	public static Map<Axis, Axis> U_CCW_Map;
	public static Map<Axis, Axis> F_CCW_Map;

	public static Move faceNormalize(Move move) {
		Axis face = move.getAxis();
		boolean flipped = false;
		if (face == Axis.L || face == Axis.D || face == Axis.B) {
			face = CubeUtil.getOpposingFace(face);
			flipped = true;
		}

		if (flipped)
			return new Move(face, 0, move.isCCW());
		return new Move(face, 0, move.isCW(), move.isCubeRotation());
	}

	public static void init() {
		R_CW_Map = new HashMap<Axis, Axis>();
		R_CW_Map.put(Axis.F, Axis.U);
		R_CW_Map.put(Axis.U, Axis.B);
		R_CW_Map.put(Axis.B, Axis.D);
		R_CW_Map.put(Axis.D, Axis.F);
		R_CW_Map.put(Axis.R, Axis.R);
		R_CW_Map.put(Axis.L, Axis.L);

		R_CCW_Map = new HashMap<Axis, Axis>();
		R_CCW_Map.put(Axis.U, Axis.F);
		R_CCW_Map.put(Axis.B, Axis.U);
		R_CCW_Map.put(Axis.D, Axis.B);
		R_CCW_Map.put(Axis.F, Axis.D);
		R_CCW_Map.put(Axis.R, Axis.R);
		R_CCW_Map.put(Axis.L, Axis.L);

		U_CW_Map = new HashMap<Axis, Axis>();
		U_CW_Map.put(Axis.R, Axis.F);
		U_CW_Map.put(Axis.B, Axis.R);
		U_CW_Map.put(Axis.L, Axis.B);
		U_CW_Map.put(Axis.F, Axis.L);
		U_CW_Map.put(Axis.U, Axis.U);
		U_CW_Map.put(Axis.D, Axis.D);

		U_CCW_Map = new HashMap<Axis, Axis>();
		U_CCW_Map.put(Axis.F, Axis.R);
		U_CCW_Map.put(Axis.R, Axis.B);
		U_CCW_Map.put(Axis.B, Axis.L);
		U_CCW_Map.put(Axis.L, Axis.F);
		U_CCW_Map.put(Axis.U, Axis.U);
		U_CCW_Map.put(Axis.D, Axis.D);

		F_CW_Map = new HashMap<Axis, Axis>();
		F_CW_Map.put(Axis.U, Axis.R);
		F_CW_Map.put(Axis.R, Axis.D);
		F_CW_Map.put(Axis.D, Axis.L);
		F_CW_Map.put(Axis.L, Axis.U);
		F_CW_Map.put(Axis.F, Axis.F);
		F_CW_Map.put(Axis.B, Axis.B);

		F_CCW_Map = new HashMap<Axis, Axis>();
		F_CCW_Map.put(Axis.R, Axis.U);
		F_CCW_Map.put(Axis.D, Axis.R);
		F_CCW_Map.put(Axis.L, Axis.D);
		F_CCW_Map.put(Axis.U, Axis.L);
		F_CCW_Map.put(Axis.F, Axis.F);
		F_CCW_Map.put(Axis.B, Axis.B);
	}

	public static Axis mapFace(Axis face, Move move) {
		move = CubeMoveUtil.faceNormalize(move);
		Axis moveFace = move.getAxis();
		boolean cw = move.isCW();

		if (moveFace == Axis.R) {
			return cw ? R_CW_Map.get(face) : R_CCW_Map.get(face);
		} else if (moveFace == Axis.U) {
			return cw ? U_CW_Map.get(face) : U_CCW_Map.get(face);
		} else if (moveFace == Axis.F) {
			return cw ? F_CW_Map.get(face) : F_CCW_Map.get(face);
		}

		return Axis.U;
	}

	public static Move normalize(Move move, int cubeSize) {
		Axis face = move.getAxis();
		boolean flipped = false;
		if (face == Axis.L || face == Axis.D || face == Axis.B) {
			face = CubeUtil.getOpposingFace(face);
			flipped = true;
		}

		int layer = move.getLayer();
		if (flipped)
			return new Move(face, cubeSize - layer - 1, move.isCCW());
		return new Move(face, layer, move.isCW(), move.isCubeRotation());
	}

}
