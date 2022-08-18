package com.github.sampeterson1.puzzle.display;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.puzzle.lib.PuzzleSizeController;
import com.github.sampeterson1.puzzle.lib.PuzzleType;

public class PuzzleMaster {
	
	private static final Map<String, PuzzleType> puzzlesByName = getPuzzlesByName();
	
	private static Puzzle puzzleCopy;
	private static PuzzleDisplay display;
	private static int puzzleSize;
	private static PuzzleType selectedPuzzleType;
	
	private static int scrambleLength = 0;
	private static int movePointer = 0;
	private static Algorithm animatingAlg;
	
	private static Map<String, PuzzleType> getPuzzlesByName() {
		Map<String, PuzzleType> retVal = new HashMap<String, PuzzleType>();
		PuzzleType[] types = PuzzleType.class.getEnumConstants();
		
		for(PuzzleType type : types) {
			retVal.put(type.getName(), type);
		}
		
		return retVal;
	}
	
	public static void update() {
		if(animatingAlg != null) {
			if(!display.isAnimating()) {
				if(movePointer < animatingAlg.length()) {
					display.makeMove(animatingAlg.getMove(movePointer++));
				} else {
					animatingAlg = null;
					movePointer = 0;
				}
			}
		}
		
		display.update();
	}
	
	public static void setAnimationSpeed(float speed) {
		display.setAnimationSpeed(speed);
	}
	
	public static void setScrambleLength(int length) {
		scrambleLength = length;
	}
	
	public static void executeAlgorithm(String alg) {
		animatingAlg = puzzleCopy.parseAlgorithm(alg);
		puzzleCopy.executeAlgorithm(animatingAlg);
	}
	
	public static void scramble() {
		animatingAlg = puzzleCopy.scramble(scrambleLength);
	}
	
	public static void solve() {
		animatingAlg = puzzleCopy.solve();
	}
	
	public static Collection<String> getPuzzleNames() {
		return puzzlesByName.keySet();
	}
	
	private static void refresh() {
		if(display != null) {
			display.delete();
		}

		display = new PuzzleDisplay(selectedPuzzleType.createPuzzle(puzzleSize));
		display.setAnimationSpeed(20f);
		display.setAnimate(true);
		puzzleCopy = selectedPuzzleType.createPuzzle(puzzleSize);
	}
	
	public static void selectPuzzle(String name) {	
		selectedPuzzleType = puzzlesByName.get(name);
		puzzleSize = selectedPuzzleType.getSizeController().getDefaultSize();
		refresh();
	}
	
	public static void setPuzzleSize(int size) {
		PuzzleSizeController sizeController = puzzleCopy.getType().getSizeController();
		if(size >= sizeController.getMinSize() && size <= sizeController.getMaxSize()) {
			puzzleSize = size;
		}
		refresh();
	}
	
}
