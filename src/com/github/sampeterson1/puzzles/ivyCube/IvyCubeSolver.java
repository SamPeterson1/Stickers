package com.github.sampeterson1.puzzles.ivyCube;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.UniversalAlgorithmParser;
import com.github.sampeterson1.puzzles.ivyCube.pieces.IvyCube;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseSolution(String line) {
		String[] tokens = line.split(";");
		
		Algorithm solution = UniversalAlgorithmParser.parseAlgorithm(tokens[1]);
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
