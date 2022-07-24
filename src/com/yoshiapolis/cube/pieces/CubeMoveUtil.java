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
import java.util.Map;

import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;

public class CubeMoveUtil {

	public static Map<Face, Face> R_CW_Map;
	public static Map<Face, Face> U_CW_Map;
	public static Map<Face, Face> F_CW_Map;
  
	public static Map<Face, Face> R_CCW_Map;
	public static Map<Face, Face> U_CCW_Map;
	public static Map<Face, Face> F_CCW_Map;
  
	public static void init() {
		R_CW_Map = new HashMap<Face, Face>();
		R_CW_Map.put(Face.F, Face.U);
		R_CW_Map.put(Face.U, Face.B);
		R_CW_Map.put(Face.B, Face.D);
		R_CW_Map.put(Face.D, Face.F);
		R_CW_Map.put(Face.R, Face.R);
		R_CW_Map.put(Face.L, Face.L);
		
		R_CCW_Map = new HashMap<Face, Face>();
		R_CCW_Map.put(Face.U, Face.F);
		R_CCW_Map.put(Face.B, Face.U);
		R_CCW_Map.put(Face.D, Face.B);
		R_CCW_Map.put(Face.F, Face.D);
		R_CCW_Map.put(Face.R, Face.R);
		R_CCW_Map.put(Face.L, Face.L);
		
		U_CW_Map = new HashMap<Face, Face>();
		U_CW_Map.put(Face.R, Face.F);
		U_CW_Map.put(Face.B, Face.R);
		U_CW_Map.put(Face.L, Face.B);
		U_CW_Map.put(Face.F, Face.L);
		U_CW_Map.put(Face.U, Face.U);
		U_CW_Map.put(Face.D, Face.D);
		
		U_CCW_Map = new HashMap<Face, Face>();
		U_CCW_Map.put(Face.F, Face.R);
		U_CCW_Map.put(Face.R, Face.B);
		U_CCW_Map.put(Face.B, Face.L);
		U_CCW_Map.put(Face.L, Face.F);
		U_CCW_Map.put(Face.U, Face.U);
		U_CCW_Map.put(Face.D, Face.D);
		
		F_CW_Map = new HashMap<Face, Face>();
		F_CW_Map.put(Face.U, Face.R);
		F_CW_Map.put(Face.R, Face.D);
		F_CW_Map.put(Face.D, Face.L);
		F_CW_Map.put(Face.L, Face.U);
		F_CW_Map.put(Face.F, Face.F);
		F_CW_Map.put(Face.B, Face.B);
		
		F_CCW_Map = new HashMap<Face, Face>();
		F_CCW_Map.put(Face.R, Face.U);
		F_CCW_Map.put(Face.D, Face.R);
		F_CCW_Map.put(Face.L, Face.D);
		F_CCW_Map.put(Face.U, Face.L);
		F_CCW_Map.put(Face.F, Face.F);
		F_CCW_Map.put(Face.B, Face.B);
	}
  
	public static Face mapFace(Face face, Move move) {
		move = CubeMoveUtil.faceNormalize(move);
		Face moveFace = move.getFace();
		boolean cw = move.isCW();
		  
		if(moveFace == Face.R) {
			return cw ? R_CW_Map.get(face) : R_CCW_Map.get(face);
		} else if(moveFace == Face.U) {
			return cw ? U_CW_Map.get(face) : U_CCW_Map.get(face);
		} else if(moveFace == Face.F) {
			return cw ? F_CW_Map.get(face) : F_CCW_Map.get(face);
		}
		  
		return Face.U;
	}  

  
	public static Move faceNormalize(Move move) {
		Face face = move.getFace();
		boolean flipped = false;
		if(face == Face.L || face == Face.D || face == Face.B) {
			face = Cube.getOpposingFace(face);
			flipped = true;
		}
	
		if(flipped) return new Move(face, 0, move.isCCW());
		return new Move(face, 0, move.isCW(), move.isCubeRotation());
	}
  
	public static Move normalize(Move move, int cubeSize) {
		Face face = move.getFace();
		boolean flipped = false;
		if(face == Face.L || face == Face.D || face == Face.B) {
			face = Cube.getOpposingFace(face);
			flipped = true;
		}

		int layer = move.getLayer();
		if(flipped) return new Move(face, cubeSize-layer-1, move.isCCW());
		return new Move(face, layer, move.isCW(), move.isCubeRotation());
	}
	
	public static Algorithm parseAlgorithm(String str) {
		if(str.length() == 0) return new Algorithm();
		Algorithm retVal = new Algorithm();
		String[] tokens = str.split(" ");
		for(String token : tokens) {
			boolean cw = true;
			int num = 1;
			boolean cubeRotation = false;
			boolean normalMove = true;
			boolean middle = false;
			if(token.length() >= 2) {
				char last = token.charAt(1);
				if(last == '\'') {
					cw = false;
				} else if(last == '2') {
					num = 2;
				}
			} 

			Face face = null;
			switch(token.charAt(0)) {
			case 'R':
				face = Face.R;
				break;
			case 'U':
				face = Face.U;
				break;
			case 'F':
				face = Face.F;
				break;
			case 'L':
				face = Face.L;
				break;
			case 'D':
				face = Face.D;
				break;
			case 'B':
				face = Face.B;
				break;
			case 'r':
				face = Face.L;
				cubeRotation = true;
				break;
			case 'u':
				face = Face.D;
				cubeRotation = true;
				break;
			case 'f':
				face = Face.B;
				cubeRotation = true;
				break;
			case 'l':
				face = Face.R;
				cubeRotation = true;
				break;
			case 'd':
				face = Face.U;
				cubeRotation = true;
				break;
			case 'b':
				face = Face.F;
				cubeRotation = true;
				break;
			case 'M':
				middle = true;
				break;
			case 'x':
				face = Face.R;
				cubeRotation = true;
				cw = !cw;
				normalMove = false;
				break;
			case 'y':
				face = Face.U;
				cw = !cw;
				cubeRotation = true;
				normalMove = false;
				break;
			case 'z':
				face = Face.F;
				cw = !cw;
				cubeRotation = true;
				normalMove = false;
				break;
			}
			
			for(int i = 0; i < num; i ++) {
				if(middle) {
					retVal.addMove(new Move(Face.R, cw));
					retVal.addMove(new Move(Face.L, !cw));
					retVal.addMove(new Move(Face.R, !cw, true));
				} else {
					if(normalMove) retVal.addMove(new Move(face, cw));
					if(cubeRotation) {
						retVal.addMove(new Move(Cube.getOpposingFace(face), cw, true));
					}
				}
			}
		}
		
		return retVal;
	}
}
