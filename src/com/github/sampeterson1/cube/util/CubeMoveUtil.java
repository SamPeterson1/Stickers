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
import java.util.Map;

import com.github.sampeterson1.cube.pieces.Cube;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;

public class CubeMoveUtil {

	public static Map<Axis, Axis> R_CW_Map;
	public static Map<Axis, Axis> U_CW_Map;
	public static Map<Axis, Axis> F_CW_Map;

	public static Map<Axis, Axis> R_CCW_Map;
	public static Map<Axis, Axis> U_CCW_Map;
	public static Map<Axis, Axis> F_CCW_Map;

	public static Move faceNormalize(Move move) {
		Axis face = move.getFace();
		boolean flipped = false;
		if (face == Axis.L || face == Axis.D || face == Axis.B) {
			face = Cube.getOpposingFace(face);
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
		Axis moveFace = move.getFace();
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
		Axis face = move.getFace();
		boolean flipped = false;
		if (face == Axis.L || face == Axis.D || face == Axis.B) {
			face = Cube.getOpposingFace(face);
			flipped = true;
		}

		int layer = move.getLayer();
		if (flipped)
			return new Move(face, cubeSize - layer - 1, move.isCCW());
		return new Move(face, layer, move.isCW(), move.isCubeRotation());
	}

	public static Algorithm parseAlgorithm(String str) {
		if (str.length() == 0)
			return new Algorithm();
		Algorithm retVal = new Algorithm();
		String[] tokens = str.split(" ");
		for (String token : tokens) {
			boolean cw = true;
			int num = 1;
			boolean cubeRotation = false;
			boolean normalMove = true;
			boolean middle = false;
			if (token.length() >= 2) {
				char last = token.charAt(1);
				if (last == '\'') {
					cw = false;
				} else if (last == '2') {
					num = 2;
				}
			}

			Axis face = null;
			switch (token.charAt(0)) {
			case 'R':
				face = Axis.R;
				break;
			case 'U':
				face = Axis.U;
				break;
			case 'F':
				face = Axis.F;
				break;
			case 'L':
				face = Axis.L;
				break;
			case 'D':
				face = Axis.D;
				break;
			case 'B':
				face = Axis.B;
				break;
			case 'r':
				face = Axis.L;
				cubeRotation = true;
				break;
			case 'u':
				face = Axis.D;
				cubeRotation = true;
				break;
			case 'f':
				face = Axis.B;
				cubeRotation = true;
				break;
			case 'l':
				face = Axis.R;
				cubeRotation = true;
				break;
			case 'd':
				face = Axis.U;
				cubeRotation = true;
				break;
			case 'b':
				face = Axis.F;
				cubeRotation = true;
				break;
			case 'M':
				middle = true;
				break;
			case 'x':
				face = Axis.R;
				cubeRotation = true;
				cw = !cw;
				normalMove = false;
				break;
			case 'y':
				face = Axis.U;
				cw = !cw;
				cubeRotation = true;
				normalMove = false;
				break;
			case 'z':
				face = Axis.F;
				cw = !cw;
				cubeRotation = true;
				normalMove = false;
				break;
			}

			for (int i = 0; i < num; i++) {
				if (middle) {
					retVal.addMove(new Move(Axis.R, cw));
					retVal.addMove(new Move(Axis.L, !cw));
					retVal.addMove(new Move(Axis.R, !cw, true));
				} else {
					if (normalMove)
						retVal.addMove(new Move(face, cw));
					if (cubeRotation) {
						retVal.addMove(new Move(Cube.getOpposingFace(face), cw, true));
					}
				}
			}
		}

		return retVal;
	}
}
