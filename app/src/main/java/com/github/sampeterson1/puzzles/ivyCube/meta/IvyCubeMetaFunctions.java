package com.github.sampeterson1.puzzles.ivyCube.meta;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.moves.UniversalAlgorithmParser;
import com.github.sampeterson1.puzzles.ivyCube.display.IvyCubeDisplayPiece;
import com.github.sampeterson1.puzzles.ivyCube.solvers.IvyCubeSolver;
import com.github.sampeterson1.puzzles.ivyCube.util.IvyCubeUtil;

public class IvyCubeMetaFunctions extends PuzzleMetaFunctions<IvyCube> {

	private IvyCubeSolver solver;
	
	public IvyCubeMetaFunctions(IvyCube puzzle) {
		super(puzzle);
		this.solver = new IvyCubeSolver(puzzle);
	}

	@Override
	public Algorithm simplify(Algorithm alg) {
		return alg;
	}

	@Override
	public Algorithm scramble(int length) {
		Algorithm scramble = new Algorithm();
		
		for(int i = 0; i < length; i ++) {
			int index = (int) Mathf.random(0, IvyCubeUtil.moveAxes.length);
			boolean cw = (Mathf.random(0, 1) > 0.5f);
			scramble.addMove(new Move(IvyCubeUtil.moveAxes[index], cw));
		}
		
		super.getPuzzle().executeAlgorithm(scramble);
		
		return scramble;
	}

	@Override
	public Algorithm solve() {
		return solver.solve();
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
		return new IvyCubeDisplayPiece(piece);
	}

	@Override
	public Algorithm parseAlgorithm(String alg) {
		return UniversalAlgorithmParser.parseAlgorithm(alg);
	}
	
}
