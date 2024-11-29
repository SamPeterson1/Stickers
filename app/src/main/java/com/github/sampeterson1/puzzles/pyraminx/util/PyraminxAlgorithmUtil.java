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

package com.github.sampeterson1.puzzles.pyraminx.util;

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.UniversalAlgorithmParser;
import com.github.sampeterson1.puzzle.moves.InvalidAlgorithmException;

public class PyraminxAlgorithmUtil {

	private static Map<String, String> moveReplacements = initMoveReplacements();
	
	private static Map<String, String> initMoveReplacements() {
		Map<String, String> moveReplacements = new HashMap<String, String>();
		moveReplacements.put("b", "~F'");	
		moveReplacements.put("r", "~L'");
		moveReplacements.put("l", "~R'");	
		moveReplacements.put("u", "~D'");	

		return moveReplacements;
	}
	
	public static Algorithm parseAlgorithm(String str, int puzzleSize) throws InvalidAlgorithmException {
		return UniversalAlgorithmParser.parseAlgorithm(str, moveReplacements, puzzleSize, PuzzleType.PYRAMINX);
	}

	public static Algorithm parseAlgorithm(String str) throws InvalidAlgorithmException {
		return UniversalAlgorithmParser.parseAlgorithm(str, moveReplacements, PuzzleType.PYRAMINX);
	}
	
}
