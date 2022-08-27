package com.github.sampeterson1.puzzle.lib;

//Provides bounds for the size of a puzzle
public class PuzzleSizeBounds {
	
	int defaultSize;
	int minSize;
	int maxSize;
	
	public PuzzleSizeBounds(int size) {
		this(size, size, size);
	}
	
	public PuzzleSizeBounds(int defaultSize, int minSize, int maxSize) {
		this.defaultSize = defaultSize;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	public int getMinSize() {
		return minSize;
	}

	public int getMaxSize() {
		return maxSize;
	}
	
}
