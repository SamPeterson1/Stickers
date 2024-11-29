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

package com.github.sampeterson1.puzzle.moves;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.PuzzleType;

//Parses algorithms in universal algorithm notation
public class UniversalAlgorithmParser {

	private static Map<PuzzleType, Map<String, Axis>> allAxes = new EnumMap<PuzzleType, Map<String, Axis>>(PuzzleType.class);
	
	private static int parseLayer(String str, int puzzleSize) throws InvalidAlgorithmException {
		try {
			int layer = Integer.parseInt(str);
			
			if (layer < 0) {
				layer = puzzleSize + layer;
			}

			if (layer < 0 || layer >= puzzleSize) {
				throw new InvalidAlgorithmException("Invalid layer for puzzle size" + puzzleSize + ": " + str);
			}

			return layer;
		} catch(NumberFormatException e) {
			throw new InvalidAlgorithmException("Invalid layer for puzzle size" + puzzleSize + ": " + str);
		}
	}
	
	private static Algorithm parseMove(String str, Map<String, String> moveReplacements, int puzzleSize, PuzzleType puzzleType) throws InvalidAlgorithmException {
		boolean invert = false;
		int iters = 0;
		
		boolean parsingMoveName = true;
		int moveNameStartIndex = 0;
		int moveNameEndIndex = str.length();

		int moveLayerStartIndex = -1;
		int moveLayerEndIndex = -1;

		for(int i = 0; i < str.length(); i ++) {
			char c = str.charAt(i);

			if(parsingMoveName) {
				if(c == '\'' || Character.isDigit(c) || c == '[') {
					parsingMoveName = false;
					moveNameEndIndex = i;
				}
			}

			if (c == '[') {
				moveLayerStartIndex = i;
			} else if (c == ']') {
				moveLayerEndIndex = i + 1;
			}

			if(Character.isDigit(c) && moveLayerStartIndex == -1) {
				iters = 10 * iters + Character.getNumericValue(c);
			} else if(c == '\'') {
				invert = true;
			}
		}

		if (iters == 0) {
			iters = 1;
		}

		String layerStr = "";
		if (moveLayerStartIndex != -1 && moveLayerEndIndex != -1) {
			layerStr = str.substring(moveLayerStartIndex, moveLayerEndIndex);
		}

		String moveName = str.substring(moveNameStartIndex, moveNameEndIndex);
		
		if(moveReplacements != null) {
			moveName = moveReplacements.getOrDefault(moveName, moveName);
		}
		
		StringBuilder algStr = new StringBuilder();
		for(int i = 0; i < iters; i ++) {
			algStr.append(moveName);
			if(i < iters - 1) algStr.append(" ");
		}
		
		Algorithm alg = new Algorithm();
		String[] moves = algStr.toString().split(" ");
		for(String move : moves) {
			alg.addMoves(parseRawMove(move + layerStr, puzzleSize, puzzleType));
		}
		
		if(invert) alg = alg.getInverse();
		
		return alg;
	}
	
	private static List<Move> parseRawMove(String move, int puzzleSize, PuzzleType puzzleType) throws InvalidAlgorithmException {		
		System.out.println(move);
		
		if (move.length() == 0) {
			throw new InvalidAlgorithmException("Empty move");
		}
		
		int i = 0;

		boolean isCubeRotation = false;
		int startLayer = 0;
		int endLayer = 0;

		if (move.charAt(i) == '~') {
			isCubeRotation = true;
			i ++;
		}

		int axisNameStartIndex = i;
		int axisNameEndIndex = move.length();

		int layerRangeStartIndex = -1;
		int layerRangeEndIndex = -1;

		while (i < move.length() && move.charAt(i) != '[' && move.charAt(i) != '\'') {
			i ++;
		}
		
		axisNameEndIndex = i;

		if(i < move.length() && move.charAt(i) == '[') {
			layerRangeStartIndex = i + 1;
			System.out.println("foo");
			for(i = layerRangeStartIndex; i < move.length(); i ++) {
				char c = move.charAt(i);
				System.out.println(c);
				if(c == ']') {
					layerRangeEndIndex = i;
					break;
				}
			}
		}

		boolean isCW = true;

		if (i < move.length() && move.charAt(i) == '\'') {
			isCW = false;

			axisNameEndIndex = Math.min(axisNameEndIndex, i);
		}

		System.out.println("layerRangeStartIndex: " + layerRangeStartIndex + ", layerRangeEndIndex: " + layerRangeEndIndex);

		if (layerRangeStartIndex == -1 || layerRangeEndIndex == -1) {
			startLayer = 0;
			endLayer = 0;
		} else {
			String layerRange = move.substring(layerRangeStartIndex, layerRangeEndIndex);
			System.out.println(layerRange);
			String[] layerRangeSplit = layerRange.split(",");

			if (layerRangeSplit.length == 1) {
				int layer = parseLayer(layerRangeSplit[0], puzzleSize);
				startLayer = layer;
				endLayer = layer;
			} else if (layerRangeSplit.length == 2) {
				startLayer = parseLayer(layerRangeSplit[0], puzzleSize);
				endLayer = parseLayer(layerRangeSplit[1], puzzleSize);

				if (startLayer > endLayer) {
					throw new InvalidAlgorithmException("Invalid layer range: [" + layerRange + "]");
				}
			} else {
				throw new InvalidAlgorithmException("Invalid layer range: [" + layerRange + "]");
			}
		}
		
		String axisName = move.substring(axisNameStartIndex, axisNameEndIndex);
		Map<String, Axis> axesByName = allAxes.get(puzzleType);

		if (!axesByName.containsKey(axisName.toString())) {
			throw new InvalidAlgorithmException("Invalid move axis: " + axisName);
		}

		Axis axis = axesByName.get(axisName.toString());
		
		List<Move> moves = new ArrayList<Move>();

		for(int layer = startLayer; layer <= endLayer; layer ++) {
			moves.add(new Move(axis, layer, isCW, isCubeRotation));
		}

		return moves;
	}
	
	public static Algorithm parseAlgorithm(String str, Map<String, String> moveReplacements, int puzzleSize, PuzzleType puzzleType) throws InvalidAlgorithmException{
		if(str.length() == 0) return new Algorithm();
		
		Algorithm alg = new Algorithm();
		String[] moves = str.split(" ");

		for(String move : moves) {
			alg.append(parseMove(move, moveReplacements, puzzleSize, puzzleType));
		}
		
		return alg;
	}
	
	public static Algorithm parseAlgorithm(String str, int puzzleSize, PuzzleType puzzleType) throws InvalidAlgorithmException {
		return parseAlgorithm(str, null, puzzleSize, puzzleType);
	}
	
	public static Algorithm parseAlgorithm(String str, Map<String, String> moveReplacements, PuzzleType puzzleType) throws InvalidAlgorithmException {
		return parseAlgorithm(str, moveReplacements, 0, puzzleType);
	}
	
	public static Algorithm parseAlgorithm(String str, PuzzleType puzzleType) throws InvalidAlgorithmException {
		return parseAlgorithm(str, null, 0, puzzleType);
	}
	
	public static void addAxis(Axis axis) {
		PuzzleType type = axis.getPuzzleType();

		if(!allAxes.containsKey(type)) {
			allAxes.put(type, new HashMap<String, Axis>());
		}

		allAxes.get(type).put(axis.getName(), axis);
	}
	
	
}
