package com.github.yoshiapolis.cube.display;

import com.github.yoshiapolis.main.Main;
import com.github.yoshiapolis.puzzle.display.DisplayPiece;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceType;

import processing.core.PMatrix3D;

public class CubeDisplayPiece extends DisplayPiece {
	
	private static final int POS = 1;
	private static final int NEG = -1;
	private static final int FLIP = -2;
	private static final int NOFLIP = 2;
	
	private static final int X = 2;
	private static final int INV_X = -2;
	private static final int Y = 3;
	private static final int INV_Y = -3;
	
	private static final int[][] edgePositions = {
			{NOFLIP, NEG, POS}, {POS, NEG, FLIP}, {FLIP, NEG, NEG}, {NEG, NEG, NOFLIP},
			{NEG, FLIP, POS}, {POS, FLIP, POS}, {POS, FLIP, NEG}, {NEG, FLIP, NEG},
			{FLIP, POS, POS}, {POS, POS, NOFLIP}, {NOFLIP, POS, NEG}, {NEG, POS, FLIP}, 
	};
	
	private static final int[][] cornerPositions = {
			{NEG, NEG, POS}, {POS, NEG, POS}, {POS, NEG, NEG}, {NEG, NEG, NEG},
			{NEG, POS, POS}, {POS, POS, POS}, {POS, POS, NEG}, {NEG, POS, NEG},
	};
	
	private static final int[][] centerPositions = {
			{POS, Y, INV_X}, {X, NEG, Y}, {X, Y, POS},
			{NEG, Y, X}, {X, POS, INV_Y}, {INV_X, Y, NEG}
	};
	
	private float pieceSize;
	
	public CubeDisplayPiece(Piece position, float drawSize) {
		super(position, drawSize);
	}

	@Override
	protected void drawShape() {
		Main.app.box(pieceSize);
	}
	
	private void translateCorner(PMatrix3D translation, Piece position, float drawSize) {
		int piecePosition = position.getPosition();
		float positionOff = (drawSize - pieceSize) / 2;
		
		float[] coords = new float[3];
		for(int i = 0; i < 3; i ++) {
			int positionCode = cornerPositions[piecePosition][i];
			coords[i] = positionOff * positionCode;
		}
		
		System.out.println(coords[0] + " " + coords[1] + " " + coords[2]);
		translation.translate(coords[0], coords[1], coords[2]);
	}
	
	private void translateEdge(PMatrix3D translation, Piece position, float drawSize) {
		int piecePosition = position.getPosition();
		int index = position.getIndex();
		int edgeSize = position.getPuzzleSize() - 2;
		
		float positionOff = (drawSize - pieceSize) / 2;
		float edgeStart = (pieceSize - edgeSize * pieceSize) / 2;
		float indexOff = edgeStart + index * pieceSize;
		
		float[] coords = new float[3];
		for(int i = 0; i < 3; i ++) {
			int positionCode = edgePositions[piecePosition][i];
			
			if(positionCode == POS) {
				coords[i] = positionOff;
			} else if(positionCode == NEG) {
				coords[i] = -positionOff;
			} else if(positionCode == NOFLIP) { 
				coords[i] = indexOff;
			} else if(positionCode == FLIP) {
				coords[i] = -indexOff;
			}
		}
		
		translation.translate(coords[0], coords[1], coords[2]);
	}
	
	private void translateCenter(PMatrix3D translation, Piece position, float drawSize) {
		int piecePosition = position.getPosition();
		int edgeSize = position.getPuzzleSize() - 2;
		
		int index = position.getIndex();
		int xIndex = index % edgeSize;
		int yIndex = index / edgeSize;
		
		float positionOff = (drawSize - pieceSize) / 2;
		float centerStart = (pieceSize - edgeSize * pieceSize) / 2;
		
		float xOff = centerStart + pieceSize * xIndex;
		float yOff = centerStart + pieceSize * yIndex;
		
		float[] coords = new float[3];
		for(int i = 0; i < 3; i ++) {
			int positionCode = centerPositions[piecePosition][i];
			
			if(positionCode == POS) {
				coords[i] = positionOff;
			} else if(positionCode == NEG) {
				coords[i] = -positionOff;
			} else if(positionCode == X) {
				coords[i] = xOff;
			} else if(positionCode == INV_X) {
				coords[i] = -xOff;
			} else if(positionCode == Y) {
				coords[i] = yOff;
			} else if(positionCode == INV_Y) {
				coords[i] = -yOff;
			}
		}
		
		translation.translate(coords[0], coords[1], coords[2]);
	}
	
	@Override
	public void setPosition(Piece position, float drawSize) {
		this.pieceSize = drawSize / position.getPuzzleSize();
		PMatrix3D translation = new PMatrix3D();
		PieceType type = position.getType();
		
		if(type == PieceType.EDGE) {
			translateEdge(translation, position, drawSize);
		} else if(type == PieceType.CORNER) {
			translateCorner(translation, position, drawSize);
		} else if(type == PieceType.CENTER) {
			translateCenter(translation, position, drawSize);
		}
		
		super.setTranslationMat(translation);
	}

}
