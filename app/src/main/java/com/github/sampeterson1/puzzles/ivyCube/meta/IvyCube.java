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

package com.github.sampeterson1.puzzles.ivyCube.meta;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzle.templates.SimplePuzzle;
import com.github.sampeterson1.puzzles.ivyCube.pieces.IvyCubeCenterBehavior;
import com.github.sampeterson1.puzzles.ivyCube.pieces.IvyCubeCornerBehavior;

//An implementation of Puzzle that represents an Ivy Cube
public class IvyCube extends SimplePuzzle {
	
	private static final int NUM_CORNERS = 4;
	private static final int NUM_CENTERS = 6;

	public IvyCube() {
		super(PuzzleType.IVY_CUBE, true);
		
		super.createPieces(new IvyCubeCornerBehavior(this), NUM_CORNERS);
		super.createPieces(new IvyCubeCenterBehavior(this), NUM_CENTERS);	
	}

	public Piece getCenter(int position) {
		return super.getPiece(PieceType.CENTER, position);
	}
	
	public Piece getCorner(int position) {
		return super.getPiece(PieceType.CORNER, position);
	}

	@Override
	protected PuzzleMetaFunctions<? extends Puzzle> createMetaFunctions() {
		return new IvyCubeMetaFunctions(this);
	}

}
