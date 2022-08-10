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

package com.github.sampeterson1.cube.pieces;

import com.github.sampeterson1.cube.solvers.MasterCubeSolver;
import com.github.sampeterson1.cube.util.CubeAlgorithmUtil;
import com.github.sampeterson1.cube.util.CubeMoveUtil;
import com.github.sampeterson1.cube.util.CubeUtil;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;

//An implementation of Puzzle that represents the pieces of an n by n Rubk's Cube
public class Cube extends Puzzle {

	private static final int NUM_CENTERS = 6;
	private static final int NUM_EDGES = 12;
	private static final int NUM_CORNERS = 8;
	
	private MasterCubeSolver solver;
	
	public Cube(int size) {
		super(size);

		super.createPieces(new CubeCenterBehavior(), NUM_CENTERS);
		super.createPieces(new CubeEdgeBehavior(), NUM_EDGES);
		super.createPieces(new CubeCornerBehavior(), NUM_CORNERS);
		
		this.solver = new MasterCubeSolver(this);
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
		
		return getCenter(face).getPiece().getColor();
	}

	@Override
	public Algorithm solve() {
		return solver.solve();
	}

	@Override
	public Axis transposeAxis(Axis face) {
		for (Move move : super.getRotations()) {
			face = CubeMoveUtil.mapFace(face, move);
		}

		return face;
	}

	@Override
	public Algorithm simplify(Algorithm alg) {
		return CubeAlgorithmUtil.simplify(alg);
	}

	@Override
	public Algorithm scramble(int length) {
		Algorithm scramble = CubeAlgorithmUtil.generateScramble(length, super.getSize());
		executeAlgorithm(scramble);
		
		return scramble;
	}
}
