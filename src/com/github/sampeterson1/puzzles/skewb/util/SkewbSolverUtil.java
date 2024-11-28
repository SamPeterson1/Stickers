package com.github.sampeterson1.puzzles.skewb.util;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzles.skewb.meta.Skewb;

public class SkewbSolverUtil {
	
	private static final int[][][] FACE_CORNERS = findFaceCorners();

	private static final int[] FREE_CORNERS = new int[] {0, 2, 5, 7};
	private static final int[] FIXED_CORNERS = new int[] {1, 3, 4, 6};

	private static int[][][] findFaceCorners() {
		int[][][] faceCorners = new int[6][4][2];
		
		for(int face = 0; face < 6; face ++) {
			Color faceColor = SkewbUtil.CENTER_COLORS[face];
			faceCorners[face] = findFaceCorners(faceColor);
		}
		
		return faceCorners;
	}
	
	private static int[][] findFaceCorners(Color color) {
		int[][] faceCorners = new int[4][2];
		
		int i = 0;
		for(int position = 0; position < Skewb.NUM_CORNERS; position ++) {
			for(int colorIndex = 0; colorIndex < 3; colorIndex ++) {
				if(SkewbUtil.CORNER_COLORS[position][colorIndex] == color) {
					faceCorners[i++] = new int[] {position, colorIndex};
				}
			}
		}
		
		return faceCorners;
	}
	
	public static boolean isSolved(Skewb skewb) {
		for(int face = 0; face < 6; face ++) {
			Color faceColor = skewb.getCenter(face).getColor();
			for(int corner = 0; corner < 4; corner ++) {
				int position = FACE_CORNERS[face][corner][0];
				int colorIndex = FACE_CORNERS[face][corner][1];
				
				Color cornerColor = skewb.getCorner(position).getColor(colorIndex);
				if(cornerColor != faceColor) return false;
			}
		}
		
		return true;
	}
	
	private static int getTwist(Piece corner) {
		int whiteIndex = corner.indexOfColor(Color.WHITE);
		
		if(whiteIndex < 0) return corner.indexOfColor(Color.YELLOW);
		return whiteIndex;
	}
	
	private static int findCorner(Skewb skewb, Color[] colors) {
		for(int position = 0; position < Skewb.NUM_CORNERS; position ++) {
			if(skewb.getCorner(position).hasColors(colors)) return position;
		}
		
		return -1;
	}
	
	private static int findCenter(Skewb skewb, Color color) {
		for(int position = 0; position < Skewb.NUM_CORNERS; position ++) {
			if(skewb.getCenter(position).getColor() == color) return position;
		}
		
		return -1;
	}

	private static int getFreeCornerIndex(Skewb skewb, Color[] colors, int excludedIndex) {
		int position = findCorner(skewb, colors);
		for(int i = 0; i < 4; i ++) {
			if(FREE_CORNERS[i] == position) {
				return (i < excludedIndex) ? i : (i - 1);
			}
		}
				
		return -1;
	}
	
	private static int getCenterIndex(int position, int[] excludedPositions) {
		int index = position;
		for(int excludedPosition : excludedPositions) {
			if(position > excludedPosition) index --;
		}
		
		return index;
	}
	
	public static int hash(Skewb skewb) {
		int hash = 0;
		int placeVal = 1;
		
		for(int i = 0; i < 3; i ++) {
			Piece corner = skewb.getCorner(FIXED_CORNERS[i]);
			hash += placeVal * getTwist(corner);
			placeVal *= 3;
		}
		
		for(int i = 0; i < 3; i ++) {
			Piece corner = skewb.getCorner(FREE_CORNERS[i]);
			hash += placeVal * getTwist(corner);
			placeVal *= 3;
		}
		
		int lastFreeCornerIndex = Integer.MAX_VALUE;
		int placeValMultiplier = 4;
		
		for(int i = 0; i < 2; i ++) {
			Color[] colors = SkewbUtil.CORNER_COLORS[FREE_CORNERS[i]];
			int index = getFreeCornerIndex(skewb, colors, lastFreeCornerIndex);
			
			hash += placeVal * index;
			placeVal *= placeValMultiplier;
			placeValMultiplier --;
			lastFreeCornerIndex = index;
		}
		
		int[] excludedPositions = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE };
		placeValMultiplier = 6;
		for(int i = 0; i < 4; i ++) {
			Color color = SkewbUtil.CENTER_COLORS[i];
			int position = findCenter(skewb, color);
			int index = getCenterIndex(position, excludedPositions);
			hash += placeVal * index;
			placeVal *= placeValMultiplier;
			placeValMultiplier --;
			if(i < 3) excludedPositions[i] = position;
		}
			
		return hash;
	}
	
}
