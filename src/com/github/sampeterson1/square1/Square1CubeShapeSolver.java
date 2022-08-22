package com.github.sampeterson1.square1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.display.PuzzleMaster;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;

public class Square1CubeShapeSolver {
	
	private Square1 sq1;
	private List<Square1CubeShapeCase> cases;
	
	public Square1CubeShapeSolver(Square1 sq1) {
		this.sq1 = sq1;
		this.cases = new ArrayList<Square1CubeShapeCase>();
		
		BufferedReader reader = ResourceLoader.openFile("square1/cubeShapeAlgorithms.txt");
		String line;
		try {
			while((line = reader.readLine()) != null)
				parseCaseLine(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean[] parseLayerToken(String token) {
		boolean[] retVal = new boolean[token.length()];
		for(int i = 0; i < token.length(); i ++) {
			retVal[i] = (token.charAt(i) == 'c') ? Square1CubeShapeCase.CORNER : Square1CubeShapeCase.EDGE;
		}
		
		return retVal;
	}
	
	private void parseCaseLine(String line) {
		String[] tokens = line.split(" ");
		boolean[] piecesTop = parseLayerToken(tokens[0]);
		boolean[] piecesBottom = parseLayerToken(tokens[1]);
		String solution = tokens[2];
		
		addCase(solution, piecesTop, piecesBottom);
	}
	
	private void addCase(String solution, boolean[] cornersTop, boolean[] cornersBottom) {
		Square1CubeShapeCase base = new Square1CubeShapeCase(solution, cornersTop, cornersBottom);
		
		cases.add(base);
		cases.add(base.getFlip());
	}
	
	private void testCases() {
		for(Square1CubeShapeCase cubeShapeCase : cases) {
			int uMoves = (int) Mathf.random(0, 12);
			int dMoves = (int) Mathf.random(0, 12);
			
			sq1.executeAlgorithm(cubeShapeCase.getSolution().getInverse());
			for(int i = 0; i < uMoves; i ++) sq1.makeMove(new Move(Axis.SU, true));
			for(int i = 0; i < dMoves; i ++) sq1.makeMove(new Move(Axis.SD, true));
			if(!cubeShapeCase.solve(sq1)) System.out.println("NOOO");
		}
	}
	
	public Algorithm solve() {
		sq1.clearMoveLog();
		sq1.setLogMoves(true);
		//testCases();
		
		for(Square1CubeShapeCase cubeShapeCase : cases) {
			if(cubeShapeCase.solve(sq1)) {
				System.out.println("solved!");
				cubeShapeCase.print();
				break;
			}
		}
		
		
		//for(int i = 12; i < 24; i ++) if(sq1.getPiece(i) != null) System.out.println(sq1.getPiece(i).getType());

		return sq1.getMoveLog();
	}
	
}
