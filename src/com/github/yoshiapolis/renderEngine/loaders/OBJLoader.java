package com.github.yoshiapolis.renderEngine.loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.renderEngine.models.ColoredModel;
import com.github.yoshiapolis.renderEngine.models.ColoredVertexGroup;
import com.github.yoshiapolis.renderEngine.models.ModelData;

public class OBJLoader {
	
	private static final String OBJ_PATH = "obj/";
	
	private static String lastObjectName;
	private static int totalPositionsRead;
	private static int totalNormalsRead;
	private static int totalTexCoordsRead;
	
	private static class OBJData {
		public String objectName;
		public float[] positions;
		public float[] texCoords;
		public float[] normals;
		public int[] baseIndices;
		public int[] accentIndices;
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
	
	private static OBJData loadObject(BufferedReader reader) {
		OBJData data = new OBJData();
		String line = null;
		
		List<Integer> currentIndices = null;
		List<Integer> baseIndices = new ArrayList<Integer>();
		List<Integer> accentIndices = new ArrayList<Integer>();
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
						String[] indices = tokens[i + 1].split("/");
						int index = Integer.parseInt(indices[0]) - 1;
						currentIndices.add(index);
						
						int relativeIndex = index - totalPositionsRead;
						int normalIndex = Integer.parseInt(indices[2]) - 1 - totalNormalsRead;
						int texCoordIndex = Integer.parseInt(indices[1]) - 1 - totalTexCoordsRead;
												
						normalsArr[3 * relativeIndex] = normals.get(3 * normalIndex);
						normalsArr[3 * relativeIndex + 1] = normals.get(3 * normalIndex + 1);
						normalsArr[3 * relativeIndex + 2] = normals.get(3 * normalIndex + 2);
						
						texCoordsArr[2 * relativeIndex] = texCoords.get(2 * texCoordIndex);
						texCoordsArr[2 * relativeIndex + 1] = texCoords.get(2 * texCoordIndex + 1);
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
				} else if(tokens[0].equals("usemtl")) {
					if(tokens[1].equalsIgnoreCase("base")) {
						currentIndices = baseIndices;
					} else if(tokens[1].equalsIgnoreCase("accent")) {
						currentIndices = accentIndices;
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
			data.baseIndices = toIntArr(baseIndices);
			data.accentIndices = toIntArr(accentIndices);
			
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
		List<Float> texCoords = new ArrayList<Float>();
		List<Float> normals = new ArrayList<Float>();
		List<Integer> indices = new ArrayList<Integer>();
		
		List<ColoredVertexGroup> colorGroups = new ArrayList<ColoredVertexGroup>();
		BufferedReader reader = openFile(filePath);
		
		lastObjectName = null;
		OBJData object = null;
		while((object = loadObject(reader)) != null) {
			addFloatArr(positions, object.positions);
			addFloatArr(texCoords, object.texCoords);
			addFloatArr(normals, object.normals);
			addIntArr(indices, object.baseIndices);
			addIntArr(indices, object.accentIndices);
			
			ColoredVertexGroup colorGroup = new ColoredVertexGroup(object.objectName, object.baseIndices, object.accentIndices);
			colorGroups.add(colorGroup);
		}
		
		float[] positionsArr = toFloatArr(positions);
		float[] texCoordsArr = toFloatArr(texCoords);
		float[] normalsArr = toFloatArr(normals);
		int[] indicesArr = toIntArr(indices);
		
		ModelData modelData = ModelLoader.loadDynamicTexturedModel(positionsArr, normalsArr, texCoordsArr, indicesArr);
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
