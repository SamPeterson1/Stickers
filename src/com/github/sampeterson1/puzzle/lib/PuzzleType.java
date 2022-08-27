package com.github.sampeterson1.puzzle.lib;

import java.util.function.Function;

import com.github.sampeterson1.puzzles.cube.pieces.Cube;
import com.github.sampeterson1.puzzles.ivyCube.pieces.IvyCube;
import com.github.sampeterson1.puzzles.pyraminx.pieces.Pyraminx;
import com.github.sampeterson1.puzzles.square1.pieces.Square1;

//Contains all puzzle types
public enum PuzzleType {

	CUBE("Rubik's Cube", new PuzzleSizeBounds(3, 2, 100), (Integer size) -> new Cube(size)),
	PYRAMINX("Pyraminx", new PuzzleSizeBounds(3, 2, 100), (Integer size) -> new Pyraminx(size)),
	IVY_CUBE("Ivy Cube", new PuzzleSizeBounds(1), (Integer size) -> new IvyCube()),
	SQUARE1("Square-1", new PuzzleSizeBounds(1), (Integer size) -> new Square1());
	
	private Function<Integer, Puzzle> init;
	private PuzzleSizeBounds sizeController;
	private String fullName;
	
	private PuzzleType(String fullName, PuzzleSizeBounds sizeController, Function<Integer, Puzzle> init) {
		this.init = init;
		this.fullName = fullName;
		this.sizeController = sizeController;
	}
	
	public Puzzle createPuzzle(int size) {
		return init.apply(size);
	}

	public PuzzleSizeBounds getSizeController() {
		return sizeController;
	}

	public String getName() {
		return fullName;
	}
	
}
