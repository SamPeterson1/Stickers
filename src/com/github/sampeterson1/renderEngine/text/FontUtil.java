package com.github.sampeterson1.renderEngine.text;

public class FontUtil {

	public static float getWidth(Font font, String text) {
		if(text.length() == 0) return 0;
		
		float scale = (float) font.getFontSize() / font.getAtlasSize();
		float width = 0;
		
		for(int i = 0; i < text.length(); i ++) {
			Glyph glyph = font.getGlyph(text.charAt(i));
			if(i != text.length() - 1) {
				width += (glyph.getAdvance() + font.getSpacing()) * scale;
			} else {
				width += (glyph.getXOffset() + Math.max(glyph.getAdvance(), glyph.getWidth())) * scale;
			}
		}

		return width;
	}
	
	public static float getScaledLineHeight(Font font) {
		float scale = (float) font.getFontSize() / font.getAtlasSize();
		return font.getLineHeight() * scale;
	}
	
}
