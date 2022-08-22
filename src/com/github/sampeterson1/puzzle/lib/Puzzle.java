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

public abstract class Puzzle {
	
	private int size;
	
	private ArrayList<Move> rotations;
	private ArrayList<Integer> rotationStack;
	private Algorithm moveLog;
	private boolean logMoves;
		
	public Puzzle(int size) {
		this.size = size;
		this.rotations = new ArrayList<Move>();	
		this.rotationStack = new ArrayList<Integer>();
		this.moveLog = new Algorithm();
	}
	
	public abstract List<Piece> getAffectedPieces(Move move);

	public abstract List<Piece> getAllPieces();
	
	public abstract Axis transposeAxis(Axis face);
	
	public abstract Algorithm parseAlgorithm(String alg);
	
	public abstract Algorithm simplify(Algorithm alg);
	
	public abstract Algorithm scramble(int length);
	
	public abstract Algorithm solve();
	
	public abstract ColorPalette createDefaultColorPalette();
	
	public abstract DisplayPiece createDisplayPiece(Piece piece);
	
	public abstract PuzzleType getType();
	
	protected abstract void movePieces(Move move);
	
	public int getSize() {
		return size;
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
	
	public void makeRotation(Axis face, boolean cw) {
		Move move = new Move(face, cw, true);
		makeMove(move, false);
	}
	
	public ArrayList<Move> getRotations() {
		return this.rotations;
	}
	
	public void executeAlgorithm(Algorithm alg, boolean log) {
		List<Move> moves = alg.getMoves();
		for(Move move : moves) {
			makeMove(move, log);
		}
	}
	
	public void makeMove(Move move, boolean log) {	
		movePieces(move);
		
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
	
}
