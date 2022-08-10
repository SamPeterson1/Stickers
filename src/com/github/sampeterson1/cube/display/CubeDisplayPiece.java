package com.github.sampeterson1.cube.display;

import com.github.sampeterson1.cube.util.CubeAlgorithmUtil;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.display.Colors;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.renderEngine.loaders.OBJLoader;
import com.github.sampeterson1.renderEngine.models.ColoredModel;

//An implementation of DisplayPiece that represents a piece on a cube
public class CubeDisplayPiece extends DisplayPiece {
	
	private static final float CUBE_DRAW_SIZE = 20;
	
	/*
	 * These algorithms take pieces at their "origin positions" (see getOrigin methods below)
	 * and move them to their correct positions and orientations
	 */
	private static final Algorithm[] cornerRotations = initCornerRotations();
	private static final Algorithm[] edgeRotations = initEdgeRotations();
	private static final Algorithm[] centerRotations = initCenterRotations();

	private float pieceSize;
	
	private static final Algorithm[] initCornerRotations() {
		Algorithm[] cornerRotations = new Algorithm[8];
		
		cornerRotations[0] = CubeAlgorithmUtil.parseAlgorithm("R F'");
		cornerRotations[1] = CubeAlgorithmUtil.parseAlgorithm("F'");
		cornerRotations[2] = CubeAlgorithmUtil.parseAlgorithm("F' U'");
		cornerRotations[3] = CubeAlgorithmUtil.parseAlgorithm("F' U2");
		cornerRotations[4] = CubeAlgorithmUtil.parseAlgorithm("D'");
		cornerRotations[5] = CubeAlgorithmUtil.parseAlgorithm("");
		cornerRotations[6] = CubeAlgorithmUtil.parseAlgorithm("D");
		cornerRotations[7] = CubeAlgorithmUtil.parseAlgorithm("D2");

		return cornerRotations;
	}
	
	private static final Algorithm[] initEdgeRotations() {
		Algorithm[] edgeRotations = new Algorithm[12];
		
		edgeRotations[0] = CubeAlgorithmUtil.parseAlgorithm("F2");
		edgeRotations[1] = CubeAlgorithmUtil.parseAlgorithm("F2 U'");
		edgeRotations[2] = CubeAlgorithmUtil.parseAlgorithm("F2 U2");
		edgeRotations[3] = CubeAlgorithmUtil.parseAlgorithm("F2 U");
		edgeRotations[4] = CubeAlgorithmUtil.parseAlgorithm("F");
		edgeRotations[5] = CubeAlgorithmUtil.parseAlgorithm("F U'");
		edgeRotations[6] = CubeAlgorithmUtil.parseAlgorithm("F U2");
		edgeRotations[7] = CubeAlgorithmUtil.parseAlgorithm("F U");
		edgeRotations[8] = CubeAlgorithmUtil.parseAlgorithm("");
		edgeRotations[9] = CubeAlgorithmUtil.parseAlgorithm("D");
		edgeRotations[10] = CubeAlgorithmUtil.parseAlgorithm("D2");
		edgeRotations[11] = CubeAlgorithmUtil.parseAlgorithm("D'");
		
		return edgeRotations;
	}
	
	private static final Algorithm[] initCenterRotations() {
		Algorithm[] centerRotations = new Algorithm[6];
		
		centerRotations[0] = CubeAlgorithmUtil.parseAlgorithm("U'");
		centerRotations[1] = CubeAlgorithmUtil.parseAlgorithm("R");
		centerRotations[2] = CubeAlgorithmUtil.parseAlgorithm("");
		centerRotations[3] = CubeAlgorithmUtil.parseAlgorithm("U");
		centerRotations[4] = CubeAlgorithmUtil.parseAlgorithm("R'");
		centerRotations[5] = CubeAlgorithmUtil.parseAlgorithm("U2");
		
		return centerRotations;
	}
	
	public CubeDisplayPiece(Piece position) {
		super(position);
	}
	
	/*
	 * Return a matrix representing the world position of a corner piece at
	 * the bottom right corner of the front face. This position is the "origin"
	 * at which every corner piece will be placed initially.
	 */
	private Matrix3D getCornerOrigin() {
		float positionCoord = (CUBE_DRAW_SIZE - pieceSize) / 2;
		
		Matrix3D translation = new Matrix3D();
		translation.translate(positionCoord, -positionCoord, positionCoord);
		
		return translation;
	}
	
	/*
	 * Return a matrix representing the world position of an edge piece at
	 * the bottom edge of the front face. This position is the "origin"
	 * at which every edge piece will be placed initially.
	 */
	private Matrix3D getEdgeOrigin(Piece piece) {
		int edgeSize = piece.getPuzzleSize() - 2;
		int index = piece.getIndex();
		
		//the distance from the center of the cube to the center of the edge piece
		float displacement = (CUBE_DRAW_SIZE - pieceSize) / 2;
		
		//the x position of an edge piece with index 0
		float edgeXOrigin = -(pieceSize - edgeSize * pieceSize) / 2;
		
		float xPos = edgeXOrigin - index * pieceSize;

		Matrix3D translation = new Matrix3D();
		translation.translate(xPos, -displacement, displacement);
		
		return translation;
	}
	
	/*
	 * Return a matrix representing the world position of a center piece on the front face.
	 * This position is the "origin" at which every center piece will be placed initially.
	 */
	private Matrix3D getCenterOrigin(Piece piece) {
		int centerSize = piece.getPuzzleSize() - 2;
		
		//split the 1-dimensional center index into an x and y index
		int index = piece.getIndex();
		int xIndex = index % centerSize;
		int yIndex = index / centerSize;
		
		//the distance from the center of the cube to the center of the center piece
		float displacement = (CUBE_DRAW_SIZE - pieceSize) / 2;
		
		//the position of a center piece with index 0
		float centerStart = (pieceSize - centerSize * pieceSize) / 2;
		
		//use the x and y indices to calculate the position of the piece
		float xCoord = centerStart + pieceSize * xIndex;
		float yCoord = -centerStart - pieceSize * yIndex;
		
		Matrix3D translation = new Matrix3D();
		translation.translate(xCoord, yCoord, displacement);
		
		return translation;
	}
	
	//Apply the rotation algorithms into a rotation matrix
	private Matrix3D getPieceRotation(Piece piece) {
		PieceType type = piece.getType();
		int position = piece.getPosition();

		Algorithm alg = null;
		if(type == PieceType.CENTER) {
			alg = centerRotations[position];
		} else if(type == PieceType.EDGE) {
			alg = edgeRotations[position];
		} else if(type == PieceType.CORNER) {
			alg = cornerRotations[position];
		}
		
		return Algorithm.getRotationFromAlgorithm(alg);
	}
	
	@Override
	public void setWorldPosition(Piece piece) {
		pieceSize = CUBE_DRAW_SIZE / piece.getPuzzleSize();
		Matrix3D transformation = new Matrix3D();
		transformation.scale(pieceSize / 2);
		PieceType type = piece.getType();
		
		if(type == PieceType.EDGE) {
			transformation.multiply(getEdgeOrigin(piece));
		} else if(type == PieceType.CORNER) {
			transformation.multiply(getCornerOrigin());
		} else if(type == PieceType.CENTER) {
			transformation.multiply(getCenterOrigin(piece));
		}
		
		transformation.multiply(getPieceRotation(piece));
		
		super.setTransformationMat(transformation);
	}
	
	@Override
	protected ColoredModel loadModel(Piece piece) {
		ColoredModel model = null;
		
		if(piece.getType() == PieceType.CORNER) {
			model = OBJLoader.loadColoredModel("cube/Corner.obj");
			model.setColor("Front", Colors.convertColor(piece.getColor(0)));
			model.setColor("Right", Colors.convertColor(piece.getColor(1)));
			model.setColor("Bottom", Colors.convertColor(piece.getColor(2)));
		} else if(piece.getType() == PieceType.EDGE) {
			model = OBJLoader.loadColoredModel("cube/Edge.obj");
			model.setColor("Bottom", Colors.convertColor(piece.getColor(0)));
			model.setColor("Front", Colors.convertColor(piece.getColor(1)));
		} else if(piece.getType() == PieceType.CENTER) {
			model = OBJLoader.loadColoredModel("cube/Center.obj");
			model.setColor("Front", Colors.convertColor(piece.getColor(0)));
		}

		return model;
	}

}
