package com.yoshiapolis.puzzle;

public class PuzzlePiece {
	
	private int index;
	private int position;
	private Color[] colors;
	private PieceType type;
	private boolean solved;
	
	public PuzzlePiece(PieceType type, int position, int index, int numColors) {
		this.type = type;
		this.position = position;
		this.index = index;
		this.colors = new Color[numColors];
	}
	
	public boolean isSolved() {
		return this.solved;
	}
	
	public PieceType getType() {
		return this.type;
	}
	
	public int indexOfColor(Color c) {
		for(int i = 0; i < colors.length; i ++) {
			if(c == colors[i]) return i;
 		}
		return -1;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public Color getColor() {
		return this.colors[0];
	}
	
	public Color getColor(int index) {
		return this.colors[index];
	}
	
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public void setColor(int index, Color color) {
		this.colors[index] = color;
	}
	
	public void setColor(Color color) {
		this.colors[0] = color;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{" + colors[0]);
		for(int i = 1; i < colors.length; i ++) {
			sb.append(", " + colors[i]);
		}
		sb.append("}");
		
		return sb.toString();
	}

}
