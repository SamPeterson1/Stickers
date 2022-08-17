package com.github.sampeterson1.puzzle.lib;

public class PuzzleSizeController {
	
	int defaultSize;
	int minSize;
	int maxSize;
	
	public PuzzleSizeController(int size) {
		this(size, size, size);
	}
	
	public PuzzleSizeController(int defaultSize, int minSize, int maxSize) {
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
