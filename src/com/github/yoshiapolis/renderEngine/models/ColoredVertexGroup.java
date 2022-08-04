package com.github.yoshiapolis.renderEngine.models;

import java.util.List;

public class ColoredVertexGroup {
	
	private String name;
	private int baseColor;
	private int accentColor;
	private int[] baseIndices;
	private int[] accentIndices;
	
	public ColoredVertexGroup(String name, int[] baseIndices, int[] accentIndices) {
		this.name = name;
		this.baseIndices = baseIndices;
		this.accentIndices = accentIndices;
	}
	
	public int[] getBaseIndices() {
		return this.baseIndices;
	}
	
	public int[] getAccentIndices() {
		return this.accentIndices;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getBaseColor() {
		return this.baseColor;
	}
	
	public int getAccentColor() {
		return this.accentColor;
	}

	public void setBaseColor(int baseColor) {
		this.baseColor = baseColor;
	}

	public void setAccentColor(int accentColor) {
		this.accentColor = accentColor;
	}
}
