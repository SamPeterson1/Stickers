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

package com.github.sampeterson1.puzzle.puzzles.pyraminx.pieces;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.puzzle.puzzles.pyraminx.util.PyraminxCornerUtil;

public class PyraminxCornerBehavior extends PieceBehavior {

	private static final Map<Axis, Integer> oppositeCorners = initOppositeCorners();
	
	private static Map<Axis, Integer> initOppositeCorners() {
		Map<Axis, Integer> oppositeCorners = new EnumMap<Axis, Integer>(Axis.class);
		oppositeCorners.put(Axis.PF, 2);
		oppositeCorners.put(Axis.PR, 3);
		oppositeCorners.put(Axis.PL, 1);
		oppositeCorners.put(Axis.PD, 0);
		
		return oppositeCorners;
	}
	
	public PyraminxCornerBehavior(Puzzle puzzle) {
		super(PieceType.CORNER, puzzle);
	}
	
	@Override
	public Piece createPiece(int position, int index) {
		Color[] colors = PyraminxCornerUtil.getColors(position);
		Piece corner = new Piece(super.getPuzzle(), PieceType.CORNER, position, index);
		for(int i = 0; i < 3; i ++)
			corner.setColor(i, colors[i]);
		
		return corner;
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		int position = group.getPosition();
		int puzzleSize = group.getPuzzleSize();	
		int layer = move.getLayer();
		Axis moveFace = move.getAxis();
		
		List<Piece> pieces = new ArrayList<Piece>();
		if(position == oppositeCorners.get(moveFace)) {
			if(layer == puzzleSize - 2) {
				pieces.add(group.getPiece(0)); 
			} else if(layer == puzzleSize - 1) {
				pieces.add(group.getPiece(1));
			}
		} else if(layer == 0) {
			return group.getPieces();
		}
		
		return pieces;
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		PyraminxCornerUtil.mapCorner(move, piece);
	}

	@Override
	public int getNumPieces(int puzzleSize) {
		return 2;
	}

}
