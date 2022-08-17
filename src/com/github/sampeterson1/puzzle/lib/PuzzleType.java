package com.github.sampeterson1.puzzle.lib;

import java.util.function.Function;

import com.github.sampeterson1.cube.pieces.Cube;
import com.github.sampeterson1.ivyCube.IvyCube;
import com.github.sampeterson1.pyraminx.pieces.Pyraminx;

public enum PuzzleType {

	CUBE("Rubik's Cube", new PuzzleSizeController(3, 2, 100), (Integer size) -> new Cube(size)),
	PYRAMINX("Pyraminx", new PuzzleSizeController(3, 2, 100), (Integer size) -> new Pyraminx(size)),
	IVY_CUBE("Ivy Cube", new PuzzleSizeController(1), (Integer size) -> new IvyCube());
	
	private Function<Integer, Puzzle> init;
	private PuzzleSizeController sizeController;
	private String fullName;
	
	private PuzzleType(String fullName, PuzzleSizeController sizeController, Function<Integer, Puzzle> init) {
		this.init = init;
		this.fullName = fullName;
		this.sizeController = sizeController;
	}
	
	public Puzzle createPuzzle(int size) {
		return init.apply(size);
	}

	public PuzzleSizeController getSizeController() {
		return sizeController;
	}

	public String getName() {
		return fullName;
	}
	
}
