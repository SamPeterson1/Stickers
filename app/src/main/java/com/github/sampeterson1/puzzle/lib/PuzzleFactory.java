package com.github.sampeterson1.puzzle.lib;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.cube.meta.Cube;
import com.github.sampeterson1.puzzles.ivyCube.meta.IvyCube;
import com.github.sampeterson1.puzzles.pyraminx.meta.Pyraminx;
import com.github.sampeterson1.puzzles.skewb.meta.Skewb;
import com.github.sampeterson1.puzzles.square1.meta.Square1;

public class PuzzleFactory {
	
	private static Map<PuzzleType, Function<Integer, ? extends Puzzle>> initializers = initInitializers();
	
	private static Map<PuzzleType, Function<Integer, ? extends Puzzle>> initInitializers() {
		Map<PuzzleType, Function<Integer, ? extends Puzzle>> initializers = new EnumMap<PuzzleType, Function<Integer, ? extends Puzzle>>(PuzzleType.class);
		
		initializers.put(PuzzleType.CUBE, (Integer size) -> new Cube(size));
		initializers.put(PuzzleType.IVY_CUBE, (Integer size) -> new IvyCube());
		initializers.put(PuzzleType.PYRAMINX, (Integer size) -> new Pyraminx(size));
		initializers.put(PuzzleType.SQUARE1, (Integer size) -> new Square1());
		initializers.put(PuzzleType.SKEWB, (Integer size) -> new Skewb());
		
		return initializers;
	}
	
	public static Puzzle createPuzzle(PuzzleType type, int size) {
		return initializers.get(type).apply(size);
	}
}
