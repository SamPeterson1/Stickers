package com.github.sampeterson1.renderEngine.text;

public class FontUtil {

	public static float getWidth(Font font, String text) {
		float scale = (float) font.getFontSize() / font.getAtlasSize();
		int pxWidth = 0;
		
		for(char c : text.toCharArray()) {
			Glyph glyph = font.getGlyph(c);
			pxWidth += glyph.getAdvance() + font.getSpacing();
		}
		
		return pxWidth * scale;
	}
	
}
