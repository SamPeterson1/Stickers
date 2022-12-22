package com.github.sampeterson1.puzzles.skewb.util;

import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.moves.Axis;

public class SkewbUtil {

	public static final Color[][] CORNER_COLORS = { 
			{ Color.ORANGE, Color.WHITE, Color.GREEN },
			{ Color.GREEN, Color.WHITE, Color.RED }, 
			{ Color.RED, Color.WHITE, Color.BLUE },
			{ Color.BLUE, Color.WHITE, Color.ORANGE }, 
			{ Color.ORANGE, Color.GREEN, Color.YELLOW },
			{ Color.GREEN, Color.RED, Color.YELLOW },
			{ Color.RED, Color.BLUE, Color.YELLOW },
			{ Color.BLUE, Color.ORANGE, Color.YELLOW }
	};
	
	public static final Color[] CENTER_COLORS = {
			Color.WHITE, Color.GREEN, Color.RED, 
			Color.BLUE, Color.ORANGE, Color.YELLOW
	};
	
	private static final int[] fCenterPositions = new int[] {0, 2, 1};
	private static final int[] lCenteRPositions = new int[] {0, 1, 4};
	private static final int[] rCenterPositions = new int[] {0, 3, 2};
	private static final int[] bCenterPositions = new int[] {0, 4, 3};
	
	//private static final int[] 
	
	//private static final Map<Axis, Map<Integer, Integer>> centerPositionMap;
	//private static final Map<Axis, Map<Integer, Integer>> cornerPositionMap;
	//private static final Map<Axis, Map<Integer, Integer>> cornerRotationMap;
	
}
