package com.github.sampeterson1.puzzles.pyraminx.meta;

import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzles.pyraminx.display.PyraminxDisplayPiece;
import com.github.sampeterson1.puzzles.pyraminx.solvers.PyraminxCenterSolver;
import com.github.sampeterson1.puzzles.pyraminx.solvers.PyraminxEdgeSolver;
import com.github.sampeterson1.puzzles.pyraminx.solvers.PyraminxRingSolver;
import com.github.sampeterson1.puzzles.pyraminx.util.PyraminxMoveUtil;

public class PyraminxMetaFunctions extends PuzzleMetaFunctions<Pyraminx> {

	private PyraminxCenterSolver centerSolver;
	private PyraminxEdgeSolver edgeSolver;
	private PyraminxRingSolver ringSolver;
	
	public PyraminxMetaFunctions(Pyraminx puzzle) {
		super(puzzle);
		
		edgeSolver = new PyraminxEdgeSolver(puzzle);
		centerSolver = new PyraminxCenterSolver(puzzle);
		ringSolver = new PyraminxRingSolver(puzzle);
	}
	
	@Override
	public Algorithm simplify(Algorithm alg) {
		return alg;
	}

	@Override
	public Algorithm scramble(int length) {
		Algorithm scramble = new Algorithm();
		for(int i = 0; i < length; i ++) {
			scramble.addMove(PyraminxMoveUtil.getRandomMove(super.getPuzzle().getSize()));
		}
		
		super.getPuzzle().executeAlgorithm(scramble);
		return scramble;
	}
	
	@Override
	public Algorithm solve() {
		
		Algorithm alg = centerSolver.solve();
		alg.append(edgeSolver.solve());
		alg.append(ringSolver.solve());
		
		return alg;
	}

	@Override
	public ColorPalette createDefaultColorPalette() {
		ColorPalette palette = new ColorPalette();
		
		palette.putColor(Color.BORDER);
		palette.putColor(Color.GREEN);
		palette.putColor(Color.BLUE);
		palette.putColor(Color.RED);
		palette.putColor(Color.YELLOW);
		
		return palette;
	}

	@Override
	public DisplayPiece createDisplayPiece(Piece piece) {
		return new PyraminxDisplayPiece(piece);
	}

	@Override
	public Algorithm parseAlgorithm(String alg) {
		return new Algorithm();
	}

	
}
