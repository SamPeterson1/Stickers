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

package com.github.sampeterson1.ivyCube;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.puzzle.display.Colors;
import com.github.sampeterson1.puzzle.display.DisplayPiece;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.renderEngine.loaders.OBJLoader;
import com.github.sampeterson1.renderEngine.models.ColoredModel;

public class IvyCubeDisplayPiece extends DisplayPiece {

	
	public IvyCubeDisplayPiece(Piece position) {
		super(position);
	}
	
	@Override
	protected ColoredModel loadModel(Piece piece) {
		PieceType type = piece.getType();
		ColoredModel model = null;
		if(type == PieceType.CENTER) {
			model = OBJLoader.loadColoredModel("IvyPetal.obj"); 
			model.setColor("Front", Colors.convertColor(piece.getColor()));
			model.setColor("Border", Colors.WHITE);
		} else if(type == PieceType.CORNER) {
			model = OBJLoader.loadColoredModel("IvyCorner.obj");
			model.setColor("Border", Colors.WHITE);
			model.setColor("Top", Colors.convertColor(piece.getColor(0)));
			model.setColor("Front", Colors.convertColor(piece.getColor(1)));
			model.setColor("Left", Colors.convertColor(piece.getColor(2)));
		}
		
		return model;
	}
	
	@Override
	public void setWorldPosition(Piece piece) {
		PieceType type = piece.getType();
		int position = piece.getPosition();
		
		Matrix3D transform = new Matrix3D();
		if(type == PieceType.CORNER) { 
			if(position == IvyCube.R_CORNER) {
				transform.rotateZ(Mathf.PI);
			} else if(position == IvyCube.B_CORNER) {
				transform.rotateY(Mathf.PI);
			} else if(position == IvyCube.D_CORNER) {
				transform.rotateY(Mathf.PI);
				transform.rotateZ(Mathf.PI);
			}
		} else if(type == PieceType.CENTER) {
			if(position == 0) {
				//red face
				transform.rotateY(Mathf.PI);
			} else if(position == 1) {
				//white face
				transform.rotateX(Mathf.PI/2);
				transform.rotateZ(Mathf.PI/2);
			} else if(position == 2) {
				//green face
				transform.rotateX(Mathf.PI/2);
				transform.rotateY(-Mathf.PI/2);
			} else if(position == 3) {
				//orange face
			} else if(position == 4) {
				//yellow face
				transform.rotateX(Mathf.PI/2);
				transform.rotateZ(-Mathf.PI/2);
			} else if(position == 5) {
				//blue face
				transform.rotateX(Mathf.PI/2);
				transform.rotateY(Mathf.PI/2);
			}
		}
		
		super.setTransformationMat(transform);
	}

}
