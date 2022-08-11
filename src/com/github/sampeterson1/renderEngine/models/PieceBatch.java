package com.github.sampeterson1.renderEngine.models;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.github.sampeterson1.puzzle.display.DisplayPiece;

public class PieceBatch extends InstancedMeshBatch {

	private static final int INSTANCE_DATA_LENGTH = 16;
	
	private int indexPtr = 0;
	private DisplayPiece[] pieces;
	
	private FloatBuffer pieceData;
	
	public PieceBatch(int numInstances) {
		super(INSTANCE_DATA_LENGTH, numInstances);
		this.pieces = new DisplayPiece[numInstances];
		this.pieceData = BufferUtils.createFloatBuffer(numInstances * INSTANCE_DATA_LENGTH);
	}

	public void addPiece(DisplayPiece piece) {
		pieces[indexPtr++] = piece;
	}
	
	public void prepare() {
		pieceData.clear();
		for(DisplayPiece piece : pieces) {
			piece.getTransform().store(pieceData);
		}
		
		super.updateInstanceData(pieceData);
	}
}
