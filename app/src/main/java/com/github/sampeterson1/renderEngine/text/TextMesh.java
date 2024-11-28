package com.github.sampeterson1.renderEngine.text;

import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.models.Texture;
import com.github.sampeterson1.renderEngine.rendering.MeshType;

public class TextMesh extends Mesh {

	private Texture atlas;
	
	public TextMesh(MeshData data, Texture atlas) {
		super(data, MeshType.TEXT);
		this.atlas = atlas;
	}

	public Texture getTexture() {
		return this.atlas;
	}
	
}
