/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.puzzles.pyraminx.pieces;

import java.util.ArrayList;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.GroupedPuzzle;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzles.pyraminx.display.PyraminxDisplayPiece;
import com.github.sampeterson1.puzzles.pyraminx.solvers.PyraminxCenterSolver;
import com.github.sampeterson1.puzzles.pyraminx.solvers.PyraminxEdgeSolver;
import com.github.sampeterson1.puzzles.pyraminx.solvers.PyraminxRingSolver;
import com.github.sampeterson1.puzzles.pyraminx.util.PyraminxCenterUtil;
import com.github.sampeterson1.puzzles.pyraminx.util.PyraminxMoveUtil;

public class Pyraminx extends GroupedPuzzle {
	
	public static Axis[] faces = {Axis.PF, Axis.PR, Axis.PL, Axis.PD};
		
	private static final int NUM_CENTERS = 4;
	private static final int NUM_EDGES = 6;
	private static final int NUM_CORNERS = 4;
	
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
	
	private PyraminxCenterSolver centerSolver;
	private PyraminxEdgeSolver edgeSolver;
	private PyraminxRingSolver ringSolver;
	
	public Pyraminx(int size) {
		super(PuzzleType.PYRAMINX, size);
		
		super.createPieces(new PyraminxCenterBehavior(this), NUM_CENTERS);
		super.createPieces(new PyraminxEdgeBehavior(this), NUM_EDGES);
		super.createPieces(new PyraminxCornerBehavior(this), NUM_CORNERS);
		
		edgeSolver = new PyraminxEdgeSolver(this);
		centerSolver = new PyraminxCenterSolver(this);
		ringSolver = new PyraminxRingSolver(this);
	}
	
	@Override
	public Algorithm solve() {
		Algorithm alg = centerSolver.solve();
		alg.append(edgeSolver.solve());
		alg.append(ringSolver.solve());
		
		return alg;
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

	@Override
	public ColorPalette createDefaultColorPalette() {
		ColorPalette palette = new ColorPalette();
		
		palette.putColor(Color.BORDER);
		palette.putColor(Color.GREEN);
		palette.putColor(Color.BLUE);
		palette.putColor(Color.RED);
		palette.putColor(Color.YELLOW);
		
		return palette;
	}

	@Override
	public DisplayPiece createDisplayPiece(Piece piece) {
		return new PyraminxDisplayPiece(piece);
	}

	@Override
	public Algorithm parseAlgorithm(String alg) {
		return new Algorithm();
	}

}
