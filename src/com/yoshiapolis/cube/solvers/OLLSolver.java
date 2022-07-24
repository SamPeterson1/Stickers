package com.yoshiapolis.cube.solvers;

import java.util.ArrayList;

import com.yoshiapolis.cube.pieces.Cube;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;

public class OLLSolver {
	
	private Cube cube;
	private ArrayList<OLLCase> cases;
	
	public OLLSolver(Cube cube) {
		this.cube = cube;
		cases = new ArrayList<OLLCase>();
		
		addCase("R U2 R2 F R F' U2 R' F R F'", 			new int[] {0, 1, 2, 1, 0, 1, 2, 1});
		addCase("r U r' U2 r U2 R' U2 R U' r'", 		new int[] {0, 1, 2, 1, 2, 1, 0, 1});
		addCase("r' R2 U R' U r U2 r' U M'", 			new int[] {1, 1, 0, 1, 0, 1, 0, 1});
		addCase("M U' r U2 r' U' R U' R' M'", 			new int[] {2, 1, 1, 1, 2, 1, 2, 1});
		addCase("l' U2 L U L' U l", 					new int[] {0, 1, 0, 1, 0, 0, 1, 0});
		addCase("r U2 R' U' R U' r'", 					new int[] {2, 1, 2, 0, 1, 0, 2, 1});
		addCase("r U R' U R U2 r'", 					new int[] {1, 1, 0, 1, 0, 0, 0, 0});
		addCase("l' U' L U' L' U2 l", 					new int[] {2, 1, 1, 0, 2, 0, 2, 1});
		addCase("R U R' U' R' F R2 U R' U' F'", 		new int[] {2, 1, 1, 1, 2, 0, 2, 0});
		addCase("R U R' U R' F R F' R U2 R'", 			new int[] {0, 0, 0, 1, 1, 1, 0, 0});
		addCase("r U R' U R' F R F' R U2 r'", 			new int[] {0, 1, 0, 1, 1, 0, 0, 0});
		addCase("M' R' U' R U' R' U2 R U' R r'",		new int[] {2, 1, 2, 0, 2, 0, 1, 1});
		addCase("F U R U' R2 F' R U R U' R'", 			new int[] {1, 1, 0, 0, 0, 1, 0, 0});
		addCase("R' F R U R' F' R F U' F'", 			new int[] {2, 1, 1, 0, 2, 1, 2, 0});
		addCase("l' U' l L' U' L U l' U l", 			new int[] {0, 1, 0, 0, 0, 1, 1, 0});
		addCase("r U r' R U R' U' r U' r'", 			new int[] {2, 1, 2, 0, 1, 1, 2, 0});
		addCase("F R' F' R2 r' U R U' R' U' M'",		new int[] {2, 1, 1, 1, 0, 1, 1, 1});
		addCase("r U R' U R U2 r2 U' R U' R' U2 r", 	new int[] {2, 1, 0, 1, 1, 1, 1, 1});
		addCase("r' R U R U R' U' M' R' F R F'", 		new int[] {0, 1, 2, 1, 1, 1, 1, 1});
		addCase("r U R' U' M2 U R U' R' U' M'", 		new int[] {1, 1, 1, 1, 1, 1, 1, 1});
		addCase("R U2 R' U' R U R' U' R U' R'", 		new int[] {2, 0, 0, 0, 2, 0, 0, 0});
		addCase("R U2 R2 U' R2 U' R2 U2 R", 			new int[] {0, 0, 0, 0, 2, 0, 2, 0});
		addCase("R2 D' R U2 R' D R U2 R", 				new int[] {1, 0, 1, 0, 2, 0, 0, 0});
		addCase("r U R' U' r' F R F'", 					new int[] {2, 0, 1, 0, 1, 0, 0, 0});
		addCase("F' r U R' U' r' F R", 					new int[] {1, 0, 0, 0, 1, 0, 2, 0});
		addCase("R U2 R' U' R U' R'", 					new int[] {2, 0, 2, 0, 1, 0, 2, 0});
		addCase("R U R' U R U2 R'", 					new int[] {1, 0, 0, 0, 0, 0, 0, 0});
		addCase("r U R' U' r' R U R U' R'", 			new int[] {1, 1, 1, 1, 1, 0, 1, 0});
		addCase("R U R' U' R U' R' F' U' F R U R'", 	new int[] {2, 1, 1, 1, 1, 0, 0, 0});
		addCase("F R' F R2 U' R' U' R U R' F2", 		new int[] {1, 1, 1, 1, 0, 0, 2, 0});
		addCase("R' U' F U R U' R' F' R", 				new int[] {2, 1, 1, 0, 1, 0, 0, 1});
		addCase("L U F' U' L' U L F L'", 				new int[] {1, 1, 0, 1, 2, 0, 1, 0});
		addCase("R U R' U' R' F R F'", 					new int[] {2, 1, 1, 0, 1, 1, 0, 0});
		addCase("R U R2 U' R' F R U R U' F'", 			new int[] {1, 1, 1, 0, 0, 1, 2, 0});
		addCase("R U2 R2 F R F' R U2 R'", 				new int[] {2, 0, 1, 0, 0, 1, 1, 1});
		addCase("L' U' L U' L' U L U L F' L' F", 		new int[] {0, 1, 1, 0, 2, 0, 1, 1});
		addCase("F R' F' R U R U' R'", 					new int[] {2, 1, 1, 1, 0, 0, 1, 0});
		addCase("R U R' U R U' R' U' R' F R F'", 		new int[] {1, 1, 2, 1, 1, 0, 0, 0});
		addCase("L F' L' U' L U F U' L'", 				new int[] {1, 1, 2, 0, 1, 1, 0, 0});
		addCase("R' F R U R' U' F' U R", 				new int[] {0, 1, 1, 0, 2, 1, 1, 0});
		addCase("R U R' U R U2 R' F R U R' U' F'", 		new int[] {1, 1, 1, 1, 2, 0, 0, 0});
		addCase("R' U' R U' R' U2 R F R U R' U' F'",	new int[] {2, 0, 0, 1, 1, 1, 1, 0});
		addCase("F' U' L' U L F", 						new int[] {0, 1, 1, 0, 1, 0, 2, 1});
		addCase("F U R U' R' F'", 						new int[] {1, 1, 2, 1, 0, 0, 1, 0});
		addCase("F R U R' U' F'", 						new int[] {0, 1, 1, 0, 1, 1, 2, 0});
		addCase("R' U' R' F R F' U R", 					new int[] {1, 0, 2, 1, 0, 0, 1, 1});
		addCase("R' U' R' F R F' R' F R F' U R", 		new int[] {2, 1, 2, 0, 0, 0, 0, 1});
		addCase("F R U R' U' R U R' U' F'", 			new int[] {0, 1, 0, 1, 2, 0, 2, 0});
		addCase("r U' r2 U r2 U r2 U' r", 				new int[] {0, 1, 0, 0, 2, 0, 2, 1});
		addCase("r' U r2 U' r2 U' r2 U r'", 			new int[] {0, 0, 0, 0, 2, 1, 2, 1});
		addCase("F U R U' R' U R U' R' F'", 			new int[] {2, 1, 2, 0, 0, 1, 0, 0});
		addCase("R U R' U R U' B U' B' R'", 			new int[] {2, 0, 2, 1, 0, 0, 0, 1});
		addCase("l' U2 L U L' U' L U L' U l", 			new int[] {2, 1, 0, 0, 2, 0, 0, 1});
		addCase("r U2 R' U' R U R' U' R U' r'",			new int[] {2, 1, 0, 1, 2, 0, 0, 0});
		addCase("R' F R U R U' R2 F' R2 U' R' U R U R'",new int[] {2, 1, 0, 0, 2, 1, 0, 0});
		addCase("r' U' r U' R' U R U' R' U R r' U r", 	new int[] {0, 1, 2, 0, 0, 1, 2, 0});
		addCase("R U R' U' M' U R U' r'", 				new int[] {1, 1, 1, 0, 1, 1, 1, 0});
		addCase("", 									new int[] {1, 0, 1, 0, 1, 0, 1, 0});
	}
	
	private void addCase(String alg, int[] position) {
		cases.add(new OLLCase(alg, position));
	}
	
	private void OLLParity() {
		int cubeSize = cube.getSize();
		for(int layer = 1; layer < cubeSize/2; layer ++) {
			cube.makeMove(new Move(Face.R, layer, true));
			cube.makeMove(new Move(Face.R, layer, true));
		}
		
		cube.makeMove(new Move(Face.B, 0, true));
		cube.makeMove(new Move(Face.B, 0, true));
		
		cube.makeMove(new Move(Face.U, 0, true));
		cube.makeMove(new Move(Face.U, 0, true));
		
		for(int layer = 1; layer < cubeSize/2; layer ++) {
			cube.makeMove(new Move(Face.L, layer, true));
		}
		
		cube.makeMove(new Move(Face.U, 0, true));
		cube.makeMove(new Move(Face.U, 0, true));
		
		for(int layer = 1; layer < cubeSize/2; layer ++) {
			cube.makeMove(new Move(Face.R, layer, false));
		}
		
		cube.makeMove(new Move(Face.U, 0, true));
		cube.makeMove(new Move(Face.U, 0, true));
		
		for(int layer = 1; layer < cubeSize/2; layer ++) {
			cube.makeMove(new Move(Face.R, layer, true));
		}
		
		cube.makeMove(new Move(Face.U, 0, true));
		cube.makeMove(new Move(Face.U, 0, true));
		
		cube.makeMove(new Move(Face.F, 0, true));
		cube.makeMove(new Move(Face.F, 0, true));
		
		for(int layer = 1; layer < cubeSize/2; layer ++) {
			cube.makeMove(new Move(Face.R, layer, true));
		}
		
		cube.makeMove(new Move(Face.F, 0, true));
		cube.makeMove(new Move(Face.F, 0, true));
		
		for(int layer = 1; layer < cubeSize/2; layer ++) {
			cube.makeMove(new Move(Face.L, layer, false));
		}
		
		cube.makeMove(new Move(Face.B, 0, true));
		cube.makeMove(new Move(Face.B, 0, true));
		
		for(int layer = 1; layer < cubeSize/2; layer ++) {
			cube.makeMove(new Move(Face.R, layer, true));
			cube.makeMove(new Move(Face.R, layer, true));
		}
	}
	
	private Algorithm getOLLSolution() {
		Algorithm solution = null;
		for(int i = 0; i < 4; i ++) {
			for(OLLCase c : cases) {
				if(c.recognize(cube)) {
					return c.getSolution();
				}
			}
			cube.makeRotation(Face.U, true);
		}
		
		return solution;
	}
	
	private Algorithm solveParity() {
		OLLParity();
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
		
		Algorithm solution = getOLLSolution();
		if(solution != null) {
			cube.executeAlgorithm(solution);
		} else {
			return solveParity();
		}
		
		cube.popRotations();
		cube.setLogMoves(false);
		
		Algorithm alg = cube.getMoveLog();
		alg.simplify();
		
		return alg;
	}
}
