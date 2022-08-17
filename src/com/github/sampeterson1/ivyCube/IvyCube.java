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

package com.github.sampeterson1.ivyCube;

import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.puzzle.lib.PuzzleSizeController;
import com.github.sampeterson1.puzzle.lib.PuzzleType;

//An implementation of Puzzle that represents an Ivy Cube
public class IvyCube extends Puzzle {
	
	//indices of the corner pieces
	public static final int L_CORNER = 0;
	public static final int B_CORNER = 1;
	public static final int R_CORNER = 2;
	public static final int D_CORNER = 3;
	
	//indices of the center pieces on the cube
	public static final int RED_CENTER = 0;
	public static final int WHITE_CENTER = 1;
	public static final int GREEN_CENTER = 2;
	public static final int ORANGE_CENTER = 3;
	public static final int YELLOW_CENTER = 4;
	public static final int BLUE_CENTER = 5;
	
	private static final int NUM_CORNERS = 4;
	private static final int NUM_CENTERS = 6;
	
	public IvyCube() {
		super(1);
		
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
	public PuzzleType getType() {
		return PuzzleType.IVY_CUBE;
	}

}
