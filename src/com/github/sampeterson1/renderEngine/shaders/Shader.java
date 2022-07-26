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

package com.github.sampeterson1.renderEngine.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;

public abstract class Shader {
	
	private static final String SHADER_PATH = "shaders/";
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private Map<String, Integer> uniformLocations;
	
	public Shader(String vertexShader, String fragShader) {
		vertexShaderID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragShader, GL20.GL_FRAGMENT_SHADER);
		uniformLocations = new HashMap<String, Integer>();
		
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		locateUniforms(getAllUniformNames());
	}
	
	private void locateUniforms(String[] uniformNames) {
		for(String name : uniformNames) {
			int location = GL20.glGetUniformLocation(programID, name);
			uniformLocations.put(name, location);
		}
	}
	
	protected abstract void bindAttributes();
	
	protected abstract String[] getAllUniformNames();
	
	protected void loadFloat(String uniformName, float value) {
		int location = uniformLocations.get(uniformName);
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector3fList(String uniformName, List<Vector3f> values) {
		float[] arr = new float[values.size() * 3];
		for(int i = 0; i < arr.length; i += 3) {
			Vector3f v = values.get(i/3);
			arr[i] = v.x;
			arr[i + 1] = v.y;
			arr[i + 2] = v.z;
		}
		
		int location = uniformLocations.get(uniformName);
		GL20.glUniform3fv(location, arr);
	}
	
	protected void loadInt(String uniformName, int value) {
		int location = uniformLocations.get(uniformName);
		GL20.glUniform1i(location, value);
	}
	
	protected void loadBoolean(String uniformName, boolean value) {
		int location = uniformLocations.get(uniformName);
		GL20.glUniform1i(location, value ? 1 : 0);
	}
	
	protected void loadVector2f(String uniformName, Vector2f value) {
		int location = uniformLocations.get(uniformName);
		GL20.glUniform2f(location, value.x, value.y);
	}
	
	protected void loadVector3f(String uniformName, Vector3f value) {
		int location = uniformLocations.get(uniformName);
		GL20.glUniform3f(location, value.x, value.y, value.z);
	}
	
	protected void loadMatrix(String uniformName, Matrix3D matrix) {
		int location = uniformLocations.get(uniformName);
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	protected void bindAttribute(int attribute, String name) {
		GL20.glBindAttribLocation(programID, attribute, name);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void dispose() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteProgram(programID);
		GL20.glDeleteProgram(programID);
	}
	
	private static int loadShader(String file, int type) {
		int shaderID = GL20.glCreateShader(type);
		
		String shaderSource = readTextFile(SHADER_PATH + file);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader " + "\"" + file + "\".");
			System.exit(-1);
		}
		
		return shaderID;
	}
	
	private static String readTextFile(String file) {
		StringBuilder content = new StringBuilder();		
		try {
			BufferedReader reader = ResourceLoader.openFile(file);
			
			String line;
			while((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
			
			reader.close();
		} catch(IOException e) {
			System.err.println("Could not read shader file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return content.toString();
	}
	
}
