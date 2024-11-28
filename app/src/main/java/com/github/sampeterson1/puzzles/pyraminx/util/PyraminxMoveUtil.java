/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.puzzles.pyraminx.util;

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.pyraminx.meta.Pyraminx;

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
	
	public static Move getRandomMove(int puzzleSize) {
		int i = (int) Mathf.random(0, Pyraminx.faces.length);
		Axis f = Pyraminx.faces[i];

		int layer = (int) Mathf.random(0, puzzleSize);
		boolean cw = (Mathf.random(0, 1) < 0.5);

		return new Move(f, layer, cw);
	}
	
	public static Axis mapFace(Axis face, Move move) {
		Map<Axis, Axis> moveMap = faceMaps.get(move.getAxis());
		int iters = move.isCW() ? 1 : 2;
		for(int i = 0; i < iters; i ++) face = moveMap.get(face);
		
		return face;
	}
}
