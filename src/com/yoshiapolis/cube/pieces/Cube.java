/*
    PrimePuzzle Twisty Puzzle Simulator
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

package com.yoshiapolis.cube.pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.yoshiapolis.cube.solvers.CenterSolver;
import com.yoshiapolis.cube.solvers.CornerSolver;
import com.yoshiapolis.cube.solvers.CrossSolver;
import com.yoshiapolis.cube.solvers.EdgeSolver;
import com.yoshiapolis.cube.solvers.F2LSolver;
import com.yoshiapolis.cube.solvers.OLLSolver;
import com.yoshiapolis.cube.solvers.PLLSolver;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PieceType;
import com.yoshiapolis.puzzle.Puzzle;
import com.yoshiapolis.puzzle.PuzzlePieceGroup;

public class Cube extends Puzzle {
	 
	public static Face[] faces = {Face.R, Face.U, Face.F, Face.L, Face.D, Face.B};
	 
	private static Map<Face, Face> opposingFaces;
	private static Map<Color, Color> opposingColors;

	CenterSolver centerSolver;
	EdgeSolver edgeSolver;
	CrossSolver crossSolver;
	CornerSolver cornerSolver;
	F2LSolver f2lSolver;
	OLLSolver ollSolver;
	PLLSolver pllSolver;
	 
	public Cube(int size) {
		super(size);
		
		ArrayList<PuzzlePieceGroup> centers = new ArrayList<PuzzlePieceGroup>();
		CubeCenterBehavior centerBehavior = new CubeCenterBehavior();
		
		for(int i = 0; i < 6; i ++) {
			centers.add(new PuzzlePieceGroup(centerBehavior, this, i));
		}
		 
		ArrayList<PuzzlePieceGroup> edges = new ArrayList<PuzzlePieceGroup>();
		CubeEdgeBehavior edgeBehavior = new CubeEdgeBehavior();
		
		for(int i = 0; i < 12; i ++) {
			edges.add(new PuzzlePieceGroup(edgeBehavior, this, i));
		}
		 
		ArrayList<PuzzlePieceGroup> corners = new ArrayList<PuzzlePieceGroup>();
		CubeCornerBehavior cornerBehavior = new CubeCornerBehavior();
		
		for(int i = 0; i < 8; i ++) {
			corners.add(new PuzzlePieceGroup(cornerBehavior, this, i));
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
	
	@Override
	public Face transposeFace(Face face) {
		for(Move move : super.getRotations()) {
			face = CubeMoveUtil.mapFace(face, move);
		}
		
		return face;
	}
	
	public static boolean isRUF(Face face) {
		return (face == Face.R || face == Face.U || face == Face.F);
	}
	
	public static Face getFace(int index) {
		return faces[index];
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

	public PuzzlePieceGroup getCorner(int position) {
		return super.getGroup(PieceType.CORNER, position);
	}
	
	public PuzzlePieceGroup getEdge(int position) {
		return super.getGroup(PieceType.EDGE, position);
	}
	
	public PuzzlePieceGroup getCenter(Face face) {
		return super.getGroup(PieceType.CENTER, face.getIndex());
	}
	 
	public static Color getOpposingColor(Color color) {
		return opposingColors.get(color);
	}
	 
	public static Face getOpposingFace(Face face) {
		return opposingFaces.get(face);
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
	
	public Color getColor(Face face) {
		if(this.size > 2) {
			return getCenter(face).getPiece(0).getColor(); 
		} else {
			Face transposed = transposeFace(face);
			return transposed.getColor();
		}
	}
}
