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

package com.github.sampeterson1.puzzles.pyraminx.meta;

import java.util.ArrayList;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.lib.Rotateable;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.templates.GroupedPuzzle;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.pyraminx.pieces.PyraminxCenterBehavior;
import com.github.sampeterson1.puzzles.pyraminx.pieces.PyraminxCornerBehavior;
import com.github.sampeterson1.puzzles.pyraminx.pieces.PyraminxEdgeBehavior;
import com.github.sampeterson1.puzzles.pyraminx.util.PyraminxCenterUtil;
import com.github.sampeterson1.puzzles.pyraminx.util.PyraminxMoveUtil;

public class Pyraminx extends GroupedPuzzle implements Rotateable {
	
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
	
	
	
	public Pyraminx(int size) {
		super(PuzzleType.PYRAMINX, size);
		
		super.createPieces(new PyraminxCenterBehavior(this), NUM_CENTERS);
		super.createPieces(new PyraminxEdgeBehavior(this), NUM_EDGES);
		super.createPieces(new PyraminxCornerBehavior(this), NUM_CORNERS);
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
	protected PuzzleMetaFunctions<? extends Puzzle> createMetaFunctions() {
		return new PyraminxMetaFunctions(this);
	}
	
}
