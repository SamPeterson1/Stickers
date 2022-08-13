package com.github.sampeterson1.renderEngine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.models.PieceBatch;
import com.github.sampeterson1.renderEngine.shaders.PieceShader;

public class PieceRenderer {

private static final int NUM_ATTRIBS = 8;
	
	private PieceShader shader;
	private OrbitalCamera camera;
	
	public PieceRenderer(OrbitalCamera camera) {
		this.shader = new PieceShader();
		this.camera = camera;
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
	
	public void dispose() {
		shader.dispose();
	}
	
}
