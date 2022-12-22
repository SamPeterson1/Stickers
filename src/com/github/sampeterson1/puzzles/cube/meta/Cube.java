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

package com.github.sampeterson1.puzzles.cube.meta;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.lib.Rotateable;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.templates.GroupedPuzzle;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.cube.pieces.CubeCenterBehavior;
import com.github.sampeterson1.puzzles.cube.pieces.CubeCornerBehavior;
import com.github.sampeterson1.puzzles.cube.pieces.CubeEdgeBehavior;
import com.github.sampeterson1.puzzles.cube.util.CubeMoveUtil;
import com.github.sampeterson1.puzzles.cube.util.CubeUtil;

//An implementation of Puzzle that represents the pieces of an n by n Rubk's Cube
public class Cube extends GroupedPuzzle implements Rotateable {

	private static final int NUM_CENTERS = 6;
	private static final int NUM_EDGES = 12;
	private static final int NUM_CORNERS = 8;

	public Cube(int size) {
		super(PuzzleType.CUBE, size);

		super.createPieces(new CubeCenterBehavior(this), NUM_CENTERS);
		super.createPieces(new CubeEdgeBehavior(this), NUM_EDGES);
		super.createPieces(new CubeCornerBehavior(this), NUM_CORNERS);		
	}

	public PieceGroup getCorner(int position) {
		return super.getGroup(PieceType.CORNER, position);
	}

	public PieceGroup getEdge(int position) {
		return super.getGroup(PieceType.EDGE, position);
	}
	
	public PieceGroup getCenter(Axis face) {
		return super.getGroup(PieceType.CENTER, CubeUtil.getFacePosition(face));
	}

	/*
	 * Given a face, return the color that it should be when solved.
	 * 
	 * If the cube has center pieces (its size is greater than 2), we
	 * can determine this by looking at the color of the center piece on that face.
	 * Otherwise, we transpose the face to align with the current cube rotations
	 * and return a predetermined face color defined by CubeUtil.
	 */
	public Color getSolveColor(Axis face) {
		if(super.getSize() == 2) {
			face = transposeAxis(face);
			return CubeUtil.getFaceColor(face);
		}
		
		int centerIndex = (super.getSize() - 2) * (super.getSize() - 2) / 2;
		return getCenter(face).getPiece(centerIndex).getColor();
	}

	@Override
	public Axis transposeAxis(Axis face) {
		for (Move move : super.getRotations()) {
			face = CubeMoveUtil.mapFace(face, move);
		}

		return face;
	}

	@Override
	protected PuzzleMetaFunctions<? extends Puzzle> createMetaFunctions() {
		return new CubeMetaFunctions(this);
	}
	
}
