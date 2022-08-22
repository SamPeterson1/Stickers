package com.github.sampeterson1.square1;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.renderEngine.loaders.OBJLoader;
import com.github.sampeterson1.renderEngine.models.ColoredMesh;

public class Square1DisplayPiece extends DisplayPiece {
	
	private static ColoredMesh cornerPieceMesh;
	private static ColoredMesh edgePieceMesh;
	private static ColoredMesh centerPieceMesh;
	
	public Square1DisplayPiece(Piece piece) {
		super(piece);
		tryLoadMeshes();
	}

	private static void tryLoadMeshes() {
		if(cornerPieceMesh == null || cornerPieceMesh.getData().isDeleted()) {
			cornerPieceMesh = OBJLoader.loadColoredMesh("square1/Corner.obj");
			edgePieceMesh = OBJLoader.loadColoredMesh("square1/Edge.obj");
			centerPieceMesh = OBJLoader.loadColoredMesh("square1/Center.obj");
		}
	}
	
	@Override
	protected ColoredMesh getMesh() {
		Piece piece = super.getPiece();
		PieceType type = piece.getType();
		
		if(type == PieceType.CORNER) {
			return cornerPieceMesh;
		} else if(type == PieceType.EDGE) {
			return edgePieceMesh;
		} else if(type == PieceType.SQUARE1_CENTER) {
			return centerPieceMesh;
		}
		
		return null;
	}

	@Override
	protected void setColors() {
		Piece piece = super.getPiece();
		PieceType type = piece.getType();
		
		super.setColor("Border", Color.BORDER);
		
		if(type == PieceType.CORNER) {
			super.setColor("Front", piece.getColor(0));
			super.setColor("Top", piece.getColor(1));
			super.setColor("Right", piece.getColor(2));
		} else if(type == PieceType.EDGE) {
			super.setColor("Front", piece.getColor(0));
			super.setColor("Top", piece.getColor(1));
		} else if(type == PieceType.SQUARE1_CENTER) {
			super.setColor("Front", piece.getColor(0));
			super.setColor("Right", piece.getColor(1));
			super.setColor("Back", piece.getColor(2));
		}
	}
	
	private Matrix3D getCenterPosition(Piece piece) {
		Matrix3D transform = new Matrix3D();
		int position = piece.getPosition();
		
		if(position == 1) {
			transform.rotateY(Mathf.PI);
		}
		
		return transform;
	}
	
	private Matrix3D getEdgePosition(Piece piece) {
		Matrix3D transform = new Matrix3D();
		int position = piece.getPosition() / 3;
		
		if(position < 4) {
			transform.rotateY(-position * Mathf.PI / 2);
		} else {
			transform.rotateY(position * Mathf.PI / 2);
			transform.rotateZ(Mathf.PI);
		}
		
		return transform;
	}

	private Matrix3D getCornerPosition(Piece piece) {
		Matrix3D transform = new Matrix3D();
		int position = (piece.getPosition() - 1) / 3;
		
		if(position < 4) {
			transform.rotateY(-position * Mathf.PI / 2);
		} else {
			transform.rotateY((position + 1) * Mathf.PI / 2);
			transform.rotateZ(Mathf.PI);
		}
		
		return transform;
	}
	
	@Override
	protected Matrix3D getWorldPosition() {
		Piece piece = super.getPiece();
		PieceType type = piece.getType();
		
		if(type == PieceType.CORNER) {
			return getCornerPosition(piece);
		} else if(type == PieceType.EDGE) {
			return getEdgePosition(piece);
		} else if(type == PieceType.SQUARE1_CENTER) {
			return getCenterPosition(piece);
		}
		
		return null;
	}

}
