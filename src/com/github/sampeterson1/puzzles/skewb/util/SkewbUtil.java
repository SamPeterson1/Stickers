package com.github.sampeterson1.puzzles.skewb.util;

import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzles.skewb.meta.Skewb;

public class SkewbUtil {

	public static final Axis[] AXES = new Axis[] { Axis.SKF, Axis.SKL, Axis.SKR, Axis.SKB };
	
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
	private static final int[] lCenterPositions = new int[] {0, 1, 4};
	private static final int[] rCenterPositions = new int[] {0, 3, 2};
	private static final int[] bCenterPositions = new int[] {0, 4, 3};
	
	private static final int[] fCornerPositions = new int[] {0, 2, 5};
	private static final int[] lCornerPositions = new int[] {1, 4, 3};
	private static final int[] rCornerPositions = new int[] {1, 3, 6};
	private static final int[] bCornerPositions = new int[] {2, 0, 7};
	
	private static final int[][] fCornerRotations = new int[][] {{1, 1}, {0, 2}, {2, 0}, {5, 1}};
	private static final int[][] lCornerRotations = new int[][] {{0, 1}, {1, 0}, {4, 1}, {3, 2}};
	private static final int[][] rCornerRotations = new int[][] {{2, 1}, {1, 2}, {3, 0}, {6, 1}};
	private static final int[][] bCornerRotations = new int[][] {{3, 1}, {2, 2}, {0, 0}, {7, 1}};

	private static final Map<Axis, int[]> centerPositionMap = initCenterPositionMap();
	private static final Map<Axis, int[]> cornerPositionMap = initCornerPositionMap();
	private static final Map<Axis, int[]> cornerRotationMap = initCornerRotationMap();
	
	private static Map<Axis, int[]> initCornerRotationMap() {
		Map<Axis, int[]> map = new EnumMap<Axis, int[]>(Axis.class);
	
		map.put(Axis.SKF, toMap(fCornerRotations, Skewb.NUM_CORNERS));
		map.put(Axis.SKL, toMap(lCornerRotations, Skewb.NUM_CORNERS));
		map.put(Axis.SKR, toMap(rCornerRotations, Skewb.NUM_CORNERS));
		map.put(Axis.SKB, toMap(bCornerRotations, Skewb.NUM_CORNERS));
		
		return map;
	}
	
	private static int[] toMap(int[][] arr, int numValues) {
		int[] map = new int[numValues];
		
		for(int i = 0; i < arr.length; i ++) {
			if(arr[i][1] != 0) map[arr[i][0]] = arr[i][1];
		}
		
		return map;
	}
	
	private static Map<Axis, int[]> initCenterPositionMap() {
		Map<Axis, int[]> map = new EnumMap<Axis, int[]>(Axis.class);
		
		map.put(Axis.SKF, cycleToMap(fCenterPositions, Skewb.NUM_CENTERS));
		map.put(Axis.SKL, cycleToMap(lCenterPositions, Skewb.NUM_CENTERS));
		map.put(Axis.SKR, cycleToMap(rCenterPositions, Skewb.NUM_CENTERS));
		map.put(Axis.SKB, cycleToMap(bCenterPositions, Skewb.NUM_CENTERS));
	
		return map;
	}
	
	private static Map<Axis, int[]> initCornerPositionMap() {
		Map<Axis, int[]> map = new EnumMap<Axis, int[]>(Axis.class);
		
		map.put(Axis.SKF, cycleToMap(fCornerPositions, Skewb.NUM_CORNERS));
		map.put(Axis.SKL, cycleToMap(lCornerPositions, Skewb.NUM_CORNERS));
		map.put(Axis.SKR, cycleToMap(rCornerPositions, Skewb.NUM_CORNERS));
		map.put(Axis.SKB, cycleToMap(bCornerPositions, Skewb.NUM_CORNERS));
		
		return map;
	}
	
	private static int[] cycleToMap(int[] positions, int numValues) {
		int[] map = new int[numValues];
		
		for(int i = 0; i < map.length; i ++)
			map[i] = i;
		
		for(int i = 0; i < positions.length; i ++) {
			int next = (i + 1) % positions.length;
			map[positions[i]] = positions[next];
		}
		
		return map;
	}
	
	public static int mapCenterPosition(Axis axis, int position) {
		return centerPositionMap.get(axis)[position];
	}
	
	public static int mapCornerPosition(Axis axis, int position) {
		return cornerPositionMap.get(axis)[position];
	}
	
	public static int mapCornerRotation(Axis axis, int position) {
		return cornerRotationMap.get(axis)[position];
	}
	
}
