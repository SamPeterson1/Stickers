package com.github.sampeterson1.puzzles.skewb.meta;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.moves.UniversalAlgorithmParser;
import com.github.sampeterson1.puzzles.skewb.display.SkewbDisplayPiece;
import com.github.sampeterson1.puzzles.skewb.solvers.SkewbSolver;
import com.github.sampeterson1.puzzles.skewb.util.SkewbSolverUtil;
import com.github.sampeterson1.puzzles.skewb.util.SkewbUtil;

public class SkewbMetaFunctions extends PuzzleMetaFunctions<Skewb> {

	private SkewbSolver solver;
	
	public SkewbMetaFunctions(Skewb puzzle) {
		super(puzzle);
		this.solver = new SkewbSolver(puzzle);
	}

	@Override
	public Algorithm simplify(Algorithm alg) {
		return alg;
	}

	@Override
	public Algorithm scramble(int length) {
		Algorithm scramble = new Algorithm();

		for(int i = 0; i < length; i ++) {
			Axis axis = SkewbUtil.AXES[(int) Mathf.random(0, SkewbUtil.AXES.length)];
			boolean cw = (Math.random() > 0.5);
			
			scramble.addMove(new Move(axis, cw));
		}
		
		super.getPuzzle().executeAlgorithm(scramble);
		
		super.getPuzzle().print();
		System.out.println("Hash: " + SkewbSolverUtil.hash(getPuzzle()));
		
		return scramble;
	}

	@Override
	public Algorithm solve() {
		Algorithm solution = solver.solve();
		super.getPuzzle().executeAlgorithm(solution);
		
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
		return new SkewbDisplayPiece(piece);
	}

	@Override
	public Algorithm parseAlgorithm(String alg) {
		return UniversalAlgorithmParser.parseAlgorithm(alg);
	}

}
