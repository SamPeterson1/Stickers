/*
    PrimePuzzle Twisty Puzzle Simulator and Solver
    Copyright (C) 2022 Sam Peterson
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.github.yoshiapolis.pyraminx.pieces;

import java.util.ArrayList;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.puzzle.lib.Puzzle;
import com.github.yoshiapolis.pyraminx.solvers.PyraminxCenterSolver;
import com.github.yoshiapolis.pyraminx.util.PyraminxCenterUtil;
import com.github.yoshiapolis.pyraminx.util.PyraminxMoveUtil;

public class Pyraminx extends Puzzle {
	
	public static Axis[] faces = {Axis.PF, Axis.PR, Axis.PL, Axis.PD};
	
	public static final int NUM_CENTERS = 4;
	public static final int NUM_EDGES = 6;
	public static final int NUM_CORNERS = 4;
	
	public static void init() {
		PyraminxCenterUtil.init();
		PyraminxMoveUtil.init();
	}
	
	public static int getAxisIndex(Axis axis) {
		if(axis == Axis.PF) return 0;
		if(axis == Axis.PR) return 1;
		if(axis == Axis.PL) return 2;
		return 3;
	}
	
	public static Axis getFace(Piece piece) {
		return faces[piece.getPosition()];
	}
	
	PyraminxCenterSolver centerSolver;
	
	public Pyraminx(int size) {
		super(size);
		
		super.createPieces(new PyraminxCenterBehavior(), NUM_CENTERS);
		super.createPieces(new PyraminxEdgeBehavior(), NUM_EDGES);
		super.createPieces(new PyraminxCornerBehavior(), NUM_CORNERS);
		
		centerSolver = new PyraminxCenterSolver(this);
	}
	
	@Override
	public Algorithm solve() {
		return centerSolver.solve();
	}
	
	private Move getRandomMove(int puzzleSize) {
		int i = (int) Mathf.random(0, faces.length);
		Axis f = faces[i];

		int layer = (int) Mathf.random(0, puzzleSize);
		boolean cw = (Mathf.random(0, 1) < 0.5);

		return new Move(f, layer, cw);
	}
	
	@Override
	public Axis transposeAxis(Axis face) {
		ArrayList<Move> cubeRotations = super.getRotations();
		for(Move move : cubeRotations) {
			face = PyraminxMoveUtil.mapFace(face, move);
		}
		
		return face;
	}

	public PieceGroup getGroup(PieceType type, Axis face) {
		int position = getAxisIndex(face);
		return super.getGroup(type, position);
	}
	
	@Override
	public Algorithm simplify(Algorithm alg) {
		return alg;
	}

	@Override
	public Algorithm scramble(int length) {
		Algorithm scramble = new Algorithm();
		for(int i = 0; i < length; i ++) {
			scramble.addMove(getRandomMove(super.getSize()));
		}
		
		super.executeAlgorithm(scramble);
		return scramble;
	}

	
}
