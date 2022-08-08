package com.github.sampeterson1.pyraminx.display;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.display.Colors;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.pyraminx.util.PyraminxAlgorithmUtil;
import com.github.sampeterson1.pyraminx.util.PyraminxCenterUtil;
import com.github.sampeterson1.renderEngine.loaders.OBJLoader;
import com.github.sampeterson1.renderEngine.models.ColoredModel;

public class PyraminxDisplayPiece extends DisplayPiece {

	private static final float SIDE_LENGTH = 2 / Mathf.sqrt(6);
	private static final float X_OFFSET = SIDE_LENGTH / 2;
	private static final float Y_OFFSET = 1.0f/6;
	private static final float Z_OFFSET = SIDE_LENGTH * Mathf.sqrt(3)/6;
	private static final float LAYER_HEIGHT = 2.0f/3;
	private static final float LAYER_DEPTH = SIDE_LENGTH * Mathf.sqrt(3)/2;
	
	private static final Algorithm[] cornerRotationAlgs = initCornerRotationAlgs();
	private static final Algorithm[] edgeRotationAlgs = initEdgeRotationAlgs();
	private static final Algorithm[] centerRotationAlgs = initCenterRotationAlgs();
	
	private static Algorithm[] initCornerRotationAlgs() {
		Algorithm[] algs = new Algorithm[4];
		
		algs[0] = PyraminxAlgorithmUtil.parseAlgorithm("F'");
		algs[1] = PyraminxAlgorithmUtil.parseAlgorithm("");
		algs[2] = PyraminxAlgorithmUtil.parseAlgorithm("D");
		algs[3] = PyraminxAlgorithmUtil.parseAlgorithm("D'");
		
		return algs;
	}
	
	private static Algorithm[] initEdgeRotationAlgs() {
		Algorithm[] algs = new Algorithm[6];
		
		algs[0] = PyraminxAlgorithmUtil.parseAlgorithm("F'");
		algs[1] = PyraminxAlgorithmUtil.parseAlgorithm("F' D");
		algs[2] = PyraminxAlgorithmUtil.parseAlgorithm("F' D'");
		algs[3] = PyraminxAlgorithmUtil.parseAlgorithm("");
		algs[4] = PyraminxAlgorithmUtil.parseAlgorithm("D");
		algs[5] = PyraminxAlgorithmUtil.parseAlgorithm("D'");

		return algs;
	}
	
	
	private static Algorithm[] initCenterRotationAlgs() {
		Algorithm[] algs = new Algorithm[4];
		
		algs[0] = PyraminxAlgorithmUtil.parseAlgorithm("");
		algs[1] = PyraminxAlgorithmUtil.parseAlgorithm("D");
		algs[2] = PyraminxAlgorithmUtil.parseAlgorithm("D'");
		algs[3] = PyraminxAlgorithmUtil.parseAlgorithm("L D'");
		
		return algs;
	}
	
	
	public PyraminxDisplayPiece(Piece position) {
		super(position);
	}

	/*
	 * Given a piece, determine if it should use the up facing piece model
	 */
	private boolean isUpFacing(Piece piece) {
		PieceType type = piece.getType();
		int index = piece.getIndex();
		
		if(type == PieceType.CORNER) {
			return (index % 2 == 1);
		} else if(type == PieceType.EDGE) {
			return (index % 2 == 0);
		} else {
			int size = piece.getPuzzleSize();
			int layer = PyraminxCenterUtil.getZPosition(piece.getIndex(), size - 3);
			int indexOff = piece.getIndex() - PyraminxCenterUtil.getIndexAtZPosition(layer, size - 3);
			return (indexOff % 2) == 0;
		}
	}
	
	private Matrix3D placeUpPiece(int layer, int centeredIndex, int puzzleSize) {
		Matrix3D transform = new Matrix3D();

		transform.translateY((layer - puzzleSize/3 + 1) * LAYER_HEIGHT);
		transform.translateZ(-(layer - puzzleSize + 1) * LAYER_DEPTH / 3);
		if(puzzleSize % 3 == 0) {
			transform.translateX(centeredIndex * X_OFFSET);
			transform.translateZ(-Z_OFFSET);
			transform.translateY((puzzleSize/3 + 1) * Y_OFFSET);
		} else if(puzzleSize % 3 == 1) {
			transform.translateX(centeredIndex * X_OFFSET);
			transform.translateY((puzzleSize/3) * Y_OFFSET);
		} else if(puzzleSize % 3 == 2) {
			transform.translateX(centeredIndex * X_OFFSET);
			transform.translateZ(-Z_OFFSET);
			transform.translateY((puzzleSize/3 - 1) * Y_OFFSET);
		}

		return transform;
	}
	
	private Matrix3D placeDownPiece(int layer, int centeredIndex, int puzzleSize) {
		Matrix3D transform = new Matrix3D();

		transform.translateY((layer - puzzleSize/3 + 1) * LAYER_HEIGHT);
		transform.translateZ(-(layer - puzzleSize + 1) * LAYER_DEPTH / 3);

		if(puzzleSize % 3 == 0) {
			transform.translateX(centeredIndex * X_OFFSET);
			transform.translateZ(-2*Z_OFFSET);
			transform.translateY((puzzleSize/3 + 2) * Y_OFFSET);
		} else if(puzzleSize % 3 == 1) {
			transform.translateX(centeredIndex * X_OFFSET);
			transform.translateZ(-Z_OFFSET);
			transform.translateY((puzzleSize/3 + 1) * Y_OFFSET);
		} else if(puzzleSize % 3 == 2) {
			transform.translateX(centeredIndex * X_OFFSET);
			transform.translateZ(-2*Z_OFFSET);
			transform.translateY((puzzleSize/3) * Y_OFFSET);
		}
	
		return transform;
	}
	
	private Matrix3D getCenterOrigin(Piece piece) {
		int size = piece.getPuzzleSize();
		int layer = PyraminxCenterUtil.getZPosition(piece.getIndex(), size - 3);
		int indexOff = piece.getIndex() - PyraminxCenterUtil.getIndexAtZPosition(layer, size - 3);
		int centeredIndex = indexOff - PyraminxCenterUtil.getLayerSize(layer, size - 3)/2;

		if(isUpFacing(piece)) {
			return placeUpPiece(layer, centeredIndex, size);
		} else {
			return placeDownPiece(layer, centeredIndex, size);
		}
	
	}
	
	private Matrix3D getCornerOrigin(Piece piece) {
		Matrix3D transform = new Matrix3D();
		int puzzleSize = piece.getPuzzleSize();
		int edgeSize = 2 * (puzzleSize - 3) + 1;
		int bottomLayers = puzzleSize / 3;
		
		transform.translateY(-bottomLayers * LAYER_HEIGHT);
		transform.translateZ(bottomLayers * LAYER_DEPTH);
		if(isUpFacing(piece)) {
			int centeredIndex = edgeSize/2 + 2;
			transform = placeUpPiece(-1, centeredIndex, puzzleSize);
		} else {
			int centeredIndex = edgeSize/2 + 1;
			transform = placeDownPiece(-1, centeredIndex, puzzleSize);
		}
		
		return transform;
	}
	
	private Matrix3D getEdgeOrigin(Piece piece) {
		int index = piece.getIndex();
		int puzzleSize = piece.getPuzzleSize();
		int edgeSize = 2 * (puzzleSize - 3) + 1;
		int centeredIndex = index - edgeSize/2;
		
		if(isUpFacing(piece)) {
			return placeUpPiece(-1, centeredIndex, puzzleSize);
		} else {
			return placeDownPiece(-1, centeredIndex, puzzleSize);
		}
	}
	
	private Matrix3D getPieceRotation(Piece piece) {
		PieceType type = piece.getType();
		int position = piece.getPosition();
		Algorithm alg = null;
		
		if(type == PieceType.CENTER) {
			alg = centerRotationAlgs[position];
		} else if(type == PieceType.EDGE) {
			alg = edgeRotationAlgs[position];
		} else if(type == PieceType.CORNER) {
			alg = cornerRotationAlgs[position];
		}
		
		if(alg == null) return new Matrix3D();
		return Algorithm.getRotationFromAlgorithm(alg);
	}
	
	@Override
	public void setWorldPosition(Piece piece) {
		PieceType type = piece.getType();
		Matrix3D transform = new Matrix3D();
		
		if(type == PieceType.EDGE) {
			transform = getEdgeOrigin(piece);
		} else if(type == PieceType.CORNER) {	
			transform = getCornerOrigin(piece);
		} else if(type == PieceType.CENTER) {
			transform = getCenterOrigin(piece);
		}
		
		transform.multiply(getPieceRotation(piece));
		super.setTransformationMat(transform);
	}
	
	/*
	 * See DisplayPiece
	 */
	@Override
	protected ColoredModel loadModel(Piece piece) {
		ColoredModel model = null;
		if(isUpFacing(piece)) {
			model = OBJLoader.loadColoredModel("PyraminxUp.obj");
		} else {
			model = OBJLoader.loadColoredModel("PyraminxDown.obj");
		}
		
		model.setColor("Border", Colors.WHITE);
		PieceType type = piece.getType();
		if(type == PieceType.CENTER) {
			model.setColor("Front", Colors.convertColor(piece.getColor()));
		} else if(type == PieceType.EDGE) {
			model.setColor("Front", Colors.convertColor(piece.getColor(0)));
			model.setColor("Bottom", Colors.convertColor(piece.getColor(1)));
		} else if(type == PieceType.CORNER) {
			model.setColor("Front", Colors.convertColor(piece.getColor(0)));
			model.setColor("Bottom", Colors.convertColor(piece.getColor(1)));
			model.setColor("BackRight", Colors.convertColor(piece.getColor(2)));
		}
		
		return model;
	}
	
}
