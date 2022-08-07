package com.github.yoshiapolis.renderEngine.loaders;

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

import com.github.yoshiapolis.renderEngine.models.ModelData;
import com.github.yoshiapolis.renderEngine.models.Texture;

public class ModelLoader {
	
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();
	
	public static ModelData loadData(float[] positions, float[] texCoords, int[] indices) {
		int vaoID = createVAO();
		storeIndicesBuffer(indices);
		
		int[] vboIDs = new int[2];
		vboIDs[0] = storeAttributeData(0, 3, positions, GL15.GL_STATIC_DRAW);
		vboIDs[1] = storeAttributeData(1, 2, texCoords, GL15.GL_STATIC_DRAW);
		//vboIDs[2] = storeAttributeData(2, 3, normals, GL15.GL_STATIC_DRAW);
		GL30.glBindVertexArray(0);
		
		return new ModelData(vaoID, vboIDs, positions.length/3, indices.length);
	}
	
	public static ModelData loadDynamicColoredModel(float[] positions, float[] normals, float[] colors, int[] indices) {
		int vaoID = createVAO();
		storeIndicesBuffer(indices);
		
		int[] vboIDs = new int[3];
		vboIDs[0] = storeAttributeData(0, 3, positions, GL15.GL_STATIC_DRAW);
		vboIDs[1] = storeAttributeData(1, 3, colors, GL15.GL_DYNAMIC_DRAW);
		vboIDs[2] = storeAttributeData(2, 3, normals, GL15.GL_STATIC_DRAW);
		GL30.glBindVertexArray(0);
		
		return new ModelData(vaoID, vboIDs, positions.length/3, indices.length);
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
	
	public static void updateAttributeData(ModelData modelData, int attributeID, float[] data) {
		int vboID = modelData.getVboID(attributeID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static void cleanUp() {
		for(int vao : vaos)
			GL30.glDeleteVertexArrays(vao);
		for(int vbo : vbos)
			GL15.glDeleteBuffers(vbo);
		for(int texture : textures)
			GL11.glDeleteTextures(texture);
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
