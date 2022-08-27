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

package com.github.sampeterson1.puzzle.puzzles.ivyCube.display;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.puzzles.ivyCube.pieces.IvyCube;
import com.github.sampeterson1.renderEngine.loaders.OBJLoader;
import com.github.sampeterson1.renderEngine.models.ColoredMesh;

//An implementation of DisplayPiece that represents a piece on an Ivy Cube
public class IvyCubeDisplayPiece extends DisplayPiece {
	
	private static ColoredMesh petalPieceMesh;
	private static ColoredMesh cornerPieceMesh;
	
	private static void loadMeshes() {
		petalPieceMesh = OBJLoader.loadColoredMesh("IvyPetal.obj");
		cornerPieceMesh = OBJLoader.loadColoredMesh("IvyCorner.obj");
	}
	
	public IvyCubeDisplayPiece(Piece position) {
		super(position);
		if(petalPieceMesh == null || petalPieceMesh.getData().isDeleted())
			loadMeshes();
	}
	
	private Matrix3D getCornerWorldPosition(int position) {
		Matrix3D transform = new Matrix3D();
		
		if(position == IvyCube.R_CORNER) {
			transform.rotateZ(Mathf.PI);
		} else if(position == IvyCube.B_CORNER) {
			transform.rotateY(Mathf.PI);
		} else if(position == IvyCube.D_CORNER) {
			transform.rotateY(Mathf.PI);
			transform.rotateZ(Mathf.PI);
		}
		
		return transform;
	}
	
	private Matrix3D getCenterWorldPosition(int position) {
		Matrix3D transform = new Matrix3D();
		
		if(position == IvyCube.RED_CENTER) {
			transform.rotateY(Mathf.PI);
		} else if(position == IvyCube.WHITE_CENTER) {
			transform.rotateX(Mathf.PI/2);
			transform.rotateZ(Mathf.PI/2);
		} else if(position == IvyCube.GREEN_CENTER) {
			transform.rotateX(Mathf.PI/2);
			transform.rotateY(-Mathf.PI/2);
		} else if(position == IvyCube.ORANGE_CENTER) {
			//this center is positioned correctly by default
		} else if(position == IvyCube.YELLOW_CENTER) {
			transform.rotateX(Mathf.PI/2);
			transform.rotateZ(-Mathf.PI/2);
		} else if(position == IvyCube.BLUE_CENTER) {
			transform.rotateX(Mathf.PI/2);
			transform.rotateY(Mathf.PI/2);
		}
		
		return transform;
	}
	
	@Override
	protected Matrix3D getWorldPosition() {
		Piece piece = super.getPiece();
		PieceType type = piece.getType();
		int position = piece.getPosition();

		if(type == PieceType.CORNER) { 
			return getCornerWorldPosition(position);
		} else if(type == PieceType.CENTER) {
			return getCenterWorldPosition(position);
		}
		
		return null;
	}
	
	@Override
	protected void setColors() {
		Piece piece = super.getPiece();
		PieceType type = piece.getType();
		
		super.setColor("Border", Color.BORDER);
		if(type == PieceType.CENTER) {
			super.setColor("Front", piece.getColor());
		} else {
			super.setColor("Top", piece.getColor(0));
			super.setColor("Front", piece.getColor(1));
			super.setColor("Left", piece.getColor(2));
		}
	}
	
	@Override
	protected ColoredMesh getMesh() {
		Piece piece = super.getPiece();
		PieceType type = piece.getType();
		
		if(type == PieceType.CENTER) {
			return petalPieceMesh;
		} else {
			return cornerPieceMesh;
		}
	}

}
