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

package com.github.yoshiapolis.cube.pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.yoshiapolis.cube.solvers.CenterSolver;
import com.github.yoshiapolis.cube.solvers.CornerSolver;
import com.github.yoshiapolis.cube.solvers.CrossSolver;
import com.github.yoshiapolis.cube.solvers.EdgeSolver;
import com.github.yoshiapolis.cube.solvers.F2LSolver;
import com.github.yoshiapolis.cube.solvers.OLLSolver;
import com.github.yoshiapolis.cube.solvers.PLLSolver;
import com.github.yoshiapolis.cube.util.CubeCenterUtil;
import com.github.yoshiapolis.cube.util.CubeCornerUtil;
import com.github.yoshiapolis.cube.util.CubeEdgeUtil;
import com.github.yoshiapolis.cube.util.CubeMoveUtil;
import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.puzzle.lib.Puzzle;

public class Cube extends Puzzle {

	public static Face[] faces = { Face.R, Face.U, Face.F, Face.L, Face.D, Face.B };

	private static Map<Face, Face> opposingFaces;
	private static Map<Color, Color> opposingColors;

	public static Face getFace(int index) {
		return faces[index];
	}

	public static Color getOpposingColor(Color color) {
		return opposingColors.get(color);
	}

	public static Face getOpposingFace(Face face) {
		return opposingFaces.get(face);
	}

	public static void init() {
		opposingFaces = new HashMap<Face, Face>();
		opposingFaces.put(Face.R, Face.L);
		opposingFaces.put(Face.U, Face.D);
		opposingFaces.put(Face.F, Face.B);
		opposingFaces.put(Face.L, Face.R);
		opposingFaces.put(Face.D, Face.U);
		opposingFaces.put(Face.B, Face.F);

		opposingColors = new HashMap<Color, Color>();
		opposingColors.put(Color.WHITE, Color.YELLOW);
		opposingColors.put(Color.RED, Color.ORANGE);
		opposingColors.put(Color.BLUE, Color.GREEN);
		opposingColors.put(Color.GREEN, Color.BLUE);
		opposingColors.put(Color.ORANGE, Color.RED);
		opposingColors.put(Color.YELLOW, Color.WHITE);

		CubeCenterUtil.init();
		CubeEdgeUtil.init();
		CubeCornerUtil.init();
		CubeMoveUtil.init();
	}

	public static boolean isRUF(Face face) {
		return (face == Face.R || face == Face.U || face == Face.F);
	}

	CenterSolver centerSolver;
	EdgeSolver edgeSolver;
	CrossSolver crossSolver;
	CornerSolver cornerSolver;
	F2LSolver f2lSolver;
	OLLSolver ollSolver;
	PLLSolver pllSolver;

	public Cube(int size) {
		super(size);

		ArrayList<PieceGroup> centers = new ArrayList<PieceGroup>();
		CubeCenter centerBehavior = new CubeCenter();

		for (int i = 0; i < 6; i++) {
			centers.add(new PieceGroup(centerBehavior, this, i));
		}

		ArrayList<PieceGroup> edges = new ArrayList<PieceGroup>();
		CubeEdge edgeBehavior = new CubeEdge();

		for (int i = 0; i < 12; i++) {
			edges.add(new PieceGroup(edgeBehavior, this, i));
		}

		ArrayList<PieceGroup> corners = new ArrayList<PieceGroup>();
		CubeCorner cornerBehavior = new CubeCorner();

		for (int i = 0; i < 8; i++) {
			corners.add(new PieceGroup(cornerBehavior, this, i));
		}

		super.pieces.put(PieceType.CENTER, centers);
		super.pieces.put(PieceType.EDGE, edges);
		super.pieces.put(PieceType.CORNER, corners);

		centerSolver = new CenterSolver(this);
		edgeSolver = new EdgeSolver(this);
		crossSolver = new CrossSolver(this);
		cornerSolver = new CornerSolver(this);
		f2lSolver = new F2LSolver(this);
		ollSolver = new OLLSolver(this);
		pllSolver = new PLLSolver(this);
	}

	public PieceGroup getCenter(Face face) {
		return super.getGroup(PieceType.CENTER, face.getIndex());
	}

	public Color getColor(Face face) {
		if (this.size > 2) {
			return getCenter(face).getPiece(0).getColor();
		} else {
			Face transposed = transposeFace(face);
			return transposed.getColor();
		}
	}

	public PieceGroup getCorner(int position) {
		return super.getGroup(PieceType.CORNER, position);
	}

	public PieceGroup getEdge(int position) {
		return super.getGroup(PieceType.EDGE, position);
	}

	public Algorithm solve() {
		Algorithm alg = centerSolver.solve();
		alg.append(edgeSolver.solve());
		alg.append(crossSolver.solve());
		alg.append(cornerSolver.solve());
		alg.append(f2lSolver.solve());
		alg.append(ollSolver.solve());
		alg.append(pllSolver.solve());

		return alg;
	}

	@Override
	public Face transposeFace(Face face) {
		for (Move move : super.getRotations()) {
			face = CubeMoveUtil.mapFace(face, move);
		}

		return face;
	}
}
