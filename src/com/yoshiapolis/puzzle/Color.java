package com.yoshiapolis.puzzle;

import processing.core.PVector;

public enum Color {
	
	RED(255, 0, 0), GREEN(0, 255, 0), BLUE(0, 0, 255), 
	YELLOW(255, 255, 0), ORANGE(255, 125, 0), WHITE(255, 255, 255),
	PURPLE(153, 0, 255), LIME_GREEN(150, 255, 97), PINK(255, 0, 255),
	PALE_YELLOW(255, 255, 133), LIGHT_BLUE(0, 179, 255), GRAY(150, 150, 150);
	
	PVector rgb;
	
	Color(int r, int g, int b) {
		this.rgb = new PVector(r, g, b);
	}
	
	public PVector getRGB() {
		return this.rgb;
	}
	
}
