package com.github.yoshiapolis.pyraminx.util;

import java.util.EnumMap;
import java.util.Map;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.pyraminx.pieces.Pyraminx;

public class PyraminxCenterUtil {
	
	private static Color[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW};
	private static Map<Face, Integer> dRotation;
	private static Map<Face, Integer> fRotation;
	private static Map<Face, Integer> rRotation;
	private static Map<Face, Integer> lRotation;

	public static void init() {		
		dRotation = new EnumMap<>(Face.class);
		dRotation.put(Face.PD, 1);
		dRotation.put(Face.PF, 0);
		dRotation.put(Face.PR, 0);
		dRotation.put(Face.PL, 0);
		
		fRotation = new EnumMap<>(Face.class);
		fRotation.put(Face.PF, 1);
		fRotation.put(Face.PR, -1);
		fRotation.put(Face.PD, -1);
		fRotation.put(Face.PL, -1);
		
		rRotation = new EnumMap<>(Face.class);
		rRotation.put(Face.PR, 1);
		rRotation.put(Face.PF, -1);
		rRotation.put(Face.PL, 0);
		rRotation.put(Face.PD, 1);
		
		lRotation = new EnumMap<>(Face.class);
		lRotation.put(Face.PL, 1);
		lRotation.put(Face.PF, 1);
		lRotation.put(Face.PD, 0);
		lRotation.put(Face.PR, -1);
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
	
	public static int getRotationOffset(Face pivot, Face origin) {
		Face targetFace = null;
		int rotation = 0;
		
		switch(pivot) {
		case PD:
			targetFace = Face.PF;
			rotation = 0;
			break;
		case PR:
			targetFace = Face.PF;
			rotation = -1;
			break;
		case PL:
			targetFace = Face.PF;
			rotation = 1;
			break;
		case PF:
			targetFace = Face.PD;
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
	
	public static int getLayer(Piece piece, Face face) {
		
		Face pieceFace = Pyraminx.faces[piece.getPosition()];
		if(pieceFace == face) return 0;		
		
		int rotation = (getRotationOffset(face, pieceFace) + 3) % 3;
		int index = piece.getIndex();
		int centerSize = piece.getPuzzleSize() - 3;
		
		for(int i = 0; i < rotation; i ++) {
			index = rotateIndexCCW(index, centerSize);
		}

		return getZPosition(index, centerSize) + 1;
	}
	
	public static int getZPosition(int index, int centerSize) {
		return (int)(centerSize - Mathf.sqrt(centerSize * centerSize - index));
	}
	
	public static int getRotation(Face pivot, Face from) {
		Map<Face, Integer> rotationMap = null;
		if(pivot == Face.PF) rotationMap = fRotation;
		else if(pivot == Face.PL) rotationMap = lRotation;
		else if(pivot == Face.PD) rotationMap = dRotation;
		else if(pivot == Face.PR) rotationMap = rRotation;
		
		return rotationMap.get(from);
	}
	
	public static int mapIndex(Face from, Face to, Face pivot, int index, int puzzleSize) {
		Move move = new Move(pivot, true);
		while(from != to) {
			index = mapIndex(move, from, index, puzzleSize);
			from = PyraminxMoveUtil.mapFace(from, move);
		}
		
		return index;
	}
	
	public static int mapIndex(Move move, Face from, int index, int puzzleSize) {
		int direction = move.isCW() ? 1 : -1;
		
		Face pivot = move.getFace();
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
