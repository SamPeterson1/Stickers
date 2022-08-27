package com.github.sampeterson1.square1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;

public class Square1CSSolver {
	
	private Square1 sq1;
	private List<Square1CSCase> cases;
	
	public Square1CSSolver(Square1 sq1) {
		this.sq1 = sq1;
		this.cases = new ArrayList<Square1CSCase>();
		
		BufferedReader reader = ResourceLoader.openFile("square1/CS_Algs.txt");
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
			retVal[i] = (token.charAt(i) == 'c') ? Square1CSCase.CORNER : Square1CSCase.EDGE;
		}
		
		return retVal;
	}
	
	private void parseCaseLine(String line) {
		String[] tokens = line.split(" ");
		boolean[] piecesTop = parseLayerToken(tokens[0]);
		boolean[] piecesBottom = parseLayerToken(tokens[1]);
		
		System.out.println(line);
		
		addCase(piecesTop, piecesBottom);
	}
	
	private void addCase(boolean[] cornersTop, boolean[] cornersBottom) {
		Square1CSCase base = new Square1CSCase(cornersTop, cornersBottom);
		
		cases.add(base);
		cases.add(base.getMirror());
	}

	public Algorithm solve() {	
		sq1.clearMoveLog();
		sq1.setLogMoves(true);

		boolean recognizedCase = false;
		
		/*
		for(int i = 0; i < 12; i ++) {
			Piece piece = sq1.getPiece(i);
			if(piece != null) System.out.print((piece.getType() == PieceType.CORNER) ? "c" : "e");
		}
		System.out.print(" ");
		for(int i = 12; i < 24; i ++) {
			Piece piece = sq1.getPiece(i);
			if(piece != null) System.out.print((piece.getType() == PieceType.CORNER) ? "c" : "e");
		}
		System.out.println();
		*/
		
		for(int i = 0; i < 7; i ++) {
			for(Square1CSCase cubeShapeCase : cases) {
				if(cubeShapeCase.solve(sq1)) {
					recognizedCase = true;
					break;
				}
			}
			
			if(!recognizedCase) break;
		}
		
		if(!recognizedCase) { 
			sq1.executeAlgorithm(Square1Util.parseAlgorithm("/(6,6)/"));
			for(int i = 0; i < 7; i ++) {
				for(Square1CSCase cubeShapeCase : cases) {
					if(cubeShapeCase.solve(sq1)) {
						break;
					}
				}
			}
		}

		if(!recognizedCase) {
			int i = 0;
			while(!Square1Util.topSquare(sq1)) sq1.makeMove(new Move(Axis.SU, true));
			while(!Square1Util.bottomSquare(sq1)) sq1.makeMove(new Move(Axis.SD, true));
		}
		
		return sq1.simplify(sq1.getMoveLog());
	}
	
}
