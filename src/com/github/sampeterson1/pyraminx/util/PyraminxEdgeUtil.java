package com.github.sampeterson1.pyraminx.util;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;

public class PyraminxEdgeUtil {
	
	private static Color[][] colors = {
			{Color.GREEN, Color.BLUE},
			{Color.BLUE, Color.RED},
			{Color.RED, Color.GREEN},
			{Color.GREEN, Color.YELLOW},
			{Color.BLUE, Color.YELLOW},
			{Color.RED, Color.YELLOW}
	};
	
	private static int[] edgeMap_L = {-4, -3, 6, -5, 1, -2};
	private static int[] edgeMap_R = {-2, 5, -6, 3, -1, -4};
	private static int[] edgeMap_D = {2, 3, 1, 5, 6, 4};
	private static int[] edgeMap_F = {4, -5, -1, -3, -6, 2};
	
	public static Color[] getColors(int position) {
		return colors[position];
	}
	
	public static int invertIndex(int index, int puzzleSize) {
		return 2*(puzzleSize-3) - index;
	}
	
	public static int getEdgeMapVal(Axis face, int position) {
		int[] map = null;
		
		if(face == Axis.PL) map = edgeMap_L;
		else if(face == Axis.PR) map = edgeMap_R;
		else if(face == Axis.PD) map = edgeMap_D;
		else if(face == Axis.PF) map = edgeMap_F; 
		
		return map[position];
	}

	public static Axis getFace(int position, int side) {
		if(side == 0) {
			if(position >= 3 && position <= 5) {
				return Axis.PD;
			} else if(position == 0) {
				return Axis.PF;
			} else if(position == 1) {
				return Axis.PR;
			} else if(position == 2) {
				return Axis.PL;
			}
		} else if(side == 1) {
			if(position == 0 || position == 4) {
				return Axis.PR;
			} else if(position == 1 || position == 5) {
				return Axis.PL;
			} else if(position == 2 || position == 3) {
				return Axis.PF;
			}
		}
		
		return null;
	}
	
}
