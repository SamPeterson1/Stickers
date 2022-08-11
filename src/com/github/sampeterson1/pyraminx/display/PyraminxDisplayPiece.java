/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.pyraminx.display;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.display.Colors;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.pyraminx.util.PyraminxAlgorithmUtil;
import com.github.sampeterson1.pyraminx.util.PyraminxCenterUtil;
import com.github.sampeterson1.renderEngine.loaders.OBJLoader;
import com.github.sampeterson1.renderEngine.models.ColoredMesh;

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
	
	private static ColoredMesh upPieceMesh;
	private static ColoredMesh downPieceMesh;
	
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
	
	private static void loadMeshes() {
		upPieceMesh = OBJLoader.loadColoredMesh("PyraminxUp.obj");
		downPieceMesh = OBJLoader.loadColoredMesh("PyraminxDown.obj");
	}
	
	public PyraminxDisplayPiece(Piece position) {
		super(position);
		if(upPieceMesh == null)
			loadMeshes();
	}

	//Given a piece, determine if we should use the up facing piece model
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
			//calculate the index relative to the layer that the piece is in
			int indexOff = piece.getIndex() - PyraminxCenterUtil.getIndexAtZPosition(layer, size - 3);
			return (indexOff % 2) == 0;
		}
	}
	
	//Return the position of an up facing piece given its layer, index, and the size of the puzzle
	private Matrix3D placeUpPiece(int layer, int centeredIndex, int puzzleSize) {
		Matrix3D transform = new Matrix3D();

		transform.translateY((layer - puzzleSize/3 + 1) * LAYER_HEIGHT);
		transform.translateZ(-(layer - puzzleSize + 1) * LAYER_DEPTH / 3);
		
		int yOff = 1 - (puzzleSize % 3);
		transform.translateX(centeredIndex * X_OFFSET);
		transform.translateY((puzzleSize/3 + yOff) * Y_OFFSET);
		transform.translateZ(-Z_OFFSET);

		return transform;
	}
	
	//Return the position of a down facing piece given its layer, index, and the size of the puzzle
	private Matrix3D placeDownPiece(int layer, int centeredIndex, int puzzleSize) {
		Matrix3D transform = new Matrix3D();

		transform.translateY((layer - puzzleSize/3 + 1) * LAYER_HEIGHT);
		transform.translateZ(-(layer - puzzleSize + 1) * LAYER_DEPTH / 3);

		int yOff = 2 - (puzzleSize % 3);
		transform.translateX(centeredIndex * X_OFFSET);
		transform.translateY((puzzleSize/3 + yOff) * Y_OFFSET);
		transform.translateZ(-2*Z_OFFSET);

		return transform;
	}
	
	//Given a center piece, return its world position centered at the front face of the pyraminx
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
	
	/*
	 * Given a corner piece, return its world position centered at 
	 * the bottom right corner of the front face of the pyraminx
	 */
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
	
	//Given an edge piece, return its world position centered at the bottom edge of the front face of the pyraminx
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
	
	//Apply the correct rotation algorithm to a rotation matrix
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
	public void setWorldPosition() {
		Piece piece = super.getPiece();
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
	
	@Override
	protected ColoredMesh getMesh() {
		Piece piece = super.getPiece();
		ColoredMesh mesh = null;
		if(isUpFacing(piece)) {
			mesh = upPieceMesh;
		} else {
			mesh = downPieceMesh;
		}
		
		
		mesh.setColor("Border", Colors.WHITE);
		PieceType type = piece.getType();
		if(type == PieceType.CENTER) {
			mesh.setColor("Front", Colors.convertColor(piece.getColor()));
		} else if(type == PieceType.EDGE) {
			mesh.setColor("Front", Colors.convertColor(piece.getColor(0)));
			mesh.setColor("Bottom", Colors.convertColor(piece.getColor(1)));
		} else if(type == PieceType.CORNER) {
			mesh.setColor("Front", Colors.convertColor(piece.getColor(0)));
			mesh.setColor("Bottom", Colors.convertColor(piece.getColor(1)));
			mesh.setColor("BackRight", Colors.convertColor(piece.getColor(2)));
		}
		
		return mesh;
	}
	
}
