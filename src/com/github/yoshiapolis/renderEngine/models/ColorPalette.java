package com.github.yoshiapolis.renderEngine.models;

import com.github.yoshiapolis.renderEngine.loaders.ModelLoader;

public class ColorPalette {
		
	private static final int CELL_SIZE = 16;
	
	private static int rowSize;
	private static Texture texture;
	
	public static float[] getTextureCoords(int colorID) {
		int xIndex = colorID % rowSize;
		int yIndex = colorID / rowSize;
		
		float x = (xIndex + 0.5f) * CELL_SIZE / texture.getWidth();
		float y = (yIndex + 0.5f) * CELL_SIZE / texture.getHeight();
		
		return new float[] {x, y};
	}
	
	public static void loadPaletteTexture(String path) {
		texture = ModelLoader.loadTexture(path);
		rowSize = texture.getWidth() / CELL_SIZE;
	}
	
	public static int getTextureID() {
		return texture.getID();
	}
}
