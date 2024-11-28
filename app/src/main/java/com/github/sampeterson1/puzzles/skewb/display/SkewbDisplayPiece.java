package com.github.sampeterson1.puzzles.skewb.display;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.renderEngine.loaders.OBJLoader;
import com.github.sampeterson1.renderEngine.models.ColoredMesh;

public class SkewbDisplayPiece extends DisplayPiece {
	
	private static ColoredMesh cornerPieceMesh;
	private static ColoredMesh centerPieceMesh;
	
	public SkewbDisplayPiece(Piece position) {
		super(position);
		if(cornerPieceMesh == null || cornerPieceMesh.getData().isDeleted())
			loadMeshes();
	}
	
	private void loadMeshes() {
		cornerPieceMesh = OBJLoader.loadColoredMesh("skewb/Corner.obj");
		centerPieceMesh = OBJLoader.loadColoredMesh("skewb/Center.obj");
	}

	@Override
	protected void setColors() {
		Piece piece = super.getPiece();
		PieceType type = piece.getType();
		super.setColor("Border", Color.WHITE);
		
		if(type == PieceType.CORNER) {
			super.setColor("Front", piece.getColor(0));
			super.setColor("Top", piece.getColor(1));
			super.setColor("Right", piece.getColor(2));
		} else if(type == PieceType.CENTER) {
			super.setColor("Top", piece.getColor());
		}
	}
	
	private Matrix3D positionCorner(int position) {
		Matrix3D transform = new Matrix3D();
		
		if(position == 0) {
			transform.rotateY(-Mathf.PI / 2);
		} else if(position == 1) {
			transform.rotateY(Mathf.PI);
		} else if(position == 2) {
			transform.rotateY(Mathf.PI / 2);
		} else if(position == 3) {
			//position 3 is positioned correctly by default
		} else if(position == 4) {
			transform.rotateZ(-Mathf.PI / 2);
			transform.rotateY(-Mathf.PI / 2);
		} else if(position == 5) {
			transform.rotateZ(-Mathf.PI / 2);
			transform.rotateY(Mathf.PI);
		} else if(position == 6) {
			transform.rotateZ(-Mathf.PI / 2);
			transform.rotateY(Mathf.PI / 2);
		} else if(position == 7) {
			transform.rotateZ(-Mathf.PI / 2);	
		}
		
		return transform;
	}
	
	private Matrix3D positionCenter(int position) {
		Matrix3D transform = new Matrix3D();
		
		if(position == 0) {
			//white center
		} else if(position == 1) {
			//green center
			transform.rotateX(-Mathf.PI / 2);
		} else if(position == 2) {
			//red center
			transform.rotateZ(Mathf.PI / 2);
		} else if(position == 3) {
			//blue center
			transform.rotateX(Mathf.PI / 2);
		} else if(position == 4) {
			//orange center
			transform.rotateZ(-Mathf.PI / 2);
		} else if(position == 5) {
			//yellow center
			transform.rotateZ(Mathf.PI);
		}
		
		return transform;
	}
	
	@Override
	protected Matrix3D getWorldPosition() {
		
		Piece piece = super.getPiece();
		PieceType type = piece.getType();
		int position = piece.getPosition(); 
		
		Matrix3D transform = null;
		if(type == PieceType.CENTER) {
			transform = positionCenter(position);
		} else if(type == PieceType.CORNER) {
			transform = positionCorner(position);
		}
		
		
		return transform;
	}
	
	@Override
	public ColoredMesh getMesh() {
		ColoredMesh mesh = null;
		Piece piece = super.getPiece();
		
		if(piece.getType() == PieceType.CORNER) {
			return cornerPieceMesh;
		} else if(piece.getType() == PieceType.CENTER) {
			return centerPieceMesh;
		}

		return mesh;
	}
	
}
