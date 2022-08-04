package com.github.yoshiapolis.renderEngine.models;

import com.github.yoshiapolis.renderEngine.loaders.ModelLoader;

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
