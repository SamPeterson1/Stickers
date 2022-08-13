package com.github.sampeterson1.renderEngine.text;

import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.rendering.MeshType;

public class TextMeshGenerator {
	
	public static TextMesh generateMesh(String text, Font font) {
		int cursorX = 0;
		
		float[] vertices = new float[text.length() * 12];
		float[] texCoords = new float[text.length() * 8];
		int[] indices = new int[text.length() * 6]; 
		
		int vertexPtr = 0;
		int texCoordPtr = 0;
		int indexPtr = 0;
		
		float scale = 1.0f / font.getAtlasSize();
		
		for(char c : text.toCharArray()) {
			Glyph glyph = font.getGlyph(c); 
			
			float x = scale * (cursorX + glyph.getXOffset());
			float y = -scale * glyph.getYOffset();
			float width = scale * glyph.getWidth();
			float height = scale * glyph.getHeight();
			
			float texX = scale * glyph.getTexCoordX();
			float texY = scale * glyph.getTexCoordY();
			
			int vertexIndex = vertexPtr / 3;
			
			indices[indexPtr++] = vertexIndex + 1;
			indices[indexPtr++] = vertexIndex;
			indices[indexPtr++] = vertexIndex + 2;
			
			indices[indexPtr++] = vertexIndex + 3;
			indices[indexPtr++] = vertexIndex + 2;
			indices[indexPtr++] = vertexIndex;
			
			vertices[vertexPtr++] = x;
			vertices[vertexPtr++] = y;
			vertices[vertexPtr++] = 0;
			
			texCoords[texCoordPtr++] = texX;
			texCoords[texCoordPtr++] = texY;
			
			vertices[vertexPtr++] = x + width;
			vertices[vertexPtr++] = y;
			vertices[vertexPtr++] = 0;
			
			texCoords[texCoordPtr++] = texX + width;
			texCoords[texCoordPtr++] = texY;
			
			vertices[vertexPtr++] = x + width;
			vertices[vertexPtr++] = y - height;
			vertices[vertexPtr++] = 0;
			
			texCoords[texCoordPtr++] = texX + width;
			texCoords[texCoordPtr++] = texY + height;
			
			vertices[vertexPtr++] = x;
			vertices[vertexPtr++] = y - height;
			vertices[vertexPtr++] = 0;
			
			texCoords[texCoordPtr++] = texX;
			texCoords[texCoordPtr++] = texY + height;
			
			cursorX += glyph.getAdvance();
		}
		
		System.out.println((font.getTexture() == null));
		MeshData data = Loader.load2DTexturedMesh(vertices, texCoords, indices);
		return new TextMesh(data, font.getTexture());
	}
	
}
