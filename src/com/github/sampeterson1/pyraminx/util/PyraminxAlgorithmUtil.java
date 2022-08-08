package com.github.sampeterson1.pyraminx.util;

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Algorithm;

public class PyraminxAlgorithmUtil {

	private static Map<String, String> moveReplacements = initMoveReplacements();
	
	private static Map<String, String> initMoveReplacements() {
		Map<String, String> moveReplacements = new HashMap<String, String>();
		moveReplacements.put("b", "F ~F'");	
		moveReplacements.put("r", "L ~L'");
		moveReplacements.put("l", "R ~R'");	
		moveReplacements.put("u", "D ~D'");	
		
		moveReplacements.put("F", "PF");	
		moveReplacements.put("L", "PL");
		moveReplacements.put("R", "PR");	
		moveReplacements.put("D", "PD");	

		return moveReplacements;
	}
	
	public static Algorithm parseAlgorithm(String str) {
		return Algorithm.parseAlgorithm(str, moveReplacements);
	}
	
}
