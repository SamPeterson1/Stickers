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

public abstract class Puzzle {
	
	private MoveSimplificationRule simplificationRule;
	
	private List<Piece> allPieces;
	private HashMap<PieceType, List<PieceGroup>> groupsByType;
	private int size;
	
	private ArrayList<Move> rotations;
	private ArrayList<Integer> rotationStack;
	private Algorithm moveLog;
	private boolean logMoves;
		
	public Puzzle(int size) {
		this.size = size;
		
		this.groupsByType = new HashMap<PieceType, List<PieceGroup>>();
		this.allPieces = new ArrayList<Piece>();
		
		this.rotations = new ArrayList<Move>();	
		this.rotationStack = new ArrayList<Integer>();
	}
	
	
	
	/*
	protected void addGroupType(PieceType type, List<PieceGroup> groups) {
		groupsByType.put(type, groups);
		
		for(PieceGroup group : groups) {
			for(Piece piece : group.getPieces()) {
				allPieces.add(piece);
			}
		}
	}
	*/
	
	public abstract Axis transposeAxis(Axis face);
	public abstract Algorithm simplify(Algorithm alg);
	public abstract Algorithm scramble(int length);
	public abstract Algorithm solve();
	
	private void addAllPieces(List<PieceGroup> groups) {
		for(PieceGroup group : groups) {
			for(Piece piece : group.getPieces()) {
				allPieces.add(piece);
			}
		}
	}

	
	protected void createPieces(PieceBehavior behavior, int numGroups) {
		List<PieceGroup> groups = new ArrayList<PieceGroup>();
		for(int i = 0; i < numGroups; i ++) {
			groups.add(new PieceGroup(behavior, this, i));
		}
		
		groupsByType.put(behavior.getType(), groups);
		addAllPieces(groups);
	}
	
	public final int getSize() {
		return size;
	}
	
	public final Algorithm getSimplifiedAlgorithm(Algorithm alg) {
		return alg.simplify(simplificationRule);
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
	
	public final void makeRotation(Axis face, boolean cw) {
		Move move = new Move(face, cw, true);
		makeMove(move, false);
	}
	
	public final ArrayList<Move> getRotations() {
		return this.rotations;
	}

	public final PieceGroup getGroup(PieceType type, int position) {
		return this.groupsByType.get(type).get(position);
	}
	
	public final List<PieceGroup> getGroups(PieceType type) {
		return this.groupsByType.get(type);
	}
	
	public final HashMap<PieceType, List<PieceGroup>> getAllGroups() {
		return this.groupsByType;
	}
	
	public final List<Piece> getAllPieces() {
		return this.allPieces;
	}
	
	public final void executeAlgorithm(Algorithm alg, boolean log) {
		List<Move> moves = alg.getMoves();
		for(Move move : moves) {
			makeMove(move, log);
		}
	}
	
	public final void makeMove(Move move, boolean log) {	
		for(List<PieceGroup> groups : groupsByType.values()) {
			for(PieceGroup group : groups) {
				group.makeMove(move);
			}
		}
		
		for(List<PieceGroup> groups : groupsByType.values()) {
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
		for(PieceType type : groupsByType.keySet()) {
			List<PieceGroup> groups = groupsByType.get(type);
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
