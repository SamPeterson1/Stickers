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

package com.github.yoshiapolis.puzzle.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Puzzle {
	
	protected HashMap<PieceType, ArrayList<PieceGroup>> pieces;
	protected int size;
	
	private ArrayList<Move> rotations;
	private ArrayList<Integer> rotationStack;
	private Algorithm moveLog;
	private boolean logMoves;
		
	public Puzzle(int size) {
		this.size = size;
		pieces = new HashMap<PieceType, ArrayList<PieceGroup>>();
		rotations = new ArrayList<Move>();
		rotationStack = new ArrayList<Integer>();
	}
	
	public abstract Face transposeFace(Face face);
	public abstract Algorithm solve();
	
	public final int getSize() {
		return size;
	}
	
	public final Algorithm getMoveLog() {
		return this.moveLog;
	}
	
	public final void clearMoveLog() {
		this.moveLog = new Algorithm();
	}
	
	public final void setLogMoves(boolean logMoves) {
		this.logMoves = logMoves;
	}
	
	public final void pushRotations() {
		rotationStack.add(rotations.size());
	}
	
	public final void popRotations() {
		int numRotations = rotationStack.remove(rotationStack.size() - 1);
		while(rotations.size() != numRotations) {
			Move move = rotations.remove(0);
			makeMove(move, false);
			rotations.remove(0);
		}
	}
	
	public final void makeRotation(Face face, boolean cw) {
		Move move = new Move(face, cw, true);
		makeMove(move, false);
	}
	
	public final ArrayList<Move> getRotations() {
		return this.rotations;
	}
	
	public final PieceGroup getGroup(PieceType type, Face face) {
		return pieces.get(type).get(face.getIndex());
	}
	
	public final PieceGroup getGroup(PieceType type, int position) {
		return pieces.get(type).get(position);
	}
	
	public final void executeAlgorithm(Algorithm alg, boolean log) {
		List<Move> moves = alg.getMoves();
		for(Move move : moves) {
			makeMove(move, log);
		}
	}
	
	public final void makeMove(Move move, boolean log) {	
		for(ArrayList<PieceGroup> groups : pieces.values()) {
			for(PieceGroup group : groups) {
				group.makeMove(move);
			}
		}
		for(ArrayList<PieceGroup> groups : pieces.values()) {
			for(PieceGroup group : groups) {
				group.applyMoves();
			}
		}
	
		if(move.isCubeRotation()) {
			rotations.add(0, move.getInverse());
		}
		move = move.transpose(this);
		
		if(log && !move.isCubeRotation()) moveLog.addMove(move); 
	}
	
	public final void executeAlgorithm(Algorithm alg) {
		executeAlgorithm(alg, logMoves);
	}
	
	public final void makeMove(Move move) {
		makeMove(move, logMoves);
	}
	
	public final void print() {
		for(PieceType type : pieces.keySet()) {
			ArrayList<PieceGroup> groups = pieces.get(type);
			System.out.println(type);
			for(PieceGroup group : groups) {
				System.out.print(group.getPosition() + ": [");
				for(Piece piece : group.getPieces()) {
					System.out.print(piece + ", ");
				}
				System.out.println("]");
			}
			System.out.println();
		}
	}
}
