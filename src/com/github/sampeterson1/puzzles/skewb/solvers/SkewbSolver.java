package com.github.sampeterson1.puzzles.skewb.solvers;

import java.util.HashMap;

import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.skewb.meta.Skewb;
import com.github.sampeterson1.puzzles.skewb.util.SkewbSolutionIO;
import com.github.sampeterson1.puzzles.skewb.util.SkewbSolverUtil;
import com.github.sampeterson1.puzzles.skewb.util.SkewbUtil;

public class SkewbSolver {
	
	public static final int MAX_SOLUTION_LENGTH = 8;
	public static final int NUM_HASHES = 3149280;

	private Skewb skewb;
	
	private Move[] possibleMoves;
	private int[][] solutions;
	
	public SkewbSolver(Skewb skewb) {
		
		this.skewb = skewb;
		//test();
		generateSolutions();
	}
	
	private void test() {
		int[] numPositions = new int[MAX_SOLUTION_LENGTH + 1];
		for(int hash = 0; hash < NUM_HASHES; hash ++) {
			if(hash % 10000 == 0) System.out.println((float) 100 * hash / NUM_HASHES);
			Algorithm alg = SkewbSolutionIO.getSolution(hash);
			numPositions[alg.length()] ++;
		}
		
		for(int i = 0; i < numPositions.length; i ++) {
			System.out.println("Solution length: " + i + ", Positions: " + numPositions[i]);
		}
	}
	
	@SuppressWarnings("unused")
	private void generateSolutions() {
		this.solutions = new int[NUM_HASHES][MAX_SOLUTION_LENGTH];
		this.possibleMoves = new Move[2 * SkewbUtil.AXES.length];
		
		int i = 0;
		for(Axis axis : SkewbUtil.AXES) {
			for(int direction = 0; direction < 2; direction ++) {
				boolean cw = (direction == 0);
				possibleMoves[i ++] = new Move(axis, cw);
			}
		}

		System.out.println("Foobar");
		int[] scramble = newAlgorithm();
		logSolution(scramble);
		
		for(int firstMove = 0; firstMove < possibleMoves.length; firstMove ++) {
			System.out.println("foo");
			scramble(scramble, firstMove);
		}
		System.out.println("bar");
		
		SkewbSolutionIO.writeSolutions(solutions);
	}

	public Algorithm solve() {
		int hash = SkewbSolverUtil.hash(skewb);
		System.out.println(hash + " " + SkewbSolverUtil.isSolved(skewb));
		return SkewbSolutionIO.getSolution(hash);
	}
	
	private int[] newAlgorithm() {
		int[] alg = new int[MAX_SOLUTION_LENGTH];
		for(int i = 0; i < alg.length; i ++)
			alg[i] = -1;
		
		return alg;
	}

	private int[] copy(int[] alg) {
		int[] newAlg = new int[alg.length];
		for(int i = 0; i < alg.length; i ++) {
			newAlg[i] = alg[i];
		}
		
		return newAlg;
	}
	
	private void logSolution(int[] scramble) {
		int hash = SkewbSolverUtil.hash(skewb);
		int[] currentSolution = solutions[hash];

		if(currentSolution == null || length(scramble) < length(currentSolution)) {
			solutions[hash] = invert(scramble);
		}
	}
	
	private int length(int[] scramble) {
		for(int i = 0; i < scramble.length; i ++) {
			if(scramble[i] == -1) return i;
		}
		
		return scramble.length;
	}
	
	private int invert(int move) {
		return (move % 2 == 0) ? (move + 1) : (move - 1);
	}
	
	private int[] invert(int[] alg) {
		int[] inverse = new int[alg.length];
		int j = 0;
		for(int i = alg.length - 1; i >= 0; i --) {
			inverse[j ++] = invert(alg[i]);
		}
		for(; j < inverse.length; j ++) inverse[j] = -1;
		
		return inverse;
	}
	
	private void scramble(int[] scramble, int move) {	
		int len = length(scramble);
		if(len < 3) System.out.println(len);
		
		if(len == MAX_SOLUTION_LENGTH) {
			logSolution(scramble);
			return;
		}
		
		scramble = copy(scramble);
		scramble[len] = move;
		skewb.makeMove(possibleMoves[move], false);
		
		logSolution(scramble);
		
		for(int nextMove = 0; nextMove < possibleMoves.length; nextMove ++) {
			if(nextMove / 2 != move / 2) {
				scramble(scramble, nextMove);
			}
		}
		
		skewb.makeMove(possibleMoves[invert(move)], false);
	}
	
}
