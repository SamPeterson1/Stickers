package com.yoshiapolis.cube.solvers;

import com.yoshiapolis.cube.pieces.Cube;
import com.yoshiapolis.cube.pieces.CubeMoveUtil;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.PuzzlePiece;

public class OLLCase {
	
	private Algorithm solution;
	int[] edgeLocations;
	int[] cornerLocations;
	
	public OLLCase(String solution, int[] locations) {
		this.solution = CubeMoveUtil.parseAlgorithm(solution);
		cornerLocations = new int[4];
		for(int i = 0; i < 8; i += 2) cornerLocations[i/2] = locations[i];
		edgeLocations = new int[4];
		for(int i = 1; i < 8; i += 2) edgeLocations[i/2] = locations[i];
	}
	
	public Algorithm getSolution() {
		return this.solution;
	}
	
	public boolean recognize(Cube cube) {
		Color top = cube.getColor(Face.U);
		boolean retVal = true;
		if(cube.getSize() > 2) {
			for(int i = 0; i < 4; i ++) {
				PuzzlePiece piece = cube.getEdge(i).getPiece(0);
				if(piece.indexOfColor(top) != edgeLocations[i]) {
					retVal = false;
				}
			}
		}
		
		for(int i = 0; i < 4; i ++) {
			PuzzlePiece piece = cube.getCorner(i).getPiece();
			if(piece.indexOfColor(top) != cornerLocations[i]) {
				retVal = false;
			}
		}
		
		return retVal;
	}
}
