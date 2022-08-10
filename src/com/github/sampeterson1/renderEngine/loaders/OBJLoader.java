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

package com.github.sampeterson1.renderEngine.loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.github.sampeterson1.renderEngine.models.ColoredModel;
import com.github.sampeterson1.renderEngine.models.ColoredVertexGroup;
import com.github.sampeterson1.renderEngine.models.ModelData;

public class OBJLoader {
	
	private static final String OBJ_PATH = "obj/";
	
	private static String lastObjectName;
	private static int totalPositionsRead;
	private static int totalNormalsRead;
	private static int totalTexCoordsRead;
	
	private static class OBJData {
		public String objectName;
		public float[] positions;
		@SuppressWarnings("unused")
		public float[] texCoords;
		public float[] normals;
		public int[] indices;
	}
	
	private static BufferedReader openFile(String filePath) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(ResourceLoader.getResourceStream(OBJ_PATH + filePath)));
		} catch (FileNotFoundException e) {
			System.err.println("Could not load OBJ file " + "\"" + filePath + "\"");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return reader;
	}
	
	private static int safeParseInt(String str) {
		if(str.equals("")) return 0;
		return Integer.parseInt(str);
	}
	
	private static OBJData loadObject(BufferedReader reader) {
		OBJData data = new OBJData();
		String line = null;
		
		List<Integer> indices = new ArrayList<Integer>();
		List<Float> positions = new ArrayList<Float>();
		List<Float> normals = new ArrayList<Float>();
		List<Float> texCoords = new ArrayList<Float>();
	
		float[] normalsArr = null;
		float[] texCoordsArr = null;

		try {
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				
				if(tokens[0].equals("v")) {
					addCoordinateToList(tokens, positions, 3);
				} else if(tokens[0].equals("vt")) {
					addCoordinateToList(tokens, texCoords, 2);
				} else if(tokens[0].equals("vn")) {
					addCoordinateToList(tokens, normals, 3);
				} else if(tokens[0].equals("f")) {
					if(normalsArr == null) {
						int numVertices = positions.size() / 3;
						normalsArr = new float[numVertices * 3];
						texCoordsArr = new float[numVertices * 2];
					}
					
					for(int i = 0; i < 3; i ++) {
						String[] indicesStr = tokens[i + 1].split("/");
						int index = Integer.parseInt(indicesStr[0]) - 1;
						indices.add(index);
						
						int relativeIndex = index - totalPositionsRead;
						int normalIndex = safeParseInt(indicesStr[2]) - 1 - totalNormalsRead;
						int texCoordIndex = safeParseInt(indicesStr[1]) - 1 - totalTexCoordsRead;
						
						if(normalIndex >= 0) {
							normalsArr[3 * relativeIndex] = normals.get(3 * normalIndex);
							normalsArr[3 * relativeIndex + 1] = normals.get(3 * normalIndex + 1);
							normalsArr[3 * relativeIndex + 2] = normals.get(3 * normalIndex + 2);
						}
						
						if(texCoordIndex >= 0) {
							texCoordsArr[2 * relativeIndex] = texCoords.get(2 * texCoordIndex);
							texCoordsArr[2 * relativeIndex + 1] = texCoords.get(2 * texCoordIndex + 1);
						}
					}					
				} else if(tokens[0].equals("o")) {
					if(data.objectName == null) {
						if(lastObjectName == null) {
							data.objectName = tokens[1];
						} else {
							data.objectName = lastObjectName;
							lastObjectName = tokens[1];
							break;
						}
					} else {
						lastObjectName = tokens[1];
						break;
					}
				}
			}
			
			if(data.objectName == null) {
				if(lastObjectName != null) {
					data.objectName = lastObjectName;
					lastObjectName = null;
				} else {
					return null;
				}
			}
			
			data.positions = toFloatArr(positions);
			data.normals = normalsArr;
			data.texCoords = texCoordsArr;
			data.indices = toIntArr(indices);
			
			totalPositionsRead += positions.size() / 3;
			totalTexCoordsRead += texCoords.size() / 2;
			totalNormalsRead += normals.size() / 3;
			
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
			System.exit(-1);
		}
				
		return data;
	}
	
	public static ColoredModel loadColoredModel(String filePath) {
		List<Float> positions = new ArrayList<Float>();
		List<Float> normals = new ArrayList<Float>();
		List<Integer> indices = new ArrayList<Integer>();
		
		List<ColoredVertexGroup> colorGroups = new ArrayList<ColoredVertexGroup>();
		BufferedReader reader = openFile(filePath);
		
		totalPositionsRead = 0;
		totalNormalsRead = 0;
		totalTexCoordsRead = 0;
		lastObjectName = null;
		
		OBJData object = null;
		while((object = loadObject(reader)) != null) {
			addFloatArr(positions, object.positions);
			addFloatArr(normals, object.normals);
			addIntArr(indices, object.indices);
			
			ColoredVertexGroup colorGroup = new ColoredVertexGroup(object.objectName, object.indices);
			colorGroups.add(colorGroup);
		}
		
		float[] positionsArr = toFloatArr(positions);
		float[] emptyColors = new float[positionsArr.length]; 
		for(int i = 0; i < positionsArr.length; i ++) emptyColors[i] = 1;
		float[] normalsArr = toFloatArr(normals);
		int[] indicesArr = toIntArr(indices);
		
		ModelData modelData = ModelLoader.loadDynamicColoredModel(positionsArr, normalsArr, emptyColors, indicesArr);
		ColoredModel model = new ColoredModel(modelData);
		
		for(ColoredVertexGroup colorGroup : colorGroups)
			model.addColorGroup(colorGroup);
		
		return model;
	}
	
	private static void addIntArr(List<Integer> list, int[] arr) {
		for(int i : arr) {
			list.add(i);
		}
	}
	
	public static void addFloatArr(List<Float> list, float[] arr) {
		for(float f : arr) {
			list.add(f);
		}
	}
	
	private static float[] toFloatArr(List<Float> list) {
		float[] arr = new float[list.size()];
		for(int i = 0; i < arr.length; i ++) {
			arr[i] = list.get(i);
		}
		
		return arr;
	}
	
	private static int[] toIntArr(List<Integer> list) {
		int[] arr = new int[list.size()];
		for(int i = 0; i < arr.length; i ++) {
			arr[i] = list.get(i);
		}
		
		return arr;
	}
	
	private static void addCoordinateToList(String[] tokens, List<Float> list, int coordinateSize) {
		for(int i = 0; i < coordinateSize; i ++) {
			list.add(Float.parseFloat(tokens[i + 1]));
		}
	}
}
