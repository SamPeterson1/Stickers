package com.github.sampeterson1.renderEngine.text;

public class Glyph {
	
	private char charCode;
	private int texCoordX;
	private int texCoordY;
	private int width;
	private int height;
	private int xOffset;
	private int yOffset;
	private int advance;
	
	public Glyph(String[] tokens) {
		for(int i = 1; i < tokens.length; i ++) {
			String[] splitToken = tokens[i].split("=");
			String key = splitToken[0];
			int value = Integer.valueOf(splitToken[1]);
			
			if(key.equals("id")) {
				charCode = (char) value;
			} else if(key.equals("x")) {
				texCoordX = value;
			} else if(key.equals("y")) {
				texCoordY = value;
			} else if(key.equals("width")) {
				width = value;
			} else if(key.equals("height")) {
				height = value;
			} else if(key.equals("xoffset")) {
				xOffset = value;
			} else if(key.equals("yoffset")) {
				yOffset = value;
			} else if(key.equals("xadvance")) {
				advance = value;
			}
		}
	}

	public int getASCII() {
		return (int) charCode;
	}
	
	public char getChar() {
		return charCode;
	}

	public int getTexCoordX() {
		return texCoordX;
	}

	public int getTexCoordY() {
		return texCoordY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getXOffset() {
		return xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}

	public int getAdvance() {
		return advance;
	}
	
}
