package com.github.sampeterson1.renderEngine.rendering;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.github.sampeterson1.renderEngine.gui.GUIComponent;
import com.github.sampeterson1.renderEngine.gui.GUIDropdownBox;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.gui.GUITextBox;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.shaders.ButtonShader;
import com.github.sampeterson1.renderEngine.shaders.CheckboxShader;
import com.github.sampeterson1.renderEngine.shaders.DropdownBoxShader;
import com.github.sampeterson1.renderEngine.shaders.GUIColorShader;
import com.github.sampeterson1.renderEngine.shaders.SliderShader;
import com.github.sampeterson1.renderEngine.shaders.TextBoxShader;

public class GUIRenderer {
	
	private Map<MeshType, GUIColorShader> shaders;
	private DropdownBoxShader dropdownBoxShader;
	private TextBoxShader textBoxShader;
	
	public GUIRenderer() {
		shaders = new EnumMap<MeshType, GUIColorShader>(MeshType.class);
		shaders.put(MeshType.BUTTON, new ButtonShader());
		shaders.put(MeshType.SLIDER, new SliderShader());
		shaders.put(MeshType.CHECKBOX, new CheckboxShader());
		textBoxShader = new TextBoxShader();
		dropdownBoxShader = new DropdownBoxShader();
	}
	
	public void render() {
		for(MeshType type : shaders.keySet()) {
			renderMeshType(type);
		}

		GL11.glDisable(GL11.GL_CULL_FACE);
		renderDropdownBoxes();
		renderTextBoxes();
	}
	
	private void renderTextBoxes() {
		textBoxShader.start();
		List<GUIComponent> components = GUIMaster.getComponentsByType(MeshType.TEXT_BOX);
		
		if(components != null) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, GUITextBox.getTexture().getID());
			for(GUIComponent component : components) {
				if(component.isVisible()) {
					GUITextBox textBox = (GUITextBox) component;
					textBoxShader.loadGUIComponent(component);
					
					MeshData meshData = textBox.getMesh().getData();
					GL30.glBindVertexArray(meshData.getVaoID());
					enableAttribs(2);
					
	
					GL11.glDrawElements(GL11.GL_TRIANGLES, meshData.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
					
					disableAttribs(2);
					GL30.glBindVertexArray(0);
				}
			}
		}
		
		dropdownBoxShader.stop();
	}
	
	private void renderDropdownBoxes() {
		dropdownBoxShader.start();
		List<GUIComponent> components = GUIMaster.getComponentsByType(MeshType.DROPDOWN_BOX);
		
		if(components != null) {
			for(GUIComponent component : components) {
				if(component.isVisible()) {
					GUIDropdownBox dropdownBox = (GUIDropdownBox) component;
					dropdownBoxShader.loadDropdown(dropdownBox);
					
					MeshData meshData = dropdownBox.getMesh().getData();
					GL30.glBindVertexArray(meshData.getVaoID());
					enableAttribs(3);
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, dropdownBox.getTexture().getID());
	
					GL11.glDrawElements(GL11.GL_TRIANGLES, meshData.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
					
					disableAttribs(3);
					GL30.glBindVertexArray(0);
				}
			}
		}
		
		dropdownBoxShader.stop();
	}
	
	private void renderMeshType(MeshType type) {
		GUIColorShader shader = shaders.get(type);
		shader.start();
		List<GUIComponent> components = GUIMaster.getComponentsByType(type);
		
		if(components != null) {
			for(GUIComponent component : components) {
				if(component.isVisible()) {
					shader.loadGUIComponent(component);
					MeshData meshData = component.getMesh().getData();
					GL30.glBindVertexArray(meshData.getVaoID());
					enableAttribs(3);
	
					GL11.glDrawElements(GL11.GL_TRIANGLES, meshData.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
					
					disableAttribs(3);
					GL30.glBindVertexArray(0);
				}
			}	
		}
		shader.stop();
	}
	
	private void renderMesh(Mesh mesh) {
		MeshData meshData = mesh.getData();
		GL30.glBindVertexArray(meshData.getVaoID());
		enableAttribs(1);

		GL11.glDrawElements(GL11.GL_TRIANGLES, meshData.getNumIndices(), GL11.GL_UNSIGNED_INT, 0);
		
		disableAttribs(1);
		GL30.glBindVertexArray(0);
	}
	
	private void disableAttribs(int numAttribs) {
		for(int i = 0; i < numAttribs; i ++) 
			GL30.glEnableVertexAttribArray(i);
	}
	
	private void enableAttribs(int numAttribs) {
		for(int i = 0; i < numAttribs; i ++) 
			GL30.glEnableVertexAttribArray(i);
	}
	
	public void dispose() {
		for(GUIColorShader shader : shaders.values()) {
			shader.dispose();
		}
	}
	
}
