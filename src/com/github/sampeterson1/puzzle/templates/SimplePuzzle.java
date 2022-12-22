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

package com.github.sampeterson1.puzzle.templates;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;
import com.github.sampeterson1.puzzle.moves.Move;

public abstract class SimplePuzzle extends Puzzle {
	
	private boolean sparse;
	
	private List<Piece> allPieces;
	private Map<PieceType, List<Piece>> piecesByType;
	private Map<PieceType, SimplePieceBehavior> pieceBehaviors;
	
	public SimplePuzzle(PuzzleType type) {
		this(type, false);
	}
	
	public SimplePuzzle(PuzzleType type, boolean sparse) {
		super(type);
		
		this.sparse = sparse;
		this.allPieces = new ArrayList<Piece>();
		this.piecesByType = new EnumMap<PieceType, List<Piece>>(PieceType.class);
		this.pieceBehaviors = new EnumMap<PieceType, SimplePieceBehavior>(PieceType.class);
	}
	
	protected final Map<PieceType, List<Piece>> getPiecesByType() {
		return this.piecesByType;
	}
	
	protected final Piece getPiece(PieceType type, int position) {
		List<Piece> pieces = piecesByType.get(type);
		
		if(sparse) {
			for(Piece piece : pieces) {
				if(piece.getPosition() == position) return piece;
			}
			
			return null;
		}
		
		return pieces.get(position);
	}
	
	protected final void createPieces(SimplePieceBehavior behavior, int[] positions) {
		List<Piece> pieces = new ArrayList<Piece>();
		PieceType type = behavior.getType();
		pieceBehaviors.put(type, behavior);
		
		for(int i : positions) {
			pieces.add(behavior.createPiece(i));
		}
		
		piecesByType.put(type, pieces);
		allPieces.addAll(pieces);
	}
	
	protected final void createPieces(SimplePieceBehavior behavior, int numPieces) {
		int[] positions = new int[numPieces];
		for(int i = 0; i < numPieces; i ++) positions[i] = i;
		
		createPieces(behavior, positions);
	}
	
	@Override
	public final List<Piece> getAffectedPieces(Move move) {
		List<Piece> affectedPieces = new ArrayList<Piece>();
		
		for(PieceType type : piecesByType.keySet()) {
			List<Piece> pieces = piecesByType.get(type);
			SimplePieceBehavior behavior = pieceBehaviors.get(type);
			
			for(Piece piece : pieces) {
				if(behavior.affectedByMove(move, piece)) {
					affectedPieces.add(piece);
				}
			}
		}
		
		return affectedPieces;
	}

	@Override
	public final List<Piece> getAllPieces() {
		return this.allPieces;
	}

	@Override
	protected final void movePieces(Move move) {
		for(PieceType type : piecesByType.keySet()) {
			List<Piece> pieces = piecesByType.get(type);
			SimplePieceBehavior behavior = pieceBehaviors.get(type);
			
			for(Piece piece : pieces) {
				if(behavior.affectedByMove(move, piece)) {
					
					behavior.movePiece(move, piece);
				}
			}
		}
	}

}
