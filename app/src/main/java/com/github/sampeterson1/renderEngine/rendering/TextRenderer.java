package com.github.sampeterson1.renderEngine.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.github.sampeterson1.renderEngine.gui.GUIComponent;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.shaders.TextShader;
import com.github.sampeterson1.renderEngine.text.GUIText;

public class TextRenderer {
	
	private static final int NUM_ATTRIBS = 2;
	
	private TextShader shader;
	
	public TextRenderer() {
		this.shader = new TextShader();
	}
	
	public void render() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
		
		List<GUIComponent> allText = GUIMaster.getComponentsByType(MeshType.TEXT);
		if(allText != null) {
			for(GUIComponent textComponent : allText) {
				if(textComponent.isVisible()) {
					GUIText text = (GUIText) textComponent;
					shader.loadText(text);
					MeshData data = text.getMesh().getData();
					GL30.glBindVertexArray(data.getVaoID());
					enableAttribs();
					
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL15.GL_TEXTURE_2D, text.getAtlasTexture().getID());
					
					GL11.glDrawElements(GL11.GL_TRIANGLES, data.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
					
					disableAttribs();
					GL30.glBindVertexArray(0);
				}
			}
		}
		
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
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
