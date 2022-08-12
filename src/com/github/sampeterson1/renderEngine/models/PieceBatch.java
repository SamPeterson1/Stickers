package com.github.sampeterson1.renderEngine.models;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.github.sampeterson1.puzzle.display.ColorPalette;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.renderEngine.loaders.Loader;

public class PieceBatch {

	private static final int INSTANCE_FLOAT_DATA_LENGTH = 16;
	private static final int INSTANCE_INT_DATA_LENGTH = 4;
	
	private ColorPalette palette;
	private ColoredMesh mesh;
	
	private List<DisplayPiece> pieces;
	private boolean created;
	
	private FloatBuffer positionData;
	private IntBuffer colorData;
	
	private int positionVboID;
	private int colorVboID;
	
	public PieceBatch(ColoredMesh mesh, ColorPalette palette) {
		this.mesh = mesh;
		this.palette = palette;
		this.pieces = new ArrayList<DisplayPiece>();
	}
	
	public void create() {
		int floatBytes = INSTANCE_FLOAT_DATA_LENGTH * pieces.size() * 4;	
		int intBytes = INSTANCE_INT_DATA_LENGTH * pieces.size() * 4;
		
		this.positionVboID = Loader.createDynamicVBO(floatBytes);
		this.colorVboID = Loader.createDynamicVBO(intBytes);
		
		this.positionData = BufferUtils.createFloatBuffer(pieces.size() * INSTANCE_FLOAT_DATA_LENGTH);
		this.colorData = BufferUtils.createIntBuffer(pieces.size() * INSTANCE_INT_DATA_LENGTH);

		int vaoID = mesh.getData().getVaoID();
		Loader.addInstancedAttribute(positionVboID, vaoID, 3, 4, INSTANCE_FLOAT_DATA_LENGTH, 0);
		Loader.addInstancedAttribute(positionVboID, vaoID, 4, 4, INSTANCE_FLOAT_DATA_LENGTH, 4);
		Loader.addInstancedAttribute(positionVboID, vaoID, 5, 4, INSTANCE_FLOAT_DATA_LENGTH, 8);
		Loader.addInstancedAttribute(positionVboID, vaoID, 6, 4, INSTANCE_FLOAT_DATA_LENGTH, 12);
		
		Loader.addInstancedAttributeI(colorVboID, vaoID, 7, 4, INSTANCE_INT_DATA_LENGTH, 0);
		
		created = true;
	}

	public void addPiece(DisplayPiece piece) {
		if(!created) {
			piece.setPieceBatch(this);
			pieces.add(piece);
		} else {
			System.err.println("Cannot add piece after creation!");
		}
	}
	
	public void prepare() {
		positionData.clear();
		for(DisplayPiece piece : pieces) {
			piece.getTransform().store(positionData);
		}
		positionData.flip();
		
		Loader.updateVBO(positionVboID, positionData);
		
		colorData.clear();
		for(DisplayPiece piece : pieces) {
			colorData.put(piece.getColorIDs());
		}
		colorData.flip();
		
		Loader.updateVBO(colorVboID, colorData);
	}
	
	public ColorPalette getPalette() {
		return this.palette;
	}
	
	public ColoredMesh getMesh() {
		return this.mesh;
	}
	
	public int getNumInstances() {
		return pieces.size();
	}

}
