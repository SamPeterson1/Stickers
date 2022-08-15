package com.github.sampeterson1.renderEngine.rendering;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.github.sampeterson1.renderEngine.gui.GUIButton;
import com.github.sampeterson1.renderEngine.gui.GUIComponent;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.gui.GUISlider;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.shaders.ButtonShader;
import com.github.sampeterson1.renderEngine.shaders.CheckboxShader;
import com.github.sampeterson1.renderEngine.shaders.GUIColorShader;
import com.github.sampeterson1.renderEngine.shaders.SliderShader;

public class GUIRenderer {
	
	private static final int NUM_ATTRIBS = 2;
	private Map<MeshType, GUIColorShader> shaders;
	
	public GUIRenderer() {
		shaders = new EnumMap<MeshType, GUIColorShader>(MeshType.class);
		shaders.put(MeshType.BUTTON, new ButtonShader());
		shaders.put(MeshType.SLIDER, new SliderShader());
		shaders.put(MeshType.CHECKBOX, new CheckboxShader());
	}
	
	public void render() {
		GL11.glEnable(GL11.GL_BLEND);
		for(MeshType type : shaders.keySet()) {
			renderMeshType(type);
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void renderMeshType(MeshType type) {
		GUIColorShader shader = shaders.get(type);
		shader.start();
		List<GUIComponent> components = GUIMaster.getComponentsByType(type);
		
		if(components != null) {
			for(GUIComponent component : components) {
				shader.loadGUIComponent(component);
				renderMesh(component.getMesh());
			}	
		}
	}
	
	private void renderMesh(Mesh mesh) {
		MeshData meshData = mesh.getData();
		GL30.glBindVertexArray(meshData.getVaoID());
		enableAttribs();

		GL11.glDrawElements(GL11.GL_TRIANGLES, meshData.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
		
		disableAttribs();
		GL30.glBindVertexArray(0);
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
		for(GUIColorShader shader : shaders.values()) {
			shader.dispose();
		}
	}
	
}
