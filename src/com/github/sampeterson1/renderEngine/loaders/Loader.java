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
import org.lwjgl.opengl.GL40;

import com.github.sampeterson1.renderEngine.models.ColoredMesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.models.Texture;

public class Loader {
	
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();
	
	public static MeshData loadColoredMesh(float[] positions, float[] normals, int[] colorIndices, 
			int[] indices) {
		int vaoID = createVAO();
		storeIndicesBuffer(indices);
		
		int[] vboIDs = new int[3];
		vboIDs[0] = storeAttributeData(0, 3, positions, GL15.GL_STATIC_DRAW);
		vboIDs[1] = storeAttributeData(1, 3, normals, GL15.GL_STATIC_DRAW);
		//for(int i = 0; i < colorIndices.length; i ++) colorIndices[i] = 1;
		vboIDs[2] = storeAttributeDataI(2, 1, colorIndices, GL15.GL_STATIC_DRAW);
		
		GL30.glBindVertexArray(0);
		
		return new MeshData(vaoID, vboIDs, indices.length, positions.length/3);
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
	
	public static void addInstancedAttribute(int vboID, int vaoID, int attributeID, 
			int coordinateSize, int dataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL30.glBindVertexArray(vaoID);
		
		GL20.glVertexAttribPointer(attributeID, coordinateSize, GL11.GL_FLOAT, false, dataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attributeID, 1);	
		
		GL30.glBindVertexArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static void addInstancedAttributeI(int vboID, int vaoID, int attributeID, 
			int coordinateSize, int dataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL30.glBindVertexArray(vaoID);
		
		GL30.glVertexAttribIPointer(attributeID, coordinateSize, GL11.GL_INT, dataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attributeID, 1);	
		
		GL30.glBindVertexArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static void updateAttribVBO(MeshData meshData, int attributeID, int[] data) {
		int vboID = meshData.getVboID(attributeID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(data);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, vboID, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static void updateVBO(int vboID, IntBuffer data) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, data);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static void updateVBO(int vboID, FloatBuffer data) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, data);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static int createDynamicVBO(int bytes) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bytes, GL15.GL_DYNAMIC_DRAW);
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
	
	private static int storeAttributeDataI(int attributeID, int coordinateSize, int[] data, int drawMode) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		IntBuffer buffer = storeDataInIntBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, drawMode);
		GL30.glVertexAttribIPointer(attributeID, coordinateSize, GL11.GL_INT, 0, 0);
	
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		return vboID;
	}

	private static int storeAttributeData(int attributeID, int coordinateSize, float[] data, int drawMode) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, drawMode);
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
