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
	
	private int paddingTop;
	private int paddingLeft;
	private int paddingBottom;
	private int paddingRight;
	
	private int desiredPadding = 8;
	private int spacing = 3;
	private int lineHeight;
	
	public Font(String fontFile, String atlasFile, float fontSize) {
		this.glyphs = new HashMap<Character, Glyph>();
		this.fontSize = fontSize;

		try {
			this.texture = TextureLoader.loadTexture(atlasFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedReader reader = ResourceLoader.openFile(FONT_PATH + fontFile);
		load(reader);
	}
	
	private void load(BufferedReader reader ) {
		String line;
		
		try {
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split("\\s+");
				if(tokens[0].equals("common")) {
					loadCommon(tokens);
				} else if(tokens[0].equals("info")) {
					loadInfo(tokens);
				} else if(tokens[0].equals("char")){
					Glyph glyph = new Glyph(tokens, paddingTop, paddingLeft, paddingBottom, paddingRight, desiredPadding);
					glyphs.put(glyph.getChar(), glyph);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadInfo(String[] tokens) {
		for(String token : tokens) {
			String[] splitToken = token.split("=");
			if(splitToken[0].equals("padding")) {
				String[] paddingValues = splitToken[1].split(",");
				
				paddingTop = Integer.valueOf(paddingValues[0]);
				paddingLeft = Integer.valueOf(paddingValues[1]);
				paddingBottom = Integer.valueOf(paddingValues[2]);
				paddingRight = Integer.valueOf(paddingValues[3]);
			} 
		}
	}
	
	private void loadCommon(String[] tokens) {
		for(String token : tokens) {
			String[] splitToken = token.split("=");
			if(splitToken[0].equals("scaleW")) {
				atlasSize = Integer.valueOf(splitToken[1]);
			} else if(splitToken[0].equals("lineHeight")) {
				lineHeight = Integer.valueOf(splitToken[1]) - paddingTop - paddingBottom;
			}
		}
	}
	
	public int getLineHeight() {
		return this.lineHeight;
	}
	
	public int getSpacing() {
		return this.spacing;
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
