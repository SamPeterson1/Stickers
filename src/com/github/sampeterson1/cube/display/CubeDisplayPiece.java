package com.github.sampeterson1.cube.display;

import java.util.EnumMap;
import java.util.Map;

import com.github.sampeterson1.cube.pieces.Cube;
import com.github.sampeterson1.cube.util.CubeEdgeUtil;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.display.Colors;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.renderEngine.loaders.OBJLoader;
import com.github.sampeterson1.renderEngine.models.ColoredModel;

public class CubeDisplayPiece extends DisplayPiece {
	
	private static final float CUBE_DRAW_SIZE = 20;
	
	private static final int POS = 0; //place object in the positive direction
	private static final int NEG = 1; //place object in the negative direction
	private static final int FLIP = 2; //reverse the indices of an edge
	private static final int NOFLIP = 3; //keep indices as they are
	
	private static final int X = 4; //positive x axis 
	private static final int INV_X = 5; //negative x axis
	private static final int Y = 6; //positive y axis
	private static final int INV_Y = 7; //negative y axis
	
	/*
	 * Defines how to place each edge
	 * 
	 * Each edge is represented by {x, y, z}, where x, y, and z determine 
	 * how to place each piece on the x, y, and z axis respectively. The index of
	 * each 3-element array aligns with the position attribute of each edge in the "Piece" object
	 * 
	 * For example, consider the array {FLIP, NEG, POS}
	 * FLIP (x): place the edge parallel to the x-axis and reverse its indices
	 * POS (y): place the edge such that its y coordinate is at the top of the cube
	 * NEG (z): place the edge such that its z coordinate is at the back of the cube
	 */
	private static final int[][] edgePositions = {
			{NOFLIP, POS, POS}, {POS, POS, FLIP}, {FLIP, POS, NEG}, {NEG, POS, NOFLIP},
			{NEG, NOFLIP, POS}, {POS, NOFLIP, POS}, {POS, NOFLIP, NEG}, {NEG, NOFLIP, NEG},
			{FLIP, NEG, POS}, {POS, NEG, NOFLIP}, {NOFLIP, NEG, NEG}, {NEG, NEG, FLIP}, 
	};
	
	/*
	 * Defines how to place each corner
	 * Each corner is similarly represented by {x, y, z}, where POS and NEG have the same effect,
	 * and the index of each array aligns with the position attribute of each corner
	 */
	private static final int[][] cornerPositions = {
			{NEG, POS, POS}, {POS, POS, POS}, {POS, POS, NEG}, {NEG, POS, NEG},
			{NEG, NEG, POS}, {POS, NEG, POS}, {POS, NEG, NEG}, {NEG, NEG, NEG},
	};
	
	/*
	 * Defines how to place each center
	 * This array uses a similar system and follows the rules stated above.
	 * However, the values {X, Y, INV_X, INV_Y} are also required to place each individual piece.
	 * 
	 * Consider the array {X, NEG, INV_Y}:
	 * X (x): the x coordinate of a center piece's "index" attribute aligns with the cube's positive x-axis
	 * NEG (y): place the center such that its y coordinate is at the bottom of the cube
	 * INV_Y (z): the y coordinate of a center piece's "index" attribute aligns with the cube's negative z-axis
	 */
	private static final int[][] centerPositions = {
			{POS, INV_Y, INV_X}, {X, POS, Y}, {X, INV_Y, POS},
			{NEG, INV_Y, X}, {X, NEG, INV_Y}, {INV_X, INV_Y, NEG}
	};
	
	//For each corner, define the three faces that are visible
	private static final Axis[][] exposedCornerFaces = {
			{Axis.L, Axis.U, Axis.F},
			{Axis.F, Axis.U, Axis.R},
			{Axis.R, Axis.U, Axis.B},
			{Axis.B, Axis.U, Axis.L},
			{Axis.L, Axis.D, Axis.F},
			{Axis.F, Axis.D, Axis.R},
			{Axis.R, Axis.D, Axis.B},
			{Axis.B, Axis.D, Axis.L}
	};
	
	//The names of each face on the cube model
	private static final Map<Axis, String> faceNames = initFaceNames();
	private static final Map<Axis, Vector3f> colorScheme = initColorScheme();
	
	private float pieceSize;
	
	private static final Map<Axis, Vector3f> initColorScheme() {
		Map<Axis, Vector3f> colors = new EnumMap<Axis, Vector3f>(Axis.class);
		colors.put(Axis.R, Colors.RED);
		colors.put(Axis.U, Colors.WHITE);
		colors.put(Axis.F, Colors.GREEN);
		colors.put(Axis.L, Colors.ORANGE);
		colors.put(Axis.D, Colors.YELLOW);
		colors.put(Axis.B, Colors.BLUE);
		
		return colors;
	}
	
	private static final Map<Axis, String> initFaceNames() {
		Map<Axis, String> names = new EnumMap<Axis, String>(Axis.class);
		names.put(Axis.R, "XPos");
		names.put(Axis.U, "YPos");
		names.put(Axis.F, "ZPos");
		names.put(Axis.L, "XNeg");
		names.put(Axis.D, "YNeg");
		names.put(Axis.B, "ZNeg");
		
		return names;
	}
	
	public CubeDisplayPiece(Piece position) {
		super(position);
	}
	
	//Return a matrix representing the world position of a corner piece
	private Matrix3D getCornerPosition(Piece piece) {
		int piecePosition = piece.getPosition();
		float positionOff = (CUBE_DRAW_SIZE - pieceSize) / 2;
		
		float[] coords = new float[3];
		for(int i = 0; i < 3; i ++) {
			int positionCode = cornerPositions[piecePosition][i];
			
			if(positionCode == POS) {
				coords[i] = positionOff;
			} else if(positionCode == NEG) {
				coords[i] = -positionOff;
			}
		}
		
		Matrix3D translation = new Matrix3D();
		translation.translate(coords[0], coords[1], coords[2]);
		
		return translation;
	}
	
	//Return a matrix representing the world position of an edge piece
	private Matrix3D getEdgePosition(Piece piece) {
		int piecePosition = piece.getPosition();
		int index = piece.getIndex();
		int edgeSize = piece.getPuzzleSize() - 2;
		
		float positionOff = (CUBE_DRAW_SIZE - pieceSize) / 2;
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
		
		Matrix3D translation = new Matrix3D();
		translation.translate(coords[0], coords[1], coords[2]);
		
		return translation;
	}
	
	//Return a matrix representing the world position of a center piece
	private Matrix3D getCenterPosition(Piece piece) {
		int piecePosition = piece.getPosition();
		int edgeSize = piece.getPuzzleSize() - 2;
		
		int index = piece.getIndex();
		int xIndex = index % edgeSize;
		int yIndex = index / edgeSize;
		
		float positionOff = (CUBE_DRAW_SIZE - pieceSize) / 2;
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
		
		Matrix3D translation = new Matrix3D();
		translation.translate(coords[0], coords[1], coords[2]);
		
		return translation;
	}
	
	//Color one face of a model
	private void setModelColor(ColoredModel model, Axis face) {
		String faceName = faceNames.get(face);
		Vector3f color = colorScheme.get(face);
		model.setColor(faceName, color);
	}
	
	private void setCornerColor(Piece piece, ColoredModel model) {
		for(int i = 0; i < 3; i ++) {
			Axis face = exposedCornerFaces[piece.getPosition()][i];
			setModelColor(model, face);
		}
	}
	
	private void setEdgeColor(Piece piece, ColoredModel model) {
		Axis face1 = CubeEdgeUtil.getFace(piece.getPosition(), 0);
		Axis face2 = CubeEdgeUtil.getFace(piece.getPosition(), 1);
		
		setModelColor(model, face1);
		setModelColor(model, face2);
	}
	
	private void setCenterColor(Piece piece, ColoredModel model) {
		Axis face = Cube.getFace(piece.getPosition());
		setModelColor(model, face);
	}
	
	@Override
	public void setWorldPosition(Piece piece) {
		pieceSize = CUBE_DRAW_SIZE / piece.getPuzzleSize();
		Matrix3D transformation = new Matrix3D();
		transformation.scale(pieceSize / 2);
		PieceType type = piece.getType();
		
		if(type == PieceType.EDGE) {
			transformation.multiply(getEdgePosition(piece));
		} else if(type == PieceType.CORNER) {
			transformation.multiply(getCornerPosition(piece));
		} else if(type == PieceType.CENTER) {
			transformation.multiply(getCenterPosition(piece));
		}
		
		super.setTransformationMat(transformation);
	}

	@Override
	protected ColoredModel loadModel(Piece piece) {
		ColoredModel model = OBJLoader.loadColoredModel("CubePiece.obj");
		
		//model.setAccentColor(new Vector3f(0, 0, 0));
		//model.setBaseColor(Colors.PINK);
		PieceType type = piece.getType();
		
		if(type == PieceType.CENTER) {
			setCenterColor(piece, model);
		} else if(type == PieceType.EDGE) {
			setEdgeColor(piece, model);
		} else if(type == PieceType.CORNER) {
			setCornerColor(piece, model);
		}
		
		return model;
	}

}
