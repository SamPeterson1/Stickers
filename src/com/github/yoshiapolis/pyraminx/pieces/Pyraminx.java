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

import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceBehavior;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.puzzle.lib.Puzzle;
import com.github.yoshiapolis.pyraminx.solvers.PyraminxCenterSolver;
import com.github.yoshiapolis.pyraminx.util.PyraminxCenterUtil;
import com.github.yoshiapolis.pyraminx.util.PyraminxMoveUtil;

public class Pyraminx extends Puzzle {
	
	public static Face[] faces = {Face.PF, Face.PR, Face.PL, Face.PD};
	
	public static void init() {
		PyraminxCenterUtil.init();
		PyraminxMoveUtil.init();
	}
	
	public static Face getFace(Piece piece) {
		return faces[piece.getPosition()];
	}
	
	PyraminxCenterSolver centerSolver;
	
	public Pyraminx(int size) {
		super(size);
		
		ArrayList<PieceGroup> centers = new ArrayList<PieceGroup>();
		PieceBehavior centerBehavior = new PyraminxCenterBehavior();
		
		for(int i = 0; i < 4; i ++) {
			centers.add(new PieceGroup(centerBehavior, this, i));
		}

		ArrayList<PieceGroup> edges = new ArrayList<PieceGroup>();
		PieceBehavior edgeBehavior = new PyraminxEdgeBehavior();
		
		for(int i = 0; i < 6; i ++) {
			edges.add(new PieceGroup(edgeBehavior, this, i));
		}
		
		ArrayList<PieceGroup> corners = new ArrayList<PieceGroup>();
		PieceBehavior cornerBehavior = new PyraminxCornerBehavior();
		
		for(int i = 0; i < 4; i ++) {
			corners.add(new PieceGroup(cornerBehavior, this, i));
		}
		
		super.pieces.put(PieceType.CENTER, centers);
		super.pieces.put(PieceType.EDGE, edges);
		super.pieces.put(PieceType.CORNER, corners);
		
		centerSolver = new PyraminxCenterSolver(this);
	}
	
	@Override
	public Algorithm solve() {
		return centerSolver.solve();
	}
	
	@Override
	public Face transposeFace(Face face) {
		ArrayList<Move> cubeRotations = super.getRotations();
		for(Move move : cubeRotations) {
			face = PyraminxMoveUtil.mapFace(face, move);
		}
		
		return face;
	}

	
}
