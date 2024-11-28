package com.github.sampeterson1.puzzle.lib;

//Provides bounds for the size of a puzzle
public class PuzzleSizeController {
	
	int defaultSize;
	int minSize;
	int maxSize;
	
	public PuzzleSizeController() {}
	
	public PuzzleSizeController(int size) {
		this.defaultSize = size;
		this.minSize = size;
		this.maxSize = size;
	}

	public PuzzleSizeController withDefault(int defaultSize) {
		this.defaultSize = defaultSize;
		return this;
	}
	
	public PuzzleSizeController withMin(int minSize) {
		this.minSize = minSize;
		return this;
	}
	
	public PuzzleSizeController withMax(int maxSize) {
		this.maxSize = maxSize;
		return this;
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	public int restrictSize(int size) {
		if(size > maxSize) return maxSize;
		if(size < minSize) return minSize;
		
		return size;
	}
	
}
