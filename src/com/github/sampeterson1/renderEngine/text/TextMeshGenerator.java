package com.github.sampeterson1.renderEngine.text;

import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.Mesh;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.window.Window;

public class TextMeshGenerator {
	
	private static String wrapText(String text, Font font, float lineWidth) {
		String[] words = text.split(" ");
		StringBuilder wrapped = new StringBuilder();
		float currentLineWidth = 0;
		for(int i = 0; i < words.length; i ++) {
			String word = words[i];
			currentLineWidth += FontUtil.getWidth(font, word + " ");
			if(currentLineWidth > lineWidth) {
				currentLineWidth = 0;
				i--;
				wrapped.append("\n");
			} else if(i < words.length) {
				wrapped.append(word + " ");
			} else {
				wrapped.append(word);
			}
		}
		
		return wrapped.toString();
	}
	
	public static TextMesh generateMesh(String text, Font font) {
		text = wrapText(text, font, 1.5f);
		int cursorX = 0;
		int cursorY = 0;
		
		float[] vertices = new float[text.length() * 12];
		float[] texCoords = new float[text.length() * 8];
		int[] indices = new int[text.length() * 6]; 
		
		int vertexPtr = 0;
		int texCoordPtr = 0;
		int indexPtr = 0;
		
		float texScale = 1.0f / font.getAtlasSize();
		float posScale = font.getFontSize() * texScale;
		float aspect = (float) Window.getWidth() / Window.getHeight();
			
		for(char c : text.toCharArray()) {
			if(c == '\n') {
				cursorY += font.getLineHeight();
				cursorX = 0;
			} else {
				Glyph glyph = font.getGlyph(c); 
				
				float x = posScale * (cursorX + glyph.getXOffset());
				float y = -posScale * (cursorY + glyph.getYOffset()) * aspect;
				float width = posScale * glyph.getWidth();
				float height = posScale * glyph.getHeight() * aspect;
				
				float texWidth = texScale * glyph.getWidth();
				float texHeight = texScale * glyph.getHeight();
				
				float texX = texScale * glyph.getTexCoordX();
				float texY = texScale * glyph.getTexCoordY();
				
				int vertexIndex = vertexPtr / 2;
				
				indices[indexPtr++] = vertexIndex + 1;
				indices[indexPtr++] = vertexIndex;
				indices[indexPtr++] = vertexIndex + 2;
				
				indices[indexPtr++] = vertexIndex + 3;
				indices[indexPtr++] = vertexIndex + 2;
				indices[indexPtr++] = vertexIndex;
				
				vertices[vertexPtr++] = x;
				vertices[vertexPtr++] = y;
				
				texCoords[texCoordPtr++] = texX;
				texCoords[texCoordPtr++] = texY;
				
				vertices[vertexPtr++] = x + width;
				vertices[vertexPtr++] = y;
				
				texCoords[texCoordPtr++] = texX + texWidth;
				texCoords[texCoordPtr++] = texY;
				
				vertices[vertexPtr++] = x + width;
				vertices[vertexPtr++] = y - height;
				
				texCoords[texCoordPtr++] = texX + texWidth;
				texCoords[texCoordPtr++] = texY + texHeight;
				
				vertices[vertexPtr++] = x;
				vertices[vertexPtr++] = y - height;
				
				texCoords[texCoordPtr++] = texX;
				texCoords[texCoordPtr++] = texY + texHeight;
				
				cursorX += glyph.getAdvance() + font.getSpacing();
			}
		}
		
		MeshData data = Loader.load2DTexturedMesh(vertices, texCoords, indices);
		return new TextMesh(data, font.getTexture());
	}
	
}
