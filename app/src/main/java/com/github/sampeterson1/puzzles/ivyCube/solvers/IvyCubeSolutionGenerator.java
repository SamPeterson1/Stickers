package com.github.sampeterson1.puzzles.ivyCube.solvers;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.InvalidAlgorithmException;
import com.github.sampeterson1.puzzle.moves.UniversalAlgorithmParser;
import com.github.sampeterson1.puzzles.ivyCube.meta.IvyCube;
import com.github.sampeterson1.puzzles.ivyCube.util.IvyCubeUtil;
import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;

public class IvyCubeSolutionGenerator {

	private static void readSolutions(Map<Integer, Algorithm> solutions) {		
		BufferedReader reader = ResourceLoader.openFile("ivyCube/Solutions.txt");
		String line;
		
		try {
			while((line = reader.readLine()) != null) {
				parseSolution(line, solutions);
			}
			reader.close();
		} catch (IOException | InvalidAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private static void parseSolution(String line, Map<Integer, Algorithm> solutions) throws InvalidAlgorithmException {
		String[] tokens = line.split(";");
		
		Algorithm solution = UniversalAlgorithmParser.parseAlgorithm(tokens[1], PuzzleType.IVY_CUBE);
		int stateHash = Integer.parseInt(tokens[0]);
		
		if(!solutions.containsKey(stateHash)) {
			solutions.put(stateHash, solution);
		}
	}
	
	public static void generateSolutions(int scrambleDepth) {
		Map<Integer, Algorithm> solutions = new HashMap<Integer, Algorithm>();
		readSolutions(solutions);
		FileWriter writer = null;
		
		try {
			writer = new FileWriter("res/ivyCube/Solutions.txt");
			int i = 0;
			for(Algorithm alg : solutions.values()) {
				IvyCubeSolution solution = new IvyCubeSolution(alg, false);
				String str = solution.toString();
				if((i++) % 100 == 0) {
					System.out.println(i + "/"  + solutions.size());
				}
				writer.write(str + "\n");
			}
			
			while(solutions.size() < 29160) {
				IvyCube solutionFinder = new IvyCube();
				solutionFinder.addMetaFunctions();
				Algorithm scramble = solutionFinder.getMetaFunctions().scramble(scrambleDepth);
				int stateHash = IvyCubeUtil.hash(solutionFinder);
				
				if(!solutions.containsKey(stateHash)) {
					IvyCubeSolution solution = new IvyCubeSolution(scramble);
					solutions.put(stateHash, solution.getSolution());
					
					String str = solution.toString();
					System.out.println("Num solutions: " + solutions.size() + " " + str);
					writer.write(str + "\n");
				}
			}
			
			writer.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
