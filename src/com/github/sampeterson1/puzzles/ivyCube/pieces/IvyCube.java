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

package com.github.sampeterson1.puzzles.ivyCube.pieces;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.lib.SimplePuzzle;
import com.github.sampeterson1.puzzle.lib.UniversalAlgorithmParser;
import com.github.sampeterson1.puzzles.ivyCube.IvyCubeSolver;
import com.github.sampeterson1.puzzles.ivyCube.IvyCubeUtil;
import com.github.sampeterson1.puzzles.ivyCube.display.IvyCubeDisplayPiece;

//An implementation of Puzzle that represents an Ivy Cube
public class IvyCube extends SimplePuzzle {
	
	//indices of the corner pieces
	public static final int L_CORNER = 0;
	public static final int B_CORNER = 1;
	public static final int R_CORNER = 2;
	public static final int D_CORNER = 3;
	
	//indices of the center pieces
	public static final int RED_CENTER = 0;
	public static final int WHITE_CENTER = 1;
	public static final int GREEN_CENTER = 2;
	public static final int ORANGE_CENTER = 3;
	public static final int YELLOW_CENTER = 4;
	public static final int BLUE_CENTER = 5;
	
	private static final int NUM_CORNERS = 4;
	private static final int NUM_CENTERS = 6;
	
	private IvyCubeSolver solver;
	
	public IvyCube() {
		super(PuzzleType.IVY_CUBE, 1, true);
		
		super.createPieces(new IvyCubeCornerBehavior(this), NUM_CORNERS);
		super.createPieces(new IvyCubeCenterBehavior(this), NUM_CENTERS);
		
		this.solver = new IvyCubeSolver(this);
	}

	public Piece getCenter(int position) {
		return super.getPiece(PieceType.CENTER, position);
	}
	
	public Piece getCorner(int position) {
		return super.getPiece(PieceType.CORNER, position);
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
		Algorithm scramble = new Algorithm();
		
		for(int i = 0; i < length; i ++) {
			int index = (int) Mathf.random(0, IvyCubeUtil.moveAxes.length);
			boolean cw = (Mathf.random(0, 1) > 0.5f);
			scramble.addMove(new Move(IvyCubeUtil.moveAxes[index], cw));
		}
		
		super.executeAlgorithm(scramble);
		
		return scramble;
	}

	@Override
	public Algorithm solve() {
		return solver.solve();
	}

	@Override
	public ColorPalette createDefaultColorPalette() {
		ColorPalette palette = new ColorPalette();
		
		palette.putColor(Color.BORDER);
		palette.putColor(Color.WHITE);
		palette.putColor(Color.YELLOW);
		palette.putColor(Color.ORANGE);
		palette.putColor(Color.BLUE);
		palette.putColor(Color.GREEN);
		palette.putColor(Color.RED);
		
		return palette;
	}

	@Override
	public DisplayPiece createDisplayPiece(Piece piece) {
		return new IvyCubeDisplayPiece(piece);
	}

	@Override
	public Algorithm parseAlgorithm(String alg) {
		return UniversalAlgorithmParser.parseAlgorithm(alg);
	}

}
