package com.github.sampeterson1.renderEngine.gui;

import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.MeshData;

public class DropdownMeshUtil {

	public static MeshData createMesh(int numOptions, float width, float height) {
		float[] vertices = new float[(numOptions + 2) * 8];
		float[] texCoords = new float[vertices.length];
		int[] optionIDs = new int[vertices.length / 2];
		int[] indices = new int[(numOptions + 2) * 6];
		
		for(int i = 0; i < numOptions + 1; i ++) {
			float maxX = (i == 0) ? (width - height) : width;
			float yOff = -i * height;
			
			vertices[8*i] = 0;
			vertices[8*i + 1] = yOff;
			
			vertices[8*i + 2] = maxX;
			vertices[8*i + 3] = yOff;
			
			vertices[8*i + 4] = maxX;
			vertices[8*i + 5] = yOff - height;
			
			vertices[8*i + 6] = 0;
			vertices[8*i + 7] = yOff - height;
			
			indices[6*i] = 4*i;
			indices[6*i + 1] = 4*i + 2;
			indices[6*i + 2] = 4*i + 1;
			
			indices[6*i + 3] = 4*i + 0;
			indices[6*i + 4] = 4*i + 3;
			indices[6*i + 5] = 4*i + 2;
			
			for(int j = 0; j < 4; j ++) {
				optionIDs[4*i + j] = i - 1;
			}
		}
		
		float texPadding = 0.2f;
		int boxVertIndex = (numOptions + 1) * 8;
		
		vertices[boxVertIndex] = width - height;
		vertices[boxVertIndex + 1] = 0;
		texCoords[boxVertIndex] = -texPadding;
		texCoords[boxVertIndex + 1] = -texPadding;
		
		vertices[boxVertIndex + 2] = width;
		vertices[boxVertIndex + 3] = 0;
		texCoords[boxVertIndex + 2] = 1 + texPadding;
		texCoords[boxVertIndex + 3] = -texPadding;
		
		vertices[boxVertIndex + 4] = width;
		vertices[boxVertIndex + 5] = -height;
		texCoords[boxVertIndex + 4] = 1 + texPadding;
		texCoords[boxVertIndex + 5] = 1 + texPadding;
		
		vertices[boxVertIndex + 6] = width - height;
		vertices[boxVertIndex + 7] = -height;
		texCoords[boxVertIndex + 6] = -texPadding;
		texCoords[boxVertIndex + 7] = 1 + texPadding;
		
		for(int i = 0; i < 4; i ++) {
			optionIDs[boxVertIndex / 2 + i] = -1;
		}
		
		int indicesIndex = (numOptions + 1) * 6;
		indices[indicesIndex++] = boxVertIndex / 2;
		indices[indicesIndex++] = boxVertIndex / 2 + 2;
		indices[indicesIndex++] = boxVertIndex / 2 + 1;
		
		indices[indicesIndex++] = boxVertIndex / 2;
		indices[indicesIndex++] = boxVertIndex / 2 + 3;
		indices[indicesIndex++] = boxVertIndex / 2 + 2;
		
		return Loader.loadDropdownMesh(vertices, texCoords, indices, optionIDs);
	}
	
}
