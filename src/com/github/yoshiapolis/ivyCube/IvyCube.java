package com.github.yoshiapolis.ivyCube;

import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Puzzle;

public class IvyCube extends Puzzle {

	public static final int L_CORNER = 0;
	public static final int B_CORNER = 1;
	public static final int R_CORNER = 2;
	public static final int D_CORNER = 3;
	
	private static final int NUM_CORNERS = 4;
	private static final int NUM_CENTERS = 6;
	
	public IvyCube(int size) {
		super(size);
		
		super.createPieces(new IvyCubeCornerBehavior(), NUM_CORNERS);
		super.createPieces(new IvyCubeCenterBehavior(), NUM_CENTERS);
	}

	@Override
	public Axis transposeAxis(Axis face) {
		return face;
	}

	@Override
	public Algorithm simplify(Algorithm alg) {
		return alg;
	}

	@Override
	public Algorithm scramble(int length) {
		return new Algorithm();
	}

	@Override
	public Algorithm solve() {
		return new Algorithm();
	}

}
