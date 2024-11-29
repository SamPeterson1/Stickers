/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.puzzle.display;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.PuzzleFactory;
import com.github.sampeterson1.puzzle.lib.PuzzleSizeController;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.InvalidAlgorithmException;
import com.github.sampeterson1.puzzle.templates.Puzzle;

//Controls the selected puzzle and provides functionality to switch between puzzles
public class PuzzleMaster {
	
	private static final Map<String, PuzzleType> puzzlesByName = getPuzzlesByName();
	private static final Map<PuzzleType, PuzzleSizeController> sizeControllers = createSizeControllers();
	
	private static Puzzle puzzleCopy;
	private static PuzzleDisplay display;
	private static int puzzleSize;
	private static PuzzleType selectedPuzzleType;
	
	private static int scrambleLength = 0;
	private static int movePointer = 0;
	private static Algorithm animatingAlg;
	
	private static Map<PuzzleType, PuzzleSizeController> createSizeControllers() {
		Map<PuzzleType, PuzzleSizeController> sizeControllers = new EnumMap<PuzzleType, PuzzleSizeController>(PuzzleType.class);
		
		PuzzleType[] types = PuzzleType.class.getEnumConstants();
		for(PuzzleType type : types) 
			sizeControllers.put(type, new PuzzleSizeController());
		
		sizeControllers.put(PuzzleType.CUBE, new PuzzleSizeController().withDefault(3).withMin(2).withMax(100));
		sizeControllers.put(PuzzleType.PYRAMINX, new PuzzleSizeController().withDefault(3).withMin(3).withMax(100));
		
		return sizeControllers;
	}

	private static void setAnimation(Algorithm alg) {
		if (animatingAlg != null) {
			animatingAlg = alg;
		}
	}
	
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
		if (animatingAlg != null) {
			return;
		}

		try {
			Algorithm parsedAlg = puzzleCopy.getMetaFunctions().parseAlgorithm(alg);

			puzzleCopy.clearMoveLog();
			
			puzzleCopy.pushRotations();
			puzzleCopy.executeAlgorithm(parsedAlg, true);
			puzzleCopy.popRotations();

			animatingAlg = puzzleCopy.getMoveLog();
			puzzleCopy.clearMoveLog();
		} catch(InvalidAlgorithmException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void scramble() {
		if (animatingAlg != null) {
			return;
		}

		animatingAlg = puzzleCopy.getMetaFunctions().scramble(scrambleLength);
	}
	
	public static void solve() {
		if (animatingAlg != null) {
			return;
		}

		animatingAlg = puzzleCopy.getMetaFunctions().solve();
	}
	
	public static Collection<String> getPuzzleNames() {
		return puzzlesByName.keySet();
	}
	
	private static void refresh() {
		if(display != null) {
			display.delete();
		}
		
		display = new PuzzleDisplay(PuzzleFactory.createPuzzle(selectedPuzzleType, puzzleSize));
		display.setAnimationSpeed(200000f);
		display.setAnimate(true);
		puzzleCopy = PuzzleFactory.createPuzzle(selectedPuzzleType, puzzleSize);
	}
	
	public static void selectPuzzle(String name) {	
		selectedPuzzleType = puzzlesByName.get(name);
		puzzleSize = sizeControllers.get(selectedPuzzleType).getDefaultSize();
		
		refresh();
	}
	
	public static void setPuzzleSize(int size) {
		puzzleSize = sizeControllers.get(selectedPuzzleType).restrictSize(size);
		refresh();
	}
	
}
