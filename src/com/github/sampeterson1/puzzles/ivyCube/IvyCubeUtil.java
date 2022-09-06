package com.github.sampeterson1.puzzles.ivyCube;

import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzles.ivyCube.pieces.IvyCube;

public class IvyCubeUtil {

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
