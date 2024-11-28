package com.github.sampeterson1.puzzle.lib;

//Contains all puzzle types
public enum PuzzleType {

	CUBE("Rubik's Cube"),
	PYRAMINX("Pyraminx"),
	IVY_CUBE("Ivy Cube"),
	SQUARE1("Square-1"),
	SKEWB("Skewb");

	private String name;
	
	private PuzzleType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
