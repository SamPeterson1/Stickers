package com.github.sampeterson1.renderEngine.models;

import com.github.sampeterson1.renderEngine.loaders.ModelLoader;

public class TexturedModel {
	
	private ModelData data;
	private Texture texture;
	
	public TexturedModel(ModelData data, String texture) {
		this.data = data;
		this.texture = ModelLoader.loadTexture(texture);
	}
	
	public int getTextureID() {
		return this.texture.getID();
	}
	
	public ModelData getData() {
		return this.data;
	}
	
}
