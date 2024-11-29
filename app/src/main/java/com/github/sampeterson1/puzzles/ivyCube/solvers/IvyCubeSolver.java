package com.github.sampeterson1.puzzles.ivyCube.solvers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.InvalidAlgorithmException;
import com.github.sampeterson1.puzzle.moves.UniversalAlgorithmParser;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.ivyCube.meta.IvyCube;
import com.github.sampeterson1.puzzles.ivyCube.util.IvyCubeUtil;
import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;

public class IvyCubeSolver {

	private IvyCube ivy;
	private Map<Integer, Algorithm> solutions;
	
	public IvyCubeSolver(IvyCube ivy) {
		this.ivy = ivy;
		readSolutions();
	}
	
	private void readSolutions() {
		this.solutions = new HashMap<Integer, Algorithm>();
		
		BufferedReader reader = ResourceLoader.openFile("ivyCube/Solutions.txt");
		String line;
		
		try {
			while((line = reader.readLine()) != null) {
				parseSolution(line);
			}
			reader.close();
		} catch (IOException | InvalidAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private void parseSolution(String line) throws InvalidAlgorithmException{
		String[] tokens = line.split(";");
		
		Algorithm solution = UniversalAlgorithmParser.parseAlgorithm(tokens[1], PuzzleType.IVY_CUBE);
		int stateHash = Integer.parseInt(tokens[0]);
		
		solutions.put(stateHash, solution);
	}
	
	public Algorithm solve() {
		int hash = IvyCubeUtil.hash(ivy);
		
		Algorithm solution = solutions.get(hash);
		ivy.executeAlgorithm(solution);
		
		return solution;
	}
	
	
	
}
