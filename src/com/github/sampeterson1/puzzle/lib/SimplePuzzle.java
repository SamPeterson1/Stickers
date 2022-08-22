package com.github.sampeterson1.puzzle.lib;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class SimplePuzzle extends Puzzle {
	
	private boolean sparse;
	
	private List<Piece> allPieces;
	private Map<PieceType, List<Piece>> piecesByType;
	private Map<PieceType, SimplePieceBehavior> pieceBehaviors;
	
	public SimplePuzzle(int size) {
		this(size, false);
	}
	
	public SimplePuzzle(int size, boolean sparse) {
		super(size);
		
		this.sparse = sparse;
		this.allPieces = new ArrayList<Piece>();
		this.piecesByType = new EnumMap<PieceType, List<Piece>>(PieceType.class);
		this.pieceBehaviors = new EnumMap<PieceType, SimplePieceBehavior>(PieceType.class);
	}
	
	protected Map<PieceType, List<Piece>> getPiecesByType() {
		return this.piecesByType;
	}
	
	protected Piece getPiece(PieceType type, int position) {
		List<Piece> pieces = piecesByType.get(type);
		
		if(sparse) {
			for(Piece piece : pieces) {
				if(piece.getPosition() == position) return piece;
			}
			
			return null;
		}
		
		return pieces.get(position);
	}
	
	protected void createPieces(SimplePieceBehavior behavior, int[] positions) {
		List<Piece> pieces = new ArrayList<Piece>();
		PieceType type = behavior.getType();
		pieceBehaviors.put(type, behavior);
		
		for(int i : positions) {
			pieces.add(behavior.createPiece(i));
		}
		
		piecesByType.put(type, pieces);
		allPieces.addAll(pieces);
	}
	
	protected void createPieces(SimplePieceBehavior behavior, int numPieces) {
		int[] positions = new int[numPieces];
		for(int i = 0; i < numPieces; i ++) positions[i] = i;
		
		createPieces(behavior, positions);
	}
	
	@Override
	public List<Piece> getAffectedPieces(Move move) {
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
	public List<Piece> getAllPieces() {
		return this.allPieces;
	}

	@Override
	protected void movePieces(Move move) {
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
