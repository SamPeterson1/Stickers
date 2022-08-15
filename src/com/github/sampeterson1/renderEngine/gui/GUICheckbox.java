package com.github.sampeterson1.renderEngine.gui;

import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.rendering.MeshType;

public class GUICheckbox extends GUIComponent {

	private static final int[] quadIndices = {
			0, 1, 2, 
			2, 3, 0
	};
	
	public GUICheckbox(String name, float x, float y, float size) {
		super(name, x, y, size, size);
		float[] vertices = createVertices();
		MeshData meshData = Loader.load2DMesh(vertices, quadIndices);
		super.setMesh(new Mesh(meshData, MeshType.CHECKBOX));
	}
	
	private float[] createVertices() {
		return new float[] {
				-1, -1,
				1, -1,
				1, 1,
				-1, 1
		};
	}
	
}
