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

import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PuzzlePiece;

public class CubeCenterUtil {

	private static Map<Face, Color> colors = new HashMap<Face, Color>();
	private static Map<OrderedFacePair, Integer> faceTranspositions;
	
	private static void putTransposition(Face a, Face b, int rotation) {
		faceTranspositions.put(new OrderedFacePair(a, b), rotation);
		if(a != b) faceTranspositions.put(new OrderedFacePair(b, a), -rotation);
	}
	
	public static void init() {
		faceTranspositions = new HashMap<OrderedFacePair, Integer>();
		
		putTransposition(Face.R, Face.R, 0);
		putTransposition(Face.U, Face.U, 0);
		putTransposition(Face.F, Face.F, 0);
		putTransposition(Face.L, Face.L, 0);
		putTransposition(Face.D, Face.D, 0);
		putTransposition(Face.B, Face.B, 0);
		
		putTransposition(Face.R, Face.U, -1);
		putTransposition(Face.R, Face.F, 0);
		putTransposition(Face.R, Face.D, 1);
		putTransposition(Face.R, Face.B, 0);
		
		putTransposition(Face.U, Face.F, 0);
		putTransposition(Face.U, Face.L, -1);
		putTransposition(Face.U, Face.B, 2);
		
		putTransposition(Face.F, Face.L, 0);
		putTransposition(Face.F, Face.D, 0);
		
		putTransposition(Face.L, Face.D, -1);
		putTransposition(Face.L, Face.B, 0);
		
		putTransposition(Face.D, Face.B, 2);

		colors.put(Face.R, Color.RED);
		colors.put(Face.U, Color.WHITE);
		colors.put(Face.F, Color.GREEN);
		colors.put(Face.L, Color.ORANGE);
		colors.put(Face.D, Color.YELLOW);
		colors.put(Face.B, Color.BLUE);
	}
	
	public static int getLayer(PuzzlePiece piece, Face moveFace, int size) {
		
		Face face = Cube.getFace(piece.getPosition());
		int index = piece.getIndex();
		
		if(face == moveFace) return 0;
		if(face == Cube.getOpposingFace(moveFace)) return size+1;
		
		boolean invert = (moveFace == Face.D || moveFace == Face.B || moveFace == Face.L);
		if(invert) moveFace = Cube.getOpposingFace(moveFace);
		
		int layer = 0;
		
		if(moveFace == Face.U) {
			int fIndex = CubeCenterUtil.mapIndex(Face.U, face, Face.F, index, size);
			layer = fIndex / size + 1;
		} else if(moveFace == Face.R) {
			int fIndex = CubeCenterUtil.mapIndex(Face.R, face, Face.F, index, size);
			layer = size - fIndex % size;
		} else if(moveFace == Face.F) {
			int uIndex = CubeCenterUtil.mapIndex(Face.F, face, Face.U, index, size);
			layer = size - uIndex / size;
		}
		
		if(invert) return size - layer + 1;
		return layer;
	}
	
	public static Color getColor(Face face) {
		return colors.get(face);
	}
	
	public static int rotateCW(int index, int size) {
		float off = ((size % 2 == 0) ? 0.5f : 0) - size/2;
		float x = index / size + off;
		float y = index % size + off;
		
		float tmp = y;
		y = -x;
		x = tmp;
		
		x -= off;
		y -= off;
		
		return (int)(x*size+y);
	}
	
	public static int rotateCCW(int index, int size) {
		float off = ((size % 2 == 0) ? 0.5f : 0) - size/2;
		float x = index / size + off;
		float y = index % size + off;
		
		float tmp = x;
		x = -y;
		y = tmp;
		
		x -= off;
		y -= off;
		
		return (int)(x*size+y);
	}
	
	public static int mapIndex(Face moveFace, Face origin, Face dest, int index, int size) {
		Move fakeMove = new Move(moveFace, 0, true);
		Face next = CubeMoveUtil.mapFace(origin, fakeMove);
		while(origin != dest) {
			OrderedFacePair key = new OrderedFacePair(origin, next);
			int rotation = faceTranspositions.get(key);
			
			if(rotation < 0) {
				for(int i = 0; i < -rotation; i ++) index = rotateCCW(index, size);
			} else {
				for(int i = 0; i < rotation; i ++) index = rotateCW(index, size);
			}
			
			origin = next;
			next = CubeMoveUtil.mapFace(origin, fakeMove);
		}
		
		return index;
	}
}
