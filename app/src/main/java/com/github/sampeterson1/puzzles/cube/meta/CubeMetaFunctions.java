package com.github.sampeterson1.puzzles.cube.meta;

import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PuzzleMetaFunctions;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.InvalidAlgorithmException;
import com.github.sampeterson1.puzzles.cube.display.CubeDisplayPiece;
import com.github.sampeterson1.puzzles.cube.solvers.MasterCubeSolver;
import com.github.sampeterson1.puzzles.cube.util.CubeAlgorithmUtil;

public class CubeMetaFunctions extends PuzzleMetaFunctions<Cube> {

	public CubeMetaFunctions(Cube cube) {
		super(cube);
	}
	
	@Override
	public Algorithm simplify(Algorithm alg) {
		return CubeAlgorithmUtil.simplify(alg);
	}

	@Override
	public Algorithm scramble(int length) {
		Algorithm scramble = CubeAlgorithmUtil.generateScramble(length, super.getPuzzle().getSize());
		super.getPuzzle().executeAlgorithm(scramble);
		
		return scramble;
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
		return new CubeDisplayPiece(piece);
	}

	@Override
	public Algorithm parseAlgorithm(String alg) throws InvalidAlgorithmException {
		return CubeAlgorithmUtil.parseAlgorithm(alg, super.getPuzzle().getSize());
	}

	@Override
	public Algorithm solve() {
		return new MasterCubeSolver(super.getPuzzle()).solve();
	}

}
