package com.github.sampeterson1.renderEngine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.renderEngine.models.ColoredModel;
import com.github.sampeterson1.renderEngine.models.ModelData;
import com.github.sampeterson1.renderEngine.shaders.StaticShader;

public class Renderer {

	private StaticShader shader = new StaticShader();
	private OrbitalCamera camera;
	
	public Renderer(OrbitalCamera camera) {
		this.camera = camera;
		GL11.glEnable(GL11.GL_DEPTH_TEST);  
	}
	
	public void render() {
		shader.start();
		
		Matrix3D projectionMatrix = Matrix3D.createProjectionMatrix(Scene.getSettings());
		shader.setProjectionMatrix(projectionMatrix);
		shader.setViewMatrix(camera.getViewMatrix());
		shader.setLightDirection(Scene.getLightDirection());
		
		for(DisplayPiece piece : Scene.getPieces()) {
			ColoredModel model = piece.getModel();
			model.prepareColors();
			ModelData data = model.getData();
			Matrix3D pieceTransformation = new Matrix3D();
			pieceTransformation.multiply(piece.getTransformationMat());
			shader.setTransformationMatrix(pieceTransformation);
			
			GL30.glBindVertexArray(data.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);

			GL11.glDrawElements(GL11.GL_TRIANGLES, data.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
			
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			
			GL30.glBindVertexArray(0);
		}
		
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
