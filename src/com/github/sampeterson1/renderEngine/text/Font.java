package com.github.sampeterson1.renderEngine.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.renderEngine.loaders.ResourceLoader;
import com.github.sampeterson1.renderEngine.loaders.TextureLoader;
import com.github.sampeterson1.renderEngine.models.Texture;

public class Font {

	private static final String FONT_PATH = "fonts/";
	private Map<Character, Glyph> glyphs;
	
	private Texture texture;
	private float fontSize;
	private int atlasSize;
	
	public Font(String fontFile, String atlasFile, float fontSize) {
		this.glyphs = new HashMap<Character, Glyph>();
		this.fontSize = fontSize;
		
		try {
			this.texture = TextureLoader.loadTexture(atlasFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedReader reader = ResourceLoader.openFile(FONT_PATH + fontFile);
		String line;
		
		try {
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split("\\s+");
				if(tokens[0].equals("common")) {
					atlasSize = getAtlasSize(tokens);
				} else if(tokens[0].equals("char")){
					Glyph character = new Glyph(tokens);
					glyphs.put(character.getChar(), character);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int getAtlasSize(String[] tokens) {
		for(String token : tokens) {
			String[] splitToken = token.split("=");
			if(splitToken[0].equals("scaleW")) {
				return Integer.valueOf(splitToken[1]);
			}
		}
		
		return 0;
	}
	
	public float getFontSize() {
		return this.fontSize;
	}
	
	public int getAtlasSize() {
		return this.atlasSize;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public Glyph getGlyph(char c) {
		return glyphs.get(c);
	}
	
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
}
