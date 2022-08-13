package com.github.sampeterson1.renderEngine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.github.sampeterson1.renderEngine.gui.GUIComponent;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.shaders.GUIShader;

public class GUIRenderer {
	
	private static final int NUM_ATTRIBS = 2;
	
	private GUIShader shader;
	
	public GUIRenderer() {
		this.shader = new GUIShader();
	}
	
	public void render() {
		shader.start();
		
		for(GUIComponent component : GUIMaster.getComponents()) {
			/*
			shader.setTransformationMatrix(component.getTransformationMatrix());
			MeshData data = component.getMesh().getData();
			GL30.glBindVertexArray(data.getVaoID());
			enableAttribs();

			GL11.glDrawElements(GL11.GL_TRIANGLES, data.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
			
			disableAttribs();
			GL30.glBindVertexArray(0);
			*/
		}
		
		shader.stop();
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
