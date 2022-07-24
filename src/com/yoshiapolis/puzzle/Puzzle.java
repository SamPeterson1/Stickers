/*
    PrimePuzzle Twisty Puzzle Simulator
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

package com.yoshiapolis.puzzle;

import java.util.ArrayList;
import java.util.HashMap;

public class Puzzle {
	
	protected HashMap<PieceType, ArrayList<PuzzlePieceGroup>> pieces;
	protected int size;
	
	private ArrayList<Move> rotations;
	private ArrayList<Integer> rotationStack;
	private Algorithm moveLog;
	private boolean logMoves;
		
	public Puzzle(int size) {
		this.size = size;
		pieces = new HashMap<PieceType, ArrayList<PuzzlePieceGroup>>();
		rotations = new ArrayList<Move>();
		rotationStack = new ArrayList<Integer>();
	}
	
	public int getSize() {
		return size;
	}
	
	public Face transposeFace(Face face) {
		return face;
	}
	
	public Algorithm getMoveLog() {
		return this.moveLog;
	}
	
	public void clearMoveLog() {
		this.moveLog = new Algorithm();
	}
	
	public void setLogMoves(boolean logMoves) {
		this.logMoves = logMoves;
	}
	
	public void pushRotations() {
		rotationStack.add(rotations.size());
	}
	
	public void popRotations() {
		int numRotations = rotationStack.remove(rotationStack.size() - 1);
		while(rotations.size() != numRotations) {
			Move move = rotations.remove(0);
			makeMove(move, false);
			rotations.remove(0);
		}
	}
	
	public void makeRotation(Face face, boolean cw) {
		Move move = new Move(face, cw, true);
		makeMove(move, false);
	}
	
	public ArrayList<Move> getRotations() {
		return this.rotations;
	}
	
	public PuzzlePieceGroup getGroup(PieceType type, int position) {
		return pieces.get(type).get(position);
	}
	
	public void executeAlgorithm(Algorithm alg, boolean log) {
		ArrayList<Move> moves = alg.getMoves();
		for(Move move : moves) {
			makeMove(move, log);
		}
	}
	
	public void makeMove(Move move, boolean log) {	
		for(ArrayList<PuzzlePieceGroup> groups : pieces.values()) {
			for(PuzzlePieceGroup group : groups) {
				group.makeMove(move);
			}
		}
		for(ArrayList<PuzzlePieceGroup> groups : pieces.values()) {
			for(PuzzlePieceGroup group : groups) {
				group.applyMoves();
			}
		}
	
		if(move.isCubeRotation()) {
			rotations.add(0, move.getInverse());
		}
		move = move.transpose(this);
		
		if(log && !move.isCubeRotation()) moveLog.addMove(move); 
	}
	
	public void executeAlgorithm(Algorithm alg) {
		executeAlgorithm(alg, logMoves);
	}
	
	public void makeMove(Move move) {
		makeMove(move, logMoves);
	}
	
	public void print() {
		for(PieceType type : pieces.keySet()) {
			ArrayList<PuzzlePieceGroup> groups = pieces.get(type);
			System.out.println(type);
			for(PuzzlePieceGroup group : groups) {
				System.out.print(group.getPosition() + ": [");
				for(PuzzlePiece piece : group.getPieces()) {
					System.out.print(piece + ", ");
				}
				System.out.println("]");
			}
			System.out.println();
		}
	}
}
