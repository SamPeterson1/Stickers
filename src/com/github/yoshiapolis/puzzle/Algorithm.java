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

package com.github.yoshiapolis.puzzle;

import java.util.ArrayList;

public class Algorithm {

	public ArrayList<Move> moves;
	
	public Algorithm() {
		this.moves = new ArrayList<Move>();
	}
	
	public ArrayList<Move> getMoves() {
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
		ArrayList<Move> otherMoves = other.getMoves();
		for(Move move : otherMoves) {
			moves.add(move);
		}
	}
	
	public void addMove(Move move) {
		moves.add(move);
	}
	
	public int length() {
		return this.moves.size();
	}
	
	public void simplify() {
		
	}
}
