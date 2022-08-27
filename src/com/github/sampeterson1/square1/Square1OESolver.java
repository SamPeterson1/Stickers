package com.github.sampeterson1.square1;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;

public class Square1OESolver {
	
	private static final Color W = Color.WHITE;
	private static final Color Y = Color.YELLOW;
	
	private Square1 sq1;
	private List<Square1OECase> cases;
	
	public Square1OESolver(Square1 sq1) {
		this.sq1 = sq1;
		this.cases = new ArrayList<Square1OECase>();
		
		addCase("(1,0)/(3,0)/(3,0)/(-1,-1)/(-2,1)/(-3,0)/(-1,0)", W, Y, W, W, Y, Y, W, Y);
		addCase("(0,-1)/(1,1)/(-1,0)", Y, W, Y, W, W, Y, W, Y);
		addCase("(1,0)/(-4,-1)/(1,1)/(3,0)/(-1,0)", W, W, Y, Y, Y, W, W, Y);
		addCase("(1,0)/(3,0)/(3,0)/(-1,-1)/(-2,1)/(-4,-1)/(0,1)", W, W, Y, Y, W, Y, W, Y);
		addCase("(0,-1)/(3,0)/(3,0)/(1,1)/(-3,0)/(-3,0)/(-1,0)", Y, Y, Y, W, W, W, W, Y);
		addCase("(1,0)/(-1,-1)/(3,3)/(1,1)/(-1,0)", Y, Y, Y, Y, W, W, W, W);
		addCase("(1,0)/(-3,0)/(3,0)/(-1,-1)/(-3,0)/(3,0)/(0,1)", Y, W, Y, W, Y, W, W, Y);
	}
	
	private void testCases() {
		for(Square1OECase oeCase : cases) {
			int uMoves = (int) Mathf.random(0, 4) * 3;
			int dMoves = (int) Mathf.random(0, 4) * 3;
			
			sq1.executeAlgorithm(oeCase.getSolution().getInverse());
			for(int i = 0; i < uMoves; i ++) sq1.makeMove(new Move(Axis.SU, true));
			for(int i = 0; i < dMoves; i ++) sq1.makeMove(new Move(Axis.SD, true));
			if(!oeCase.solve(sq1)) System.out.println("NOOO");
		}
	}
	
	private void addCase(String solution, Color... cornerColors) {
		cases.add(new Square1OECase(solution, cornerColors));
	}
	
	public Algorithm solve() {
		sq1.clearMoveLog();
		sq1.setLogMoves(true);
		
		for(Square1OECase oeCase : cases) {
			if(oeCase.solve(sq1)) {
				break;
			}
		}
		
		return sq1.getMoveLog();
	}
	
}
