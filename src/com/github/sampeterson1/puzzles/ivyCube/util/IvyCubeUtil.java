package com.github.sampeterson1.puzzles.ivyCube.util;

import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzles.ivyCube.meta.IvyCube;

public class IvyCubeUtil {

	//indices of the corner pieces
	public static final int L_CORNER = 0;
	public static final int B_CORNER = 1;
	public static final int R_CORNER = 2;
	public static final int D_CORNER = 3;
	
	//indices of the center pieces
	public static final int RED_CENTER = 0;
	public static final int WHITE_CENTER = 1;
	public static final int GREEN_CENTER = 2;
	public static final int ORANGE_CENTER = 3;
	public static final int YELLOW_CENTER = 4;
	public static final int BLUE_CENTER = 5;
	
	public static Color[] centerColors = {
			Color.RED, Color.WHITE, Color.GREEN,
			Color.ORANGE, Color.YELLOW, Color.BLUE
	};
	
	public static final Color[][] cornerColors = {
			{Color.WHITE, Color.GREEN, Color.ORANGE},
			{Color.WHITE, Color.BLUE, Color.RED},
			{Color.YELLOW, Color.GREEN, Color.RED},
			{Color.YELLOW, Color.BLUE, Color.ORANGE}
	};
	
	public static final Axis[] moveAxes = {Axis.IR, Axis.IL, Axis.ID, Axis.IB};
	private static final IvyCube solvedState = new IvyCube();
	private static final Map<Color, Integer> colorHashIDs = initColorHashIDs();
	
	private static final Map<Color, Integer> initColorHashIDs() {
		Map<Color, Integer> colorHashIDs = new EnumMap<Color, Integer>(Color.class);
		
		for(int i = 0; i < 6; i ++) {
			colorHashIDs.put(centerColors[i], i);
		}
		
		return colorHashIDs;
	}
	
	public static boolean isSolved(IvyCube ivy) {
		return equals(ivy, solvedState);
	}
	
	public static boolean equals(IvyCube a, IvyCube b) {
		return (hash(a) == hash(b));
	}
	
	public static int hash(IvyCube ivy) {
		int placeValue = 1;
		int hash = 0;
		
		for(int i = 0; i < 6; i ++) {
			Color color = ivy.getCenter(i).getColor();
			hash += placeValue * colorHashIDs.get(color);
			placeValue *= 6;
		}
		for(int i = 0; i < 4; i ++) {
			Color color = ivy.getCorner(i).getColor();
			hash += placeValue * colorHashIDs.get(color);
			placeValue *= 6;
		}
		
		return hash;
	}
	
}
