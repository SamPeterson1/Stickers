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

package com.github.sampeterson1.puzzles.cube.util;

import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;

//Utilities relating to the Rubik's Cube in general
public class CubeUtil {

	private static final Axis[] faces = { Axis.R, Axis.U, Axis.F, Axis.L, Axis.D, Axis.B };
	
	private static final Map<Axis, Axis> opposingFaces = initOpposingFaces();
	private static final Map<Axis, Integer> facePositions = initFacePositions();
	private static final Map<Axis, Color> faceColors = initFaceColors();
	
	private static Map<Axis, Axis> initOpposingFaces() {
		Map<Axis, Axis> faces = new EnumMap<Axis, Axis>(Axis.class);
		faces.put(Axis.R, Axis.L);
		faces.put(Axis.U, Axis.D);
		faces.put(Axis.F, Axis.B);
		faces.put(Axis.L, Axis.R);
		faces.put(Axis.D, Axis.U);
		faces.put(Axis.B, Axis.F);
		
		return faces;
	}
	
	private static Map<Axis, Integer> initFacePositions() {
		Map<Axis, Integer> positions = new EnumMap<Axis, Integer>(Axis.class);
		positions.put(Axis.R, 0);
		positions.put(Axis.U, 1);
		positions.put(Axis.F, 2);
		positions.put(Axis.L, 3);
		positions.put(Axis.D, 4);
		positions.put(Axis.B, 5);
		
		return positions;
	}
	
	private static Map<Axis, Color> initFaceColors() {
		Map<Axis, Color> colors = new EnumMap<Axis, Color>(Axis.class);
		colors.put(Axis.R, Color.RED);
		colors.put(Axis.U, Color.WHITE);
		colors.put(Axis.F, Color.GREEN);
		colors.put(Axis.L, Color.ORANGE);
		colors.put(Axis.D, Color.YELLOW);
		colors.put(Axis.B, Color.BLUE);
		
		return colors;
	}
	
	public static Axis[] getFaces() {
		return faces;
	}
	
	public static Axis getFace(int index) {
		return faces[index];
	}

	public static Axis getOpposingFace(Axis face) {
		return opposingFaces.get(face);
	}

	public static Color getFaceColor(Axis face) {
		return faceColors.get(face);
	}
	
	public static int getFacePosition(Axis face) {
		return facePositions.get(face);
	}
	
	public static void init() {
		CubeMoveUtil.init();
	}

	public static boolean isRUF(Axis face) {
		return (face == Axis.R || face == Axis.U || face == Axis.F);
	}
	
}
