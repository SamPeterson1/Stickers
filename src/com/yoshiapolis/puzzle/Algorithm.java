package com.yoshiapolis.puzzle;

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
