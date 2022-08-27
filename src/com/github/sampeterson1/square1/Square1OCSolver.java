package com.github.sampeterson1.square1;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;

public class Square1OCSolver {
	
	private static final Color W = Color.WHITE;
	private static final Color Y = Color.YELLOW;
	
	private Square1 sq1;
	private List<Square1OCCase> cases;
	
	public Square1OCSolver(Square1 sq1) {
		this.sq1 = sq1;
		this.cases = new ArrayList<Square1OCCase>();
		
		addCase("(1,0)/(-1,0)", Y, Y, W, W, W, W, Y, Y);
		addCase("(1,0)/(-3,0)/(-1,0)", W, W, W, Y, W, Y, Y, Y);
		addCase("(1,0)/(-3,-3)/(-1,0)", W, Y, W, Y, W, Y, W, Y);
		addCase("(1,0)/(0,-3)/(0,-3)/(-1,0)", Y, Y, W, W, Y, W, Y, W);
		addCase("(1,0)/(-3,0)/(-3,0)/(-1,0)", Y, W, Y, W, W, W, Y, Y);
		addCase("(1,0)/(-3,0)/(-3,0)/(-1,0)", Y, W, Y, W, W, W, Y, Y);
		addCase("/(6,6)/(-1,1)", Y, Y, Y, Y, W, W, W, W);
		addCase("", W, W, W, W, Y, Y, Y, Y);
	}
	
	private void testCases() {
		for(Square1OCCase ocCase : cases) {
			int uMoves = (int) Mathf.random(0, 4) * 3;
			int dMoves = (int) Mathf.random(0, 4) * 3;
			
			sq1.executeAlgorithm(ocCase.getSolution().getInverse());
			for(int i = 0; i < uMoves; i ++) sq1.makeMove(new Move(Axis.SU, true));
			for(int i = 0; i < dMoves; i ++) sq1.makeMove(new Move(Axis.SD, true));
			if(!ocCase.solve(sq1)) System.out.println("NOOO");
		}
	}
	
	private void addCase(String solution, Color... cornerColors) {
		cases.add(new Square1OCCase(solution, cornerColors));
	}
	
	public Algorithm solve() {
		sq1.clearMoveLog();
		sq1.setLogMoves(true);
		
		for(Square1OCCase ocCase : cases) {
			if(ocCase.solve(sq1)) {
				System.out.println("Orient corners solved");
				break;
			}
		}
		
		return sq1.getMoveLog();
	}
	
}
