package com.github.sampeterson1.puzzles.square1.meta;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.square1.display.Square1DisplayPiece;
import com.github.sampeterson1.puzzles.square1.solvers.Square1CSSolver;
import com.github.sampeterson1.puzzles.square1.solvers.Square1OLSolver;
import com.github.sampeterson1.puzzles.square1.solvers.Square1PLSolver;
import com.github.sampeterson1.puzzles.square1.util.Square1Util;

public class Square1MetaFunctions extends PuzzleMetaFunctions<Square1> {

	private Square1CSSolver csSolver;
	private Square1OLSolver olSolver;
	private Square1PLSolver plSolver;

	public Square1MetaFunctions(Square1 puzzle) {
		super(puzzle);
		
		this.csSolver = new Square1CSSolver(puzzle);
		this.olSolver = new Square1OLSolver(puzzle);
		this.plSolver = new Square1PLSolver(puzzle);
	}
	
	@Override
	public Algorithm parseAlgorithm(String algStr) {
		return Square1Util.parseAlgorithm(algStr);
	}

	@Override
	public Algorithm simplify(Algorithm alg) {
		return Square1Util.simplify(alg);
	}

	@Override
	public Algorithm scramble(int length) {
		Square1 sq1 = super.getPuzzle();
		
		sq1.setLogMoves(true);
		sq1.clearMoveLog();
		
		for(int i = 0; i < length; i ++) {
			int top = (int) Mathf.random(0, 7);
			int bottom = (int) Mathf.random(0, 7);
			boolean topCW = (Mathf.random(0, 1) > 0.5f);
			boolean bottomCW = (Mathf.random(0, 1) > 0.5f);
			
			for(int j = 0; j < top; j ++) sq1.makeMove(new Move(Axis.SU, topCW));
			for(int j = 0; j < bottom; j ++) sq1.makeMove(new Move(Axis.SD, bottomCW));
			while(Square1Util.topLocked(sq1)) sq1.makeMove(new Move(Axis.SU, true));
			while(Square1Util.bottomLocked(sq1)) sq1.makeMove(new Move(Axis.SD, true));
			sq1.makeMove(new Move(Axis.S1, true));
		}
		
		return sq1.getMoveLog();
	}

	@Override
	public Algorithm solve() {
		Algorithm solution = csSolver.solve();
		solution.append(olSolver.solve());
		solution.append(plSolver.solve());
		
		return solution;
	}
	
	@Override
	public ColorPalette createDefaultColorPalette() {
		ColorPalette palette = new ColorPalette();
		
		palette.putColor(Color.BORDER);
		palette.putColor(Color.WHITE);
		palette.putColor(Color.YELLOW);
		palette.putColor(Color.ORANGE);
		palette.putColor(Color.BLUE);
		palette.putColor(Color.GREEN);
		palette.putColor(Color.RED);
		
		return palette;
	}

	@Override
	public DisplayPiece createDisplayPiece(Piece piece) {
		return new Square1DisplayPiece(piece);
	}
	
}
