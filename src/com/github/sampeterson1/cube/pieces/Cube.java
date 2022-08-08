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

package com.github.sampeterson1.cube.pieces;

import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.cube.solvers.MasterCubeSolver;
import com.github.sampeterson1.cube.util.CubeAlgorithmUtil;
import com.github.sampeterson1.cube.util.CubeCornerUtil;
import com.github.sampeterson1.cube.util.CubeEdgeUtil;
import com.github.sampeterson1.cube.util.CubeMoveUtil;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;

public class Cube extends Puzzle {

	public static Axis[] faces = { Axis.R, Axis.U, Axis.F, Axis.L, Axis.D, Axis.B };

	private static Map<Axis, Axis> opposingFaces = initOpposingFaces();
	private static Map<Axis, Integer> facePositions = initFacePositions();
	private static Map<Axis, Color> faceColors = initFaceColors();
	private static Map<Color, Color> opposingColors = initOpposingColors();
	
	private static final int NUM_CENTERS = 6;
	private static final int NUM_EDGES = 12;
	private static final int NUM_CORNERS = 8;
	
	private static Map<Axis, Axis> initOpposingFaces() {
		Map<Axis, Axis> faces = new EnumMap<Axis, Axis>(Axis.class);
		faces.put(Axis.R, Axis.L);
		faces.put(Axis.U, Axis.D);
		faces.put(Axis.F, Axis.B);
		faces.put(Axis.L, Axis.R);
		faces.put(Axis.D, Axis.U);
		faces.put(Axis.B, Axis.F);
		
		return faces;
	}
	
	private static Map<Axis, Integer> initFacePositions() {
		Map<Axis, Integer> positions = new EnumMap<Axis, Integer>(Axis.class);
		positions.put(Axis.R, 0);
		positions.put(Axis.U, 1);
		positions.put(Axis.F, 2);
		positions.put(Axis.L, 3);
		positions.put(Axis.D, 4);
		positions.put(Axis.B, 5);
		
		return positions;
	}
	
	private static Map<Axis, Color> initFaceColors() {
		Map<Axis, Color> colors = new EnumMap<Axis, Color>(Axis.class);
		colors.put(Axis.R, Color.RED);
		colors.put(Axis.U, Color.WHITE);
		colors.put(Axis.F, Color.GREEN);
		colors.put(Axis.L, Color.ORANGE);
		colors.put(Axis.D, Color.YELLOW);
		colors.put(Axis.B, Color.BLUE);
		
		return colors;
	}
	
	private static Map<Color, Color> initOpposingColors() {
		Map<Color, Color> colors = new EnumMap<Color, Color>(Color.class);
		colors = new EnumMap<Color, Color>(Color.class);
		colors.put(Color.WHITE, Color.YELLOW);
		colors.put(Color.RED, Color.ORANGE);
		colors.put(Color.BLUE, Color.GREEN);
		colors.put(Color.GREEN, Color.BLUE);
		colors.put(Color.ORANGE, Color.RED);
		colors.put(Color.YELLOW, Color.WHITE);
		
		return colors;
	}
	
	public static Axis getFace(int index) {
		return faces[index];
	}

	public static Axis getOpposingFace(Axis face) {
		return opposingFaces.get(face);
	}

	public static Color getFaceColor(Axis face) {
		return faceColors.get(face);
	}
	
	public static int getFacePosition(Axis face) {
		return facePositions.get(face);
	}
	
	public static void init() {
		CubeEdgeUtil.init();
		CubeCornerUtil.init();
		CubeMoveUtil.init();
	}

	public static boolean isRUF(Axis face) {
		return (face == Axis.R || face == Axis.U || face == Axis.F);
	}

	private MasterCubeSolver solver;
	
	public Cube(int size) {
		super(size);

		super.createPieces(new CubeCenterBehavior(), NUM_CENTERS);
		super.createPieces(new CubeEdgeBehavior(), NUM_EDGES);
		super.createPieces(new CubeCornerBehavior(), NUM_CORNERS);
		
		this.solver = new MasterCubeSolver(this);
	}

	public PieceGroup getCenter(Axis face) {
		return super.getGroup(PieceType.CENTER, facePositions.get(face));
	}

	public Color getCenterColor(Axis face) {
		if(super.getSize() == 2) {
			face = transposeAxis(face);
			return getFaceColor(face);
		}
		
		int centerSize = (super.getSize() - 2) * (super.getSize() - 2);
		int centerIndex = centerSize / 2;
		return getCenter(face).getPiece(centerIndex).getColor();
	}

	public PieceGroup getCorner(int position) {
		return super.getGroup(PieceType.CORNER, position);
	}

	public PieceGroup getEdge(int position) {
		return super.getGroup(PieceType.EDGE, position);
	}

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
