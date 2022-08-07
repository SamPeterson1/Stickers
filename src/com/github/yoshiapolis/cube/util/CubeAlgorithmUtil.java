package com.github.yoshiapolis.cube.util;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Move;

public class CubeAlgorithmUtil {
	
	private static Axis[] axes = {Axis.R, Axis.U, Axis.F, Axis.L, Axis.D, Axis.B};
	
	private static List<Move> removeInversePairs(List<Move> moves) {
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
	
	private static List<Move> mergeRepeatedMoves(List<Move> moves) {
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
	
	public static Algorithm simplify(Algorithm alg) {
		int prevNumMoves = Integer.MAX_VALUE;
		List<Move> moves = alg.getMoves();
		
		while (moves.size() < prevNumMoves) {
			prevNumMoves = moves.size();
			moves = mergeRepeatedMoves(moves);
			moves = removeInversePairs(moves);
		}
		
		return new Algorithm(moves);
	}
	
	private static Move getRandomMove(int puzzleSize) {
		int i = (int) Mathf.random(0, axes.length);
		Axis f = axes[i];

		int layer = (int) Mathf.random(0, puzzleSize);
		boolean cw = (Mathf.random(0, 1) < 0.5);

		return new Move(f, layer, cw);
	}
	
	public static Algorithm generateScramble(int length, int puzzleSize) {
		Algorithm scramble = new Algorithm();
		for(int i = 0; i < length; i ++) {
			scramble.addMove(getRandomMove(puzzleSize));
		}
		
		return scramble;
	}
	
}
