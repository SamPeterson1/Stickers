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

package com.github.sampeterson1.puzzle.lib;

import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;

//provides the template and base functionality for (in theory) any type of twisty puzzle
public abstract class Puzzle {
	
	private PuzzleType type;
	private int size;
	
	private ArrayList<Move> rotations;
	private ArrayList<Integer> rotationStack;
	
	private Algorithm moveLog;
	private ArrayList<Integer> moveLogStack;
	
	private boolean logMoves;
		
	public Puzzle(PuzzleType type, int size) {
		this.logMoves = true;
		this.type = type;
		this.size = size;
		this.rotations = new ArrayList<Move>();	
		this.rotationStack = new ArrayList<Integer>();
		this.moveLogStack = new ArrayList<Integer>();
		this.moveLog = new Algorithm();
	}
	
	//returns a list of all of the pieces on this puzzle that are affected by a given move
	public abstract List<Piece> getAffectedPieces(Move move);

	//returns a list of all of the pieces on this puzzle
	public abstract List<Piece> getAllPieces();
	
	//maps a move axis to a new axis depending on the current puzzle rotations
	public abstract Axis transposeAxis(Axis face);
	
	//parses a string into an algorithm
	public abstract Algorithm parseAlgorithm(String alg);
	
	//simplifies an algorithm to have a less or equal length
	public abstract Algorithm simplify(Algorithm alg);
	
	//scrambles this puzzle and returns the scramble used
	public abstract Algorithm scramble(int length);
	
	//solves this puzzle and returns the solution used
	public abstract Algorithm solve();
	
	//creates the default sticker color values for this puzzle 
	public abstract ColorPalette createDefaultColorPalette();
	
	//creates a DisplayPiece that represents the given piece
	public abstract DisplayPiece createDisplayPiece(Piece piece);
	
	//apply a move to this puzzle's internal piece structure
	protected abstract void movePieces(Move move);
	
	public final PuzzleType getType() {
		return this.type;
	}
	
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
	
	//save the current puzzle rotation state
	public final void pushRotations() {
		rotationStack.add(rotations.size());
	}
	
	public final void pushState() {
		moveLogStack.add(moveLog.length());
	}
	
	public final void clearStates() {
		moveLogStack.clear();
	}
	
	//restore the last puzzle rotation state
	public final void popRotations() {
		int numRotations = rotationStack.remove(rotationStack.size() - 1);
		
		while(rotations.size() != numRotations) {
			Move move = rotations.remove(0);
			makeMove(move, false);
			rotations.remove(0);
		}
	}
	
	public final void popState() {
		int targetSize = moveLogStack.remove(moveLogStack.size() - 1);
		
		while(moveLog.length() != targetSize) {
			Move move = moveLog.pop();
			makeMove(move.getInverse(), false);
		}
	}
	
	public final void makeRotation(Axis face, boolean cw) {
		Move move = new Move(face, cw, true);
		makeMove(move, false);
	}
	
	public final ArrayList<Move> getRotations() {
		return this.rotations;
	}
	
	public final void executeAlgorithm(Algorithm alg, boolean log) {
		List<Move> moves = alg.getMoves();
		for(Move move : moves) {
			makeMove(move, log);
		}
	}
	
	public final void executeAlgorithm(Algorithm alg) {
		executeAlgorithm(alg, logMoves);
	}
	
	public final void makeMove(Move move, boolean log) {
		for(Move repetition : move.expandRepetitions()) {
			movePieces(repetition);
			
			if(repetition.isCubeRotation()) {
				rotations.add(0, repetition.getInverse());
			}
			repetition = repetition.transpose(this);
			
			if(log && !repetition.isCubeRotation()) moveLog.addMove(repetition); 
		}
	}
	
	public final void makeMove(Move move) {
		makeMove(move, logMoves);
	}
	
}
