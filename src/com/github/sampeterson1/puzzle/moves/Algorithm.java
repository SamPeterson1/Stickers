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
import java.util.List;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;

//Represents an algorithm (sequence of moves) for any twisty puzzle
public class Algorithm {
	
	public static Matrix3D getRotationFromAlgorithm(Algorithm alg) {
		Matrix3D rotation = new Matrix3D();
		
		for(Move move : alg.getMoves()) {
			Axis axis = move.getAxis();
			Vector3f rotationAxis = axis.getRotationAxis();
			float rotationAmount = axis.getRotationAmount();
			if(move.isCW()) {
				rotation.rotateAroundAxis(rotationAxis, rotationAmount);
			} else {
				rotation.rotateAroundAxis(rotationAxis, -rotationAmount);
			}
		}
		
		return rotation;
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
	
	public Move pop() {
		return moves.remove(moves.size() - 1);
	}
	
	public void addMove(Move move) {
		moves.add(move);
	}
	
	public int length() {
		return this.moves.size();
	}
	
}
