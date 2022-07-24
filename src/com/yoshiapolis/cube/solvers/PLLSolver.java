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

package com.yoshiapolis.cube.solvers;

import java.util.ArrayList;

import com.yoshiapolis.cube.pieces.Cube;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;

public class PLLSolver {
	
	private Cube cube;
	private ArrayList<PLLCase> cases;
	
	private static final int B = 0;
	private static final int R = 1;
	private static final int G = 2;
	private static final int O = 3;
	
	public PLLSolver(Cube cube) {
		this.cube = cube;
		cases = new ArrayList<PLLCase>();
		
		addCase("x L2 D2 L' U' L D2 L' U L' x'", new int[] {O, B, R, R, B, G, G, O});
		addCase("x' L2 D2 L U L' D2 L U' L x", new int[] {R, B, O, R, G, G, B, O});
		addCase("R' U' F' R U R' U' R' F R2 U' R' U' R U R' U R", new int[] {B, G, G, R, R, B, O, O});
		addCase("R2 U R' U R' U' R U' R2 U' D R' U R D'", new int[] {B, R, G, O, R, B, O, G});
		addCase("R' U' R U D' R2 U R' U R U' R U' R2 D", new int[] {B, G, G, B, R, O, O, R});
		addCase("R2 U' R U' R U R' U R2 U D' R U' R' D", new int[] {B, G, G, O, R, R, O, B});
		addCase("R U R' U' D R2 U' R U' R' U R' U R2 D'", new int[] {B, O, G, G, R, B, O, R});
		addCase("x R2 F R F' R U2 r' U r U2 x'", new int[] {B, B, G, G, R, R, O, O});
		addCase("R U R' F' R U R' U' R' F R2 U' R'", new int[] {O, B, R, O, B, R, G, G});
		addCase("R U' R' U' R U R D R' U' R D' R' U2 R'", new int[] {O, O, R, B, B, G, G, R});
		addCase("R2 F R U R U' R' F' R U2 R' U2 R", new int[] {R, B, O, G, G, O, B, R});
		addCase("R U R' U' R' F R2 U' R' U' R U R' F'", new int[] {B, B, G, O, R, G, O, R});
		addCase("x' L' U L D' L' U' L D L' U' L D' L' U L D x", new int[] {O, B, G, R, R, G, B, O});
		addCase("z U R' D R2 U' R D' U R' D R2 U' R D' z' U'", new int[] {G, B, R, O, B, G, O, R});
		addCase("R' U R U' R' F' U' F R U R' F R' F' R U' R", new int[] {B, B, O, O, G, G, R, R});
		addCase("R' U R' U' y R' F' R2 U' R' U R' F R F y'", new int[] {B, B, O, G, G, R, R, O});
		addCase("F R U' R' U' R U R' F' R U R' U' R' F R F'", new int[] {B, B, O, R, G, O, R, G});
		addCase("M2 U M2 U2 M2 U M2", new int[] {B, G, R, O, G, B, O, R});
		addCase("R U' R U R U R U' R' U' R2", new int[] {B, R, R, O, G, G, O, B});
		addCase("R2 U R U R' U' R' U' R' U R'", new int[] {B, O, R, B, G, G, O, R});
		addCase("M' U M2 U M2 U M' U2 M2", new int[] {O, G, B, R, R, B, G, O});
		addCase("", new int[] {B, B, R, R, G, G, O, O});
	}
	
	private void addCase(String alg, int[] faces) {
		cases.add(new PLLCase(alg, faces));
	}
	
	private void PLLParity() {
		cube.makeMove(new Move(Face.R, false));
		cube.makeMove(new Move(Face.U, true));
		cube.makeMove(new Move(Face.R, true));
		cube.makeMove(new Move(Face.U, false));
		
		int cubeSize = cube.getSize();
		
		for(int i = 1; i < cubeSize/2; i ++) {
			cube.makeMove(new Move(Face.R, i, true));
			cube.makeMove(new Move(Face.R, i, true));
		}
		
		cube.makeMove(new Move(Face.U, true));
		cube.makeMove(new Move(Face.U, true));
		
		for(int i = 1; i < cubeSize/2; i ++) {
			cube.makeMove(new Move(Face.R, i, true));
			cube.makeMove(new Move(Face.R, i, true));
		}
		
		for(int i = 0; i < cubeSize/2; i ++) {
			cube.makeMove(new Move(Face.U, i, true));
			cube.makeMove(new Move(Face.U, i, true));
		}
		
		for(int i = 1; i < cubeSize/2; i ++) {
			cube.makeMove(new Move(Face.R, i, true));
			cube.makeMove(new Move(Face.R, i, true));
		}
		
		for(int i = 1; i < cubeSize/2; i ++) {
			cube.makeMove(new Move(Face.U, i, true));
			cube.makeMove(new Move(Face.U, i, true));
		}
		
		cube.makeMove(new Move(Face.U, true));
		cube.makeMove(new Move(Face.R, false));
		cube.makeMove(new Move(Face.U, false));
		cube.makeMove(new Move(Face.R, true));
		
	}
	
	private Algorithm getPLLSolution() {
		for(int i = 0; i < 4; i ++) {
			for(int j = 0; j < 4; j ++) {
				for(PLLCase c : cases) {
					if(c.recognize(cube, j)) {
						return c.getSolution();
					}
				}
			}
			cube.makeRotation(Face.U, true);
		}
		
		return null;
	}
	
	private Algorithm solveParity() {
		PLLParity();
		Algorithm alg = cube.getMoveLog();
		cube.popRotations();
		cube.setLogMoves(false);
		alg.append(solve());
		alg.simplify();
		return alg;
	}
	
	public Algorithm solve() {
		cube.setLogMoves(true);
		cube.clearMoveLog();
		cube.pushRotations();
		
		Algorithm solution = getPLLSolution();
		if(solution != null) {
			cube.executeAlgorithm(solution);
		} else {
			return solveParity();
		}
		
		Color fColor = cube.getColor(Face.F);
		while(cube.getCorner(0).getPiece().getColor(2) != fColor) {
			cube.makeMove(new Move(Face.U, true));
		}
		
		cube.popRotations();
		cube.setLogMoves(false);
		
		Algorithm alg = cube.getMoveLog();
		alg.simplify();
		
		return alg;
	}
	
}
