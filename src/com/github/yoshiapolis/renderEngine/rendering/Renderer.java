package com.github.yoshiapolis.renderEngine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.github.yoshiapolis.math.Matrix3D;
import com.github.yoshiapolis.renderEngine.models.ColorPalette;
import com.github.yoshiapolis.renderEngine.models.ColoredModel;
import com.github.yoshiapolis.renderEngine.models.ModelData;
import com.github.yoshiapolis.renderEngine.shaders.StaticShader;

public class Renderer {

	private StaticShader shader = new StaticShader();
	
	public Renderer() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);  
	}
	
	public void render() {
		shader.start();
		
		Matrix3D projectionMatrix = Matrix3D.createProjectionMatrix(Scene.getSettings());
		shader.setProjectionMatrix(projectionMatrix);
		shader.setLightDirection(Scene.getLightDirection());
		
		for(Entity entity : Scene.getEntities()) {
			ColoredModel model = entity.getModel();
			model.prepareColors();
			ModelData data = model.getData();
			
			shader.setTransformationMatrix(entity.getTransformationMatrix());
			
			GL30.glBindVertexArray(data.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, ColorPalette.getTextureID());
			
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
