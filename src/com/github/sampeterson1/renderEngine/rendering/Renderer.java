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

package com.github.sampeterson1.renderEngine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.models.PieceBatch;
import com.github.sampeterson1.renderEngine.shaders.PieceShader;

public class Renderer {
	
	private static final int NUM_ATTRIBS = 8;
	
	private PieceShader shader = new PieceShader();
	private OrbitalCamera camera;
	
	public Renderer(OrbitalCamera camera) {
		this.camera = camera;
		GL11.glEnable(GL11.GL_DEPTH_TEST);  
		//GL11.glEnable(GL11.GL_CULL_FACE);  
		//GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void render() {
		shader.start();
		
		Matrix3D projectionMatrix = Matrix3D.createProjectionMatrix(Scene.getSettings());
		shader.setProjectionMatrix(projectionMatrix);
		shader.setViewMatrix(camera.getViewMatrix());
		shader.setLightDirection(Scene.getLightDirection());
		
		for(PieceBatch pieceBatch : Scene.getPieceBatches()) {
			shader.setColorPalette(pieceBatch.getPalette());
			pieceBatch.prepare();
			MeshData data = pieceBatch.getMesh().getData();
			
			GL30.glBindVertexArray(data.getVaoID());
			for(int i = 0; i < NUM_ATTRIBS; i ++)
				GL20.glEnableVertexAttribArray(i);
			
			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, data.getNumIndices(), GL11.GL_UNSIGNED_INT, 0, pieceBatch.getNumInstances());
			
			for(int i = 0; i < NUM_ATTRIBS; i ++)
				GL20.glDisableVertexAttribArray(i);
			GL30.glBindVertexArray(0);
		}
		
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
