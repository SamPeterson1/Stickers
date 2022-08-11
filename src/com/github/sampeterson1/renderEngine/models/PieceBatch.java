package com.github.sampeterson1.renderEngine.models;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.github.sampeterson1.puzzle.display.DisplayPiece;

public class PieceBatch extends InstancedMeshBatch {

	private static final int INSTANCE_DATA_LENGTH = 16;
	
	private List<DisplayPiece> pieces;
	private FloatBuffer pieceData;
	private boolean created;
	
	public PieceBatch(Mesh mesh) {
		super(mesh);
		this.pieces = new ArrayList<DisplayPiece>();
	}
	
	public void create() {
		super.createInstances(INSTANCE_DATA_LENGTH, pieces.size());
		this.pieceData = BufferUtils.createFloatBuffer(pieces.size() * INSTANCE_DATA_LENGTH);

		super.addInstancedAttribute(3, 4, 0);
		super.addInstancedAttribute(4, 4, 4);
		super.addInstancedAttribute(5, 4, 8);
		super.addInstancedAttribute(6, 4, 12);
		
		created = true;
	}

	public void addPiece(DisplayPiece piece) {
		if(!created) {
			pieces.add(piece);
		} else {
			System.err.println("Cannot add piece after creation!");
		}
	}
	
	public void prepare() {
		pieceData.clear();
		for(DisplayPiece piece : pieces) {
			piece.getTransform().store(pieceData);
		}
		pieceData.flip();
		
		super.updateInstanceData(pieceData);
	}
}
