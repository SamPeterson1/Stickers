package com.github.yoshiapolis.cube.util;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.MoveSimplificationRule;

public class CubeSimplificationRule implements MoveSimplificationRule {

	private List<Move> removeInversePairs(List<Move> moves) {
		List<Move> newMoves = new ArrayList<Move>();
		
		for (int i = 1; i < moves.size(); i ++) {
			Move move = moves.get(i);
			Move previous = moves.get(i - 1);
			
			if (move.getInverse().equals(previous)) {
				newMoves.remove(i - 1);
			} else {
				newMoves.add(move);
				previous = move;
			}
		}
		
		return newMoves;
	}
	
	private List<Move> mergeRepeatedMoves(List<Move> moves) {
		List<Move> newMoves = new ArrayList<Move>();
		
		for (int i = 2; i < moves.size(); i ++) {
			Move move = moves.get(i);
			Move prev1 = moves.get(i - 1);
			Move prev2 = moves.get(i - 2);
			
			if (move.equals(prev1) && prev1.equals(prev2)) {
				newMoves.remove(i - 1);
				newMoves.remove(i - 1);
			} else {
				newMoves.add(move);
			}
		}
		
		return newMoves;
	}
	
	@Override
	public List<Move> simplify(List<Move> moves) {
		int prevNumMoves = Integer.MAX_VALUE;
		
		while(moves.size() < prevNumMoves) {
			prevNumMoves = moves.size();
			moves = mergeRepeatedMoves(moves);
			moves = removeInversePairs(moves);
		}
		
		return moves;
	}

}
