package com.yoshiapolis.cube.solvers;

import java.util.HashMap;

import com.yoshiapolis.cube.pieces.Cube;
import com.yoshiapolis.cube.pieces.CubeMoveUtil;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;

public class PLLCase {
	
	private int[] edgeColors;
	private int[] cornerColors;
	private Algorithm solution;
	
	
	public PLLCase(String solution, int[] faces) {
		this.solution = CubeMoveUtil.parseAlgorithm(solution);
		
		cornerColors = new int[4];
		for(int i = 0; i < 8; i += 2) {
			cornerColors[i/2] = faces[i];
		}
		edgeColors = new int[4];
		for(int i = 1; i < 8; i += 2) {
			edgeColors[i/2] = faces[i];
		}
	}
	
	public Algorithm getSolution() {
		return this.solution;
	}
	
	private Color getColor(Cube cube, int index) {
		Face f = null;
		if(index == 0) f = Face.F;
		else if(index == 1) f = Face.R;
		else if(index == 2) f = Face.B;
		else if(index == 3) f = Face.L;
		
		return cube.getColor(f);
	}
	
	public boolean recognize(Cube cube, int off) {
		
		HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>();
		for(int i = 0; i < 4; i ++) {
			colorMap.put(i, getColor(cube, (i + off) % 4));
		}
		
		for(int i = 0; i < 4; i ++) {
			Color compareTo = colorMap.get(cornerColors[i]);
			Color actual = cube.getCorner(i).getPiece().getColor(2);
			
			if(compareTo != actual) {
				return false;
			}
		}
		
		if(cube.getSize() > 2) {
			for(int i = 0; i < 4; i ++) {
				Color compareTo = colorMap.get(edgeColors[i]);
				Color actual = cube.getEdge(i).getPiece(0).getColor(1);
				
				if(compareTo != actual) {
					return false;
				}
			}
		}
		
		return true;
	}
}
