package com.github.sampeterson1.renderEngine.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.github.sampeterson1.renderEngine.gui.GUIButton;
import com.github.sampeterson1.renderEngine.gui.GUIComponent;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.shaders.ButtonShader;

public class GUIRenderer {
	
	private static final int NUM_ATTRIBS = 2;
	
	private ButtonShader shader;
	
	public GUIRenderer() {
		this.shader = new ButtonShader();
	}
	
	public void render() {
		GL11.glEnable(GL11.GL_BLEND);
		shader.start();
		List<GUIComponent> components = GUIMaster.getComponentsByType(MeshType.BUTTON);
		if(components != null) {
			for(GUIComponent buttonComponent : components) {
				GUIButton button = (GUIButton) buttonComponent;
				shader.loadButton(button);

				MeshData data = button.getMesh().getData();
				GL30.glBindVertexArray(data.getVaoID());
				enableAttribs();
	
				GL11.glDrawElements(GL11.GL_TRIANGLES, data.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
				
				disableAttribs();
				GL30.glBindVertexArray(0);
			}
		}
		
		GL11.glDisable(GL11.GL_BLEND);
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
