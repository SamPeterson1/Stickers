package com.github.sampeterson1.puzzles.skewb.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.skewb.solvers.SkewbSolver;

public class SkewbSolutionIO {

	private static final Map<Axis, Integer> AXIS_CODES = initAxisCodes();
	private static final int BYTES_PER_SOLUTION = (SkewbSolver.MAX_SOLUTION_LENGTH + 1) / 2;
	
	private static Map<Axis, Integer> initAxisCodes() {
		Map<Axis, Integer> map = new EnumMap<Axis, Integer>(Axis.class);
		
		for(int i = 0; i < 4; i ++) {
			map.put(SkewbUtil.AXES[i], i + 1);
		}
		
		return map;
	}
	
	public static void writeSolutions(int[][] solutions) {
		byte[] bytes = new byte[SkewbSolver.NUM_HASHES * BYTES_PER_SOLUTION];
		int cursor = 0;

		for(int[] solution : solutions) {
			if(solution == null) cursor += BYTES_PER_SOLUTION;
			else cursor = writeAlgorithm(bytes, cursor, solution);
		}
		
		write(bytes);
	}
	
	private static int writeAlgorithm(byte[] bytes, int cursor, int[] alg) {

		for(int i = 0; i < alg.length; i += 2) {
			int codeA = (alg[i] != -1) ? (alg[i] + 2) : 0;
			int codeB = ((i + 1) < alg.length && alg[i + 1] != -1) ? (alg[i + 1] + 2) : 0;
			
			bytes[cursor ++] = (byte) (16 * codeA + codeB - 128);
		}
		
		return cursor;
	}
	
	public static Algorithm getSolution(int hash) {
		byte[] bytes = read(hash * BYTES_PER_SOLUTION);
		
		Algorithm solution = new Algorithm();
		for(int i = 0; i < BYTES_PER_SOLUTION; i ++) {
			Move[] moves = readMoves(bytes[i]);
			
			if(moves[0] != null) solution.addMove(moves[0]);
			if(moves[1] != null) solution.addMove(moves[1]);
		}
		
		return solution;
	}
	
	private static int getMoveCode(Move move) {
		if(move == null) return 0;
		
		int directionCode = move.isCW() ? 0 : 1;
		int axisCode = AXIS_CODES.get(move.getAxis());
		
		return 2 * axisCode + directionCode;
	}
	
	private static byte getMoveCode(Move a, Move b) {
		int codeA = getMoveCode(a);
		int codeB = getMoveCode(b);
		
		int moveCode = codeA * 16 + codeB;
		return (byte) (moveCode - 128);
	}
	
	private static Move readMoveCode(int code) {
		if(code == 0) return null;
		
		Axis axis = SkewbUtil.AXES[code / 2 - 1];
		boolean cw = (code % 2 == 0);
		
		return new Move(axis, cw);
	}
	
	private static Move[] readMoves(byte b) {
		int unsigned = b + 128;
		int codeA = unsigned / 16;
		int codeB = unsigned % 16;
		
		return new Move[] { readMoveCode(codeA), readMoveCode(codeB) };
	}
	
	private static byte[] read(int off) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream("res/skewb/solutions.bin");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		byte[] bytes = new byte[BYTES_PER_SOLUTION];
		try {
			inputStream.skip(off);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bytes;
	}
	
	private static void write(byte[] bytes) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream("res/skewb/solutions.bin");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			outputStream.write(bytes);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
