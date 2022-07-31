package com.github.yoshiapolis.pyraminx.util;

import java.util.HashMap;
import java.util.Map;

import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;

public class PyraminxMoveUtil {

	private static Map<Face, Map<Face, Face>> faceMaps; 

	public static void init() {
		Map<Face, Face> dMap = new HashMap<Face, Face>();
		dMap.put(Face.PD, Face.PD);
		dMap.put(Face.PL, Face.PF);
		dMap.put(Face.PF, Face.PR);
		dMap.put(Face.PR, Face.PL);
		
		Map<Face, Face> lMap = new HashMap<Face, Face>();
		lMap.put(Face.PL, Face.PL);
		lMap.put(Face.PF, Face.PD);
		lMap.put(Face.PD, Face.PR);
		lMap.put(Face.PR, Face.PF);
		
		Map<Face, Face> fMap = new HashMap<Face, Face>();
		fMap.put(Face.PF, Face.PF);
		fMap.put(Face.PL, Face.PR);
		fMap.put(Face.PR, Face.PD);
		fMap.put(Face.PD, Face.PL);
		
		Map<Face, Face> rMap = new HashMap<Face, Face>();
		rMap.put(Face.PR, Face.PR);
		rMap.put(Face.PF, Face.PL);
		rMap.put(Face.PL, Face.PD);
		rMap.put(Face.PD, Face.PF);
		
		faceMaps = new HashMap<Face, Map<Face, Face>>();
		faceMaps.put(Face.PD, dMap);
		faceMaps.put(Face.PL, lMap);
		faceMaps.put(Face.PF, fMap);
		faceMaps.put(Face.PR, rMap);
	}
	
	public static Face mapFace(Face face, Move move) {
		Map<Face, Face> moveMap = faceMaps.get(move.getFace());
		int iters = move.isCW() ? 1 : 2;
		for(int i = 0; i < iters; i ++) face = moveMap.get(face);
		
		return face;
	}
}
