package com.github.yoshiapolis.pyraminx.pieces;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceBehavior;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.pyraminx.util.PyraminxCenterUtil;
import com.github.yoshiapolis.pyraminx.util.PyraminxMoveUtil;

public class PyraminxCenterBehavior implements PieceBehavior {

	@Override
	public Piece createPiece(int position, int index) {
		Piece center = new Piece(PieceType.CENTER, position, index);
		center.setColor(PyraminxCenterUtil.getColor(position));
		
		return center;
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {

		Face face = Pyraminx.faces[group.getPosition()];
		Face pivot = move.getFace();
		
		int layer = move.getLayer();
		int puzzleSize = group.getPuzzleSize();
		
		List<Piece> pieces = new ArrayList<Piece>();
		if(face == pivot) {
			if(move.getLayer() == 0)
				return group.getPieces();
		} else if(layer > 0 && layer < puzzleSize - 2) {
			int rotation = PyraminxCenterUtil.getRotationOffset(pivot, face);
			int centerSize = group.getPuzzleSize() - 3;
			int sqrSize = centerSize * centerSize;
			
			int invLayer = centerSize - move.getLayer() + 1;
			int lowerIndex = sqrSize - invLayer * invLayer;
			invLayer --;
			int upperIndex = sqrSize - (invLayer * invLayer);
			
			for(int i = lowerIndex; i < upperIndex; i ++) {
				int index = i;
				if(rotation < 0) {
					for(int j = 0; j < -rotation; j ++)
						index = PyraminxCenterUtil.rotateIndexCCW(index, puzzleSize - 3);
				} else if(rotation > 0) {
					for(int j = 0; j < rotation; j ++)
						index = PyraminxCenterUtil.rotateIndexCW(index, puzzleSize - 3);
				}
				
				pieces.add(group.getPiece(index));
			}
		}
		
		return pieces;
	}

	@Override
	public void movePiece(Move move, Piece piece, int puzzleSize) {
		Face currentFace = Pyraminx.faces[piece.getPosition()];
		
		int newIndex = PyraminxCenterUtil.mapIndex(move, currentFace, piece.getIndex(), puzzleSize);
		Face newFace = PyraminxMoveUtil.mapFace(currentFace, move);
		
		piece.setIndex(newIndex);
		piece.setPosition(newFace.getIndex());
	}

	@Override
	public int getNumPieces(int puzzleSize) {
		int a = puzzleSize - 3;
		return a*a;
	}
}
