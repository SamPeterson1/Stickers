/*
    PrimePuzzle Twisty Puzzle Simulator and Solver
    Copyright (C) 2022 Sam Peterson
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.github.sampeterson1.puzzle.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;

public class Algorithm {
	
	/*
	 * R U[0,2]
	 */
	
	private static Map<String, Axis> axesByName = new HashMap<String, Axis>();
	
	private static int parseLayer(String str, int puzzleSize) {
		char first = str.charAt(0);

		if(first == '-') {
			if(str.length() > 1) {
				String numStr = str.substring(1);
				return puzzleSize - Integer.parseInt(numStr) - 1;
			} else {
				return puzzleSize - 1;
			}
		}
		
		return Integer.parseInt(str);
	}
	
	private static Algorithm parseMove(String str, Map<String, String> moveReplacements, int puzzleSize) {
		boolean invert = false;
		int iters = 1;
		for(int i = 0; i < str.length(); i ++) {
			char c = str.charAt(i);
			if(Character.isDigit(c)) {
				iters = Character.getNumericValue(c);
				str = str.substring(0, i) + str.substring(i + 1);
				break;
			} else if(c == '\'') {
				invert = true;
				str = str.replace("\'", "");
				break;
			} else if(c == '[') {
				break;
			}
		}
		
		if(moveReplacements != null) {
			if(moveReplacements.containsKey(str)) {
				str = moveReplacements.get(str);
			}
		}
		
		StringBuilder algStr = new StringBuilder();
		for(int i = 0; i < iters; i ++) {
			algStr.append(str);
			if(i < iters - 1) algStr.append(" ");
		}
		
		
		System.out.println("Old: " + str);
		System.out.println("New: " + algStr);
		Algorithm alg = new Algorithm();
		String[] moves = algStr.toString().split(" ");
		for(String move : moves) {
			alg.addMoves(parseRawMove(move, puzzleSize));
		}
		
		if(invert) alg = alg.getInverse();
		
		return alg;
	}
	
	private static List<Move> parseRawMove(String move, int puzzleSize) {
		StringBuilder moveName = new StringBuilder();
		int i;
		char c = ' ';
		boolean isCubeRotation = false;
		for(i = 0; i < move.length(); i ++) {
			c = move.charAt(i);
			if(c == '[' || c == '\'' || Character.isDigit(c)) break;
			if(c != '~') moveName.append(c);
			else isCubeRotation = true;
		}
		
		Axis axis = axesByName.get(moveName.toString());

		boolean cw = true;
		int startLayer = 0;
		int endLayer = 0;
		int iters = 1;
		
		if(Character.isDigit(c)) {
			iters = Character.getNumericValue(c);
			c = move.charAt(i++);
		}
		
		if(c == '\'') {
			cw = false;
			if(i + 1 < move.length()) {
				c = move.charAt(++i);
			}
		}
		
		if(c == '[') {
			String[] layers = move.substring(i + 1, move.length() - 1).split(",");
			if(layers.length > 1) {
				System.out.println(layers[0] + " a " + layers[1]);
				System.out.println("Layer: " + move.substring(i+1, move.length() - 1) + " " + move);

				startLayer = parseLayer(layers[0], puzzleSize);
				endLayer = parseLayer(layers[1], puzzleSize);
			} else {
				startLayer = endLayer = parseLayer(layers[0], puzzleSize);
			}
		}
		
		List<Move> moves = new ArrayList<Move>();

		System.out.println(startLayer + " " + endLayer);
		
		if(isCubeRotation) {
			for(int j = 0; j < iters; j ++) {
				moves.add(new Move(axis, cw, isCubeRotation));
			}
		} else {
			for(int layer = startLayer; layer <= endLayer; layer ++) {
				for(int j = 0; j < iters; j ++) {
					moves.add(new Move(axis, layer, cw));
				}
			}
		}
		
		return moves;
	}
	
	public static Algorithm parseAlgorithm(String str, Map<String, String> moveReplacements, int puzzleSize) {
		if(str.length() == 0) return new Algorithm();
		
		Algorithm alg = new Algorithm();
		String[] moves = str.split(" ");
		for(String move : moves) {
			alg.append(parseMove(move, moveReplacements, puzzleSize));
		}
		
		return alg;
	}
	
	public static Algorithm parseAlgorithm(String str, int puzzleSize) {
		return parseAlgorithm(str, null, puzzleSize);
	}
	
	public static Algorithm parseAlgorithm(String str, Map<String, String> moveReplacements) {
		return parseAlgorithm(str, moveReplacements, 0);
	}
	
	public static Algorithm parseAlgorithm(String str) {
		return parseAlgorithm(str, null, 0);
	}
	
	public static Matrix3D getRotationFromAlgorithm(Algorithm alg) {
		Matrix3D rotation = new Matrix3D();
		
		for(Move move : alg.getMoves()) {
			Axis axis = move.getFace();
			Vector3f rotationAxis = axis.getRotationAxis();
			System.out.println(axis);
			float rotationAmount = axis.getRotationAmount();
			if(move.isCW()) {
				rotation.rotateAroundAxis(rotationAxis, rotationAmount);
			} else {
				rotation.rotateAroundAxis(rotationAxis, -rotationAmount);
			}
		}
		
		return rotation;
	}
	
	public static void addAxis(Axis axis) {
		axesByName.put(axis.getName(), axis);
	}
	
	public List<Move> moves;
	
	public Algorithm(List<Move> moves) {
		this.moves = moves;
	}
	
	public Algorithm() {
		this.moves = new ArrayList<Move>();
	}
	
	public Algorithm copy() {
		Algorithm copy = new Algorithm();
		for(Move move : moves) copy.addMove(move);
		
		return copy;
	}
	
	public List<Move> getMoves() {
		return this.moves;
	}
	
	public Move getMove(int index) {
		return moves.get(index);
	}
	
	public Algorithm getInverse() {
		Algorithm inv = new Algorithm();
		for(int i = moves.size()-1; i >= 0; i --) {
			inv.addMove(moves.get(i).getInverse());
		}
		return inv;
	}
	
	public void append(Algorithm other) {
		List<Move> otherMoves = other.getMoves();
		for(Move move : otherMoves) {
			moves.add(move);
		}
	}
	
	public void addMoves(List<Move> moves) {
		for(Move move : moves) addMove(move);
	}
	
	public void addMove(Move move) {
		moves.add(move);
	}
	
	public int length() {
		return this.moves.size();
	}
	
	public Algorithm simplify(MoveSimplificationRule rule) {
		if(rule != null) 
			return rule.simplify(copy());
		
		return copy();
	}
}
