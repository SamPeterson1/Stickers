package com.github.sampeterson1.pyraminx.util;

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;

public class PyraminxMoveUtil {

	private static Map<Axis, Map<Axis, Axis>> faceMaps; 

	public static void init() {
		Map<Axis, Axis> dMap = new HashMap<Axis, Axis>();
		dMap.put(Axis.PD, Axis.PD);
		dMap.put(Axis.PL, Axis.PF);
		dMap.put(Axis.PF, Axis.PR);
		dMap.put(Axis.PR, Axis.PL);
		
		Map<Axis, Axis> lMap = new HashMap<Axis, Axis>();
		lMap.put(Axis.PL, Axis.PL);
		lMap.put(Axis.PF, Axis.PD);
		lMap.put(Axis.PD, Axis.PR);
		lMap.put(Axis.PR, Axis.PF);
		
		Map<Axis, Axis> fMap = new HashMap<Axis, Axis>();
		fMap.put(Axis.PF, Axis.PF);
		fMap.put(Axis.PL, Axis.PR);
		fMap.put(Axis.PR, Axis.PD);
		fMap.put(Axis.PD, Axis.PL);
		
		Map<Axis, Axis> rMap = new HashMap<Axis, Axis>();
		rMap.put(Axis.PR, Axis.PR);
		rMap.put(Axis.PF, Axis.PL);
		rMap.put(Axis.PL, Axis.PD);
		rMap.put(Axis.PD, Axis.PF);
		
		faceMaps = new HashMap<Axis, Map<Axis, Axis>>();
		faceMaps.put(Axis.PD, dMap);
		faceMaps.put(Axis.PL, lMap);
		faceMaps.put(Axis.PF, fMap);
		faceMaps.put(Axis.PR, rMap);
	}
	
	public static Axis mapFace(Axis face, Move move) {
		Map<Axis, Axis> moveMap = faceMaps.get(move.getFace());
		int iters = move.isCW() ? 1 : 2;
		for(int i = 0; i < iters; i ++) face = moveMap.get(face);
		
		return face;
	}
}
