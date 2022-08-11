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

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.models.Texture;

public class ModelLoader {
	
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();
	
	public static MeshData loadColoredModel(float[] positions, float[] normals, int[] colorIndices, int[] indices) {
		int vaoID = createVAO();
		storeIndicesBuffer(indices);
		
		storeAttributeData(0, 3, positions);
		storeAttributeData(1, 3, normals);
		storeAttributeData(2, 1, colorIndices);
		
		GL30.glBindVertexArray(0);
		
		return new MeshData(vaoID, indices.length);
	}
	
	public static Texture loadTexture(String fileName) {
		Texture texture = null;
		
		try {
			texture = TextureLoader.loadTexture(fileName);
		} catch (IOException e) {
			System.err.println("Could not load texture " + "\"" + fileName + "\".");
			e.printStackTrace();
			System.exit(-1);
		}
		
		textures.add(texture.getID());
		return texture;
	}
	
	public static void cleanUp() {
		for(int vao : vaos)
			GL30.glDeleteVertexArrays(vao);
		for(int vbo : vbos)
			GL15.glDeleteBuffers(vbo);
		for(int texture : textures)
			GL11.glDeleteTextures(texture);
	}
	
	private static void addInstancedAttribute(int vao, int vbo, int attributeID, 
			int coordinteSize, int dataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		
		GL20.glVertexAttribPointer(attributeID, coordinteSize, GL11.GL_FLOAT, false, dataLength * 4, offset);
		GL33.glVertexAttribDivisor(attributeID, 1);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	private static int createEmptyStreamVBO(int numFloats) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, numFloats * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		return vboID;
	}
	
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	private static void storeIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
	
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private static int storeAttributeData(int attributeID, int coordinateSize, int[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		IntBuffer buffer = storeDataInIntBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeID, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
	
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		return vboID;
	}

	private static int storeAttributeData(int attributeID, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeID, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
	
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		return vboID;
	}
	
	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
}
