package com.github.sampeterson1.renderEngine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.github.sampeterson1.renderEngine.models.Entity;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.shaders.TextShader;
import com.github.sampeterson1.renderEngine.text.TextMesh;

public class TextRenderer {
	
	private static final int NUM_ATTRIBS = 2;
	
	private TextShader shader;
	
	public TextRenderer() {
		this.shader = new TextShader();
	}
	
	public void render() {
		shader.start();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		for(Entity entity : Scene.getEntities(MeshType.TEXT)) {
			TextMesh textMesh = (TextMesh) entity.getMesh();
			shader.setTransformationMatrix(entity.getTransform());
			MeshData data = entity.getMesh().getData();
			GL30.glBindVertexArray(data.getVaoID());
			enableAttribs();
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL15.GL_TEXTURE_2D, textMesh.getTexture().getID());
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, data.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
			
			disableAttribs();
			GL30.glBindVertexArray(0);
		}
		
		shader.stop();
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glDisable(GL11.GL_BLEND);
	}

	private void disableAttribs() {
		for(int i = 0; i < NUM_ATTRIBS; i ++) 
			GL30.glEnableVertexAttribArray(i);
	}
	
	private void enableAttribs() {
		for(int i = 0; i < NUM_ATTRIBS; i ++) 
			GL30.glEnableVertexAttribArray(i);
	}
	
	public void dispose() {
		shader.dispose();
	}
	
}
