package com.github.yoshiapolis.pyraminx.display;

import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.math.Matrix3D;
import com.github.yoshiapolis.puzzle.display.Colors;
import com.github.yoshiapolis.puzzle.display.DisplayPiece;
import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.pyraminx.pieces.Pyraminx;
import com.github.yoshiapolis.pyraminx.util.PyraminxCenterUtil;
import com.github.yoshiapolis.renderEngine.loaders.OBJLoader;
import com.github.yoshiapolis.renderEngine.models.ColoredModel;

public class PyraminxDisplayPiece extends DisplayPiece {

	private static final float SIDE_LENGTH = 2 / Mathf.sqrt(6);
	
	private static final float X_OFFSET = SIDE_LENGTH / 2;
	private static final float Y_OFFSET = 1.0f/6;
	private static final float Z_OFFSET = SIDE_LENGTH * Mathf.sqrt(3)/6;
	private static final float LAYER_HEIGHT = 2.0f/3;
	private static final float LAYER_DEPTH = SIDE_LENGTH * Mathf.sqrt(3)/2;
	
	public PyraminxDisplayPiece(Piece position) {
		super(position);
	}

	private boolean isUpFacing(Piece piece) {
		PieceType type = piece.getType();
		int index = piece.getIndex();
		
		if(type == PieceType.CORNER) {
			return (index % 2 == 1);
		} else if(type == PieceType.EDGE) {
			return (index % 2 == 0);
		} else {
			int size = piece.getPuzzleSize();
			int layer = 0;
			if(Pyraminx.getFace(piece) == Axis.PD) {
				layer = PyraminxCenterUtil.getLayer(piece, Axis.PF) - 1;
			} else {
				layer = PyraminxCenterUtil.getLayer(piece, Axis.PD) - 1;
			}
			int indexOff = piece.getIndex() - PyraminxCenterUtil.getIndexAtZPosition(layer, size - 3);
			return (indexOff % 2) == 0;
		}
	}
	
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
	
	private Matrix3D placePiece(Piece piece) {
		int size = piece.getPuzzleSize();
		int layer = PyraminxCenterUtil.getLayer(piece, Axis.PD) - 1;
		int indexOff = piece.getIndex() - PyraminxCenterUtil.getIndexAtZPosition(layer, size - 3);
		int centeredIndex = indexOff - PyraminxCenterUtil.getLayerSize(layer, size - 3)/2;

		if(isUpFacing(piece)) {
			return placeUpPiece(layer, centeredIndex, size);
		} else {
			return placeDownPiece(layer, centeredIndex, size);
		}
	
	}
	
	@Override
	public void setWorldPosition(Piece piece) {
		int position = piece.getPosition();
		int index = piece.getIndex();
		int puzzleSize = piece.getPuzzleSize();
		PieceType type = piece.getType();

		Matrix3D transform = new Matrix3D();
		boolean upFacing = isUpFacing(piece);
		int edgeSize = 2 * (puzzleSize - 3) + 1;
		if(type == PieceType.EDGE) {
			
			int centeredIndex = index - edgeSize/2;
			if(upFacing) {
				transform = placeUpPiece(-1, centeredIndex, puzzleSize);
			} else {
				transform = placeDownPiece(-1, centeredIndex, puzzleSize);
			}
			
			if(position == 4) {
				transform.rotateAroundAxis(Axis.PD.getRotationAxis(), 2*Mathf.PI/3);
			} else if(position == 5) {
				transform.rotateAroundAxis(Axis.PD.getRotationAxis(), -2*Mathf.PI/3);
			} else if(position == 0) {
				transform.rotateAroundAxis(Axis.PF.getRotationAxis(), -2*Mathf.PI/3);
			} else if(position == 1) {
				transform.rotateAroundAxis(Axis.PF.getRotationAxis(), -2*Mathf.PI/3);
				transform.rotateAroundAxis(Axis.PD.getRotationAxis(), 2*Mathf.PI/3);
			} else if(position == 2) {
				transform.rotateAroundAxis(Axis.PR.getRotationAxis(), 2*Mathf.PI/3);
			}
			
		} else if(type == PieceType.CORNER) {	
			
			int bottomLayers = puzzleSize / 3;
			transform.translateY(-bottomLayers * LAYER_HEIGHT);
			transform.translateZ(bottomLayers * LAYER_DEPTH);
			if(upFacing) {
				int centeredIndex = edgeSize/2 + 2;
				transform = placeUpPiece(-1, centeredIndex, puzzleSize);
			} else {
				int centeredIndex = edgeSize/2 + 1;
				transform = placeDownPiece(-1, centeredIndex, puzzleSize);
			}
			
			if(position == 0) {
				transform.rotateAroundAxis(Axis.PF.getRotationAxis(), -2*Mathf.PI/3);
			} else if(position == 3) {
				transform.rotateAroundAxis(Axis.PD.getRotationAxis(), -2*Mathf.PI/3);
			} else if(position == 2) {
				transform.rotateAroundAxis(Axis.PD.getRotationAxis(), 2*Mathf.PI/3);
			}
			
		} else if(type == PieceType.CENTER) {
			transform = placePiece(new Piece(PieceType.CENTER, 0, piece.getIndex(), puzzleSize));
			
			if(position == 1) {
				transform.rotateAroundAxis(Axis.PD.getRotationAxis(), 2*Mathf.PI/3);
			} else if(position == 2) {
				transform.rotateAroundAxis(Axis.PD.getRotationAxis(), -2*Mathf.PI/3);
			} else if(position == 3) {
				transform.rotateAroundAxis(Axis.PR.getRotationAxis(), -2*Mathf.PI/3);
				transform.rotateAroundAxis(Axis.PD.getRotationAxis(), 2*Mathf.PI/3);
			}
		}
		
		super.setTransformationMat(transform);
	}

}
