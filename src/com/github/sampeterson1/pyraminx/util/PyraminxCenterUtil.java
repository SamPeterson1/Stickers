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

package com.github.sampeterson1.pyraminx.util;

import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.pyraminx.pieces.Pyraminx;

public class PyraminxCenterUtil {
	
	private static Color[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW};
	private static Map<Axis, Integer> dRotation;
	private static Map<Axis, Integer> fRotation;
	private static Map<Axis, Integer> rRotation;
	private static Map<Axis, Integer> lRotation;

	public static void init() {		
		dRotation = new EnumMap<>(Axis.class);
		dRotation.put(Axis.PD, 1);
		dRotation.put(Axis.PF, 0);
		dRotation.put(Axis.PR, 0);
		dRotation.put(Axis.PL, 0);
		
		fRotation = new EnumMap<>(Axis.class);
		fRotation.put(Axis.PF, 1);
		fRotation.put(Axis.PR, -1);
		fRotation.put(Axis.PD, -1);
		fRotation.put(Axis.PL, -1);
		
		rRotation = new EnumMap<>(Axis.class);
		rRotation.put(Axis.PR, 1);
		rRotation.put(Axis.PF, -1);
		rRotation.put(Axis.PL, 0);
		rRotation.put(Axis.PD, 1);
		
		lRotation = new EnumMap<>(Axis.class);
		lRotation.put(Axis.PL, 1);
		lRotation.put(Axis.PF, 1);
		lRotation.put(Axis.PD, 0);
		lRotation.put(Axis.PR, -1);
	}
			
	public static Color getColor(int position) {
		return colors[position];
	}
	
	public static int getCenterIndex(int centerSize) {
		if(centerSize % 3 == 0) return -1;
		int centerLayer = centerSize/3;
		int layerSize = 2 * (centerSize - centerLayer) - 1;
		int indexOff = centerSize * centerSize - (centerSize - centerLayer) * (centerSize - centerLayer);
		
		return indexOff + layerSize/2;
	}
	
	public static int getRotationOffset(Axis pivot, Axis origin) {
		Axis targetFace = null;
		int rotation = 0;
		
		switch(pivot) {
		case PD:
			targetFace = Axis.PF;
			rotation = 0;
			break;
		case PR:
			targetFace = Axis.PF;
			rotation = -1;
			break;
		case PL:
			targetFace = Axis.PF;
			rotation = 1;
			break;
		case PF:
			targetFace = Axis.PD;
			rotation = 0;
			break;
		default:
			return 0;
		}
		
		Move move = new Move(pivot, true);
		while(origin != targetFace) {
			rotation -= PyraminxCenterUtil.getRotation(pivot, origin);
			origin = PyraminxMoveUtil.mapFace(origin, move);
		}
		
		return rotation;
	}
	
	public static int getLayer(Piece piece, Axis face) {
		
		Axis pieceFace = Pyraminx.faces[piece.getPosition()];
		if(pieceFace == face) return 0;		
		
		int rotation = (getRotationOffset(face, pieceFace) + 3) % 3;
		int index = piece.getIndex();
		int centerSize = piece.getPuzzleSize() - 3;
		
		for(int i = 0; i < rotation; i ++) {
			index = rotateIndexCCW(index, centerSize);
		}

		return getZPosition(index, centerSize) + 1;
	}
	
	public static int getCenterSize(int puzzleSize) {
		int a = puzzleSize - 3;
		return a*a;
	}

	public static int getLayerSize(int layer, int centerSize) {
		return 2 * (centerSize - layer) - 1;
	}
	
	public static int getIndexAtZPosition(int z, int centerSize) {
		return centerSize * centerSize - (centerSize - z) * (centerSize - z);
	}
	
	public static int getZPosition(int index, int centerSize) {
		return (int)(centerSize - Mathf.sqrt(centerSize * centerSize - index));
	}
	
	public static int getRotation(Axis pivot, Axis from) {
		Map<Axis, Integer> rotationMap = null;
		if(pivot == Axis.PF) rotationMap = fRotation;
		else if(pivot == Axis.PL) rotationMap = lRotation;
		else if(pivot == Axis.PD) rotationMap = dRotation;
		else if(pivot == Axis.PR) rotationMap = rRotation;
		
		return rotationMap.get(from);
	}
	
	public static int mapIndex(Axis from, Axis to, Axis pivot, int index, int puzzleSize) {
		Move move = new Move(pivot, true);
		while(from != to) {
			index = mapIndex(move, from, index, puzzleSize);
			from = PyraminxMoveUtil.mapFace(from, move);
		}
		
		return index;
	}
	
	public static int mapIndex(Move move, Axis from, int index, int puzzleSize) {
		int direction = move.isCW() ? 1 : -1;
		
		Axis pivot = move.getFace();
		if(direction == -1) {
			from = PyraminxMoveUtil.mapFace(from, move);
		}
		int rotation = direction * getRotation(pivot, from);
		
		if(rotation == 1) index = rotateIndexCW(index, puzzleSize - 3);
		else if(rotation == -1) index = rotateIndexCCW(index, puzzleSize - 3);
				
		return index;
	}
	
	public static int rotateIndexCW(int index, int centerSize) {
		int z = (int)(centerSize - Mathf.sqrt(centerSize * centerSize - index));
		int zBound = centerSize * centerSize - (centerSize - z) * (centerSize - z);
		int xy = index - zBound;
		int x = xy/2;
		int y = centerSize - 1 - x - z - (index + z) % 2;
		
		int newX = z;
		int newY = x;
		int newZ = y;
		int newZBound = centerSize * centerSize - (centerSize - newZ) * (centerSize - newZ);
		
		return newZBound + 2 * newX + (newX + newY + newZ + centerSize + 1) % 2;
	}
	
	public static int rotateIndexCCW(int index, int centerSize) {
		int z = (int)(centerSize - Mathf.sqrt(centerSize * centerSize - index));
		int zBound = centerSize * centerSize - (centerSize - z) * (centerSize - z);
		int xy = index - zBound;
		int x = xy/2;
		int y = centerSize - 1 - x - z - (index + z) % 2;
		
		int newX = y;
		int newY = z;
		int newZ = x;
		int newZBound = centerSize * centerSize - (centerSize - newZ) * (centerSize - newZ);
		
		return newZBound + 2 * newX + (newX + newY + newZ + centerSize + 1) % 2;
	}
	
}
